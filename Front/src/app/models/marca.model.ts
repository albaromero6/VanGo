export interface Marca {
    idMar: number;
    nombre: string;
}

export interface Modelo {
    idMod: number;
    nombre: string;
    marca: Marca;
} 