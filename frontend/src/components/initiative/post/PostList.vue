<script setup lang="ts">
import type { PostSummaryResponse } from '@/types/post.ts'
import Post from '@/components/initiative/post/PostItem.vue'

defineProps<{ posts: PostSummaryResponse[] }>()
</script>

<template>
  <div v-if="posts.length === 0" class="text-center py-12 text-gray-500">
    <h3 class="text-lg font-medium mb-2">No posts yet</h3>
    <p>Be the first to share your thoughts on this initiative!</p>
  </div>

  <div v-else class="space-y-4">
    <Post v-for="post in posts" :key="post.id" :post="post" />
  </div>
</template>

<style scoped></style>
