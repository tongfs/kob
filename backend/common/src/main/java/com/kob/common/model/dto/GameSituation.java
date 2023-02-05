package com.kob.common.model.dto;

import java.util.LinkedList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSituation {

    /**
     * 玩家ID
     */
    private long userId;

    /**
     * 舌头坐标
     */
    private Cell head;

    /**
     * bot代码
     */
    private String botCode;

    /**
     * 地图
     */
    private int[][] gameMap;

    /**
     * 当前回合
     */
    private int round;

    /**
     * 蛇身
     */
    private LinkedList<Cell> body1, body2;

}
