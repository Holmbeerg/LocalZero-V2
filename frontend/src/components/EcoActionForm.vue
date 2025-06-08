<script setup lang="ts">
import { Plus } from 'lucide-vue-next'
import { ref } from 'vue'
import { ecoActionsApi } from '@/services/apiService.ts'
import type { LogEcoActionRequest } from '@/types/ecoAction.ts'

const selectedActionId = ref<number | ''>('')
const selectedDate = ref('')


async function logNewAction(){
  if (!selectedActionId.value) {
    alert('Please select an action to log.')
    return
  }

  console.log(`Logged new eco action: ${selectedActionId.value}`)

  const request: LogEcoActionRequest = {
    actionId: selectedActionId.value,
    date: selectedDate.value
  }

  try {
    await ecoActionsApi.logAction(request)
    alert('Eco action logged successfully!')
  } catch (error) {
    console.error('Error logging eco action:', error)
    alert('Failed to log action. Please try again later.')
  } finally {
    selectedActionId.value = ''
    selectedDate.value = ''
  }
}

defineProps<{
  ecoActionOptions: Array<{ id: number, action: string, carbonSaved: number, category: string }>
}>()
</script>

<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
    <div class="p-6 border-b border-gray-200">
      <h2 class="text-xl font-bold text-gray-900 flex items-center">
        <Plus class="w-5 h-5 mr-2 text-green-500" />
        Log New Eco Action
      </h2>
    </div>

    <div class="p-6">
      <p class="text-sm text-gray-600 mb-4">Track your sustainable activities and their impact.</p>

      <!-- Action Form -->
      <form @submit.prevent="logNewAction" class="space-y-4">
        <div>
          <label for="action" class="block text-sm font-medium mb-1">Action</label>
          <select id="action" v-model="selectedActionId" required class="w-full px-3 py-2 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500">
            <option v-for="option in ecoActionOptions" :key="option.id" :value="option.id">{{ option.action }}</option>
          </select>
        </div>
        <div>
          <label for="date" class="block text-sm font-medium mb-1">Date</label>
          <input
            id="date"
            type="date"
            v-model="selectedDate"
            required
            class="w-full px-3 py-2 border border-gray-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500">
        </div>

        <button
          type="submit"
          class="w-full bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg transition-colors"
        >
          Log Action
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>

</style>
