export default {
    state: {
        status: 'matching',  // playing表示对战界面
        socket: null,
        opponent: {
            username: '',
            avatar: ''
        },
        gameMap: null
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent = opponent
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGameMap(state, gameMap) {
            state.gameMap = gameMap;
        },
    },
    actions: {

    },
    modules: {
    }
}