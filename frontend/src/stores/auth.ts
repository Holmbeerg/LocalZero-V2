import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '@/services/apiService.ts'

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
  const user = ref<User | null>(null) // user state, initially null
  const isInitialized = ref(false) // to track if auth has been initialized
  const isAuthenticated = computed(() => Boolean(user.value))

  async function initializeAuth() {
    if (isInitialized.value) return // prevent re-initialization
    try {
      user.value = await authApi.getCurrentUser() // save user data to the store
      isInitialized.value = true
    } catch (error) { // errors that bubble up from the API
      console.error('Failed to initialize authentication:', error)
      user.value = null
    }
  }

  async function login(credentials: LoginCredentials) {
    try {
      const response = await authApi.login(credentials)
      user.value = response.data
      return response
    } catch (error) {
      console.error('Login failed:', error)
      throw error
    }
  }

  async function register(data: RegisterData) {
    try {
      const response = await authApi.register(data)
      user.value = response.data
      return response
    } catch (error) {
      console.error('Registration failed:', error)
      throw error
    }
  }

  async function logout() {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('Logout failed:', error)
      throw error
    } finally {
      user.value = null
    }
  }

  return { user, isAuthenticated, initializeAuth, login, register, logout }
})
