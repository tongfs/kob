package com.kob.mainserver.service.impl;

import static com.kob.common.enums.ErrorCode.BOT_CONTENT_BLANK;
import static com.kob.common.enums.ErrorCode.BOT_CONTENT_TO_LONG;
import static com.kob.common.enums.ErrorCode.BOT_DESC_TO_LONG;
import static com.kob.common.enums.ErrorCode.BOT_NOT_EXIST;
import static com.kob.common.enums.ErrorCode.BOT_TITLE_BLANK;
import static com.kob.common.enums.ErrorCode.BOT_TITLE_TOO_LONG;
import static com.kob.common.enums.ErrorCode.NO_BOT_PERMISSION;

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

    private static final Integer BOT_TITLE_MAX_LENGTH = 20;
    private static final Integer BOT_DESC_MAX_LENGTH = 255;
    private static final Integer BOT_CONTENT_MAX_LENGTH = 2000;
    private static final Integer BOT_DEFAULT_RATING = 1500;

    @Autowired
    private BotMapper botMapper;

    @Override
    public void add(BotAddBO botAddBO) {

        String title = botAddBO.getTitle();
        String description = botAddBO.getDescription();
        String content = botAddBO.getContent();

        checkBotParam(title, description, content);

        long userId = AuthenticationUtils.getUserId();
        Date now = new Date();
        Bot bot = new Bot();
        BeanUtils.copyProperties(botAddBO, bot);
        bot.setUserId(userId);
        bot.setRating(BOT_DEFAULT_RATING);
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
        String content = botUpdateBO.getContent();

        checkBotParam(title, description, content);

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
}
