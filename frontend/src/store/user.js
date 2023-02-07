import $ from 'jquery'
import store from './index'

export default {
    state: {
        id: '',
        username: '',
        avatar: '',
        token: '',
        is_login: false
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.avatar = user.avatar;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.id = '';
            state.username = '';
            state.avatar = '';
            state.token = '';
            state.is_login = false;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: store.state.url + '/user/login',
                type: 'post',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({
                    username: data.username,
                    password: data.password
                }),
                success(resp) {
                    if (resp.code === 0) {
                        localStorage.setItem('jwt_token', resp.data.token);
                        context.commit('updateToken', resp.data.token);
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
                url: store.state.url + '/user/info',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + context.state.token
                },
                success(resp) {
                    if (resp.code === 0) {
                        context.commit('updateUser', {
                            ...resp.data,
                            is_login: true,
                        });
                        data.success(resp)
                    } else {
                        context.commit('logout');
                        data.error(resp)
                    }
                },
                error(resp) {
                    context.commit('logout');
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
