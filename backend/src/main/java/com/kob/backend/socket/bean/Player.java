package com.kob.backend.socket.bean;

import com.kob.backend.socket.WebSocketServer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tfs
 * @date 2022-08-23
 * @description 记录玩家的信息
 */

@Data
public class Player {

    private Long id;

    // 初始时的坐标
    private Integer x, y;

    // socket连接
    private WebSocketServer socket;

    // 记录每一步的方向
    private List<Integer> steps;

    // 记录下一步往哪儿走
    private Integer nextStep;

    // 记录自己的状态（0表示没输，1表示输了）
    private Integer lose = 0;

    public Player(WebSocketServer socket, Integer x, Integer y) {
        this.id = socket.getUser().getId();
        this.x = x;
        this.y = y;
        this.socket = socket;
        this.steps = new ArrayList<>();
    }

    public void setNextStep(Integer nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * 当前回合蛇的身体是否要增长
     */
    private boolean isIncreasing(int turn) {
        return turn <= 10 || turn % 3 == 1;
    }

    /**
     * 获得当前蛇的身体
     */
    public List<Cell> getBody() {
        List<Cell> body = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int a = x, b = y;
        body.add(new Cell(a, b));

        for (int i = 0; i < steps.size(); i++) {
            int d = steps.get(i);
            a += dx[d];
            b += dy[d];
            body.add(new Cell(a, b));
            if (!isIncreasing(i + 1)) {
                body.remove(0);
            }
        }

        return body;
    }

    @AllArgsConstructor
    static class Cell {
        int x, y;
    }

    public String steps2String() {
        StringBuilder sb = new StringBuilder();
        for (Integer step : steps) {
            sb.append(step);
        }
        return sb.toString();
    }
}
