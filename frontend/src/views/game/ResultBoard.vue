<template>
  <div class="result-board">
    <div
      v-if="$store.state.game.loser === 3"
      class="result-board-text"
    >
      🤝Draw
    </div>
    <div
      v-else-if="$store.state.game.loser === $store.state.game.identity"
      class="result-board-text"
    >
      💔Lose
    </div>
    <div
      v-else
      class="result-board-text"
    >
      🏆Win
    </div>
    <div class="col-12 result-board-btn">
      <button
        type="button"
        class="btn btn-warning btn-lg"
        @click="restart"
      >
        确定
      </button>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const restart = () => {
            store.commit('updateStatus', 'matching');
            store.commit('updateOpponent', {
                username: '我的对手',
                avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
            });
            store.commit('updateGame', {
                identity: 0,
                gameMap: null
            });
            store.commit('updateLoser', 0);
        };

        return {
            restart
        };
    }
}
</script>

<style>
div.result-board {
    height: 40vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.8);
    position: absolute;
    top: 30vh;
    left: 35vw;
    border-radius: 5%;
}

div.result-board-text {
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 600;
    padding-top: 5vh;
}

div.result-board-btn {
    padding-top: 10vh;
    text-align: center;
}
</style>