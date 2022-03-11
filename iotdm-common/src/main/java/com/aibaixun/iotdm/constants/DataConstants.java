package com.aibaixun.iotdm.constants;

/**
 * 常量信息
 * @author wangxiao@aibaixun.com
 * @date 2022/3/3
 */
public class DataConstants {

    private DataConstants() {
    }

    /**
     * 单页 最多50
     */
    public static int QUERY_PAGE_MAX = 50;

    /**
     * null 字符串
     */
    public static String NULL_STR = "NULL";

    /**
     * mqtt 服务名称
     */
    public static final String MQTT_TRANSPORT_NAME = "MQTT";


    /**
     * iotdm 会话缓存session
     */
    public static final String IOT_SESSION_CACHE_KEY_PREFIX = "IOTDM:SESSION:";


    /**
     * iotdm debug 信息 缓存
     */
    public static final String IOT_DEVICE_DEBUG_CACHE_KEY = "IOTDM:DEVICE:DEBUG:";

    /**
     * 集群订阅 redis key 过期
     */
    public static final String EXPIRED_CHANNEL = "__keyevent@0__:expired";


    public static final String PROPERTY_TS_DATA = "property_ts_data";


}
