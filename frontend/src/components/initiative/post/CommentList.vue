<script setup lang="ts">
import { useInitiativesStore } from '@/stores/initiatives.ts'
import { computed, onMounted } from 'vue'
import CommentItem from './CommentItem.vue'
import { storeToRefs } from 'pinia'

const props = defineProps<{
  initiativeId: number
  postId: number
}>()

const store = useInitiativesStore()
const { postComments, commentsLoading } = storeToRefs(store)

const comments = computed(() => postComments.value[props.postId] || [])
const isLoading = computed(() => commentsLoading.value[props.postId] || false)

const loadComments = async () => {
  await store.getCommentsForPost(props.initiativeId, props.postId)
}

onMounted(() => {
  loadComments()
})
</script>

<template>
  <div v-if="isLoading" class="text-center py-4 text-gray-500">Loading comments...</div>

  <div v-else-if="comments.length > 0" class="space-y-3">
    <CommentItem v-for="comment in comments" :key="comment.id" :comment="comment" />
  </div>

  <div v-else class="text-gray-500 text-sm py-4">No comments yet. Be the first to comment!</div>
</template>

<style scoped></style>
