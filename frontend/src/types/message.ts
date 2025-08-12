import type { UserMessageSummary } from '@/types/user.ts'

export interface MessageResponse {
  id: number
  sender: UserMessageSummary
  receiver: UserMessageSummary
  text: string
  createdAt: string
}

export interface MessageRequest {
  receiverEmail: string
  text: string
}
