<script setup lang="ts">
import type { MessageRequest } from '@/types/message.ts'
import { messagesApi } from '@/services/apiService.ts'
import { Plus } from 'lucide-vue-next'
import type { CreatePostRequest } from '@/types/post.ts'
import { ref } from 'vue'
import type { User } from '@/types/user.ts'

const content = ref('')
const receiver = ref('')

defineProps<{
  user: User | null
}>()

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
  if (Number.isNaN(receiver.value)) {
    alert('Invalid receiver ID. Please try again.')
    return
  }

  if (!content.value.trim()) {
    alert('Please enter some content for your message.')
    return
  }

  console.log('Submitting message...')

    const message: MessageRequest = {
      sender: senderId,
      receiver: receiver.value,
      text: content.value
    }

    await initiativeStore.createPost(initiativeId, postContent)

    content.value = ''
    selectedImages.value = []
  } catch (error) {
    console.error('Error creating post:', error)
    alert('Failed to create post. Please try again later.')
  }
}
</script>

<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <!-- Post creation form -->
    <h3 class="text-xl font-semibold mb-4">Share an update</h3>
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- Textarea for content -->
      <textarea
        v-model="content"
        placeholder="Share your progress, ask questions, or update the community!"
        class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
        rows="4"
      ></textarea>

      <button
        type="submit"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors cursor-pointer"
      >
        Post Update
      </button>
    </form>
  </div>
</template>

<style scoped></style>
