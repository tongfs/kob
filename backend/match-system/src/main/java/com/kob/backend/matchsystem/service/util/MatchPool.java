package com.kob.backend.matchsystem.service.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
@Component
public class MatchPool implements Runnable {

    private static final String START_GAME_URL = "http://127.0.0.1:3000/pk/start";

    private List<Player> players = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    public MatchPool() {
        // 启动线程
        new Thread(this).start();
    }

    public synchronized void addPlayer(long userId, Integer rating) {
        players.add(new Player(userId, rating));
    }

    public synchronized void removePlayer(long userId) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUserId() == userId) {
                players.remove(i);
                break;
            }
        }
    }

    /**
     * 增加玩家在匹配池里等待的时间
     */
    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    /**
     * 匹配玩家
     */
    private void match() {
        System.out.println("match players: " + players.toString());

        int n = players.size();
        boolean[] used = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < n; j++) {
                if (used[j]) continue;
                Player player1 = players.get(i);
                Player player2 = players.get(j);
                if (checkMatched(player1, player2)) {
                    used[i] = used[j] = true;
                    sendResult(player1, player2);
                    break;
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
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
    private boolean checkMatched(Player player1, Player player2) {
        int ratingDelta = Math.abs(player1.getRating() - player2.getRating());
        long waitingTime = Math.min(player1.getWaitingTime(), player2.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    /**
     * 返回匹配结果
     */
    private void sendResult(Player player1, Player player2) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id1", player1.getUserId().toString());
        map.add("id2", player2.getUserId().toString());
        restTemplate.postForObject(START_GAME_URL, map, String.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                match();
                increaseWaitingTime();
            }
        }
    }

    @Data
    static class Player {
        private Long userId;
        private Integer rating;
        private Long waitingTime;

        public Player(Long userId, Integer rating) {
            this.userId = userId;
            this.rating = rating;
            this.waitingTime = 0L;
        }
    }
}
