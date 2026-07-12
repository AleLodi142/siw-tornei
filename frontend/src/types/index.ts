export interface Commento {
    id: number;
    testo: string;
    autoreUsername: string;
    partitaId: number;
    proprietario: boolean;
}

export interface AuthResponse {
    authenticated: boolean;
    username: string | null;
}

export interface CreaCommentoRequest {
    testo: string;
}

export interface ModificaCommentoRequest {
    testo: string;
}

export interface ErroreResponse {
    messaggio: string;
}

export interface FormCommentoProps {
    partitaId: number;
    autenticato: boolean;
    onCommentoCreato: () => Promise<void>;
}