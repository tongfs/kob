export default {
    state: {
        isRecord: false,
        steps1: [],
        steps2: [],
        recordLoser: 0,
        playingStatus: 'playing'
    },
    getters: {
    },
    mutations: {
        updateIsRecord(state, isRecord) {
            state.isRecord = isRecord;
        },
        updateSteps(state, data) {
            state.steps1 = data.steps1;
            state.steps2 = data.steps2;
        },
        updateRecordLoser(state, loser) {
            state.recordLoser = loser;
        },
        updatePlayingStatus(state, status) {
            state.playingStatus = status;
        }
    },
    actions: {
    },
    modules: {
    }
}