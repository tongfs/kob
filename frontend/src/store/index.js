import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleRecord from './record'

export default createStore({
  state: {
    url: 'http://localhost:3000',
    ws: 'ws://localhost:3000/websocket'
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
    record: ModuleRecord
  }
})
