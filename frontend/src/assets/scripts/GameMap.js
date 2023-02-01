import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {  // ctx是画布, parent是画布的父元素
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;

        this.rows = 13;
        this.cols = 13;
        this.blocks_count = 30;   //  障碍物的数量

        this.walls = [];
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
        for (let i = 0; i < this.blocks_count; i += 2) {
            while (true) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);

                if (g[r][c]) continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

                g[r][c] = g[c][r] = true;
                break;
            }
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

    start() {
        while (!this.create_walls());
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
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
}
