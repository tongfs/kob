package com.kob.mainserver.service;

import java.util.Map;

import com.kob.mainserver.model.bean.Game;
import com.kob.mainserver.model.bean.UserConnection;
import com.kob.mainserver.model.po.User;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public interface GameService {

    /**
     * 开始匹配
     */
    void startMatching(User user, Map<Long, UserConnection> users);

    /**
     * 取消匹配
     */
    void stopMatching(User user);

    /**
     * 保存游戏结果
     */
    void saveResult(Game game);

    /**
     * 蛇移动
     */
    void playerMove(UserConnection userConnection, int direction);
}
