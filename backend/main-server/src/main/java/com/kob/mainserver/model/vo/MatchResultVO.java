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
public class MatchResultVO {

    private String opponentUsername;

    private String opponentAvatar;

    private int[][] gameMap;
}
