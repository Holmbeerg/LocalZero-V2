import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CreateInitiativeRequest, Initiative, InitiativeDetail } from '@/types/initiative'
import { initiativesApi } from '@/services/apiService'
import type { CreatePostRequest } from '@/types/post.ts'

export const useInitiativesStore = defineStore('initiatives', () => {
  const initiatives = ref<Initiative[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isLoaded = ref(false)

  const currentInitiative = ref<InitiativeDetail | null>(null)
  const detailLoading = ref(false)

  const fetchInitiatives = async () => {
    if (isLoaded.value) return
    loading.value = true
    error.value = null
    try {
      initiatives.value = await initiativesApi.getAccessibleInitiatives()
      isLoaded.value = true
    } catch (err) {
      error.value = 'Failed to load initiatives'
      console.error('Error fetching initiatives:', err)
    } finally {
      loading.value = false
    }
  }

  const fetchInitiativeById = async (id: number) => {
    error.value = null
    detailLoading.value = true
    try {
      currentInitiative.value = await initiativesApi.getInitiativeById(id)
      return currentInitiative.value
    } catch (err) {
      error.value = 'Failed to load initiative details'
      console.error('Error fetching initiative by ID:', err)
    } finally {
      detailLoading.value = false
    }
  }

  const createInitiative = async (initiative: CreateInitiativeRequest) => {
    error.value = null
    try {
      const newInitiative = await initiativesApi.createInitiative(initiative)
      addInitiativeToStore(newInitiative)
      return newInitiative
    } catch (err) {
      error.value = 'Failed to create initiative'
      console.error('Error creating initiative:', err)
    } finally {
      loading.value = false
    }
  }

  const joinInitiative = async (initiativeId: number) => {
    error.value = null
    try {
      const updatedInitiative = await initiativesApi.joinInitiative(initiativeId)
      const index = initiatives.value.findIndex((i) => i.id === updatedInitiative.id)
      if (index !== -1) {
        initiatives.value[index] = updatedInitiative
      }

      if (currentInitiative.value && currentInitiative.value.id === updatedInitiative.id) {
        currentInitiative.value.isUserParticipant = true
      }

      return updatedInitiative
    } catch (err) {
      error.value = 'Failed to join initiative'
      console.error('Error joining initiative:', err)
    } finally {
      loading.value = false
    }
  }

  const createPost = async (initiativeId: number, postContent: CreatePostRequest) => {
    error.value = null
    try {
      const newPost = await initiativesApi.createPost(initiativeId, postContent)
      if (currentInitiative.value) {
        currentInitiative.value.posts.push(newPost)
      }
      return newPost
    } catch (err) {
      error.value = 'Failed to create post'
      console.error('Error creating post:', err)
    } finally {
      loading.value = false
    }
  }

  const likePost = async (initiativeId: number, postId: number) => {
    error.value = null
    try {
      const updatedPost = await initiativesApi.likePost(initiativeId, postId)
      if (currentInitiative.value) {
        const postIndex = currentInitiative.value.posts.findIndex((p) => p.id === postId)
        if (postIndex !== -1) {
          currentInitiative.value.posts[postIndex] = updatedPost
        }
      }
      return updatedPost
    } catch (err) {
      error.value = 'Failed to like post'
      console.error('Error liking post:', err)
    } finally {
      loading.value = false
    }
  }

  const addInitiativeToStore = (initiative: Initiative) => {
    initiatives.value.push(initiative)
    initiatives.value.sort(
      (a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime(),
    )
  }

  function resetInitiatives() {
    initiatives.value = []
    loading.value = false
    error.value = null
    isLoaded.value = false
  }

  return {
    initiatives,
    currentInitiative,
    loading,
    error,
    fetchInitiatives,
    fetchInitiativeById,
    detailLoading,
    createInitiative,
    resetInitiatives,
    joinInitiative,
    createPost,
    likePost,
  }
})
