import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import {authApi} from '@/services/apiService.ts';

export interface User {
  id: string
  name: string
  email: string
  location: string
  createdAt: string
  roles: string[]
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterData {
  name: string
  email: string
  password: string
  location: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null); // user state, initially null
  const isAuthenticated = computed(() => Boolean(user.value));

  async function login(credentials: LoginCredentials) {
    try {
      const response = await authApi.login(credentials);
      user.value = response.data; // save user data to the store
      return response;
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  }

  async function register(data: RegisterData) {
    try {
      const response = await authApi.register(data);
      user.value = response.data;
      return response;
    } catch (error) {
      console.error('Registration failed:', error);
      throw error;
    }
  }

  async function logout() {
    try {
      await authApi.logout();
    } catch (error) {
      console.error('Logout failed:', error);
      throw error;
    } finally {
      user.value = null;
    }
  }

  return { user, isAuthenticated, login, register, logout };
})
