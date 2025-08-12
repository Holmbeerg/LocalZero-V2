<script setup lang="ts">
import type { User } from '@/types/user.ts'

defineProps<{
  user: User | null
}>()

</script>

<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
    <!-- Profile Picture -->
    <div class="flex justify-center mb-6">
      <div
        class="w-24 h-24 bg-gradient-to-br from-green-400 to-blue-500 rounded-full flex items-center justify-center"
      >
        <UserIcon class="w-12 h-12 text-white" />
      </div>
    </div>

    <!-- Short User Details -->
    <div class="text-center mb-6">
      <h2 class="text-2xl font-bold text-gray-900 mb-2">{{ user?.name || 'Loading...' }}</h2>
      <p class="text-gray-600 mb-4">{{ user?.email || '' }}</p>
    </div>
  </div>
</template>

<style scoped></style>
