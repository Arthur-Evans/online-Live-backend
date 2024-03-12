package com.niit.onlivestream.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OMUtils {

    /**
     * Object 转 HashMap<String,Object>
     * @param object 对象类需要有public无参构造，字段属性需要是public ，
     *               并且内部类也需要这样.
     * @return
     */
    public static HashMap<String,Object> ObjectToMap(Object object){
        return JSONObject.parseObject(JSONObject.toJSONString(object),HashMap.class);
    }

    public static  Object MapToObject(Map<String,Object> map, Object object){
        return JSONObject.parseObject(JSONObject.toJSONString(map),object.getClass());
    }

}
