<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useInitiativesStore } from '@/stores/initiatives.ts'
import InitiativeCard from '@/components/InitiativeCard.vue'
import NewPostForm from '@/components/NewPostForm.vue'

const route = useRoute()
const initiativesStore = useInitiativesStore()
const { detailLoading, error, currentInitiative } = storeToRefs(initiativesStore)

onMounted(() => {
  console.log('mounted with initiative id:', route.params.id)
  initiativesStore.fetchInitiativeById(Number(route.params.id))
})
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-20 px-6">
    <div class="max-w-7xl mx-auto">
      <div v-if="detailLoading" class="text-center py-8 text-gray-500">
        Loading initiative details...
      </div>
      <div v-else-if="error" class="bg-red-100 text-red-700 text-center py-4 rounded mb-4">
        {{ error }}
      </div>
      <div v-else class="grid grid-cols-1 gap-8">
        <InitiativeCard v-if="currentInitiative" :initiative="currentInitiative"></InitiativeCard>
        <NewPostForm v-if="currentInitiative?.isUserParticipant"></NewPostForm>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
