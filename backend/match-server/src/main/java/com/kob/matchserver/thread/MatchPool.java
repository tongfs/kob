package com.kob.matchserver.thread;

import java.util.ArrayList;
import java.util.List;

import com.kob.common.model.dto.PlayerDTO;
import com.kob.matchserver.service.MatchService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public class MatchPool implements Runnable {

    public MatchPool(MatchService matchService) {
        this.matchService = matchService;
        new Thread(this).start();
    }
    private static final long MATCH_INTERVAL = 1000;

    private List<PlayerDTO> players = new ArrayList<>();

    private final MatchService matchService;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(MATCH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                match();
                increaseWaitingTime();
            }
        }
    }

    /**
     * 新增匹配玩家
     */
    public void add(PlayerDTO player) {
        synchronized (this) {
            players.add(player);
        }
    }

    /**
     * 从匹配池中取消匹配
     */
    public void remove(long userId) {
        synchronized (this) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getUserId() == userId) {
                    players.remove(i);
                    break;
                }
            }
        }
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
        matchService.sendMatchResult(player1, player2);
    }
}
