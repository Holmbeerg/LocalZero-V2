<script setup lang="ts">
import type { PostSummaryResponse } from '@/types/post.ts'
import { MessageCircleMore, Heart, UserIcon } from 'lucide-vue-next'
import { useInitiativesStore } from '@/stores/initiatives.ts'
import { useRoute } from 'vue-router'
import { ref } from 'vue'
import CommentList from '@/components/initiative/post/CommentList.vue'
import { formatRelativeTime } from '@/utils/dateUtils'
import NewCommentForm from '@/components/initiative/post/NewCommentForm.vue'

const props = defineProps<{ post: PostSummaryResponse }>()
const initiativesStore = useInitiativesStore()
const route = useRoute()
const initiativeId = Number(route.params.id)

const showComments = ref(false)

const handleLike = async () => {
  if (isNaN(initiativeId)) {
    alert('Invalid initiative ID. Please try again.')
    return
  }

  try {
    await initiativesStore.likePost(initiativeId, props.post.id)
    console.log('Post liked successfully')
  } catch (error) {
    console.error('Error liking post:', error)
    alert('Failed to like post. Please try again later.')
  }
}

const handleComment = () => {
  showComments.value = !showComments.value
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
          {{ formatRelativeTime(post.createdAt) }}
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
          <Heart class="w-5 h-5" :class="{ 'text-red-500 fill-current': post.isLikedByUser }" />
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

    <!-- Comments Section -->
    <div v-if="showComments" class="mt-4 pt-4 border-t border-gray-100">
      <CommentList :initiativeId="initiativeId" :postId="post.id" />
      <NewCommentForm :initiativeId="initiativeId" :postId="post.id"/>
    </div>
  </div>
</template>

<style scoped></style>
