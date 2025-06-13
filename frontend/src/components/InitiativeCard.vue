<script setup lang="ts">
import type { Initiative, InitiativeCategory } from '@/types/initiative.ts'
import { initiativeCategoriesLabelMap } from '@/constants/initiativeCategories.ts'
import { neighborhoodLabelMap } from '@/constants/neighborhoods.ts'
import { initiativeCategoryColorMap } from '@/constants/initiativeCategories.ts'

function getCategoryColor(category: string) {
  return initiativeCategoryColorMap[category as InitiativeCategory] ?? 'bg-gray-200 text-gray-700'
}

defineProps<{
  initiative: Initiative
}>()
</script>

<template>
  <div
    class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
  >
    <div class="flex justify-between items-start mb-4">
      <span
        :class="`px-2 py-1 rounded-full text-xs font-medium ${getCategoryColor(initiative.category)}`"
      >
        {{ initiativeCategoriesLabelMap[initiative.category] || 'Other' }}
      </span>
      <div class="flex items-center space-x-1">
        <div
          class="w-2 h-2 rounded-full"
          :class="initiative.isPublic ? 'bg-green-400' : 'bg-red-400'"
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
        <span class="font-semibold">Creator:</span>
        <span>{{ initiative.creator.name }}</span>
      </div>
      <div class="flex items-center space-x-2 text-sm text-gray-500">
        <span class="font-semibold">Neighborhood:</span>
        <span>{{ neighborhoodLabelMap[initiative.location] || 'Unknown' }}</span>
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
        v-if="!initiative.isUserCreator && !initiative.isUserParticipant"
        class="px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-700 hover:bg-green-200 cursor-pointer"
      >
        Join
      </button>

      <span
        v-else-if="initiative.isUserParticipant && !initiative.isUserCreator"
        class="px-3 py-1 rounded-full text-sm font-medium bg-green-50 text-green-800 border border-green-200 ml-2"
      >
        Member
      </span>

      <span
        v-else-if="initiative.isUserCreator"
        class="px-3 py-1 rounded-full text-sm font-medium bg-blue-50 text-blue-800 border border-blue-200 ml-2"
      >
        Creator
      </span>
    </div>
  </div>
</template>

<style scoped></style>
