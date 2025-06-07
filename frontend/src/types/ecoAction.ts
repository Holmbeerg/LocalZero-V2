export type EcoActionCategory = 'Transport' | 'Waste' | 'Food';

export interface EcoAction {
  id: number;
  action: string;
  date: string;
  category: EcoActionCategory;
  carbonSaved: number;
}
