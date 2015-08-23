package com.michaelho.watermonitor.constants;

/**
 * Created by MichaelHo on 2015/4/7.
 */
public interface URLConstants {
    public static final String SERVER_IP = "http://140.118.7.117/";
    public static final String WATER_FOLDER = "water_monitor/";

    // PHPs
    public static final String URL_GET_ROOM_STATS= SERVER_IP
            + WATER_FOLDER + "get_room_stats.php";
    public static final String URL_CHECK_MAC= SERVER_IP
            + WATER_FOLDER + "check_mac.php";
    public static final String URL_REGISTER_MEMBER = SERVER_IP
            + WATER_FOLDER + "gcm_register.php";
    public static final String URL_POST_PORTRAIT = SERVER_IP
            + WATER_FOLDER + "post_portrait.php";
    public static final String URL_GET_MY_PORTRAIT = SERVER_IP
            + WATER_FOLDER + "get_my_portrait.php";
}
