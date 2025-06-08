import type { EcoActionOption } from '@/types/ecoAction.ts'

export const ecoActionOptions: EcoActionOption[] = [
  { id: 1, action: 'Biked to work', category: 'Transport', carbonSaved: 2.30 },
  { id: 2, action: 'Carpooled to event', category: 'Transport', carbonSaved: 3.10 },
  { id: 3, action: 'Used public transport', category: 'Transport', carbonSaved: 1.50 },
  { id: 4, action: 'Drove electric vehicle', category: 'Transport', carbonSaved: 2.00 },
  { id: 5, action: 'Used reusable bags for shopping', category: 'Waste', carbonSaved: 0.10 },
  { id: 6, action: 'Recycled household waste', category: 'Waste', carbonSaved: 0.50 },
  { id: 7, action: 'Composted food scraps', category: 'Waste', carbonSaved: 0.70 },
  { id: 8, action: 'Ate vegetarian meal', category: 'Food', carbonSaved: 1.00 },
  { id: 9, action: 'Participated in community garden', category: 'Food', carbonSaved: 1.20 },
  { id: 10, action: 'Chose local/seasonal produce', category: 'Food', carbonSaved: 0.80 }
] as const; // Ensure the array is readonly
