package com.kob.mainserver.service.impl;

import org.springframework.stereotype.Service;

import com.kob.mainserver.service.GameService;
import com.kob.mainserver.util.GameUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Service
public class GameServiceImpl implements GameService {

    @Override
    public int[][] getGameMap() {
        return GameUtils.getGameMap();
    }
}
