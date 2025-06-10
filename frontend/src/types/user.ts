import type { Neighborhood } from '@/constants/neighborhoods.ts'

export interface Role {
  roleId: number
  roleName: string
}

export const roleDisplayNames: Record<string, string> = {
  RESIDENT: 'Resident',
  COMMUNITY_ORGANIZER: 'Community Organizer',
}

export interface User {
  id: string
  name: string
  email: string
  location: Neighborhood
  createdAt: string
  roles: Role[]
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterData {
  name: string
  email: string
  password: string
  location: string
}
