export default {
    state: {
        status: 'matching',  // playing表示对战界面
        socket: null,
        opponent: {},
        gameMap: null,
        identity: 0,
        game: null,
        loser: 0
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
        updateGame(state, data) {
            state.gameMap = data.gameMap;
            state.identity = data.identity;
        },
        updateGameObject(state, game) {
            state.game = game;
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