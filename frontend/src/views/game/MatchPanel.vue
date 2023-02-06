<template>
  <div class="match-panel">
    <div class="row">
      <div class="col-4">
        <div class="user-avatar">
          <img
            :src="$store.state.user.avatar"
            alt="头像"
          >
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-4">
        <div class="bot-select">
          <select
            v-model="selected"
            class="form-select"
            aria-label="Default select example"
          >
            <option
              selected
              value="0"
            >
              键盘操作
            </option>
            <option
              v-for="bot in bots"
              :key="bot.id"
              :value="bot.id"
            >
              {{ bot.title }}
            </option>
          </select>
        </div>
      </div>
      <div class="col-4">
        <div class="user-avatar">
          <img
            :src="$store.state.game.opponent.avatar"
            alt="头像"
          >
        </div>
        <div class="user-username">
          {{ $store.state.game.opponent.username }}
        </div>
      </div>
      <div
        class="col-12"
        style="text-align: center; margin-top: 15vh;"
      >
        <button
          v-if="$store.state.game.identity === 0"
          type="button"
          class="btn btn-warning btn-lg"
          @click="click_match_btn"
        >
          {{ match_state.msg }}
        </button>
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

        let match_states_list = ['开始匹配', '　取消　'];

        // 匹配状态, 0-不在匹配, 1-匹配中
        let match_state = ref({
            code: 0,
            msg: match_states_list[0]
        })

        let bots = ref([]);
        let selected = ref(store.state.game.botId);

        const click_match_btn = () => {
            let code = match_state.value.code;
            code = (code + 1) & 1;
            match_state.value = {
                code: code,
                msg: match_states_list[code]
            }

            store.state.game.socket.send(JSON.stringify({
                // 传给后端时，0是取消，1是匹配
                event: code,
                botId: selected.value
            }));

            store.commit('updateBotSelected', selected.value);
        }

        const refresh_bots = () => {
            $.ajax({
                url: store.state.url + '/bot/list',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp.data;
                },
            });
        };
        refresh_bots();

        return {
            match_state,
            click_match_btn,
            bots,
            selected
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

div.user-avatar {
    text-align: center;
    padding-top: 10vh;
}

div.user-avatar>img {
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

div.bot-select>select {
    width: 60%;
    margin: 0 auto;
}
</style>