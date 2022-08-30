package com.kob.backend.mainserver.service.pk;

import com.kob.backend.mainserver.socket.WebSocketServer;
import org.springframework.stereotype.Service;

/**
 * @author tfs
 * @date 2022-08-30
 * @description
 */
@Service
public class PkBotServiceImpl implements PkBotService {
    @Override
    public String setNextStep(int userId, int direction) {
        WebSocketServer.botSetNextStep(userId, direction);
        return "set next step success";
    }
}
