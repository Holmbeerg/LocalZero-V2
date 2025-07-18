<script setup lang="ts">
import { formatRelativeTime } from '@/utils/dateUtils'
import type { Message } from '@/types/message.ts'

defineProps<{
  message: Message
}>()
</script>

<template>
  <div class="bg-gray-50 rounded-lg p-3">
    <div class="flex items-start mb-2 space-x-2">
      <span class="font-bold text-sm">{{ message.sender.name }}</span>
      <span class="font-medium text-sm">{{ message.sender.id }}</span>
      <span class="text-xs text-gray-500">•</span>
      <time class="text-xs text-gray-400" :datetime="message.createdAt">
        {{ formatRelativeTime(message.createdAt) }}
      </time>
    </div>
    <p class="text-gray-600 whitespace-pre-wrap text-sm">{{ message.text }}</p>
  </div>
</template>

<style scoped></style>
