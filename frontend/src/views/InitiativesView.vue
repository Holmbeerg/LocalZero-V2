<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useInitiativesStore } from '@/stores/initiatives'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'
import { initiativeFilterOptions } from '@/constants/initiativeCategories.ts'
import CreateInitiativeModal from '@/components/CreateInitiativeModal.vue'
import InitiativeCard from '@/components/InitiativeCard.vue'

const showCreateForm = ref(false)
const selectedFilter = ref<'ALL' | string>('ALL')

const initiativesStore = useInitiativesStore()
const { initiatives, loading, error } = storeToRefs(initiativesStore)
const { user } = storeToRefs(useAuthStore())

onMounted(() => {
  initiativesStore.fetchInitiatives()
})

const filteredInitiatives = computed(() =>
  selectedFilter.value === 'all'
    ? initiatives.value
    : initiatives.value.filter((i) => i.category === selectedFilter.value),
)

const handleInitiativeCreated = async () => {
  showCreateForm.value = false
}
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-10 px-4">
    <div class="max-w-7xl mx-auto">
      <!-- Filters -->
      <div class="flex flex-col md:flex-row justify-between items-center mb-8 gap-4">
        <div class="flex items-center gap-4">
          <h1 class="text-2xl font-semibold text-gray-900">Initiatives</h1>
          <div class="flex gap-2 ml-4">
            <button
              v-for="[value, label] in initiativeFilterOptions"
              :key="value"
              @click="selectedFilter = value"
              :class="[
                'px-3 py-1 rounded-full text-sm font-medium transition-colors cursor-pointer',
                selectedFilter === value
                  ? 'bg-green-500 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300',
              ]"
            >
              {{ label }}
            </button>
          </div>
        </div>
        <button
          @click="showCreateForm = true"
          class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 transition-colors cursor-pointer"
        >
          <span class="font-bold text-lg">＋</span>
          <span>New Initiative</span>
        </button>
      </div>

      <!-- Initiatives grid -->
      <div v-if="loading" class="text-center text-gray-500">Loading initiatives...</div>
      <div v-else-if="error" class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4">
        {{ error }}
      </div>
      <div v-else>
        <div v-if="filteredInitiatives.length === 0" class="text-center text-gray-500 py-12">
          No initiatives found.
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <InitiativeCard
            v-for="initiative in filteredInitiatives"
            :key="initiative.id"
            :initiative="initiative"
          />
        </div>
      </div>
    </div>

    <!-- Create Initiative Modal -->
    <CreateInitiativeModal
      :show="showCreateForm"
      :userNeighborhood="user?.location || ''"
      @close="showCreateForm = false"
      @created="handleInitiativeCreated"
    />
  </div>
</template>
