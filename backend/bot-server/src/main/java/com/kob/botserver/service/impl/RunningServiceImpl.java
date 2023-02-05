package com.kob.botserver.service.impl;

import static com.kob.common.constant.Constants.COLS;
import static com.kob.common.constant.Constants.ROWS;
import static com.kob.common.constant.Constants.UP;
import static com.kob.common.constant.Constants.dx;
import static com.kob.common.constant.Constants.dy;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.kob.common.model.dto.Cell;
import com.kob.common.model.dto.GameSituation;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public class RunningServiceImpl implements com.kob.botserver.service.RunningService {

    @Override
    public int getNextStep(GameSituation gameSituation) {
        Cell head = gameSituation.getHead();
        int[][] gameMap = gameSituation.getGameMap();
        LinkedList<Cell> body1 = gameSituation.getBody1();
        LinkedList<Cell> body2 = gameSituation.getBody2();

        if (!isIncreasing(gameSituation.getRound())) {
            body1.poll();
            body2.poll();
        }

        updateMap(gameMap, body1, body2);

        int[] directions = new int[4];
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            int a = head.getX() + dx[i];
            int b = head.getY() + dy[i];
            if (a >= 0 && a < ROWS && b >= 0 && b < COLS && gameMap[a][b] == 0) {
                cnt++;
                directions[i] = 1;
            }
        }

        if (cnt == 0) return UP;

        Random random = new Random();
        while (true) {
            int d = random.nextInt(4);
            if (directions[d] == 1) {
                return d;
            }
        }
    }

    /**
     * 当前回合蛇的身体是否要增长
     */
    private boolean isIncreasing(int round) {
        if (round <= 5) return true;
        if (round <= 11) return (round & 1) == 1;
        return round % 3 == 2;
    }

    /**
     * 更新地图
     */
    private void updateMap(int[][] gameMap, List<Cell> body1, List<Cell> body2) {
        for (Cell cell : body1) {
            gameMap[cell.getX()][cell.getY()] = 1;
        }
        for (Cell cell : body2) {
            gameMap[cell.getX()][cell.getY()] = 1;
        }
    }
}
