<template>
  <div>
    <MatchPanel v-if="$store.state.pk.status === 'matching'" />
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <ResultBoard v-if="$store.state.pk.loser" />
  </div>
</template>

<script>
import PlayGround from '@/components/PlayGround';
import MatchPanel from '@/components/MatchPanel';
import ResultBoard from '@/components/ResultBoard';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
  components: {
    PlayGround,
    MatchPanel,
    ResultBoard
  },

  setup() {
    const store = useStore();
    const url = `${store.state.ws}/${store.state.user.token}`;

    let socket = null;

    onMounted(() => {
      socket = new WebSocket(url);

      store.commit('updateOpponent', {
        username: '我的对手',
        avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
      });

      socket.onopen = () => {
        store.commit('updateSocket', socket)
      };

      socket.onmessage = msg => {
        const resp = JSON.parse(msg.data);
        const data = resp.data;

        if (resp.code === 1) {
          store.commit('updateOpponent', data.opponent);
          store.commit('updateGame', data);
          setTimeout(() => {
            store.commit('updateStatus', 'playing')
          }, 2000);
        } else if (resp.code === 2) {
          const game = store.state.pk.game;
          const [snake1, snake2] = game.snakes;
          snake1.set_direction(data.step1);
          snake2.set_direction(data.step2);
        } else if (resp.code === 3) {
          const game = store.state.pk.game;
          const [snake1, snake2] = game.snakes;
          if (data.loser === 1 || data.loser === 3) {
            snake1.die();
          }
          if (data.loser === 2 || data.loser === 3) {
            snake2.die();
          }
          store.commit('updateLoser', data.loser);
        }
      };

      socket.onclose = () => {
      };
    });

    onUnmounted(() => {
      socket.close();
      store.commit('updateStatus', 'matching')
    });
  }
}
</script>

<style scoped>

</style>