import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
    constructor(r, c, gameMap) {
        super();

        this.r = r;
        this.c = c;
        this.color = '#B37226';
        this.gameMap = gameMap;
    }

    update() {
        this.render();
    }

    render() {
        const L = this.gameMap.L;
        const ctx = this.gameMap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(L * this.c, L * this.r, L, L);
    }
}