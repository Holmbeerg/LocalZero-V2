import type { UserSummary } from '@/types/user.ts'

export interface CreatePostRequest {
  text: string
  imageKeys?: string[]
}

export interface PostSummaryResponse {
  id: number
  initiativeId: number
  text: string
  imageUrls: string[]
  author: UserSummary
  createdAt: string
  likeCount: number
  commentCount: number
}
