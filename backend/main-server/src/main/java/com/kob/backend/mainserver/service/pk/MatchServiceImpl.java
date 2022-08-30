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
    public String startGame(int id1, int botId1, int id2, int botId2) {
        System.out.println("start game " + id1 + " " + botId1 + " " + id2 + " " + botId2);
        WebSocketServer.startGame(id1, botId1, id2, botId2);
        return "start game success";
    }
}
