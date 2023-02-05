package com.kob.mainserver.model.bean;

import com.kob.mainserver.model.po.User;
import com.kob.mainserver.socket.WebSocket;
import com.kob.mainserver.thread.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {

    private WebSocket webSocket;

    private User user;

    private Game game;
}
