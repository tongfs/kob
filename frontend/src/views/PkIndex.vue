<template>
  <div>
    <MatchPanel v-if="$store.state.pk.status === 'matching'" />
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
  </div>
</template>

<script>
import PlayGround from '@/components/PlayGround'
import MatchPanel from '@/components/MatchPanel';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
  components: {
    PlayGround,
    MatchPanel
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
        console.log('open');
        store.commit('updateSocket', socket)
      };

      socket.onmessage = msg => {
        const resp = JSON.parse(msg.data);
        console.log(resp);
        if (resp.code === 1) {
          const data = resp.data;
          store.commit('updateOpponent', {
            username: data.opponentUsername,
            avatar: data.opponentAvatar,
          });
          setTimeout(() => {
            store.commit('updateStatus', 'playing')
          }, 2000);
          store.commit('updateGameMap', data.gameMap);
        }
      };

      socket.onclose = () => {
        console.log('onclose');
      };
    });

    onUnmounted(() => {
      socket.close();
      console.log('close')
      store.commit('updateStatus', 'matching')
    });
  }
}
</script>

<style scoped>

</style>