<script setup lang="ts">
import type { InitiativeCategory } from '@/types/initiative.ts'
import type { Initiative } from '@/types/initiative.ts'

defineProps<{
  initiative: Initiative
}>()

const getCategoryColor = (category: InitiativeCategory) => {
  switch (category) {
    case 'TOOL_SHARING':
      return 'bg-green-100 text-green-800'
    case 'FOOD_SWAP':
      return 'bg-blue-100 text-blue-800'
    case 'COMMUNITY_GARDENING':
      return 'bg-red-100 text-red-800'
    case 'RECYCLING_DRIVE':
      return 'bg-yellow-100 text-yellow-800'
    case 'RIDE_SHARING':
      return 'bg-purple-100 text-purple-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}
</script>

<template>
  <div
    class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
  >
    <div class="flex justify-between items-start mb-4">
      <span class="px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
        {{ initiative.category }}
      </span>
      <div class="flex items-center space-x-1">
        <div
          class="w-2 h-2 rounded-full"
          :class="initiative.isPublic ? 'bg-green-400' : 'bg-orange-400'"
        ></div>
        <span class="text-xs text-gray-500">
          {{ initiative.isPublic ? 'Public' : 'Neighborhood' }}
        </span>
      </div>
    </div>
    <h3 class="font-semibold text-gray-900 mb-2">{{ initiative.title }}</h3>
    <p class="text-gray-600 text-sm mb-4 line-clamp-3">{{ initiative.description }}</p>
    <div class="space-y-2 mb-4">
      <div class="flex items-center space-x-2 text-sm text-gray-500">
        <span class="font-semibold">Neighborhood:</span>
        <span>{{ initiative.location }}</span>
      </div>
      <div class="flex items-center space-x-2 text-sm text-gray-500">
        <span class="font-semibold">Start:</span>
        <span>{{ initiative.startDate }}</span>
      </div>
      <div class="flex items-center space-x-2 text-sm text-gray-500">
        <span class="font-semibold">End:</span>
        <span>{{ initiative.endDate }}</span>
      </div>
    </div>
    <div class="flex justify-end items-center pt-4 border-t border-gray-100">
      <button
        class="px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-700 hover:bg-green-200"
      >
        Join
      </button>
    </div>
  </div>
</template>

<style scoped></style>
