import { useStore } from "vuex";
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, gameMap) {  // ctx是画布, parent是画布的父元素
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.gameMap = gameMap;
        this.L = 0;

        this.rows = 13;
        this.cols = 14;

        this.walls = [];

        this.snakes = [
            new Snake({ id: 0, color: '#4876EC', r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: '#F94848', r: 1, c: this.cols - 2 }, this)
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

    add_listening_events() {
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
                this.store.state.pk.socket.send(JSON.stringify({
                    event: 'move',
                    direction: d,
                }));
            }
        });
    }

    create_walls() {
        const g = this.gameMap;

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
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
}
