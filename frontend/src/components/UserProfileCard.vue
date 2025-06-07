<script setup lang="ts">
import { User as UserIcon, Calendar, MapPin, Badge } from 'lucide-vue-next'
import { computed } from 'vue'
import type { User } from '@/types/user.ts'
import type { EcoAction } from '@/types/ecoAction'
import { roleDisplayNames } from '@/types/user.ts'

const props = defineProps<{ // use props instead of store for better reusability?
  user: User | null
  ecoActions: EcoAction[]
}>()

const totalCarbonSaved = computed(() =>
  props.ecoActions.reduce((sum, action) => sum + action.carbonSaved, 0)
)

function formatJoinDate(user: User | null): string {
  if (!user || !user.createdAt) return 'N/A'
  return new Date(user.createdAt).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

</script>

<template>
  <div class="lg:col-span-1">
    <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
      <!-- Profile Picture -->
      <div class="flex justify-center mb-6">
        <div class="w-24 h-24 bg-gradient-to-br from-green-400 to-blue-500 rounded-full flex items-center justify-center">
          <UserIcon class="w-12 h-12 text-white" />
        </div>
      </div>

      <!-- User Details -->
      <div class="text-center mb-6">
        <h2 class="text-2xl font-bold text-gray-900 mb-2">{{ user?.name || 'Loading...' }}</h2>
        <p class="text-gray-600 mb-4">{{ user?.email || '' }}</p>

        <div class="space-y-3">
          <div class="flex items-center justify-center text-gray-600">
            <MapPin class="w-4 h-4 mr-2" />
            <span>{{ user?.location || 'N/A' }}</span>
          </div>

          <div class="flex items-center justify-center text-gray-600">
            <Calendar class="w-4 h-4 mr-2" />
            <span>Joined {{ formatJoinDate(user) }}</span>
          </div>
        </div>
      </div>

      <!-- Roles -->
      <div class="mb-6" v-if="user?.roles?.length">
        <h3 class="text-sm font-semibold text-gray-700 mb-3 flex items-center">
          <Badge class="w-4 h-4 mr-2" />
          Roles
        </h3>
        <div class="flex flex-wrap gap-2">
                <span
                  v-for="role in user.roles"
                  :key="role.roleId"
                  class="px-3 py-1 bg-green-100 text-green-800 text-sm rounded-full"
                >
                  {{ roleDisplayNames[role.roleName] || role.roleName }}
                </span>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="border-t pt-6">
        <h3 class="text-sm font-semibold text-gray-700 mb-3">Impact Summary</h3>
        <div class="space-y-2">
          <div class="flex justify-between">
            <span class="text-gray-600">Total Actions:</span>
            <span class="font-semibold">{{ ecoActions.length }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Carbon Saved:</span>
            <span class="font-semibold text-green-600">{{ totalCarbonSaved.toFixed(1) }} kg</span>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>

</style>
