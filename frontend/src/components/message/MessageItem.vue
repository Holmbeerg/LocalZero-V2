<script setup lang="ts">
import { formatRelativeTime } from '@/utils/dateUtils'
import type { MessageResponse } from '@/types/message.ts'

defineProps<{
  messageResponse: MessageResponse
}>()
</script>

<template>
  <div class="bg-gray-50 rounded-lg p-3">
    <div class="flex items-start mb-2 space-x-2">
      <span class="font-bold text-sm">{{ messageResponse.sender.name }}</span>
      <span class="font-medium text-sm">{{ messageResponse.sender.email }}</span>
      <span class="text-xs text-gray-500">•</span>
      <time class="text-xs text-gray-400" :datetime="messageResponse.createdAt">
        {{ formatRelativeTime(messageResponse.createdAt) }}
      </time>
    </div>
    <p class="text-gray-600 whitespace-pre-wrap text-sm">{{ messageResponse.text }}</p>
  </div>
</template>

<style scoped></style>
