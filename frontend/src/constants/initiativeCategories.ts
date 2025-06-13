import type { InitiativeCategory } from '@/types/initiative.ts'

export const initiativeCategoriesLabelMap: Record<InitiativeCategory, string> = {
  TOOL_SHARING: 'Tool Sharing',
  FOOD_SWAP: 'Food Swap',
  COMMUNITY_GARDENING: 'Community Gardening',
  RECYCLING_DRIVE: 'Recycling Drive',
  RIDE_SHARING: 'Ride Sharing',
}

export const initiativeCategoryColorMap: Record<InitiativeCategory, string> = {
  TOOL_SHARING: 'bg-orange-100 text-orange-800',
  FOOD_SWAP: 'bg-blue-100 text-blue-800',
  COMMUNITY_GARDENING: 'bg-green-100 text-green-800',
  RECYCLING_DRIVE: 'bg-yellow-100 text-yellow-800',
  RIDE_SHARING: 'bg-purple-100 text-purple-800',
}

export const initiativeFilterOptions = [
  ['ALL', 'All'],
  ...Object.entries(initiativeCategoriesLabelMap),
]

export const initiativeCategories = Object.entries(initiativeCategoriesLabelMap)
