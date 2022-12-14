import { createStore } from 'vuex';
import user from './user';
import pk from './pk';
import record from './record';

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: user,
    pk: pk,
    record: record,
  }
})
