import type { Neighborhood } from '@/constants/neighborhoods.ts'
import type { UserSummary } from '@/types/user.ts'

export interface Initiative {
  id: number
  title: string
  description: string
  creator: UserSummary
  location: Neighborhood
  category: InitiativeCategory
  isPublic: boolean
  participantsCount: number
  startDate: string
  endDate: string
  isUserParticipant: boolean
  isUserCreator: boolean
}

export interface InitiativeParticipant extends UserSummary {
  joinedAt: string
}

export interface InitiativeDetail extends Initiative {
  participants: InitiativeParticipant[]
}

export type InitiativeCategory =
  | 'TOOL_SHARING'
  | 'FOOD_SWAP'
  | 'COMMUNITY_GARDENING'
  | 'RECYCLING_DRIVE'
  | 'RIDE_SHARING'

export interface CreateInitiativeRequest {
  title: string
  description: string
  category: InitiativeCategory
  isPublic: boolean
  startDate: string // ISO date string
  endDate: string // ISO date string
}
