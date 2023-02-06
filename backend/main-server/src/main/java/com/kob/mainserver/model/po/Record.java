package com.kob.mainserver.model.po;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@Builder
public class Record {

    private Long id;

    private Long userId1;

    private Integer x1, y1;

    private Long userId2;

    private Integer x2, y2;

    private String steps1;

    private String steps2;

    private String map;

    private Integer loserIdentity;

    private Integer originalScore1;

    private Integer getScore1;

    private Integer originalScore2;

    private Integer getScore2;

    private Date createTime;
}
