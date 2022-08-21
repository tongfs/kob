import { createRouter, createWebHistory } from 'vue-router'

import PkIndex from '@/views/pk/PkIndex'
import RecordIndex from '@/views/record/RecordIndex'
import RanklistIndex from '@/views/ranklist/RanklistIndex'
import UserBotIndex from '@/views/user/bot/UserBotIndex'
import NotFound from '@/views/error/NotFound'
import UserAccountLogin from '@/views/user/account/UserAccountLogin'
import UserAccountRegister from '@/views/user/account/UserAccountRegister'

const routes = [
  {
    path: '/',
    name: 'home',
    redirect: '/pk',
  },
  {
    path: '/pk',
    name: 'pk_index',
    component: PkIndex
  },
  {
    path: '/record',
    name: 'record_index',
    component: RecordIndex
  },
  {
    path: '/ranklist',
    name: 'ranklist_index',
    component: RanklistIndex
  },
  {
    path: '/user/bot',
    name: 'user_bot_index',
    component: UserBotIndex
  },
  {
    path: '/user/account/login',
    name: 'user_account_login',
    component: UserAccountLogin
  },
  {
    path: '/user/account/register',
    name: 'user_account_register',
    component: UserAccountRegister
  },
  {
    path: '/:catchAll(.*)',
    name: '404',
    component: NotFound
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
