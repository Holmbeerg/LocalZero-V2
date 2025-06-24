import { defineStore } from 'pinia'
import { ref } from 'vue'
import { uploadApi } from '@/services/apiService.ts'
import type { InitiateUploadRequest } from '@/types/upload.ts'

export const useUploadStore = defineStore('upload', () => {
  const error = ref<string | null>(null)
  const isUploading = ref(false)

  const getUrlAndUploadSingleFile = async (file: File): Promise<string> => {
    const request: InitiateUploadRequest = {
      fileName: file.name,
      contentType: file.type,
    }
    const response = await uploadApi.initiateUpload(request)
    await uploadApi.uploadToS3(response.presignedUrl, file)
    return response.key
  }

  const uploadFiles = async (files: File[]): Promise<string[]> => {
    if (!files.length) {
      return []
    }

    isUploading.value = true
    error.value = null

    try {
      const uploadPromises = files.map((file) => getUrlAndUploadSingleFile(file))
      return await Promise.all(uploadPromises)
    } catch (err) {
      error.value = 'File upload failed. Please try again.'
      throw err
    } finally {
      isUploading.value = false
    }
  }

  return {
    error,
    isUploading,
    uploadFiles,
  }
})
