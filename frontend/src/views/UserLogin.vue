<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label
              for="username"
              class="form-label"
            >用户名</label>
            <input
              id="username"
              v-model="username"
              type="text"
              class="form-control"
              placeholder="请输入用户名"
            >
          </div>
          <div class="mb-3">
            <label
              for="password"
              class="form-label"
            >密码</label>
            <input
              id="password"
              v-model="password"
              type="password"
              class="form-control"
              placeholder="请输入密码"
            >
          </div>
          <div class="msg">
            {{ msg }}
          </div>
          <button
            type="submit"
            class="btn btn-primary"
          >
            提交
          </button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let msg = ref('');
        const login = () => {
            store.dispatch('login', {
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch('getInfo', {
                        success() {
                            router.push({ name: 'home' })
                        }
                    })
                },
                error() {
                    msg.value = '用户名或密码错误';
                }
            })
        }
        return {
            username,
            password,
            msg,
            login
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}

div.msg {
    color: red;
}
</style>