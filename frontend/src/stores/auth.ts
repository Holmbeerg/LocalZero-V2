import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '@/services/apiService.ts'
import type { User, LoginCredentials, RegisterData } from '@/types/user.ts'
import { useEcoActionsStore } from '@/stores/ecoActions'
import { useInitiativesStore } from '@/stores/initiatives.ts'
import { useMessagesStore } from '@/stores/messages.ts'
import { useNotificationStore } from '@/stores/notifications'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null) // user state, initially null
  const isInitialized = ref(false) // to track if auth has been initialized
  const isAuthenticated = computed(() => Boolean(user.value))
  const ecoActionsStore = useEcoActionsStore()
  const initiativesStore = useInitiativesStore()
  const messagesStore = useMessagesStore()
  const notificationsStore = useNotificationStore()

  async function initializeAuth() {
    if (isInitialized.value) return // prevent re-initialization
    try {
      user.value = await authApi.getCurrentUser() // save user data to the store
      isInitialized.value = true
    } catch (error) {
      // errors that bubble up from the API
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
      console.log('User logged out successfully')
    } catch (error) {
      console.error('Logout failed:', error)
    } finally {
      clearUserState()
      console.log('User state cleared from store')
    }
  }

  function clearUserState() {
    user.value = null
    isInitialized.value = false
    ecoActionsStore.resetEcoActions()
    initiativesStore.resetInitiatives()
    messagesStore.resetMessages()
    notificationsStore.closeDropdown()
  }

  return { user, isAuthenticated, initializeAuth, login, register, logout }
})
