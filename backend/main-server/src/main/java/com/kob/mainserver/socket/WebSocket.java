package com.kob.mainserver.socket;

import static com.kob.common.enums.FrontOperation.CANCEL;
import static com.kob.common.enums.FrontOperation.MATCH;
import static com.kob.common.enums.FrontOperation.MOVE;

import java.io.IOException;
import java.util.Map;

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
import com.kob.common.util.GsonUtils;
import com.kob.mainserver.model.bean.UserConnection;
import com.kob.mainserver.model.po.User;
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

    private static UserService userService;
    private static GameService gameService;
    private static Map<Long, UserConnection> users;

    private Session session = null;
    private User user;

    @Autowired
    private void setUserService(UserService userService) {
        WebSocket.userService = userService;
    }

    @Autowired
    private void setGameService(GameService gameService) {
        WebSocket.gameService = gameService;
    }

    @Autowired
    private void setUsers(Map<Long, UserConnection> users) {
        WebSocket.users = users;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        long userId = AuthenticationUtils.getUserId(token);
        user = userService.getUserById(userId);
        if (user == null) {
            session.close();
        }
        users.put(userId, new UserConnection(this, user, null));
    }

    @OnClose
    public void onClose() {
        if (user != null) {
            users.remove(user.getId());
            gameService.stopMatching(user);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonObject jsonObject = GsonUtils.fromJson(message, JsonObject.class);
        int event = jsonObject.get("event").getAsInt();
        if (event == MATCH.getCode()) {
            gameService.startMatching(user, users);
        } else if (event == CANCEL.getCode()) {
            gameService.stopMatching(user);
        } else if (event == MOVE.getCode()) {
            int direction = jsonObject.get("direction").getAsInt();
            gameService.playerMove(users.get(user.getId()), direction);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    public synchronized void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
