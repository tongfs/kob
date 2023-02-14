/**
 * 这里给出代码样例，你可以修改 getNextStep() 方法中的内容
 */

package com.kob.botserver.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public class SupplierImpl implements java.util.function.Supplier<Integer> {

    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    @Override
    public Integer get() {
        File file = new File(this.getClass().getName());
        int result;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            result = getNextStep(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            result = 1;
        } finally {
            file.delete();
        }
        return result;
    }


    /**
     * 获得下一步的具体方法
     */
    private int getNextStep(String serializedStr) {
        String[] splits = serializedStr.split("#");

        String[] head = splits[0].split(",");
        int headX = Integer.parseInt(head[0]);
        int headY = Integer.parseInt(head[1]);

        String[] scale = splits[1].split(",");
        int rows = Integer.parseInt(scale[0]);
        int cols = Integer.parseInt(scale[1]);

        String gameMapStr = splits[2];
        int[][] gameMap = new int[rows][cols];
        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++, k++) {
                if (gameMapStr.charAt(k) == '1') {
                    gameMap[i][j] = 1;
                }
            }
        }

        int[] directions = new int[4];
        int maxDirections = -1;
        for (int i = 0; i < 4; i++) {
            int a = headX + dx[i];
            int b = headY + dy[i];
            if (a >= 0 && a < rows && b >= 0 && b < cols && gameMap[a][b] == 0) {
                directions[i] = explore(a, b, gameMap);
            } else {
                directions[i] = -1;
            }
            maxDirections = Math.max(maxDirections, directions[i]);
        }

        if (maxDirections == -1) return 0;

        Random random = new Random();
        while (true) {
            int d = random.nextInt(4);
            if (directions[d] == maxDirections) {
                return d;
            }
        }
    }

    /**
     * 探索一下如果下一步走 (x, y)，则再下一步有多少个可选位置，将结果记录在
     */
    private int explore(int x, int y, int[][] gameMap) {
        int rows = gameMap.length;
        int cols = gameMap[0].length;
        int res = 0;
        for (int i = 0; i < 4; i++) {
            int a = x + dx[i], b = y + dy[i];
            if (a >= 0 && a < rows && b >= 0 && b < cols && gameMap[a][b] == 0) {
                res++;
            }
        }
        return res;
    }
}
