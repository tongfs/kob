package com.kob.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Getter
@AllArgsConstructor
public enum SocketResultType {
    MATCHING_SUCCESS(1, "matching_success"),
    NEXT_STEP(2, "next_step"),
    GAME_RESULT(3, "game_result"),
    ;

    private final int code;
    private final String desc;
}
