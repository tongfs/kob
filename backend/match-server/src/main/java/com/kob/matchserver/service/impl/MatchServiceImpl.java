package com.kob.matchserver.service.impl;

import static com.kob.common.constant.Constants.MAIN_SERVER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kob.common.model.dto.MatchResultDTO;
import com.kob.common.model.dto.PlayerDTO;
import com.kob.matchserver.service.MatchService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Service
public class MatchServiceImpl implements MatchService, Runnable {

    private static final Class<MatchService> lock = MatchService.class;
    private static final String START_GAME_URI = "/game/start";
    private static final int MATCH_INTERVAL = 1000;

    private static List<PlayerDTO> players = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void add(PlayerDTO player) {
        System.out.println("add player: " + player.getUserId() + " " + player.getScore());
        synchronized (lock) {
            players.add(player);
        }
    }

    @Override
    public void remove(long userId) {
        System.out.println("remove player: " + userId);
        synchronized (lock) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getUserId() == userId) {
                    players.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(MATCH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock) {
                match();
                increaseWaitingTime();
            }
        }
    }

    public MatchServiceImpl() {
        new Thread(this).start();
    }

    /**
     * 增加玩家在匹配池里等待的时间
     */
    private void increaseWaitingTime() {
        for (PlayerDTO player : players) {
            player.addWaitingTime();
        }
    }

    /**
     * 匹配玩家
     */
    private void match() {
        System.out.println("match players: " + players);

        int n = players.size();
        boolean[] used = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < n; j++) {
                if (used[j]) continue;
                PlayerDTO player1 = players.get(i);
                PlayerDTO player2 = players.get(j);
                if (checkMatched(player1, player2)) {
                    used[i] = used[j] = true;
                    sendMatchResult(player1, player2);
                    break;
                }
            }
        }

        List<PlayerDTO> newPlayers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }

        players = newPlayers;
    }

    /**
     * 判断两名玩家是否匹配
     */
    private boolean checkMatched(PlayerDTO player1, PlayerDTO player2) {
        int scoreDelta = Math.abs(player1.getScore() - player2.getScore());
        long waitingTime = Math.min(player1.getWaitingTime(), player2.getWaitingTime());
        return scoreDelta <= waitingTime * 10;
    }

    /**
     * 返回匹配结果
     */
    private void sendMatchResult(PlayerDTO player1, PlayerDTO player2) {
        MatchResultDTO matchResultDTO = new MatchResultDTO();
        matchResultDTO.setPlayerId1(player1.getUserId());
        matchResultDTO.setPlayerId2(player2.getUserId());
        restTemplate.postForObject(MAIN_SERVER + START_GAME_URI, matchResultDTO, Object.class);
    }
}
