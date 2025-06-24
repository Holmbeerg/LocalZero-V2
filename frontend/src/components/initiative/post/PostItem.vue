<script setup lang="ts">
import type { PostSummaryResponse } from '@/types/post.ts'
import { MessageCircleMore, Heart, UserIcon } from 'lucide-vue-next'

const props = defineProps<{ post: PostSummaryResponse }>()

function formatDate(dateString: string): string {
  return new Date(dateString).toLocaleDateString('sv-SE', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

const handleLike = () => {
  console.log('Like post:', props.post.id)
}

const handleComment = () => {
  console.log('Comment on post:', props.post.id)
}
</script>

<template>
  <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
    <!-- Post Header -->
    <div class="flex items-start space-x-3 mb-4">
      <!-- User Avatar -->
      <UserIcon class="w-6 h-6 text-gray-500" />

      <!-- Author Info -->
      <div class="flex items-center space-x-2">
        <h4 class="text-sm font-medium text-gray-900">
          {{ post.author.name }}
        </h4>
        <span class="text-xs text-gray-500">•</span>
        <time class="text-xs text-gray-500" :datetime="post.createdAt">
          {{ formatDate(post.createdAt) }}
        </time>
      </div>
    </div>

    <!-- Post Content -->
    <div class="mb-4">
      <p class="text-gray-900 whitespace-pre-wrap">{{ post.text }}</p>
    </div>

    <!-- Post Images -->
    <div v-if="post.imageUrls.length > 0" class="mb-4">
      <div v-if="post.imageUrls.length === 1" class="rounded-lg overflow-hidden bg-gray-100">
        <img
          :src="post.imageUrls[0]"
          :alt="'Image from post by ' + post.author.name"
          class="w-full h-auto max-h-96 object-contain mx-auto"
        />
      </div>

      <div
        v-else
        :class="{
          'grid grid-cols-2 gap-3': post.imageUrls.length === 2,
          'grid grid-cols-3 gap-3': post.imageUrls.length > 2,
        }"
        class="rounded-lg overflow-hidden"
      >
        <div
          v-for="(imageUrl, index) in post.imageUrls"
          :key="index"
          class="relative aspect-square bg-gray-100"
        >
          <img
            :src="imageUrl"
            :alt="'Image ' + (index + 1) + ' from post by ' + post.author.name"
            class="w-full h-full object-cover"
          />
        </div>
      </div>
    </div>

    <!-- Post Actions -->
    <div class="flex items-center justify-between pt-3 border-t border-gray-100">
      <div class="flex items-center space-x-4">
        <!-- Like Button -->
        <button
          @click="handleLike"
          class="flex items-center space-x-1 text-gray-500 hover:text-blue-600 transition-colors duration-200 cursor-pointer"
        >
          <Heart class="w-5 h-5" />
          <span class="text-sm">{{ post.likeCount }}</span>
        </button>

        <!-- Comment Button -->
        <button
          @click="handleComment"
          class="flex items-center space-x-1 text-gray-500 hover:text-blue-600 transition-colors duration-200 cursor-pointer"
        >
          <MessageCircleMore class="w-5 h-5" />
          <span class="text-sm">{{ post.commentCount }}</span>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
