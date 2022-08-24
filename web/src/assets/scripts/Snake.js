import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor(info, gameMap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gameMap = gameMap;

        this.body = [new Cell(info.r, info.c)];  // 存放蛇的身体, body[0]存放蛇头

        this.speed = 5;         // 定义蛇每秒走多少个格子
        this.direction = -1;    // -1表示没有指令，0123分别表示上右下左
        this.status = 'idle';   // idle表示静止，move表示正在移动，die表示死亡

        this.next_cell = null;  // 下一步走到哪儿
        this.dr = [-1, 0, 1, 0];    // 行的偏移量
        this.dc = [0, 1, 0, -1];    // 列的偏移量

        this.turns = 0;
        this.eps = 1e-2;

        this.eye_direction = this.id === 0 ? 0 : 2;
        this.eye_dx = [[-1, 1], [1, 1], [1, -1], [-1, -1]];
        this.eye_dy = [[-1, -1], [-1, 1], [1, 1], [1, -1]];
    }

    start() {}

    update() {
        if (this.status === 'move')
            this.move();
        this.render();
    }

    render() {
        const factor = 0.9;
        const L = this.gameMap.L;
        const ctx = this.gameMap.ctx;

        ctx.fillStyle = this.color;
        for (const cell of this.body) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L * factor / 2, 0, Math.PI * 2);
            ctx.fill();
        }

        for (let i = 0; i < this.body.length - 1; i++) {
            const [a, b] = [this.body[i], this.body[i + 1]];
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - factor / 2) * L, Math.min(a.y, b.y) * L, L * factor, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - factor / 2) * L, Math.abs(a.x - b.x) * L, L * factor);
            }
        }

        ctx.fillStyle = '#000000';
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.body[0].x + this.eye_dx[this.eye_direction][i] * 0.2) * L;
            const eye_y = (this.body[0].y + this.eye_dy[this.eye_direction][i] * 0.2) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    set_direction(d) {
        this.direction = d;
    }
    
    // 将蛇的状态变为走下一步
    next_step() {
        const d = this.eye_direction = this.direction;
        this.next_cell = new Cell(this.body[0].r + this.dr[d], this.body[0].c + this.dc[d]);
        
        this.direction = -1;    // 清空操作
        this.status = 'move';
        this.turns++;

        const k = this.body.length;
        for (let i = k; i > 0; i--) {
            this.body[i] = JSON.parse(JSON.stringify(this.body[i - 1]));
        }
    }

    move() {
        const dx = this.next_cell.x - this.body[0].x;
        const dy = this.next_cell.y - this.body[0].y;
        const d = Math.sqrt(dx * dx + dy * dy);
        
        if (d < this.eps) {
            this.body[0] = this.next_cell;
            this.next_cell = null;
            this.status = 'idle';

            if (!this.check_increasing()) {
                this.body.pop();
            }

        } else {
            const move_d = this.speed * this.timedelta / 1000;
            this.body[0].x += move_d * dx / d;
            this.body[0].y += move_d * dy / d;

            if (!this.check_increasing()) {
                const k = this.body.length;
                const tail = this.body[k - 1], target = this.body[k - 2];
                const tail_dx = target.x - tail.x, tail_dy = target.y - tail.y;
                tail.x += move_d * tail_dx / d;
                tail.y += move_d * tail_dy / d;
            }
        }
    }

    // 判断当前回合蛇的长度是否要增加
    check_increasing() {
        if (this.turns <= 10) return true;
        if (this.turns % 3 === 1) return true;
        return false;
    }

    die() {
        this.status = 'die';
        this.color = '#FFFFFF';
    }
}