<script setup lang="ts">
import InitiativeForm from './InitiativeForm.vue'
import { type Neighborhood } from '@/constants/neighborhoods.ts'

defineProps<{
  show: boolean
  userNeighborhood: Neighborhood | ''
}>()

const emit = defineEmits(['close', 'created'])

function handleFormSubmit() {
  emit('close')
}
</script>

<template>
  <div v-if="show" class="fixed inset-0 bg-black/50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-lg max-w-md w-full max-h-[90vh] overflow-y-auto">
      <div class="p-6">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Create New Initiative</h2>
          <button
            @click="$emit('close')"
            class="text-gray-400 hover:text-gray-600 cursor-pointer text-2xl"
          >
            ×
          </button>
        </div>
        <InitiativeForm
          :userNeighborhood="userNeighborhood"
          @submit="handleFormSubmit"
          @cancel="$emit('close')"
        />
      </div>
    </div>
  </div>
</template>
