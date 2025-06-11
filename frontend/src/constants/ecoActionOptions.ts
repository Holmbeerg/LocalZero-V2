import type { EcoActionOption } from '@/types/ecoAction.ts'

export const ecoActionOptions: EcoActionOption[] = [
  { id: 1, action: 'Biked to work', category: 'Transport', carbonSaved: 2.3 },
  { id: 2, action: 'Carpooled to event', category: 'Transport', carbonSaved: 3.1 },
  { id: 3, action: 'Used public transport', category: 'Transport', carbonSaved: 1.5 },
  { id: 4, action: 'Drove electric vehicle', category: 'Transport', carbonSaved: 2.0 },
  { id: 5, action: 'Used reusable bags for shopping', category: 'Waste', carbonSaved: 0.1 },
  { id: 6, action: 'Recycled household waste', category: 'Waste', carbonSaved: 0.5 },
  { id: 7, action: 'Composted food scraps', category: 'Waste', carbonSaved: 0.7 },
  { id: 8, action: 'Ate vegetarian meal', category: 'Food', carbonSaved: 1.0 },
  { id: 9, action: 'Participated in community garden', category: 'Food', carbonSaved: 1.2 },
  { id: 10, action: 'Chose local/seasonal produce', category: 'Food', carbonSaved: 0.8 },
] as const // Ensure the array is readonly
