import api from "./api";

import type { AuthResponse } from "../types";

export async function getUtenteAutenticato()
: Promise<AuthResponse> {

    const response = await api.get<AuthResponse>(
        "/api/auth/me"
    );

    return response.data;
}