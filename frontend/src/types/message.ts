import type { UserSummary } from '@/types/user.ts'

export interface MessageUserSummary extends UserSummary {
  email: string
}

export interface Message {
  sender: MessageUserSummary
  receiver: MessageUserSummary
  text: string
  messageContent: string
  createdAt: string
}
