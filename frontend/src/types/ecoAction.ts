export type EcoActionCategory = 'Transport' | 'Waste' | 'Food';

export interface EcoActionOption {
  id: number;
  action: string;
  category: EcoActionCategory
  carbonSaved: number;
}

export interface EcoAction { // TODO: for now we return all data, but we could maybe use EcoActionOption and send less data
  id: number;
  date: string;
  action: string;
  category: EcoActionCategory;
  carbonSaved: number;
}

export interface LogEcoActionRequest {
  actionId: number;
  date: string; // ISO date string
}
