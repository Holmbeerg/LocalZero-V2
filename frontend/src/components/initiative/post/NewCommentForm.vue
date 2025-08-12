<script setup lang="ts">
import { ref } from 'vue'
import { useInitiativesStore } from '@/stores/initiatives.ts'

const props = defineProps<{ initiativeId: number, postId: number}>()
const store = useInitiativesStore()
const commentText = ref('')
const isSubmitting = ref(false)

const handleSubmit = async () => {
    if(!commentText.value.trim()) return
    isSubmitting.value = true
    try{
        await store.createCommentForPost(props.initiativeId, props.postId, commentText.value)
        commentText.value = ''
    }catch (error){
        console.log('Error submitting comment', error)
    }finally{
        isSubmitting.value = false
    }
}
</script>

<template>
    <form @submit.prevent="handleSubmit" class="mt-4 pt-4 border-t border-gray-100">
        <div class="flex gap-2">
            <input v-model="commentText" type="text" placeholder="Write a comment" class="flex-1 px-3 py-2 border border-gray-300 rounded-lg
            focus:outline-none focus:ring-2 focus:ring-green-500"/>
            <button type="submit" :disabled="isSubmitting || !commentText.trim()" class="px-4 py-2 bg-green-500 text-white rounded-lg 
            hover:bg-green-600 transition-colors disabled:opacity-50 cursor-pointer">
                {{ isSubmitting ? 'Posting...' : 'Post' }}
            </button>
        </div>
    </form>
</template>