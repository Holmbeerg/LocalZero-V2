import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Message} from '@/types/message.ts'
import { messagesApi } from '@/services/apiService'

export const useMessagesStore = defineStore('messages', () => {
  const messages = ref<Message[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)

  const fetchMessages = async () => {
    if (isLoaded.value) return
    loading.value = true
    error.value = null
    try {
      messages.value = await messagesApi.getMessages()
      messages.value.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      isLoaded.value = true
    } catch (err) {
      error.value = 'Failed to load messages'
      console.error('Error fetching messages:', err)
    } finally {
      loading.value = false
    }
  }

  function resetMessages() {
    messages.value = []
    loading.value = false
    error.value = null
    isLoaded.value = false
  }

  return {
    messages,
    loading,
    error,
    fetchMessages,
    resetMessages,
  }
})
