<script setup lang="ts">
import { Leaf, Car, Recycle } from 'lucide-vue-next'
import type { EcoAction, EcoActionCategory } from '@/types/ecoAction.ts'

const getCategoryIcon = (category: EcoActionCategory) => {
  switch (category) {
    case 'Transport': return Car
    case 'Waste': return Recycle
    case 'Food': return Leaf
    default: return Leaf
  }
}

defineProps<{ ecoActions: EcoAction[] }>()

</script>

<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
  <div class="p-6 border-b border-gray-200">
    <div class="flex items-center justify-between">
      <h2 class="text-xl font-bold text-gray-900 flex items-center">
        <Leaf class="w-5 h-5 mr-2 text-green-500" />
        My Eco Actions
      </h2>
    </div>
  </div>

  <!-- Actions List -->
  <div class="p-6">
    <div v-if="ecoActions.length === 0" class="text-center py-12 text-gray-500">
      <Leaf class="w-12 h-12 mx-auto mb-4 text-gray-300" />
      <h3 class="text-lg font-medium mb-2">No eco actions yet</h3>
      <p>Start logging your sustainable activities to track your impact!</p>
    </div>

    <div v-else class="space-y-4">
      <div
        v-for="action in ecoActions"
        :key="action.id"
        class="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
      >
        <div class="flex items-start justify-between">
          <div class="flex items-start space-x-3">
            <div class="flex-shrink-0 w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
              <component :is="getCategoryIcon(action.category)" class="w-4 h-4" />
            </div>
            <div class="flex-1">
              <h3 class="font-medium text-gray-900">{{ action.action }}</h3>
              <div class="flex items-center space-x-4 mt-1">
                          <span class="text-sm text-gray-500">
                            {{ new Date(action.date).toLocaleDateString() }}
                          </span>
                <span class="text-sm bg-gray-100 text-gray-700 px-2 py-1 rounded">
                            {{ action.category }}
                          </span>
              </div>
            </div>
          </div>
          <div class="text-right">
            <div class="text-sm font-medium text-green-600">
              {{ action.carbonSaved }} kg CO₂
            </div>
            <div class="text-xs text-gray-500">saved</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<style scoped>

</style>
