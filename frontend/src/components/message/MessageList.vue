<script setup lang="ts">
import MessageItem from './MessageItem.vue'
import type { MessageResponse } from '@/types/message.ts'

defineProps<{
  messages: MessageResponse[],
}>()
</script>

<template>
  <div v-if="messages.length > 0" class="space-y-3">
    <MessageItem v-for="message in messages" :key="message.id" :message="message" />
  </div>

  <div v-else class="text-gray-500 text-sm py-4">No messages yet! Reach out to some friends!</div>
</template>

<style scoped></style>
