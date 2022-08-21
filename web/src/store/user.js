import $ from 'jquery'

export default {
    state: {
        id: '',
        username: '',
        profile: '',
        token: '',
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.profile = user.profile;
            state.is_login = user.is_login
        },
        updateToken(state, token) {
            state.token = token
        },
        logout(state) {
            state.id = '';
            state.username = '';
            state.profile = '';
            state.token = '';
            state.is_login = false;
        },
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: 'http://localhost:3000/user/account/token',
                type: 'post',
                data: {
                    username: data.username,
                    password: data.password
                },
                success(resp) {
                    if (resp.msg === 'success') {
                        localStorage.setItem('jwt_token', resp.token);
                        context.commit('updateToken', resp.token);  // todo 这个地方为什么和下面不一样？
                        data.success(resp)
                    } else {
                        data.error(resp)
                    }
                },
                error(resp) {
                    data.error(resp)
                }
            })
        },

        getInfo(context, data) {
            $.ajax({
                url: 'http://localhost:3000/user/account/info',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + context.state.token
                },
                success(resp) {
                    if (resp.msg === 'success') {
                        context.commit('updateUser', {
                            // 不是很懂
                            ...resp,
                            is_login: true,
                        });
                        data.success(resp)
                    } else {
                        data.error(resp)
                    }
                },
                error(resp) {
                    data.error(resp)
                }
            })
        },

        logout(context) {
            localStorage.removeItem('jwt_token');
            context.commit('logout');
        }
    },
    modules: {
    }
}
