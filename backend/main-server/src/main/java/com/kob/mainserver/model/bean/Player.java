package com.kob.mainserver.model.bean;

import java.util.ArrayList;
import java.util.List;

import com.kob.mainserver.socket.WebSocket;

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
public class Player {

    public Player(Long id, Integer sx, Integer sy, WebSocket socket) {
        this.id = id;
        this.sx = sx;
        this.sy = sy;
        this.socket = socket;
    }

    private Long id;

    /**
     * 初始时的坐标
     */
    private Integer sx;
    private Integer sy;

    /**
     * socket连接
     */
    private WebSocket socket;

    /**
     * 记录下一步往哪儿走
     */
    private Integer nextStep;

    /**
     * 记录历史走过的方向
     */
    private List<Integer> steps = new ArrayList<>();

    /**
     * 记录自己的状态（0表示没输，1表示输了）
     */
    private Integer lose = 0;

    /**
     * 获得当前蛇的身体
     */
    public List<Cell> getBody() {
        List<Cell> body = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int a = sx, b = sy;
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

    /**
     * 当前回合蛇的身体是否要增长
     */
    private boolean isIncreasing(int turn) {
        if (turn <= 5) return true;
        if (turn <= 11) return true;
        return turn % 3 == 2;
    }

    @AllArgsConstructor
    public static class Cell {
        int x, y;
    }
}
