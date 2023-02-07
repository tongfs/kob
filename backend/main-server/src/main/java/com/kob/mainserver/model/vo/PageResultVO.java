package com.kob.mainserver.model.vo;

import java.util.List;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/5
 */
@Data
public class PageResultVO<T> {

    private long current;

    private long total;

    private List<T> data;
}
