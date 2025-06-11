import type { InitiativeCategory } from '@/types/initiative.ts'

export const initiativeCategoriesLabelMap: Record<InitiativeCategory, string> = {
  TOOL_SHARING: 'Tool Sharing',
  FOOD_SWAP: 'Food Swap',
  COMMUNITY_GARDENING: 'Community Gardening',
  RECYCLING_DRIVE: 'Recycling Drive',
  RIDE_SHARING: 'Ride Sharing',
}

export const initiativeFilterOptions = [
  ['ALL', 'All'],
  ...Object.entries(initiativeCategoriesLabelMap),
]

export const initiativeCategories = Object.entries(initiativeCategoriesLabelMap)
