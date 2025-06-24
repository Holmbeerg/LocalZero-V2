<script setup lang="ts">
import { ref } from 'vue'
import { Plus, X } from 'lucide-vue-next'
import { useRoute } from 'vue-router'
import { useUploadStore } from '@/stores/uploads.ts'
import { useInitiativesStore } from '@/stores/initiatives.ts'
import type { CreatePostRequest } from '@/types/post.ts'

const content = ref('')
const selectedImages = ref<File[]>([])
const uploadStore = useUploadStore()
const initiativeStore = useInitiativesStore()
const route = useRoute()

const handleSubmit = async () => {
  const initiativeId = Number(route.params.id)

  if (isNaN(initiativeId)) {
    alert('Invalid initiative ID. Please try again.')
    return
  }

  if (!content.value.trim()) {
    alert('Please enter some content for your post.')
    return
  }

  console.log('Submitting post...')

  try {
    let uploadedFileKeys: string[] = []

    if (selectedImages.value.length > 0) {
      uploadedFileKeys = await uploadStore.uploadFiles(selectedImages.value)
      console.log('Uploaded file keys:', uploadedFileKeys)
    }

    const postContent: CreatePostRequest = {
      text: content.value,
      imageKeys: uploadedFileKeys,
    }

    await initiativeStore.createPost(initiativeId, postContent)

    content.value = ''
    selectedImages.value = []
  } catch (error) {
    console.error('Error creating post:', error)
    alert('Failed to create post. Please try again later.')
  }
}

const handleFileSelection = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files) return

  const newFiles = Array.from(input.files)

  newFiles.forEach((file) => {
    if (validateFile(file)) {
      selectedImages.value.push(file)
    }
  })

  input.value = ''
  console.log('Currently selected images:', selectedImages.value)
}

const validateFile = (file: File): boolean => {
  const maxSize = 5 * 1024 * 1024 // 5MB
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif']

  if (file.size > maxSize) {
    alert(`File "${file.name}" is too large. Maximum size is 5MB.`)
    return false
  }

  if (!allowedTypes.includes(file.type)) {
    alert(`Invalid file type for "${file.name}". Only JPEG, PNG, and GIF are allowed.`)
    return false
  }

  return true
}

const removeImage = (index: number) => {
  selectedImages.value.splice(index, 1)
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

      <!-- Add photo and post button -->
      <div class="flex justify-between items-center">
        <label
          for="file-upload"
          class="flex items-center gap-2 text-gray-500 hover:text-gray-700 cursor-pointer"
        >
          <Plus class="w-6 h-6" />
          <span>Add Photo</span>
        </label>
        <input
          id="file-upload"
          type="file"
          accept="image/*"
          multiple
          @change="handleFileSelection"
          class="hidden"
        />

        <button
          type="submit"
          class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors cursor-pointer"
        >
          Post Update
        </button>
      </div>

      <!-- Display the list of selected images -->
      <div v-if="selectedImages.length > 0" class="mt-4 space-y-2">
        <p class="font-semibold text-sm">Selected images:</p>
        <ul class="space-y-2">
          <li
            v-for="(file, index) in selectedImages"
            :key="file.name"
            class="flex items-center justify-between bg-gray-100 p-2 rounded-md text-sm"
          >
            <span> {{ file.name }} ({{ (file.size / 1024).toFixed(2) }} KB)</span>

            <button @click="removeImage(index)" type="button" class="text cursor-pointer">
              <X />
            </button>
          </li>
        </ul>
      </div>
    </form>
  </div>
</template>

<style scoped></style>
