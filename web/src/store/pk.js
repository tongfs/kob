export default {
    state: {
        status: 'matching',  // playing表示对战界面
        socket: null,
        opponent_username: '',
        opponent_profile: '',
        gamemap: null,
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_profile = opponent.profile;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGamemap(state, gamemap) {
            state.gamemap = gamemap;
        },
    },
    actions: {

    },
    modules: {
    }
}
