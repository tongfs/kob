package com.kob.backend.mainserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kob.backend.mainserver.socket.bean.Game;
import com.kob.backend.mainserver.socket.bean.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tfs
 * @date 2022-08-24
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer aId;

    private Integer aX, aY;

    private Integer bId;

    private Integer bX, bY;

    private String aSteps;

    private String bSteps;

    private String map;

    // 1表示a玩家输，2表示b玩家输，3表示平局
    private Integer loserIdentity;

    private Date createTime;

    public Record(Game game) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        this.aId = player1.getId();
        this.aX = player1.getX();
        this.aY = player1.getY();
        this.bId = player2.getId();
        this.bX = player2.getX();
        this.bY = player2.getY();
        this.aSteps = player1.steps2String();
        this.bSteps = player2.steps2String();
        this.map = game.map2String();
        this.loserIdentity = game.getLoser();
        this.createTime = new Date();
    }


}
