package com.kob.common.constant;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
public class Constants {

    public static final String LOCALHOST = "127.0.0.1";
    public static final String MAIN_SERVER = "http://127.0.0.1:3000";
    public static final String MATCH_SERVER = "http://127.0.0.1:3001";
    public static final String BOT_SERVER = "http://127.0.0.1:3002";

    public static final String USER_LOGIN_URI = "/user/login";
    public static final String USER_REGISTER_URI = "/user/register";
    public static final String USER_RANK_URI = "/user/rank/*";
    public static final String GAME_RECORD_URI = "/record/list/*";

    public static final String MATCH_ADD_URI = "/match/add";
    public static final String MATCH_ADD_URL = MATCH_SERVER + MATCH_ADD_URI;

    public static final String MATCH_REMOVE_URI = "/match/remove";
    public static final String MATCH_REMOVE_URL = MATCH_SERVER + MATCH_REMOVE_URI;

    public static final String GAME_START_URI = "/game/start";
    public static final String GAME_START_URL = MAIN_SERVER + GAME_START_URI;

    public static final String BOT_ADD_URI = "/bot/add";
    public static final String BOT_ADD_URL = BOT_SERVER + BOT_ADD_URI;

    public static final String GAME_NEXT_URI = "/game/next";
    public static final String GAME_NEXT_URL = MAIN_SERVER + GAME_NEXT_URI;

    public static final int DIRECTION_UNDEFINED = -1;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static final int[] dx = {-1, 0, 1, 0};
    public static final int[] dy = {0, 1, 0, -1};
    public static final int ROWS = 13;
    public static final int COLS = 14;
    public static final int BLOCK_COUNT = 20;

    public static final int PAGE_SIZE = 10;
}
