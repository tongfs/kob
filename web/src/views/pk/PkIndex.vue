<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchPanel v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser" />
</template>

<script>
import PlayGround from './PlayGround.vue';
import MatchPanel from './MatchPanel.vue';
import ResultBoard from './ResultBoard.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchPanel,
        ResultBoard,
    },
    
    setup() {
        const store = useStore();
        const url = `wss://app2585.acapp.acwing.com.cn/websocket/${store.state.user.token}`;

        let socket = null;

        store.commit('updateLoser', 0);
        store.commit('updateIsRecord', false);

        onMounted(() => {
            socket = new WebSocket(url);
            store.commit('updateOpponent', {
                username: '我的对手',
                profile: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
            });

            socket.onopen = () => {
                console.log('open');
                store.commit('updateSocket', socket);
            };
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === 'match_result') {
                    store.commit('updateOpponent', data.opponent);
                    store.commit('updateGame', data);
                    setTimeout(() => {
                        store.commit('updateStatus', 'playing')
                    }, 1000);
                } else if (data.event === 'move') {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.direction[0]);
                    snake1.set_direction(data.direction[1]);
                } else if (data.event === 'result') {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if (data.loser === 1 || data.loser === 3) {
                        snake0.die();
                    }
                    if (data.loser === 2 || data.loser === 3) {
                        snake1.die();
                    }
                    store.commit('updateLoser', data.loser);
                }
            };
            socket.onclose = () => {
                console.log('close');
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

