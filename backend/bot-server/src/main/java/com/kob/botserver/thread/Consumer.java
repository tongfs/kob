package com.kob.botserver.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.UUID;
import java.util.function.Supplier;

import org.joor.Reflect;

import com.kob.botserver.service.BotService;
import com.kob.common.model.dto.Cell;
import com.kob.common.model.dto.GameSituation;

import lombok.AllArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@AllArgsConstructor
public class Consumer implements Runnable {

    private static final String AFTER_CLASS_NAME = " implements java.util.function.Supplier<Integer>";
    private static final String CLASS_NAME = "com.kob.botserver.service.impl.SupplierImpl";

    private final GameSituation gameSituation;
    private final BotService botService;

    @Override
    public void run() {
        try {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String code = gameSituation.getBotCode();
            int i = code.indexOf(AFTER_CLASS_NAME);
            code = code.substring(0, i) + uuid + code.substring(i);

            StringBuilder sb = new StringBuilder();
            String serializedStr = serializeGame();

            File file = new File(CLASS_NAME + uuid);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(serializedStr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Supplier<Integer> supplier = Reflect
                    .compile(CLASS_NAME + uuid, code)
                    .create()
                    .get();

            int nextStep = supplier.get();
            botService.sendNextStep(gameSituation.getUserId(), nextStep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将游戏局势序列化为字符串
     */
    private String serializeGame() {
        StringBuilder sb = new StringBuilder();

        Cell head = gameSituation.getHead();
        sb.append(head.getX()).append(',').append(head.getY()).append('#');

        adjustBodyLength();
        updateGameMap();

        int[][] gameMap = gameSituation.getGameMap();
        sb.append(gameMap.length).append(',').append(gameMap[0].length).append('#');

        for (int[] ints : gameMap) {
            for (int i : ints) {
                sb.append(i);
            }
        }

        return sb.toString();
    }

    /**
     * 更新地图
     */
    private void updateGameMap() {
        int[][] gameMap = gameSituation.getGameMap();
        for (Cell cell : gameSituation.getBody1()) {
            gameMap[cell.getX()][cell.getY()] = 1;
        }
        for (Cell cell : gameSituation.getBody2()) {
            gameMap[cell.getX()][cell.getY()] = 1;
        }
    }

    /**
     * 调整蛇的长度
     */
    private void adjustBodyLength() {
        if (!isIncreasing()) {
            gameSituation.getBody1().poll();
            gameSituation.getBody2().poll();
        }
    }

    /**
     * 当前回合蛇的身体是否要增长
     */
    private boolean isIncreasing() {
        int round = gameSituation.getRound();
        if (round <= 5) return true;
        if (round <= 11) return (round & 1) == 1;
        return round % 3 == 2;
    }
}
