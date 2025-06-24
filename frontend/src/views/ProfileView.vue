<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import UserProfileCard from '@/components/profile/UserProfileCard.vue'
import EcoActionList from '@/components/profile/EcoActionList.vue'
import { onMounted } from 'vue'
import { ecoActionOptions } from '@/constants/ecoActionOptions.ts'
import EcoActionForm from '@/components/profile/EcoActionForm.vue'
import { useEcoActionsStore } from '@/stores/ecoActions.ts'

const { user } = storeToRefs(useAuthStore())
const ecoActionsStore = useEcoActionsStore()
const { ecoActions, loading, error } = storeToRefs(useEcoActionsStore())

onMounted(() => {
  ecoActionsStore.fetchEcoActions()
})
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-20 px-6">
    <div class="max-w-7xl mx-auto">
      <div v-if="loading" class="text-center py-8 text-gray-500">Loading eco actions...</div>
      <div v-else-if="error" class="bg-red-100 text-red-700 text-center py-4 rounded mb-4">
        {{ error }}
      </div>
      <div v-else class="grid grid-cols-1 lg:grid-cols-12 gap-8">
        <!-- Left Column - User Info -->
        <div class="lg:col-span-3">
          <UserProfileCard :user="user" :ecoActions="ecoActions" />
        </div>
        <!-- Middle Column - Eco Actions -->
        <div class="lg:col-span-6">
          <EcoActionList :ecoActions="ecoActions" />
        </div>
        <!-- Right Column - Eco Action form-->
        <div class="lg:col-span-3">
          <EcoActionForm :ecoActionOptions="ecoActionOptions" />
        </div>
      </div>
    </div>
  </div>
</template>
