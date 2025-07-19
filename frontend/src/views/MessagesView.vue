<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth.ts'
import MessageUserProfileCard from '@/components/message/MessageUserProfileCard.vue'
import MessageList from '@/components/message/MessageList.vue'
import NewMessageForm from '@/components/message/NewMessageForm.vue'
import { useMessagesStore } from '@/stores/messages.ts'
import { onMounted } from 'vue'

const { user } = storeToRefs(useAuthStore())
const messagesStore = useMessagesStore()
const { userMessages, messagesLoading, error } = storeToRefs(useMessagesStore())

onMounted(() => {
  messagesStore.fetchMessages()
})
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-20 px-6">
    <div v-if="messagesLoading" class="text-center py-8 text-gray-500">Loading messages...</div>
    <div v-else-if="error" class="bg-red-100 text-red-700 text-center py-4 rounded mb-4">
      {{ error }}
    </div>
    <div class="max-w-7xl mx-auto">
      <!-- Left Column - User Info -->
      <div class="lg:col-span-3">
        <MessageUserProfileCard :user="user" />
      </div>
      <!-- Middle Column - Messages -->
      <div class="lg:col-span-6">
        <MessageList :messages="userMessages" />
      </div>
      <!-- Right Column - Send Message -->
      <div class="lg:col-span-3">
        <NewMessageForm />
      </div>
    </div>
  </div>
</template>
