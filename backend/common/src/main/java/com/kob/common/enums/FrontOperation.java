package com.kob.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Getter
@AllArgsConstructor
public enum FrontOperation {
    CANCEL(0),
    MATCH(1),
    MOVE(2),
    ;

    private final Integer code;
}
