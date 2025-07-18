import type { UserSummary } from '@/types/user.ts'

export interface Message {
  id: bigint
  sender: UserSummary
  receiver: UserSummary
  text: string
  createdAt: string
}

export interface MessageRequest {
  sender: bigint
  receiver: bigint
  text: string
}
