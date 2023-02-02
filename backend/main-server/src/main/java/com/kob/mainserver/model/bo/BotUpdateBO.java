package com.kob.mainserver.model.bo;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Data
public class BotUpdateBO {

    private Long botId;

    private String title;

    private String description;

    private String content;
}
