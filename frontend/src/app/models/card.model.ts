export interface Card {
    id?: number | null;
    number: string;
    holderName: string;
    limit: number;
    balance?: number | null;
}