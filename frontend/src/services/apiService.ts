import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import type { LoginCredentials, RegisterData, User } from '@/stores/auth'

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
  }
)

// Response interceptor
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) { // optional chaining
      // Unauthorized - could redirect to login
      console.warn('Unauthorized request')
    } else if (error.response?.status === 403) {
      // Forbidden
      console.warn('Forbidden request')
    } else if (error.response?.status >= 500) {
      // Server error
      console.error('Server error:', error.response?.data?.message)
    }
    return Promise.reject(error)
  }
)

// Auth API endpoints
export const authApi = {
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

export default apiClient
