package com.kob.mainserver.socket;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.kob.common.enums.MatchStatus;
import com.kob.common.enums.SocketResult;
import com.kob.common.model.SocketResp;
import com.kob.common.util.GsonUtils;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.MatchResultVO;
import com.kob.mainserver.service.GameService;
import com.kob.mainserver.service.UserService;
import com.kob.mainserver.util.AuthenticationUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocket {

    /**
     * 存放所有的 userId 和 socket 之间的映射关系
     */
    private static final Map<Long, WebSocket> users = new ConcurrentHashMap<>();

    /**
     * 匹配池
     */
    private static final Set<User> matchPool = new CopyOnWriteArraySet<>();

    private Session session = null;
    private User user;

    private static UserService userService;
    private static GameService gameService;

    @Autowired
    private void setUserMapper(UserService userService) {
        WebSocket.userService = userService;
    }

    @Autowired
    private void setGameService(GameService gameService) {
        WebSocket.gameService = gameService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        long userId = AuthenticationUtils.getUserId(token);
        user = userService.getUserById(userId);
        if (user == null) {
            session.close();
        }
        users.put(userId, this);
    }

    @OnClose
    public void onClose() {
        if (user != null) {
            users.remove(user.getId());
            matchPool.remove(user);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonObject jsonObject = GsonUtils.fromJson(message, JsonObject.class);
        int event = jsonObject.get("event").getAsInt();
        if (event == MatchStatus.MATCH.getCode()) {
            startMatching();
        } else if (event == MatchStatus.CANCEL.getCode()) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    private synchronized void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始匹配对手
     */
    private void startMatching() {
        System.out.println("start matching");

        matchPool.add(user);

        while (matchPool.size() >= 2) {
            Iterator<User> iterator = matchPool.iterator();
            User a = iterator.next();
            User b = iterator.next();
            matchPool.remove(a);
            matchPool.remove(b);

            int[][] gameMap = gameService.getGameMap();
            MatchResultVO result1 = new MatchResultVO(b.getUsername(), b.getAvatar(), gameMap);
            MatchResultVO result2 = new MatchResultVO(a.getUsername(), a.getAvatar(), gameMap);

            users.get(a.getId()).sendMessage(SocketResp.ok(SocketResult.MATCHING_SUCCESS, result1));
            users.get(b.getId()).sendMessage(SocketResp.ok(SocketResult.MATCHING_SUCCESS, result2));
        }
    }

    /**
     * 停止匹配
     */
    private void stopMatching() {
        System.out.println("stop matching");
        matchPool.remove(user);
    }
}
