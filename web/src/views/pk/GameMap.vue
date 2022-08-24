<template>
    <div ref="parent" class="game-map">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { GameMap } from "@/assets/scripts/GameMap";
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();
        let canvas = ref(null);
        let parent = ref(null);

        onMounted(() => {
            let gameObject = new GameMap(canvas.value.getContext('2d'), parent.value, store.state.pk.gameMap);
            store.commit('updateGameObject', gameObject);
        });

        return {
            canvas,
            parent,
        }
    }
}
</script>

<style scoped>
div.game-map {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
