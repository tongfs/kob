package com.kob.mainserver.model.vo;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Data
public class UserInfoVO {

    private Long id;

    private String username;

    private String avatar;

    private Integer score;

    private Integer winCnt;

    private Integer loseCnt;

    private Integer drawCnt;
}
