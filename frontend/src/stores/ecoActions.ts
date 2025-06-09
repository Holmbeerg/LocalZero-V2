import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { EcoAction } from '@/types/ecoAction'
import { ecoActionsApi } from '@/services/apiService'

export const useEcoActionsStore = defineStore('ecoActions', () => {
  const ecoActions = ref<EcoAction[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)

  const fetchEcoActions = async () => {
    if (isLoaded.value) return
    loading.value = true
    error.value = null
    try {
      ecoActions.value = await ecoActionsApi.getActions()
      isLoaded.value = true
    } catch (err) {
      error.value = 'Failed to load eco actions'
      console.error('Error fetching eco actions:', err)
    } finally {
      loading.value = false
    }
  }

  const addEcoAction = (action: EcoAction) => {
    ecoActions.value.push(action)
    ecoActions.value.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
  }

  function resetEcoActions() {
    ecoActions.value = []
    loading.value = false
    error.value = null
    isLoaded.value = false
  }

  return {
    ecoActions,
    loading,
    error,
    fetchEcoActions,
    addEcoAction,
    resetEcoActions,
  }
})
