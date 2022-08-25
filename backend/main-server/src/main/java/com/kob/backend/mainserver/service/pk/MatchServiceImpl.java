package com.kob.backend.mainserver.service.pk;

import com.kob.backend.mainserver.socket.WebSocketServer;
import org.springframework.stereotype.Service;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
@Service
public class MatchServiceImpl implements MatchService {
    @Override
    public String startGame(long id1, long id2) {
        System.out.println("start game " + id1 + " " + id2);
        WebSocketServer.startGame(id1, id2);
        return "start game success";
    }
}
