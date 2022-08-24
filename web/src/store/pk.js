export default {
    state: {
        status: 'matching',  // playing表示对战界面
        socket: null,
        opponent: {},   // 有username和profile两个属性
        identity: 0,
        gameMap: null,
        gameObject: null,
        loser: 0,
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent = opponent;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGame(state, data) {
            state.identity = data.identity;
            state.gameMap = data.gameMap;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        }
    },
    actions: {

    },
    modules: {
    }
}
