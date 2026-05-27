import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/bewohner',
    },
    {
      path: '/bewohner',
      name: 'bewohner-liste',
      component: () => import('../views/BewohnerListeView.vue'),
    },
    {
      path: '/bewohner/:id',
      name: 'bewohner-detail',
      component: () => import('../views/BewohnerDetailView.vue'),
    },
    {
      path: '/termine',
      name: 'termine',
      component: () => import('../views/TermineView.vue'),
    },
    {
      path: '/liegenschaft',
      name: 'liegenschaft',
      component: () => import('../views/LiegenschaftView.vue'),
    },
  ],
})

export default router
