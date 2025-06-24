<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.ts'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const { user } = storeToRefs(authStore)
const router = useRouter()

const logout = async () => {
  console.log('Logging out...')
  await authStore.logout()
  await router.push('/login')
}

const showNotifications = ref(false)
const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value
}
</script>

<template>
  <header class="bg-white shadow-md p-4 flex justify-between items-center">
    <div class="flex items-center space-x-4">
      <router-link to="/initiatives" class="text-lg font-bold text-gray-800 hover:text-blue-600">
        Initiatives
      </router-link>
    </div>

    <!-- right side -->
    <div class="flex items-center space-x-4">
      <router-link to="/profile" class="text-gray-800 hover:text-blue-600">
        <span class="font-semibold">{{ user?.name || 'Profile' }}</span>
      </router-link>

      <router-link to="/messages" class="text-gray-800 hover:text-blue-600">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="size-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M8.625 9.75a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375m-13.5 3.01c0 1.6 1.123 2.994 2.707 3.227 1.087.16 2.185.283 3.293.369V21l4.184-4.183a1.14 1.14 0 0 1 .778-.332 48.294 48.294 0 0 0 5.83-.498c1.585-.233 2.708-1.626 2.708-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z"
          />
        </svg>
      </router-link>

      <!-- Notifications -->
      <svg
        @click="toggleNotifications"
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="size-6 cursor-pointer hover:text-blue-600"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
        />
      </svg>

      <button @click="logout" class="hover:text-red-800 cursor-pointer">Logout</button>
    </div>
  </header>
</template>

<style scoped></style>
