package com.kob.backend.socket;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.util.AuthenticationUtil;
import com.kob.backend.util.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author tfs
 * @date 2022-08-22
 * @description
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    private static UserMapper userMapper;

    @Autowired
    private void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    // 存放所有的 userId 和 socket 之间的映射关系
    private static final Map<Long, WebSocketServer> users = new ConcurrentHashMap<>();
    // 匹配池
    private static final Set<User> matchPool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session = null;

    // 建立连接
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("open");
        Long userId = AuthenticationUtil.getUserId(token);
        user = userMapper.selectById(userId);

        if (user == null) {
            session.close();
        }

        users.put(userId, this);
    }

    // 关闭链接
    @OnClose
    public void onClose() {
        System.out.println("close");
        if (user != null) {
            users.remove(user.getId());
            matchPool.remove(user);
        }
    }

    // 从 Client 接收消息
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        System.out.println(event);

        if ("matching".equals(event)) {
            startMatching();
        } else if ("cancel".equals(event)) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public synchronized void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startMatching() {
        matchPool.add(user);

        while (matchPool.size() >= 2) {
            Iterator<User> iterator = matchPool.iterator();
            User a = iterator.next();
            User b = iterator.next();
            matchPool.remove(a);
            matchPool.remove(b);
            int[][] gameMap = GameUtil.getGameMap();

            System.out.println(a);
            System.out.println(b);

            JSONObject respA = new JSONObject();
            respA.put("event", "matching_success");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_profile", b.getProfile());
            respA.put("gamemap", gameMap);
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "matching_success");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_profile", a.getProfile());
            respB.put("gamemap", gameMap);
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void stopMatching() {
        System.out.println("stop-matching");
        matchPool.remove(user);
    }
}
