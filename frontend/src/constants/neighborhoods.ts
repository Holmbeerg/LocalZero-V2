export const neighborhoodLabelMap = {
  CENTRUM: 'Centrum',
  VASTRA_HAMNEN: 'Västra Hamnen',
  LIMHAMN: 'Limhamn',
  ROSENGARD: 'Rosengård',
  HYLLIE: 'Hyllie',
  MOLLEVANGEN: 'Möllevången',
  DAVIDSHALL: 'Davidshall',
  AUGUSTENBORG: 'Augustenborg',
  KULLADAL: 'Kulladal',
  LORENSBORG: 'Lorensborg',
  SOFIELUND: 'Sofielund',
  SORGENFRI: 'Sorgenfri',
  OXIE: 'Oxie',
  RORSJOSTADEN: 'Rörsjöstaden',
  KIRSEBERG: 'Kirseberg',
  BUNKEFLOSTRAND: 'Bunkeflostrand',
} as const

export const neighborhoods = Object.entries(neighborhoodLabelMap) //returns an array of [key, value] pairs

export type Neighborhood = keyof typeof neighborhoodLabelMap // keyof typeof will infer the type of a javascript object and return a type that is the union of its keys
// https://aaronbos.dev/posts/typescript-literal-types
