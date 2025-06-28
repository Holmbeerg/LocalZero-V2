import type { UserSummary } from '@/types/user.ts'

export interface CommentResponse {
  id: number
  postId: number
  text: string
  author: UserSummary
  createdAt: string
}
