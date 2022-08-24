package com.kob.backend.socket;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.socket.bean.Player;
import com.kob.backend.util.AuthenticationUtil;
import com.kob.backend.socket.bean.Game;
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
 * @description 匹配系统的 WebSocket 连接
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    private static UserMapper userMapper;
    private static RecordMapper recordMapper;

    @Autowired
    private void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    private void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    public static RecordMapper getRecordMapper() {
        return recordMapper;
    }

    // 存放所有的 userId 和 socket 之间的映射关系
    public static final Map<Long, WebSocketServer> users = new ConcurrentHashMap<>();
    // 匹配池
    private static final Set<User> matchPool = new CopyOnWriteArraySet<>();

    private User user;
    private Session session = null;
    private Game game;

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

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

    // 关闭连接
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
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        System.out.println(event);

        if ("matching".equals(event)) {
            startMatching();
        } else if ("cancel".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
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

            WebSocketServer socket1 = users.get(a.getId());
            WebSocketServer socket2 = users.get(b.getId());

            socket1.game = socket2.game = new Game(socket1, socket2);
        }
    }

    private void stopMatching() {
        matchPool.remove(user);
    }

    private void move(int direction) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        if (player1.getId().equals(user.getId())) {
            player1.setNextStep(direction);
        } else if (player2.getId().equals(user.getId())) {
            player2.setNextStep(direction);
        }
    }
}
