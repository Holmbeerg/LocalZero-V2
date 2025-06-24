import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import type { LoginCredentials, RegisterData, User } from '@/types/user.ts'
import type { EcoAction, LogEcoActionRequest } from '@/types/ecoAction.ts'
import type { CreateInitiativeRequest, Initiative, InitiativeDetail } from '@/types/initiative.ts'
import type { InitiateUploadRequest, PresignedUploadResponse } from '@/types/upload.ts'
import type { CreatePostRequest, PostSummaryResponse } from '@/types/post.ts'

const API_BASE_URL = 'http://localhost:8080/api'

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// Response interceptor
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      // optional chaining
      console.warn('Unauthorized request')
    } else if (error.response?.status === 403) {
      // Forbidden
      console.warn('Forbidden request')
    } else if (error.response?.status >= 500) {
      // Server error
      console.error('Server error:', error.response?.data?.message)
    }
    return Promise.reject(error)
  },
)

// Auth API endpoints
export const authApi = {
  async getCurrentUser(): Promise<User | null> {
    try {
      return await apiClient.get('/auth/me')
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 401) {
        // TODO: is this correct?
        return null // If unauthorized, return null
      }
      console.error('Failed to fetch current user:', error)
      throw error // bubble up other errors
    }
  },

  async login(credentials: LoginCredentials) {
    return await apiClient.post('/auth/login', credentials)
  },

  async register(data: RegisterData) {
    return await apiClient.post('/auth/register', data)
  },

  async logout() {
    return await apiClient.post('/auth/logout')
  },
}

// Eco Actions API endpoints
export const ecoActionsApi = {
  async getActions(): Promise<EcoAction[]> {
    try {
      return await apiClient.get('/eco-actions')
    } catch (error) {
      console.error('Failed to fetch eco actions:', error)
      throw error
    }
  },

  async logAction(ecoAction: LogEcoActionRequest): Promise<EcoAction> {
    try {
      return await apiClient.post('/eco-actions', ecoAction)
    } catch (error) {
      console.error('Failed to add eco action:', error)
      throw error
    }
  },
}

// Initiatives API endpoints

export const initiativesApi = {
  async getAccessibleInitiatives(): Promise<Initiative[]> {
    try {
      return await apiClient.get('/initiatives')
    } catch (error) {
      console.error('Failed to fetch initiatives:', error)
      throw error
    }
  },

  async getInitiativeById(id: number): Promise<InitiativeDetail> {
    try {
      return await apiClient.get(`/initiatives/${id}`)
    } catch (error) {
      console.error(`Failed to fetch initiative with ID ${id}:`, error)
      throw error
    }
  },

  async createInitiative(initiative: CreateInitiativeRequest): Promise<Initiative> {
    try {
      return await apiClient.post('/initiatives', initiative)
    } catch (error) {
      console.error('Failed to create initiative:', error)
      throw error
    }
  },

  async joinInitiative(initiativeId: number): Promise<Initiative> {
    try {
      return await apiClient.post(`/initiatives/${initiativeId}/join`)
    } catch (error) {
      console.error(`Failed to join initiative with ID ${initiativeId}:`, error)
      throw error
    }
  },

  async createPost(initiativeId: number, post: CreatePostRequest): Promise<PostSummaryResponse> {
    try {
      return await apiClient.post(`/initiatives/${initiativeId}/posts`, post)
    } catch (error) {
      console.error(`Failed to create post for initiative with ID ${initiativeId}:`, error)
      throw error
    }
  },
}

// Upload API endpoints

export const uploadApi = {
  async initiateUpload(content: InitiateUploadRequest): Promise<PresignedUploadResponse> {
    try {
      return await apiClient.post('/uploads/initiate', content)
    } catch (error) {
      console.error('Failed to initiate upload:', error)
      throw error
    }
  },

  async uploadToS3(presignedUrl: string, file: File): Promise<Response> {
    return fetch(presignedUrl, {
      method: 'PUT',
      body: file,
      headers: {
        'Content-Type': file.type,
      },
    })
  },
}
