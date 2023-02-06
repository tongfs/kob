package com.kob.mainserver.model.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/5
 */
@Data
@Builder
public class RecordVO {

    private long id;

    private String username1;

    private String avatar1;

    private String username2;

    private String avatar2;

    private int[] steps1;

    private int[] steps2;

    private int loserIdentity;

    private int[][] gameMap;

    private Integer originalScore1;

    private Integer getScore1;

    private Integer originalScore2;

    private Integer getScore2;

    private Date createTime;
}
