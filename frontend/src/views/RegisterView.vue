<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const location = ref('')
const error = ref('')
const loading = ref(false)
const router = useRouter()
const authStore = useAuthStore()

const isFormValid = computed(() => {
  return name.value.trim() &&
    email.value.trim() &&
    password.value.trim() &&
    confirmPassword.value.trim() &&
    location.value.trim()
})

const passwordsMatch = computed(() => {
  return password.value === confirmPassword.value
})

const clearError = () => {
  error.value = ''
}

const handleSubmit = async () => {
  if (!isFormValid.value) {
    error.value = 'Please fill in all fields.'
    return
  }

  if (!passwordsMatch.value) {
    error.value = 'Passwords do not match.'
    return
  }

  if (password.value.length < 6) {
    error.value = 'Password must be at least 6 characters long.' // we haven't implemented password strength validation yet but can start with this maybe
    return
  }

  loading.value = true
  error.value = ''

  try {
    await authStore.register({
      name: name.value.trim(),
      email: email.value.trim(),
      password: password.value,
      location: location.value.trim()
    })

    await router.push('/home');
  } catch (e: unknown) {
    if (e instanceof Error) {
      error.value = e.message || 'Registration failed. Please try again.'
    } else {
      error.value = 'Registration failed. Please try again.'
    }
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/')
}

const handleInputChange = () => {
  if (error.value) {
    clearError()
  }
}

const inputClass = "w-full px-3 py-2 border border-gray-300 rounded shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 disabled:bg-gray-50"

</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 to-blue-50 px-4">
    <div class="w-full bg-white rounded-xl shadow-lg p-8 max-w-md">
      <div class="text-center mb-4">
        <h2 class="text-2xl text-gray-900">Join LocalZero</h2>
        <p class="mt-1 text-sm text-gray-600">Connect with your neighborhood</p>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div v-if="error" class="bg-red-100 text-red-700 px-4 py-2 rounded text-center text-sm">
          {{ error }}
        </div>

        <div>
          <label for="name" class="block text-sm font-medium mb-1">Name</label>
          <input
            id="name"
            v-model="name"
            type="text"
            required
            :disabled="loading"
            @input="handleInputChange"
            :class="inputClass"
            placeholder="Enter your name"
          />
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
            :class="inputClass"
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
            :class="inputClass"
            placeholder="Create a password"
          />
        </div>

        <div>
          <label for="confirmPassword" class="block text-sm font-medium mb-1">Confirm Password</label>
          <input
            id="confirmPassword"
            v-model="confirmPassword"
            type="password"
            required
            :disabled="loading"
            @input="handleInputChange"
            :class="inputClass"
            placeholder="Confirm your password"
          />
        </div>

        <div>
          <label for="location" class="block text-sm font-medium mb-1">Location</label>
          <input
            id="location"
            v-model="location"
            type="text"
            required
            :disabled="loading"
            @input="handleInputChange"
            :class="inputClass"
            placeholder="Enter your city or neighborhood"
          />
        </div>

        <button
          type="submit"
          :disabled="!isFormValid || loading"
          class="w-full py-2 px-4 bg-green-600 text-white rounded hover:bg-green-700 transition-colors cursor-pointer
                 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ loading ? 'Creating account...' : 'Create account' }}
        </button>
      </form>

      <div class="text-center mt-4">
        <p class="text-sm text-gray-600">
          Already have an account?
          <button
            @click="goToLogin"
            :disabled="loading"
            class="text-green-600 hover:text-green-400 transition-colors ml-1 cursor-pointer"
          >
            Sign in
          </button>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
