package com.kob.mainserver.model.po;

import java.util.Date;

import com.kob.common.util.GsonUtils;
import com.kob.mainserver.model.bean.Game;
import com.kob.mainserver.model.bean.Player;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
public class Record {

    private Long id;

    private Long userId1;

    private Integer x1, y1;

    private Long userId2;

    private Integer x2, y2;

    private String steps1;

    private String steps2;

    private String map;

    // 1表示a玩家输，2表示b玩家输，3表示平局
    private Integer loserIdentity;

    private Date createTime;

    public Record(Game game) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        this.userId1 = player1.getId();
        this.x1 = player1.getSx();
        this.y1 = player1.getSy();
        this.userId2 = player2.getId();
        this.x2 = player2.getSx();
        this.y2 = player2.getSy();
        this.steps1 = GsonUtils.toJson(player1.getSteps());
        this.steps2 = GsonUtils.toJson(player2.getSteps());
        this.map = GsonUtils.toJson(game.getGameMap());
        this.loserIdentity = game.getLoser();
        this.createTime = new Date();
    }
}
