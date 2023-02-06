import { createRouter, createWebHistory } from 'vue-router'

import HomeIndex from '@/views/home/HomeIndex'
import GameIndex from '@/views/game/GameIndex'
import RecordIndex from '@/views/record/RecordIndex'
import RecordPlaying from '@/views/record/RecordPlaying'
import UserRank from '@/views/user/UserRank'
import UserSpace from '@/views/user/UserSpace'
import UserLogin from '@/views/user/UserLogin'
import UserRegister from '@/views/user/UserRegister'
import NotFound from '@/views/error/NotFound'

import store from '@/store/index'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeIndex,
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/game',
    name: 'game_index',
    component: GameIndex,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/record',
    name: 'record_index',
    component: RecordIndex,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/record/:recordId',
    name: 'record_playing',
    component: RecordPlaying,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/rank',
    name: 'rank_index',
    component: UserRank,
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/user/space',
    name: 'user_space',
    component: UserSpace,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/user/login',
    name: 'user_login',
    component: UserLogin,
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/user/register',
    name: 'user_register',
    component: UserRegister,
    meta: {
      requireAuth: false
    }
  },
  {
    path: '/:catchAll(.*)',
    name: '404',
    component: NotFound,
    meta: {
      requireAuth: false
    }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const jwt_token = localStorage.getItem('jwt_token');

  if (jwt_token) {
    store.commit('updateToken', jwt_token);
    // 判断jwt是否有效
    store.dispatch('getInfo', {
      success() {
        next();
      },
      error() {
        store.dispatch('logout');
        no_token_router(to, next, "登录验证过期，请重新登录")
      }
    });
  } else {
    no_token_router(to, next, "请先进行登录")
  }
})

const no_token_router = (to, next, msg) => {
  if (!to.meta.requireAuth) next();
  else {
    alert(msg);
    next({ name: 'user_login' });
  }
}

export default router
