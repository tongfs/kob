package com.kob.mainserver.service.impl;

import static com.kob.common.enums.ErrorCode.BOT_CONTENT_BLANK;
import static com.kob.common.enums.ErrorCode.BOT_CONTENT_TO_LONG;
import static com.kob.common.enums.ErrorCode.BOT_DESC_TO_LONG;
import static com.kob.common.enums.ErrorCode.BOT_NOT_EXIST;
import static com.kob.common.enums.ErrorCode.BOT_TITLE_BLANK;
import static com.kob.common.enums.ErrorCode.BOT_TITLE_TOO_LONG;
import static com.kob.common.enums.ErrorCode.NO_BOT_PERMISSION;
import static com.kob.common.enums.ErrorCode.REACH_LIMIT_NUMBER_OF_BOTS;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.common.exception.BotException;
import com.kob.mainserver.mapper.BotMapper;
import com.kob.mainserver.model.bo.BotAddBO;
import com.kob.mainserver.model.bo.BotUpdateBO;
import com.kob.mainserver.model.po.Bot;
import com.kob.mainserver.service.BotService;
import com.kob.mainserver.util.AuthenticationUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Service
public class BotServiceImpl implements BotService {

    private static final int BOT_TITLE_MAX_LENGTH = 20;
    private static final int BOT_DESC_MAX_LENGTH = 255;
    private static final int BOT_CONTENT_MAX_LENGTH = 10000;
    private static final int BOT_MAX_NUMBER = 10;
    private static final String BOT_DEFAULT_DESC = "这个用户很懒，什么都没有留下~";

    @Autowired
    private BotMapper botMapper;

    @Override
    public void add(BotAddBO botAddBO) {

        long count = selectCountByUserId(AuthenticationUtils.getUserId());
        if (count >= BOT_MAX_NUMBER) {
            throw new BotException(REACH_LIMIT_NUMBER_OF_BOTS);
        }

        String title = botAddBO.getTitle();
        String description = botAddBO.getDescription();
        String code = botAddBO.getCode();

        checkBotParam(title, description, code);

        long userId = AuthenticationUtils.getUserId();
        Date now = new Date();
        Bot bot = new Bot();
        BeanUtils.copyProperties(botAddBO, bot);
        if (StringUtils.isEmpty(bot.getDescription())) {
            bot.setDescription(BOT_DEFAULT_DESC);
        }
        bot.setUserId(userId);
        bot.setCreateTime(now);
        bot.setUpdateTime(now);

        botMapper.insert(bot);
    }

    @Override
    public void delete(Long botId) {
        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            throw new BotException(BOT_NOT_EXIST);
        }
        if (bot.getUserId() != AuthenticationUtils.getUserId()) {
            throw new BotException(NO_BOT_PERMISSION);
        }

        botMapper.deleteById(botId);
    }

    @Override
    public void update(BotUpdateBO botUpdateBO) {
        Long botId = botUpdateBO.getBotId();
        String title = botUpdateBO.getTitle();
        String description = botUpdateBO.getDescription();
        String code = botUpdateBO.getCode();

        checkBotParam(title, description, code);

        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            throw new BotException(BOT_NOT_EXIST);
        }
        if (bot.getUserId() != AuthenticationUtils.getUserId()) {
            throw new BotException(NO_BOT_PERMISSION);
        }

        BeanUtils.copyProperties(botUpdateBO, bot);
        bot.setUpdateTime(new Date());
        botMapper.updateById(bot);
    }

    @Override
    public List<Bot> getAllBot() {
        LambdaQueryWrapper<Bot> queryWrapper = new LambdaQueryWrapper<>();
        long userId = AuthenticationUtils.getUserId();
        queryWrapper.eq(Bot::getUserId, userId);
        return botMapper.selectList(queryWrapper);
    }

    @Override
    public Bot selectUserBotById(long botId, long userId) {
        LambdaQueryWrapper<Bot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bot::getId, botId).eq(Bot::getUserId, userId);
        return botMapper.selectOne(queryWrapper);
    }

    @Override
    public void addDefaultBot(Bot bot) {
        botMapper.insert(bot);
    }

    /**
     * 检查Bot的参数
     */
    private void checkBotParam(String title, String description, String content) {
        if (StringUtils.isBlank(title)) {
            throw new BotException(BOT_TITLE_BLANK);
        }
        if (StringUtils.length(title) > BOT_TITLE_MAX_LENGTH) {
            throw new BotException(BOT_TITLE_TOO_LONG);
        }
        if (StringUtils.length(description) > BOT_DESC_MAX_LENGTH) {
            throw new BotException(BOT_DESC_TO_LONG);
        }
        if (StringUtils.isBlank(content)) {
            throw new BotException(BOT_CONTENT_BLANK);
        }
        if (StringUtils.length(content) > BOT_CONTENT_MAX_LENGTH) {
            throw new BotException(BOT_CONTENT_TO_LONG);
        }
    }

    /**
     * 根据userId查询Bot数量
     */
    private long selectCountByUserId(long userId) {
        LambdaQueryWrapper<Bot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bot::getUserId, userId);
        return botMapper.selectCount(queryWrapper);
    }
}
