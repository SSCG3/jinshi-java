// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import JinshiSystem from '../views/JinshiSystem.vue'
import DocWriting from '../views/modules/DocWriting.vue'
import DecisionInfo from '../views/modules/DecisionInfo.vue'
import MeetingAssist from '../views/modules/MeetingAssist.vue'
import TaskAnalysis from '../views/modules/TaskAnalysis.vue'
import PartyManagement from '../views/modules/PartyManagement.vue'
import LearningTraining from '../views/modules/LearningTraining.vue'

// 路由守卫组件
const requireAuth = (to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  if (!isLoggedIn) {
    next('/login')
  } else {
    next()
  }
}

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/',
    name: 'home',
    component: JinshiSystem,
    beforeEnter: requireAuth
  },
  {
    path: '/doc-writing',
    name: 'docWriting',
    component: DocWriting,
    beforeEnter: requireAuth
  },
  {
    path: '/decision-info',
    name: 'decision-info',
    component: DecisionInfo,
    beforeEnter: requireAuth
  },
  {
    path: '/meeting-assist',
    name: 'meeting-assist',
    component: MeetingAssist,
    beforeEnter: requireAuth
  },
  {
    path: '/task-analysis',
    name: 'task-analysis',
    component: TaskAnalysis,
    beforeEnter: requireAuth
  },
  {
    path: '/party-management',
    name: 'party-management',
    component: PartyManagement,
    beforeEnter: requireAuth
  },
  {
    path: '/learning-training',
    name: 'learning-training',
    component: LearningTraining,
    beforeEnter: requireAuth
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router