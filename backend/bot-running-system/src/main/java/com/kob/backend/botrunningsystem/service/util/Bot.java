package com.kob.backend.botrunningsystem.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tfs
 * @date 2022-08-30
 * @description
 */
@Data
@AllArgsConstructor
public class Bot {
    private Integer userId;
    private String botCode;
    private String input;
}
