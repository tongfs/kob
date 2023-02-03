package com.kob.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Getter
@AllArgsConstructor
public enum GameResult {
    LOSER_1(1),
    LOSER_2(2),
    DRAW(3),
    ;

    private final int resultCode;
}
