package com.kob.backend.botrunningsystem.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author tfs
 * @date 2022-08-30
 * @description bot运行的代码模板，其中包名和类名不能更改
 */
public class RunningInterfaceImpl implements java.util.function.Supplier<Integer> {
    private static final int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private static final int ROWS = 13;
    private static final int COLS = 14;

    public int getNextStep(String input) {
        String[] split = input.split("#");
        int[][] g = new int[ROWS][COLS];
        for (int i = 0, k = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++, k++) {
                if (split[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }

        int x1 = new Integer(split[1]), y1 = new Integer(split[2]);
        int x2 = new Integer(split[4]), y2 = new Integer(split[5]);

        List<Cell> body1 = getBody(x1, y1, split[3]);
        List<Cell> body2 = getBody(x2, y2, split[6]);

        for (Cell cell : body1) {
            g[cell.x][cell.y] = 1;
        }
        for (Cell cell : body2) {
            g[cell.x][cell.y] = 1;
        }

        int[] directions = new int[4];  // -1表示该方向不可走，非负表示选择该方向后下一步还有几个位置可走(0~3)
        int maxDirections = -1;  // 下下一步最多有几个位置可走
        for (int i = 0; i < 4; i++) {
            Cell head = body1.get(body1.size() - 1);
            int a = head.x + dx[i];
            int b = head.y + dy[i];
            if (a >= 0 && a < ROWS && b >= 0 && b < COLS && g[a][b] == 0) {
                directions[i] = explore(a, b, g);
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
    private int explore(int x, int y, int[][] g) {
        int res = 0;
        for (int i = 0; i < 4; i++) {
            int a = x + dx[i], b = y + dy[i];
            if (a >= 0 && a < ROWS && b >= 0 && b < COLS && g[a][b] == 0) {
                res++;
            }
        }
        return res;
    }

    private boolean isIncreasing(int turn) {
        return turn <= 10 || turn % 3 == 1;
    }

    private List<Cell> getBody(int x, int y, String steps) {
        List<Cell> body = new ArrayList<>();
        steps = steps.substring(1, steps.length() - 1);

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int a = x, b = y;
        body.add(new Cell(a, b));

        for (int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            a += dx[d];
            b += dy[d];
            body.add(new Cell(a, b));
            if (!isIncreasing(i + 1)) {
                body.remove(0);
            }
        }

        return body;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return getNextStep(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    static class Cell {
        private final int x;
        private final int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
