<template>
  <div class="turnback-board">
    <div class="col-12 turnback-board-btn">
      <button
        type="button"
        class="btn btn-warning btn-lg"
        @click="turnback"
      >
        返回
      </button>
    </div>
  </div>          
</template>

<script>
import router from '@/router';
import { useStore } from 'vuex';

export default {
    setup() {
      const store = useStore();
        const turnback = () => {
          store.commit('updatePlayingStatus', 'playing')
          router.push({name: 'record_index'});
        };
        return {
            turnback
        };
    }
}
</script>

<style>
div.turnback-board {
    height: 40vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.8);
    position: absolute;
    top: 30vh;
    left: 35vw;
    border-radius: 5%;
}

div.turnback-board-text {
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 600;
    padding-top: 5vh;
}

div.turnback-board-btn {
    padding-top: 18vh;
    text-align: center;
}
</style>