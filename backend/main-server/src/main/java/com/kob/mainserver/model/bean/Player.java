package com.kob.mainserver.model.bean;

import static com.kob.common.constant.Constants.dx;
import static com.kob.common.constant.Constants.dy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.kob.common.model.dto.Cell;
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
public class Player {

    public Player(Long id, Integer sx, Integer sy, String botCode, WebSocket socket) {
        this.id = id;
        this.sx = sx;
        this.sy = sy;
        this.botCode = botCode;
        this.socket = socket;
        this.getBody().offer(new Cell(sx, sy));
    }

    private Long id;

    /**
     * 初始时的坐标
     */
    private int sx;
    private int sy;

    /**
     * bot代码
     */
    private String botCode;

    /**
     * socket连接
     */
    private WebSocket socket;

    /**
     * 记录下一步往哪儿走
     */
    private int nextStep = -1;

    /**
     * 当前蛇的身体
     */
    private LinkedList<Cell> body = new LinkedList<>();

    /**
     * 记录历史走过的方向
     */
    private List<Integer> steps = new ArrayList<>();

    /**
     * 判断下一步
     */
    public boolean judge(List<Cell> opponent, Game game) {

        Cell head = body.getLast();
        Cell newHead = new Cell(head.getX() + dx[nextStep], head.getY() + dy[nextStep]);
        int[][] gameMap = game.getGameMap();

        // 判断是否撞到障碍物
        if (gameMap[newHead.getX()][newHead.getY()] == 1) {
            return false;
        }

        // 判断是否碰到蛇身
        for (Cell cell : body) {
            if (isOverlapped(newHead, cell)) {
                return false;
            }
        }
        for (Cell cell : opponent) {
            if (isOverlapped(newHead, cell)) {
                return false;
            }
        }

        // 如果下一步合法，就更新蛇身体
        body.offer(newHead);

        return true;
    }

    /**
     * 判断单元格是否重复
     */
    private boolean isOverlapped(Cell a, Cell b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }
}
