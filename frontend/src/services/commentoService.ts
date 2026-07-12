import api from "./api";

import type {
    Commento,
    CreaCommentoRequest,
    ModificaCommentoRequest
} from "../types";

export async function getCommenti(
    partitaId: number
): Promise<Commento[]> {

    const response = await api.get<Commento[]>(
        `/api/partite/${partitaId}/commenti`
    );

    return response.data;
}

export async function creaCommento(
    partitaId: number,
    request: CreaCommentoRequest
): Promise<Commento> {

    const response = await api.post<Commento>(
        `/api/partite/${partitaId}/commenti`,
        request
    );

    return response.data;
}

export async function modificaCommento(
    commentoId: number,
    request: ModificaCommentoRequest
): Promise<Commento> {

    const response = await api.put<Commento>(
        `/api/commenti/${commentoId}`,
        request
    );

    return response.data;
}

export async function eliminaCommento(
    commentoId: number
): Promise<void> {

    await api.delete(
        `/api/commenti/${commentoId}`
    );
}