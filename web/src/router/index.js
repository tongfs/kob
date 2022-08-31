import { createRouter, createWebHistory } from 'vue-router'

import PkIndex from '@/views/pk/PkIndex'
import RecordIndex from '@/views/record/RecordIndex'
import RecordPlaying from '@/views/record/RecordPlaying'
import RanklistIndex from '@/views/ranklist/RanklistIndex'
import UserBotIndex from '@/views/user/bot/UserBotIndex'
import NotFound from '@/views/error/NotFound'
import UserAccountLogin from '@/views/user/account/UserAccountLogin'
import UserAccountRegister from '@/views/user/account/UserAccountRegister'
import store from '@/store/index'

const routes = [
  {
    path: '/',
    name: 'home',
    meta: {
      requireAuth: false,
    }
  },
  {
    path: '/pk',
    name: 'pk_index',
    component: PkIndex,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/record',
    name: 'record_index',
    component: RecordIndex,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/record/:recordId',
    name: 'record_playing',
    component: RecordPlaying,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/ranklist',
    name: 'ranklist_index',
    component: RanklistIndex,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/user/bot',
    name: 'user_bot_index',
    component: UserBotIndex,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/user/account/login',
    name: 'user_account_login',
    component: UserAccountLogin,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: '/user/account/register',
    name: 'user_account_register',
    component: UserAccountRegister,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: '/:catchAll(.*)',
    name: '404',
    component: NotFound,
    meta: {
      requireAuth: false,
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {

  let flag = true;  // 登录是否有效
  const jwt_token = localStorage.getItem('jwt_token');

  if (jwt_token) {
    store.commit('updateToken', jwt_token);
    store.dispatch('getInfo', {
      success() {
      },
      error() {
        localStorage.removeItem('jwt_token');
        store.commit('logout');
        alert("身份验证无效，请重新登录");
        router.push({ name: 'user_account_login' })
      }
    });
  } else {
    flag = false;
  }

  if (to.meta.requireAuth && !store.state.user.is_login) {
    if (flag) {
      next();
    } else {
      alert("请先进行登录");
      next( {name: 'user_account_login'} );
    }
  } else {
    next();
  }
})

export default router
