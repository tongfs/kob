package com.kob.mainserver.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Data
@AllArgsConstructor
public class GameMatchResultVO {

    private Opponent opponent;

    private Integer identity;

    private int[][] gameMap;

    @Data
    @AllArgsConstructor
    public static class Opponent {
        private String username;
        private String avatar;
    }

}
