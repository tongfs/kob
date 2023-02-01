import { createRouter, createWebHistory } from 'vue-router'

import HomeIndex from '@/views/HomeIndex'
import PkIndex from '@/views/PkIndex'
import RecordIndex from '@/views/RecordIndex'
import RanklistIndex from '@/views/RanklistIndex'
import UserBotIndex from '@/views/UserBotIndex'
import NotFound from '@/views/error/NotFound'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeIndex
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
