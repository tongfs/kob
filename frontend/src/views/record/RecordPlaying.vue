<template>
  <div>
    <PlayGround />
    <TurnBackBoard v-if="$store.state.record.playingStatus === 'over'" />
  </div>
</template>

<script>
import PlayGround from '@/views/game/PlayGround';
import TurnBackBoard from '@/views/record/TurnBackBoard';
import { useStore } from 'vuex';
import router from '@/router';
export default {
    components: {
        PlayGround,
        TurnBackBoard
    },
    setup() {
      const store = useStore();
      store.commit('updatePlayingStatus', 'playing');
        if (!store.state.game.gameMap) {
            router.push({ name: 'record_index' });
        }
    }
}
</script>

<style scoped>

</style>
