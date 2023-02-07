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
public class MatchResultDTO {

    private long playerId1;

    private long botId1;

    private long playerId2;

    private long botId2;
}
