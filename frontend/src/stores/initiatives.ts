import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CreateInitiativeRequest, Initiative } from '@/types/initiative'
import { initiativesApi } from '@/services/apiService' // You'd create this

export const useInitiativesStore = defineStore('initiatives', () => {
  const initiatives = ref<Initiative[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)

  const fetchInitiatives = async () => {
    if (isLoaded.value) return
    loading.value = true
    error.value = null
    try {
      initiatives.value = await initiativesApi.getAccessibleInitiatives()
      isLoaded.value = true
    } catch (err) {
      error.value = 'Failed to load initiatives'
      console.error('Error fetching initiatives:', err)
    } finally {
      loading.value = false
    }
  }

  const createInitiative = async (initiative: CreateInitiativeRequest) => {
    loading.value = true
    error.value = null

    try {
      const newInitiative = await initiativesApi.createInitiative(initiative)
      addInitiativeToStore(newInitiative)
      return newInitiative
    } catch (err) {
      error.value = 'Failed to create initiative'
      console.error('Error creating initiative:', err)
    } finally {
      loading.value = false
    }
  }

  const addInitiativeToStore = (initiative: Initiative) => {
    initiatives.value.push(initiative)
    initiatives.value.sort(
      (a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime(),
    )
  }

  function resetInitiatives() {
    initiatives.value = []
    loading.value = false
    error.value = null
    isLoaded.value = false
  }

  return {
    initiatives,
    loading,
    error,
    fetchInitiatives,
    createInitiative,
    resetInitiatives,
  }
})
