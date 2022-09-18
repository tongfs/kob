package com.kob.backend.mainserver.socket;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mainserver.mapper.BotMapper;
import com.kob.backend.mainserver.mapper.RecordMapper;
import com.kob.backend.mainserver.mapper.UserMapper;
import com.kob.backend.mainserver.pojo.User;
import com.kob.backend.mainserver.socket.bean.Game;
import com.kob.backend.mainserver.socket.bean.Player;
import com.kob.backend.mainserver.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private static RestTemplate restTemplate;
    private static BotMapper botMapper;

    // 存放所有的 userId 和 socket 之间的映射关系
    public static final Map<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    private static final String ADD_PLAYER_URL = "http://127.0.0.1:3001/pk/match/add";
    private static final String REMOVE_PLAYER_URL = "http://127.0.0.1:3001/pk/match/remove";

    private User user;
    private Session session = null;
    private Game game;

    @Autowired
    private void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    private void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    private void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    public static RecordMapper getRecordMapper() {
        return recordMapper;
    }

    public static UserMapper getUserMapper() {
        return userMapper;
    }

    public static BotMapper getBotMapper() {
        return botMapper;
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public User getUser() {
        return user;
    }

    // 建立连接
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("open");
        int userId = AuthenticationUtil.getUserId(token);
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
            stopMatching();
        }
    }

    // 从 Client 接收消息
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        System.out.println(event);

        if ("matching".equals(event)) {
            startMatching(data.getLong("botId"));
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

    private void startMatching(Long botId) {
        System.out.println("start matching");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("userId", user.getId().toString());
        map.add("rating", user.getRating().toString());
        map.add("botId", botId.toString());

        restTemplate.postForObject(ADD_PLAYER_URL, map, String.class);
    }

    public static void startGame(int id1, int botId1, int id2, int botId2) {
        Game game = new Game(id1, botId1, id2, botId2);
        if (users.get(id1) != null) {
            users.get(id1).game = game;
        }
        if (users.get(id2) != null) {
            users.get(id2).game = game;
        } else if (id2 < 0) {
            Game.robotGameHashMap.put(id2, game);
        }
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", user.getId().toString());
        restTemplate.postForObject(REMOVE_PLAYER_URL, map, String.class);
    }

    /**
     * 命令来自于玩家键盘操作
     */
    private void move(int direction) {
        // TODO 可以将这部分逻辑封装到Game里；并且和下面的方法在一定程度上整合
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        Integer userId = user.getId();
        if (player1.getId().equals(userId)) {
            if (player1.getBotId() == 0) {
                player1.setNextStep(direction);
            }
        } else if (player2.getId().equals(userId)) {
            if (player2.getBotId() == 0) {
                player2.setNextStep(direction);
            }
        }
    }

    /**
     * 命令来自于bot代码的返回值
     */
    public static void botSetNextStep(int userId, int direction) {
        Game game;
        if (userId < 0) {
            game = Game.robotGameHashMap.get(userId);
        } else {
            game = users.get(userId).game;
        }
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        if (player1.getId().equals(userId)) {
            player1.setNextStep(direction);
        } else if (player2.getId().equals(userId)) {
            player2.setNextStep(direction);
        }
    }
}
