package com.kob.mainserver.util;

import java.util.Random;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
public class GameUtils {

    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};
    private static final int ROWS = 13;
    private static final int COLS = 14;
    private static final int BLOCK_COUNT = 20;

    public static int[][] getGameMap() {
        int[][] g = new int[ROWS][COLS];
        while (!createWalls(g)) ;
        return g;
    }

    /**
     * 创建地图
     */
    private static boolean createWalls(int[][] g) {
        // 初始化地图
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                g[i][j] = 0;
            }
        }

        // 给四周加上障碍物
        for (int r = 0; r < ROWS; r++) {
            g[r][0] = g[r][COLS - 1] = 1;
        }
        for (int c = 0; c < COLS; c++) {
            g[0][c] = g[ROWS - 1][c] = 1;
        }

        // 创建随机障碍物
        Random random = new Random(System.currentTimeMillis());
        for (int cnt = 0; cnt < BLOCK_COUNT; ) {
            int r = random.nextInt(ROWS), c = random.nextInt(COLS);
            if (g[r][c] == 1) {
                continue;
            }
            if (r == ROWS - 2 && c == 1 || r == 1 && c == COLS - 2) {
                continue;
            }
            g[r][c] = g[ROWS - r - 1][COLS - c - 1] = 1;

            if (r == ROWS - r - 1 && c == COLS - c - 1) cnt += 1;
            else cnt += 2;
        }

        return checkConnectivity(ROWS - 2, 1, 1, COLS - 2, g);

    }

    /**
     * 判断地图连通性
     */
    private static boolean checkConnectivity(int x1, int y1, int x2, int y2, int[][] g) {
        if (x1 == x2 && y1 == y2) return true;
        g[x1][y1] = 1;

        for (int i = 0; i < 4; i++) {
            int x = x1 + dx[i], y = y1 + dy[i];
            if (x >= 1 && x < ROWS - 1 && y >= 1 && y < COLS - 1) {
                if (g[x][y] == 0 && checkConnectivity(x, y, x2, y2, g)) {
                    g[x1][y1] = 0;
                    return true;
                }
            }
        }

        g[x1][y1] = 0;
        return false;
    }
}
