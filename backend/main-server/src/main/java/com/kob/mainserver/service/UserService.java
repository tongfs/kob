package com.kob.mainserver.service;

import com.kob.mainserver.model.bo.UserLoginBO;
import com.kob.mainserver.model.bo.UserRegisterBO;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.PageResultVO;
import com.kob.mainserver.model.vo.UserInfoVO;
import com.kob.mainserver.model.vo.UserLoginVO;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
public interface UserService {
    /**
     * 注册
     */
    void register(UserRegisterBO userRegisterBO);

    /**
     * 登录
     */
    UserLoginVO login(UserLoginBO userLoginBO);

    /**
     * 获取个人信息
     */
    UserInfoVO getInfo();

    /**
     * 通过userId获取User实例
     */
    User selectById(long userId);

    /**
     * 分页获取天梯排名
     */
    PageResultVO<UserInfoVO> getRankList(long page);

    /**
     * 游戏结束后更新玩家天梯分
     */
    void updateScore(int loser, long id1, long id2);
}
