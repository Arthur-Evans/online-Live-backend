package com.niit.onlivestream.config;



import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.niit.onlivestream.domain.CommentLog;
import com.niit.onlivestream.domain.PresentLog;
import io.lettuce.core.ScriptOutputType;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.niit.onlivestream.contant.RedisDataUse.*;


public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private static final Map<String, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> chatRoomUserCounts = new ConcurrentHashMap<>();

    private static final String JOIN = "JOIN";
    private static final String GIFT = "GIFT";
    private static final String CHAT = "CHAT";
    private static final String PERSON = "PERSON";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 当连接建立时
        logger.info("New WebSocket connection established: " + session.getId());
    }


    @Resource(name = SocketStringRedisTemplate)
    private StringRedisTemplate redisTemplate;


    @Resource(name = CommentRedisTemplate)
    private RedisTemplate<String, Set<CommentLog>> commentTemplate;

    @Resource(name = "CommentRedis")
    private RedisTemplate<String,CommentLog> commentRedis;


    @Resource(name = "PresentRedisTemplate")
    private RedisTemplate<String,PresentLog> presentLogRedisTemplate;

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
        // 拿到发送过来的信息
        String payload = message.getPayload();
        logger.info("Received message: " + payload);
        // 解析消息 JSON对象
        JsonObject jsonMessage = JsonParser.parseString(payload).getAsJsonObject();
        // 拿到type对象 房间对象
        String type = jsonMessage.get("type").getAsString();
        String chatRoom = jsonMessage.get("chatRoom").getAsString();
        /*
         * 判断加入  第一步
         */
        if(type.equals(JOIN)){
            String userName = jsonMessage.get("userName").getAsString();
            addUserToChatRoom(chatRoom,session);
            //存入Redis
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(session.getId(),chatRoom);
            logger.info(userName+"进入直播间");
            TextMessage textMessage =new TextMessage(payload);
            broadcastMessage(chatRoom,textMessage);
            // 更新人数
            updateChatRoomUserCount(chatRoom);
        }
        // 聊天  将消息广播给聊天室内的所有用户
        if(type.equals(CHAT)){
            TextMessage textMessage =new TextMessage(payload);
            CommentLog commentLog =new CommentLog();
            String content =  jsonMessage.get("content").getAsString();
            String userName = jsonMessage.get("userName").getAsString();
            Date date =new Date();
            // 赋值
            commentLog.setComment(content);
            commentLog.setRoomlogid(Integer.valueOf(chatRoom));
            commentLog.setCreatetime(date);
            commentLog.setUserName(userName);
            // 存入Redis
            commentRedis.opsForSet().add(chatRoom,commentLog);
            broadcastMessage(chatRoom,textMessage);
        }


        /*
         * 最后一部分
         */
        if(type.equals(GIFT)){
            TextMessage textMessage =new TextMessage(payload);
            //存入Redis
            String userId = jsonMessage.get("userId").getAsString();
            String giftId = jsonMessage.get("giftId").getAsString();
            PresentLog presentLog =new PresentLog();
            presentLog.setUuid(userId);
            presentLog.setPresentid(Integer.valueOf(giftId));
            presentLog.setRoomlogId(Integer.valueOf(chatRoom));
            presentLog.setSendtime(new Date());
            // 存入Redis
            presentLogRedisTemplate.opsForSet().add(chatRoom,presentLog);
            broadcastMessage(chatRoom,textMessage);
        }

    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 当连接关闭时
        logger.info("WebSocket connection closed: " + session.getId());
        String chatRoom = getChatRoomFromSession(session);
        removeSessionFromChatRooms(session);
        updateChatRoomUserCount(chatRoom);
        // 删除
        redisTemplate.opsForValue().getAndDelete(session.getId());
    }


    // 从redis里面取东西
    private String getChatRoomFromSession(WebSocketSession session) {
        // 从WebSocketSession的Attributes中获取聊天室信息
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(session.getId());
    }

    private void broadcastMessage(String chatRoom, TextMessage message) throws IOException {
        Set<WebSocketSession> sessions = chatRooms.get(chatRoom);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                session.sendMessage(message);
            }
        }
    }

    private void removeSessionFromChatRooms(WebSocketSession session) {
        for (Set<WebSocketSession> sessions : chatRooms.values()) {
            sessions.remove(session);
        }

    }
    public static void addUserToChatRoom(String chatRoom, WebSocketSession session) {
        // 如果charRoom不存在 加入key key对应的set添加session
        chatRooms.computeIfAbsent(chatRoom, key -> ConcurrentHashMap.newKeySet()).add(session);
    }

    private void updateChatRoomUserCount(String chatRoom) {

        Set<WebSocketSession> sessions = chatRooms.get(chatRoom);

        int userCount = sessions != null ? sessions.size() : 0;

        chatRoomUserCounts.put(chatRoom, new AtomicInteger(userCount));

        sendUserCountUpdate(chatRoom, userCount);
    }


    private void sendUserCountUpdate(String chatRoom, int userCount) {
        JsonObject message = new JsonObject();
        message.addProperty("type",PERSON);
        message.addProperty("chatRoom", chatRoom);
        message.addProperty("userCount", userCount);

        for (WebSocketSession session : chatRooms.get(chatRoom)) {
            try {
                session.sendMessage(new TextMessage(message.toString()));
            } catch (IOException e) {
                logger.error("Error sending user count update to session: " + session.getId(), e);
            }
        }
    }




}