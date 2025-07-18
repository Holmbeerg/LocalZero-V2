import type { UserSummary } from '@/types/user.ts'

export interface Message {
  sender: UserSummary
  receiver: UserSummary
  text: string
  createdAt: string
}
