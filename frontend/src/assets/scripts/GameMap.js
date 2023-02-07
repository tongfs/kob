import { useStore } from "vuex";
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, gameMap) {  // ctx是画布, parent是画布的父元素
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;

        this.rows = gameMap.length;
        this.cols = gameMap[0].length;

        this.gameMap = gameMap;
        this.walls = [];

        this.snakes = [
            new Snake({ id: 1, color: '#4876EC', r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 2, color: '#F94848', r: 1, c: this.cols - 2 }, this)
        ];

        this.store = useStore();
    }

    start() {
        this.add_listening_events();
        this.create_walls();
    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render() {
        const color_even = '#AAD751', color_odd = '#A2D149'

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if ((r + c) % 2) {
                    this.ctx.fillStyle = color_odd;
                } else {
                    this.ctx.fillStyle = color_even;
                }
                this.ctx.fillRect(this.L * c, this.L * r, this.L, this.L);
            }
        }

    }

    // 增加监听事件
    add_listening_events() {
        if (this.store.state.record.isRecord) {
            // 在播放录像
            let round = 0;
            const steps1 = this.store.state.record.steps1;
            const steps2 = this.store.state.record.steps2;
            const loser = this.store.state.record.recordLoser;
            const [snake1, snake2] = this.snakes;
            const intervalId = setInterval(() => {
                if (round >= steps1.length - 1) {
                    if (loser === 1 || loser === 3) {
                        snake1.die(steps1[round]);
                    }
                    if (loser === 2 || loser === 3) {
                        snake2.die(steps2[round]);
                    }
                    this.store.commit('updatePlayingStatus', 'over')
                    clearInterval(intervalId);
                } else {
                    snake1.set_direction(steps1[round]);
                    snake2.set_direction(steps2[round]);
                    round++;
                }
            }, 500);
        } else {
            /// 在对战
            // 聚焦到画布上
            this.ctx.canvas.focus();

            // 监听按键
            this.ctx.canvas.addEventListener('keydown', e => {
                let d = -1;
                if (e.key === 'w' || e.key === 'ArrowUp') d = 0;
                else if (e.key === 'd' || e.key === 'ArrowRight') d = 1;
                else if (e.key === 's' || e.key === 'ArrowDown') d = 2;
                else if (e.key === 'a' || e.key === 'ArrowLeft') d = 3;

                if (d >= 0) {
                    this.store.state.game.socket.send(JSON.stringify({
                        event: 2,
                        direction: d
                    }));
                }
            });
        }
    }

    // 创建障碍物
    create_walls() {
        const g = this.gameMap;
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    // 判断玩家有没有准备好进行下一步
    check_ready() {
        // 准备好的条件：1. 有指令  2. 状态不是静止
        for (const snake of this.snakes) {
            if (snake.direction === -1) return false;
            if (snake.status !== 'idle') return false;
        }
        return true;
    }

    // 让两条蛇进入下一回合
    next_step() {
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    // 每一帧更新画布大小
    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
}
