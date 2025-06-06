<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()
const authStore = useAuthStore()

const isFormValid = computed(() => {
  return email.value.trim() && password.value.trim()
})

const clearError = () => {
  error.value = ''
}

const handleSubmit = async () => {
  console.log("Handling login submission...")
  if (!isFormValid.value) {
    error.value = 'Please enter both email and password.'
    return
  }

  loading.value = true
  error.value = ''

  try {
    await authStore.login({
      email: email.value.trim(),
      password: password.value
    })

    await router.push('/profile'); // Redirect to profile page after successful login
  } catch (e: unknown) {
    if (e instanceof Error) {
      error.value = e.message || 'Login failed. Please check your credentials.'
    } else {
      error.value = 'Login failed. Please check your credentials.'
    }
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}

const handleInputChange = () => {
  if (error.value) {
    clearError()
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 to-blue-50 px-4">
    <div class="w-full bg-white rounded-xl shadow-lg p-8 max-w-md">
      <div class="text-center mb-4">
        <h2 class="text-2xl text-gray-900">Sign in to LocalZero</h2>
        <p class="mt-1 text-sm text-gray-600">Connect with your neighborhood</p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div v-if="error" class="bg-red-100 text-red-700 px-4 py-2 rounded text-center text-sm">
          {{ error }}
        </div>
        <div>
          <label for="email" class="block text-sm font-medium mb-1">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            required
            :disabled="loading"
            @input="handleInputChange"
            class="w-full px-3 py-2 border border-gray-300 rounded shadow-sm placeholder-gray-400
                   focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 disabled:bg-gray-50"
            placeholder="Enter your email"
          />
        </div>
        <div>
          <label for="password" class="block text-sm font-medium mb-1">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            required
            :disabled="loading"
            @input="handleInputChange"
            class="w-full px-3 py-2 border border-gray-300 rounded shadow-sm placeholder-gray-400
                   focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 disabled:bg-gray-50"
            placeholder="Enter your password"
          />
        </div>
        <button
          type="submit"
          :disabled="!isFormValid || loading"
          class="w-full py-2 px-4 bg-green-600 text-white rounded hover:bg-green-700 transition-colors cursor-pointer
                 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ loading ? 'Signing in...' : 'Sign in' }}
        </button>
      </form>

      <div class="text-center mt-4">
        <p class="text-sm text-gray-600">
          Don't have an account?
          <button
            @click="goToRegister"
            :disabled="loading"

            class="text-green-600 hover:text-green-400 transition-colors ml-1 cursor-pointer"
          >
            Sign up
          </button>
        </p>
      </div>
    </div>
  </div>
</template>
