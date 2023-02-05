package com.kob.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {

    private Long userId;

    private Long botId;

    private Integer score;

    private Integer waitingTime;

    public void addWaitingTime() {
        waitingTime++;
    }
}
