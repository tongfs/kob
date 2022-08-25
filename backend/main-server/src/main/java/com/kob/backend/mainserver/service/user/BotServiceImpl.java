package com.kob.backend.mainserver.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mainserver.pojo.Bot;
import com.kob.backend.mainserver.pojo.User;
import com.kob.backend.mainserver.mapper.BotMapper;
import com.kob.backend.mainserver.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */

@Service
public class BotServiceImpl implements BotService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> map) {
        User user = AuthenticationUtil.getLoginUser();

        String title = map.get("title");
        String description = map.get("description");
        String content = map.get("content");

        Map<String, String> res = new HashMap<>();

        if (ObjectUtils.isEmpty(title) || ObjectUtils.isEmpty(title.trim())) {
            res.put("msg", "标题不能为空");
            return res;
        }
        if (title.length() > 100) {
            res.put("msg", "标题长度不能超过100");
            return res;
        }
        if (description.length() > 300) {
            res.put("msg", "描述的长度不能超过300");
            return res;
        }
        if (ObjectUtils.isEmpty(content) || ObjectUtils.isEmpty(content.trim())) {
            res.put("msg", "代码不能为空");
            return res;
        }
        if (content.length() > 10000) {
            res.put("msg", "代码长度不能超过10000");
            return res;
        }

        title = title.trim();
        if (ObjectUtils.isEmpty(description) || ObjectUtils.isEmpty(description.trim())) {
            description = "这个用户很懒，什么也没留下~";
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);
        botMapper.insert(bot);
        res.put("msg", "success");

        return res;
    }

    @Override
    public Map<String, String> remove(int botId) {
        User user = AuthenticationUtil.getLoginUser();
        Bot bot = botMapper.selectById(botId);
        Map<String, String> res = new HashMap<>();

        if (bot == null) {
            res.put("msg", "该Bot不存在");
            return res;
        }
        if (!bot.getUserId().equals(user.getId())) {
            res.put("msg", "没有权限删除该Bot");
            return res;
        }

        botMapper.deleteById(botId);

        res.put("msg", "success");
        return res;
    }

    @Override
    public Map<String, String> update(Map<String, String> map) {
        User user = AuthenticationUtil.getLoginUser();
        int botId = Integer.parseInt(map.get("botId"));

        String title = map.get("title");
        String description = map.get("description");
        String content = map.get("content");

        Map<String, String> res = new HashMap<>();

        if (ObjectUtils.isEmpty(title) || ObjectUtils.isEmpty(title.trim())) {
            res.put("msg", "标题不能为空");
            return res;
        }
        if (title.length() > 100) {
            res.put("msg", "标题长度不能超过100");
            return res;
        }
        if (description.length() > 300) {
            res.put("msg", "描述的长度不能超过300");
            return res;
        }
        if (ObjectUtils.isEmpty(content) || ObjectUtils.isEmpty(content.trim())) {
            res.put("msg", "代码不能为空");
            return res;
        }
        if (content.length() > 10000) {
            res.put("msg", "代码长度不能超过10000");
            return res;
        }

        title = title.trim();
        if (ObjectUtils.isEmpty(description) || ObjectUtils.isEmpty(description.trim())) {
            description = "这个用户很懒，什么也没留下~";
        }

        Bot bot = botMapper.selectById(botId);
        if (bot == null) {
            res.put("msg", "该Bot不存在");
        }
        if (bot.getUserId() != user.getId()) {
            res.put("msg", "没有权限修改该Bot");
            return res;
        }

        bot.setTitle(title);
        bot.setDescription(description);
        bot.setContent(content);
        bot.setUpdateTime(new Date());

        botMapper.updateById(bot);

        res.put("msg", "success");
        return res;
    }

    @Override
    public List<Bot> all() {
        User user = AuthenticationUtil.getLoginUser();

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());

        return botMapper.selectList(queryWrapper);
    }
}
