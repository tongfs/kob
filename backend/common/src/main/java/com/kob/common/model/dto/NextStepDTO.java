package com.kob.common.model.dto;

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
public class NextStepDTO {

    private long userId;

    private int direction;
}
