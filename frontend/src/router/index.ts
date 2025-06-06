import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ProfileView from '@/views/ProfileView.vue'
import { useAuthStore } from '@/stores/auth.ts'

const routes = [
  { path: '/login', component: LoginView, meta: { hideHeader: true } },
  { path: '/register', component: RegisterView, meta: { hideHeader: true }  },
  { path: '/profile', component: ProfileView, meta: { requiresAuth: true} },
  { path: '/', redirect: '/login' },
];


export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// Navigation guards

router.beforeEach((to) => { // not using from or next at the moment
  if (to.meta.requiresAuth) {
    const authStore = useAuthStore();
    if (!authStore.isAuthenticated) {
      return '/login';
    }
  }
})
