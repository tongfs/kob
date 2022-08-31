<template>
    <div class="match-panel">
        <div class="row">
            <div class="col-4">
                <div class="user-profile">
                    <img :src="$store.state.user.profile" alt="头像">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="bot-select">
                    <select v-model="selected" class="form-select" aria-label="Default select example">
                        <option selected value=0>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="col-4">
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
import $ from 'jquery';

export default {
    setup() {
        const store = useStore();
        let match_state = ref('开始匹配');
        let bots = ref([]);
        let selected = ref(0);

        const click_match_btn = () => {
            if (match_state.value === '开始匹配') {
                match_state.value = '　取消　';
                store.state.pk.socket.send(JSON.stringify({
                    event: 'matching',
                    botId: selected.value,
                }))
            } else {
                match_state.value = '开始匹配';
                store.state.pk.socket.send(JSON.stringify({
                    event: 'cancel',
                }))
            }
        };

        const refresh_bots = () => {
            $.ajax({
                url: 'https://app2585.acapp.acwing.com.cn/api/user/bot/all',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
            });
        };

        refresh_bots();

        return {
            match_state,
            click_match_btn,
            bots,
            selected,
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
div.bot-select {
    padding-top: 20vh;
}
div.bot-select > select {
    width: 60%;
    margin: 0 auto;
}
</style>
