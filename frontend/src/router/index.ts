import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ProfileView from '@/views/ProfileView.vue'
import { useAuthStore } from '@/stores/auth.ts'

const routes = [
  { path: '/login', component: LoginView, meta: { hideHeader: true, guest: true } },
  { path: '/register', component: RegisterView, meta: { hideHeader: true, guest: true }  },
  { path: '/profile', component: ProfileView, meta: { requiresAuth: true} },
  { path: '/', redirect: '/login' },
];


export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// Navigation guard

router.beforeEach(async(to) => {
  const authStore = useAuthStore();
  await authStore.initializeAuth();
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return '/login';
  }

  if (to.meta.guest && authStore.isAuthenticated) {
    return '/profile'; // redirect authenticated users away from login/register
  }
})
