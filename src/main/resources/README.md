# Online-live-broadcast-system

## 用户信息部分

### 用户注册

#### 请求

UserRegisterRequest

#### 逻辑

UserInfo、RoomInfo表增加一条信息。

**UserAccount唯一**，需要判断注册时是否为一。

**前后端判断**用户账号不少于四位，并且不包含特殊字符；用户密码不少于8位。

```
`~!@#$%^&*()+=|{}':;',[].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？
```

#### 返回

200 注册成功

201 UserAccount重复



### 用户登录

#### 请求

UserLoginRequest

#### 逻辑

用户登录时，需要返回多种数据，包括UserInfo、RoomInfo等。并且需要在redis创建并返回一个唯一token

#### 技术

oauth[2]

#### 返回

200 登录成功，同时在data需要返回一个token

201 用户名或密码错误



### 用户信息修改

#### 请求

UserUpdateRequest(extend UserInfo)

#### 逻辑

在header里放token，后端需要检验token是否存在。**必须是修改自己的信息，也就是token和uuid要对应起来**

信息修改，增量修改

**UserAccount不可修改**

#### 返回

200 修改成功

**500 用户未登录（token过期）**



## 主播部分





### 修改直播间信息

#### 请求

类似RoomInfo的类

header里的token

#### 逻辑

信息修改，增量修改

实际上改的就是roomName和profile、**分区**

#### 返回

200 成功

*201 无此分区*



### 开播

#### 请求

header里的token

#### 逻辑

service层里面所需要的roomLog类的内容，来源于数据库中的roomInfo，持久化roomLog。

redis里存放必要的数据：roomLog

#### 返回

200 成功

### 关闭直播

#### 请求

header里的token

#### 逻辑

和开播是相反的

update roomLog表。

mysql里面有一张表叫roomLog，roomLog有一个唯一id代表唯一一场直播，开播的时候redis存放了这个id，所以删除的时候用这个id就可以删除。

不仅要写roomLog表，还要写*礼物表*。用户聊天内容自动删除。

**异步**

删除redis的数据

#### 返回

200 成功

```
小提示：判断是否开播的方法

推流号=直播间号!=roomLog的id
where roomForeignId=推流号
```



## 观众部分

### 刷礼物

走的websocket

#### 请求

json类型

```json
{
  "type": "gift",
  "data": "1",//persentId
  "number" 1,//礼物数量
  "sender": uuid,
  "receiver": uuid,
}
```

#### 逻辑

后端监听到刷礼物请求，向redis写东西，格式和presentLog表相同。

#### 返回

websocket无返回值



### 发弹幕

#### 请求

json类型

```json
{
  "type": "chat",
  "data": "弹幕内容",//persentId
  "number" null,//礼物数量
  "sender": uuid,
  "receiver": uuid,
}
```

#### 逻辑

后端监听到发弹幕请求，向redis写东西，**格式自定义**。

#### 返回

websocket无返回值



### 关注

#### 请求

header里面要有token

#### 逻辑

后端监听到发弹幕请求，向redis写东西，**格式自定义**。

#### 返回

websocket无返回值



### 充值

#### 请求

header里面要有token

要在vo创一个新class，包括id和userBalance。



#### 逻辑

mysql中的userBalance+=class.userBalance

#### 返回

200成功。



### 高能用户

#### 请求

token都不需要

#### 逻辑

在redis里查找并排序送礼物总额最多的5个人。

#### 返回

```
array<userInfo>
```



# websocket的说明

### 逻辑

分主从，后端是主，客户端是从，类似nginx搭建的rtmp。

后端在运行java jar包时自动新开了一个端口（如：8979），网页自动连接这个端口，后端和前端都在监听聊天室的通知，并且可以发送通知。

websocket是一个组件复用的，因此我们自己去定义websocket内部的形式，是一个标准的json。

```json

{
	"type": "chat",
  "data": "聊天内容"
}

{
  "type": "gift",
  "data": "什么礼物"
}
```

如

```java
//domain.class
class a{
  private String type;
  private String data;
}
```







```xml
<dependency>
    <groupId>com.corundumstudio.socketio</groupId>
    <artifactId>netty-socketio</artifactId>
    <version>1.7.16</version>
</dependency>

<dependency>
    <groupId>io.socket</groupId>
    <artifactId>socket.io-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

系统提示：快旭直播内容及互动评论须严格遵守直播规范，严禁传播违法违规、低俗血暴、吸烟酗酒、造谣诈骗等不良有害信息。如有违规，平台将对违规直播间或账号进行相应的处罚！注意理性打赏，严禁未成年人直播或打赏。请勿轻信各类招聘征婚、代练代抽、刷钻、购买礼包码、游戏币等广告信息，且如主播在推广商品中诱导私下交易，请谨慎判断，以免上当受骗。



## 主页

### 推荐直播

下次讨论



### 分区

下次讨论



### 预加载

#### 请求

token都不需要

#### 逻辑

在用户第一次打开任何一个网页时，需要向后端请求必要信息（分区信息）

#### 返回

一个数组，包括各种分区的id和名称。