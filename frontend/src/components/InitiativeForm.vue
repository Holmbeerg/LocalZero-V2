<script setup lang="ts">
import { ref, computed } from 'vue'
import { initiativeCategories } from '@/constants/initiativeCategories'
import type { CreateInitiativeRequest, InitiativeCategory } from '@/types/initiative.ts'
import { type Neighborhood } from '@/constants/neighborhoods'
import { neighborhoodLabelMap } from '@/constants/neighborhoods'
import { useInitiativesStore } from '@/stores/initiatives.ts'

const initiativesStore = useInitiativesStore()

defineProps<{
  userNeighborhood: Neighborhood | ''
}>()
const emit = defineEmits(['cancel'])

const title = ref('')
const description = ref('')
const category = ref<InitiativeCategory | ''>('')
const isPublic = ref(true)
const startDate = ref('')
const endDate = ref('')

const isFormValid = computed(
  () =>
    title.value.trim() &&
    description.value.trim() &&
    category.value &&
    startDate.value &&
    endDate.value,
)

async function handleFormSubmit() {
  if (!isFormValid.value) return

  if (category.value === '') {
    console.error('Category should not be empty after validation')
    return
  }

  const formData: CreateInitiativeRequest = {
    title: title.value,
    description: description.value,
    category: category.value,
    isPublic: isPublic.value,
    startDate: startDate.value,
    endDate: endDate.value,
  }

  try {
    await initiativesStore.createInitiative(formData)
    alert('Initiative created successfully!')
  } catch (error) {
    console.error('Error creating initiative:', error)
    alert('Failed to create initiative. Please try again later.')
  } finally {
    title.value = ''
    description.value = ''
    category.value = ''
    isPublic.value = true
    startDate.value = ''
    endDate.value = ''
  }
}

function handleCancel() {
  emit('cancel')
}
</script>

<template>
  <form @submit.prevent="handleFormSubmit" class="space-y-4">
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Title</label>
      <input
        v-model="title"
        type="text"
        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
        placeholder="e.g., Tool Sharing in Block C"
        required
      />
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
      <textarea
        v-model="description"
        rows="3"
        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
        placeholder="Describe your initiative..."
        required
      ></textarea>
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Category</label>
      <select
        v-model="category"
        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
        required
      >
        <option value="">Select a category</option>
        <option v-for="[value, label] in initiativeCategories" :key="value" :value="value">
          {{ label }}
        </option>
      </select>
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Neighborhood</label>
      <input
        type="text"
        :value="userNeighborhood ? neighborhoodLabelMap[userNeighborhood] : ''"
        readonly
        class="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-600"
      />
    </div>

    <div class="flex space-x-2">
      <div class="flex-1">
        <label class="block text-sm font-medium text-gray-700 mb-1">Start Date</label>
        <input
          v-model="startDate"
          type="date"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
          required
        />
      </div>
      <div class="flex-1">
        <label class="block text-sm font-medium text-gray-700 mb-1">End Date</label>
        <input
          v-model="endDate"
          type="date"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
          required
        />
      </div>
    </div>

    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Visibility</label>
      <div class="space-y-2">
        <label class="flex items-center">
          <input type="radio" v-model="isPublic" :value="true" class="mr-2" />
          <span class="text-sm">Public (visible to all users)</span>
        </label>
        <label class="flex items-center">
          <input type="radio" v-model="isPublic" :value="false" class="mr-2" />
          <span class="text-sm">Neighborhood only</span>
        </label>
      </div>
    </div>

    <div class="flex space-x-3 pt-4">
      <button
        type="button"
        @click="handleCancel"
        class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
      >
        Cancel
      </button>
      <button
        type="submit"
        :disabled="!isFormValid"
        class="flex-1 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors disabled:opacity-50"
      >
        Create
      </button>
    </div>
  </form>
</template>
