<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchPanel v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import MatchPanel from '@/components/MatchPanel.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchPanel,
    },
    
    setup() {
        const store = useStore();
        const url = `ws://localhost:3000/websocket/${store.state.user.token}`;

        let socket = null;

        onMounted(() => {
            socket = new WebSocket(url);
            store.commit('updateOpponent', {
                username: '我的对手',
                profile: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
            });

            socket.onopen = () => {
                console.log('open');
                store.commit('updateSocket', socket)
            };
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === 'matching_success') {
                    store.commit('updateOpponent', {
                        username: data.opponent_username,
                        profile: data.opponent_profile,
                    });
                    setTimeout(() => {
                        store.commit('updateStatus', 'playing')
                    }, 2000);
                    store.commit('updateGamemap', data.gamemap);
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

