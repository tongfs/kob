package com.kob.mainserver.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@AllArgsConstructor
public class GameResultVO {

    private int loser;

    private int step1;

    private int step2;
}
