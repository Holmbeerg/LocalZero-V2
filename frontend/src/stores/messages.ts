import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Message } from '@/types/message.ts'
import type { UserSummary } from '@/types/user.ts'
import { messagesApi } from '@/services/apiService'

export const useMessagesStore = defineStore('messages', () => {
  const messages = ref<Message[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)
  const currentReceiver = ref<UserSummary | null>(null)

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
  const sendMessage = async (receiverId: number, request: Message) => {
    error.value = null
    try {
      // caches the current receiver for repeated messaging
      if (!currentReceiver.value || !(receiverId === currentReceiver.value.id)) {
        currentReceiver.value = await messagesApi.getUser(receiverId)
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
