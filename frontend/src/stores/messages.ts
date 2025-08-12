import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { MessageResponse } from '@/types/message.ts'
import { messagesApi } from '@/services/apiService'

export const useMessagesStore = defineStore('messages', () => {
  const userMessages = ref<MessageResponse[]>([])
  const messagesLoading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)

  const fetchMessages = async () => {
    if (isLoaded.value) return
    messagesLoading.value = true
    error.value = null
    try {
      userMessages.value = await messagesApi.getMessages()
      userMessages.value.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      isLoaded.value = true
    } catch (err) {
      error.value = 'Failed to load messages'
      console.error('Error fetching messages:', err)
    } finally {
      messagesLoading.value = false
    }
  }

  function resetMessages() {
    userMessages.value = []
    messagesLoading.value = false
    error.value = null
    isLoaded.value = false
  }

  return {
    userMessages: userMessages,
    messagesLoading: messagesLoading,
    error,
    fetchMessages,
    resetMessages,
  }
})
