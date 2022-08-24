<template>
    <div class="match-panel">
        <div class="row">
            <div class="col-6">
                <div class="user-profile">
                    <img :src="$store.state.user.profile" alt="头像">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
                <div class="user-profile">
                    <img :src="$store.state.pk.opponent.profile" alt="头像">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent.username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; margin-top: 15vh;">
                <button type="button" class="btn btn-warning btn-lg" @click="click_match_btn">{{ match_state }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();
        let match_state = ref('开始匹配');

        const click_match_btn = () => {
            if (match_state.value === '开始匹配') {
                match_state.value = '　取消　';
                store.state.pk.socket.send(JSON.stringify({
                    event: 'matching',
                }))
            } else {
                match_state.value = '开始匹配';
                store.state.pk.socket.send(JSON.stringify({
                    event: 'cancel',
                }))
            }
        }

        return {
            match_state,
            click_match_btn,
        }
    }
}
</script>

<style scoped>
div.match-panel {
    width: 60vw;
    height: 70vh;
    margin: 10vh auto;
    background-color: rgba(50, 50, 50, 0.5);
}
div.user-profile {
    text-align: center;
    padding-top: 10vh;
}
div.user-profile > img {
    border-radius: 50%;
    width: 20vh;
}
div.user-username {
    text-align: center;
    font-size: 25px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
</style>
