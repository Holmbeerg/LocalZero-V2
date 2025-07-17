import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Message, MessageUserSummary } from '@/types/message.ts'
import { messagesApi } from '@/services/apiService'

export const useMessagesStore = defineStore('messages', () => {
  const messages = ref<Message[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)
  const currentReceiver = ref<MessageUserSummary | null>(null)

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


  // TODO: move out of store?
  const sendMessage = async (email: string, request: Message) => {
    error.value = null
    try {
      // caches the current receiver for repeated messaging
      if (!currentReceiver.value || !(email === currentReceiver.value.email)) {
        currentReceiver.value = await messagesApi.getUserFromEmail(email)
        if (!currentReceiver.value) return false
      }
      request.receiver = currentReceiver.value
      await messagesApi.postMessage(request)
      return true
    } catch (err) {
      error.value = 'Failed to send message'
      console.error('Error sending message:', err)
      throw err
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
    sendMessage,
  }
})
