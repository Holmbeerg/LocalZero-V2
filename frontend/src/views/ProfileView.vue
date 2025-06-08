<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'
import UserProfileCard from '@/components/UserProfileCard.vue'
import EcoActionList from '@/components/EcoActionList.vue'
import { ref } from 'vue'
import type { EcoAction } from '@/types/ecoAction'
import { ecoActionOptions } from '@/constants/ecoActionOptions.ts'
import EcoActionForm from '@/components/EcoActionForm.vue'

const { user } = storeToRefs(useAuthStore())


const ecoActions = ref<EcoAction[]>([
  { id: 1, action: 'Biked to work', date: '2025-06-05', category: 'Transport', carbonSaved: 2.3 },
  { id: 2, action: 'Used reusable bags for shopping', date: '2025-06-04', category: 'Waste', carbonSaved: 0.1 },
  { id: 3, action: 'Participated in community garden', date: '2025-06-03', category: 'Food', carbonSaved: 1.2 },
  { id: 4, action: 'Carpooled to event', date: '2025-06-02', category: 'Transport', carbonSaved: 3.1 },
])

</script>

<template>
  <div class="min-h-screen bg-gray-50 py-20 px-6">
    <div class="max-w-7xl mx-auto">
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
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

