package com.kob.mainserver.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMatchResultVO {

    private Opponent opponent;

    private Integer identity;

    private int[][] gameMap;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Opponent {
        private String username;
        private String avatar;
    }

}
