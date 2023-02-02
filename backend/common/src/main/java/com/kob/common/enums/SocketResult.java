package com.kob.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Getter
@AllArgsConstructor
public enum SocketResult {
    MATCHING_SUCCESS(1, "matching_success"),
    ;

    private final int code;
    private final String desc;
}
