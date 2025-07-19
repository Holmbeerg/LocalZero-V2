<script setup lang="ts">
import type { MessageRequest } from '@/types/message.ts'
import { messagesApi } from '@/services/apiService.ts'
import { ref } from 'vue'

const content = ref('')
const receiver = ref('')

const sendMessage = async (request: MessageRequest) => {
  try {
    await messagesApi.postMessage(request)
    return true
  } catch (err) {
    alert('Failed to send message')
    console.error('Error sending message:', err)
    throw err
  }
}

const handleSubmit = async () => {
  if (!receiver.value.trim()) {
    alert('Please enter an email')
    return
  }

  if (!content.value.trim()) {
    alert('Please enter some content for your message.')
    return
  }

  console.log('Submitting message...')

    const message: MessageRequest = {
      receiverEmail: receiver.value,
      text: content.value
    }

    await sendMessage(message);
}
</script>

<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <!-- Message creation form -->
    <h3 class="text-xl font-semibold mb-4">Send a message</h3>
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- Text area for receiver email -->
      <textarea
        v-model="receiver"
        placeholder="E.g. janedoe@gmail.com"
        class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
        rows="1"
      ></textarea>
      <!-- Text area for content -->
      <textarea
        v-model="content"
        placeholder="Engage in the community directly through messages!"
        class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
        rows="4"
      ></textarea>

      <button
        type="submit"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors cursor-pointer"
      >
        Send
      </button>
    </form>
  </div>
</template>

<style scoped></style>
