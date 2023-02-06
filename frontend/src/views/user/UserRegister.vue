<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
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
          <div class="mb-3">
            <label
              for="confirmedPassword"
              class="form-label"
            >确认密码</label>
            <input
              id="confirmedPassword"
              v-model="confirmedPassword"
              type="password"
              class="form-control"
              placeholder="请再次输入密码"
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
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue';
import router from '@/router/index';
import $ from 'jquery';
import { useStore } from 'vuex';


export default {
    components: {
        ContentField
    },

    setup() {
        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let msg = ref('');

        const store = useStore();

        const register = () => {
            $.ajax({
                url: store.state.url + '/user/register',
                type: 'post',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value
                }),
                success(resp) {
                    if (resp.code === 0) {
                        router.push({ name: 'user_login' });
                        alert('注册成功');
                    } else {
                        msg.value = resp.message;
                    }
                },
            })
        };
        
        return {
            username,
            password,
            confirmedPassword,
            msg,
            register
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