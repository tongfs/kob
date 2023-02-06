<template>
  <div>
    <PlayGround />
    <TurnBackBoard v-if="$store.state.record.playingStatus === 'over'" />
  </div>
</template>

<script>
import PlayGround from '@/components/PlayGround';
import TurnBackBoard from '@/components/TurnBackBoard';
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
        if (!store.state.pk.gameMap) {
            router.push({ name: 'record_index' });
        }
    }
}
</script>

<style scoped>

</style>