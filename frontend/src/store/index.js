import { createStore } from 'vuex'
import ModuleUser from './user'

export default createStore({
  state: {
    url: 'http://localhost:3000'
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser
  }
})
