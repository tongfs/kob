import { createRouter, createWebHistory } from 'vue-router'

import HomeIndex from '@/views/HomeIndex'
import PkIndex from '@/views/PkIndex'
import RecordIndex from '@/views/RecordIndex'
import RanklistIndex from '@/views/RanklistIndex'
import UserSpace from '@/views/UserSpace'
import UserLogin from '@/views/UserLogin'
import UserRegister from '@/views/UserRegister'
import NotFound from '@/views/NotFound'

import store from '@/store/index'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeIndex,
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
    path: '/ranklist',
    name: 'ranklist_index',
    component: RanklistIndex,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: '/user/space',
    name: 'user_space',
    component: UserSpace,
    meta: {
      requireAuth: true,
    }
  },
  {
    path: '/user/login',
    name: 'user_login',
    component: UserLogin,
    meta: {
      requireAuth: false,
    }
  },
  {
    path: '/user/register',
    name: 'user_register',
    component: UserRegister,
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
