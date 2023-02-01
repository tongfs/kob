import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {  // ctx是画布, parent是画布的父元素
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;

        this.rows = 13;
        this.cols = 14;

        this.blocks_count = 30;   //  障碍物的数量
        this.walls = [];

        this.snakes = [
            new Snake({ id: 1, color: '#4876EC', r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 2, color: '#F94848', r: 1, c: this.cols - 2 }, this)
        ];
    }

    start() {
        this.add_listening_events();
        while (!this.create_walls());
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
        // 聚焦到画布上
        this.ctx.canvas.focus();

        // 监听按键
        const [snake1, snake2] = this.snakes;
        this.ctx.canvas.addEventListener('keydown', e => {
            if (e.key === 'w') snake1.set_direction(0);
            else if (e.key === 'd') snake1.set_direction(1);
            else if (e.key === 's') snake1.set_direction(2);
            else if (e.key === 'a') snake1.set_direction(3);
            else if (e.key === 'ArrowUp') snake2.set_direction(0);
            else if (e.key === 'ArrowRight') snake2.set_direction(1);
            else if (e.key === 'ArrowDown') snake2.set_direction(2);
            else if (e.key === 'ArrowLeft') snake2.set_direction(3);
        });
    }

    // 判断地图是否连通
    check_connectivity(g, x1, y1, x2, y2) {
        if (x1 == x2 && y1 == y2) return true;
        g[x1][y1] = true;

        let dx = [0, -1, 0, 1], dy = [-1, 0, 1, 0];
        for (let i = 0; i < 4; i++) {
            let x = x1 + dx[i], y = y1 + dy[i];
            if (!g[x][y] && this.check_connectivity(g, x, y, x2, y2))
                return true;
        }

        return false;
    }

    // 创建障碍物
    create_walls() {
        const g = [];
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        // 给四周加上障碍物
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }
        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // 创建随机障碍物
        for (let cnt = 0; cnt < this.blocks_count; ) {
            let r = parseInt(Math.random() * this.rows);
            let c = parseInt(Math.random() * this.cols);

            if (g[r][c]) continue;
            if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

            g[r][c] = g[this.rows - r - 1][this.cols - c - 1] = true;

            if (r == this.rows - r - 1 && c == this.cols - c - 1) cnt += 1;
            else cnt +=2;
        }

        const copy_g = JSON.parse(JSON.stringify(g));
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2))
            return false;

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
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

    // 判断玩家走位是否合法
    check_valid(cell) {
        // 撞墙
        for (const wall of this.walls) {
            if (cell.r === wall.r && cell.c === wall.c) {
                return false;
            }
        }

        // 撞到自己或他人
        for (const snake of this.snakes) {
            let k = snake.body.length;
            if (!snake.check_increasing()) k--;
            for (let i = 0; i < k; i++) {
                if (snake.body[i].r === cell.r && snake.body[i].c === cell.c) {
                    return false;
                }
            }
        }

        return true;
    }
}
