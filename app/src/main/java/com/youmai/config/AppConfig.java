package com.youmai.config;

public class AppConfig {

    public static final int SEND_BUFFER_SIZE = 1024; //1KB

    /**
     * pref文件名定义
     */
    public static final String SHARED_PREFERENCES = "sdk_app";

    /**
     * HuXin 服务器连接配置
     */
    private final static int LAUNCH_MODE = 0; //0 IM本地服务器        1 IM 50服务器

    private final static String SOCKET_HOST[] = new String[]{"192.168.0.110", "120.24.37.50"};

    private final static int SOCKET_PORT[] = new int[]{12345, 7654};

    public static String getSocketHost() {
        return SOCKET_HOST[LAUNCH_MODE];
    }

    public static int getSocketPort() {
        return SOCKET_PORT[LAUNCH_MODE];
    }

}
