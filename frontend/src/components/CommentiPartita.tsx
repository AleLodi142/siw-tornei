import { useEffect, useState } from "react";
import axios from "axios";

import {
    Alert,
    CircularProgress,
    Stack,
    Typography
} from "@mui/material";

import FormCommento from "./FormCommento";
import ListaCommenti from "./ListaCommenti";

import { getUtenteAutenticato }
    from "../services/authService";

import {
    eliminaCommento,
    getCommenti,
    modificaCommento
} from "../services/commentoService";

import type {
    AuthResponse,
    Commento,
    ErroreResponse
} from "../types";

interface Props {
    partitaId: number;
}

export default function CommentiPartita({
    partitaId
}: Props) {

    const [commenti, setCommenti] =
        useState<Commento[]>([]);

    const [utente, setUtente] =
        useState<AuthResponse | null>(null);

    const [loading, setLoading] =
        useState(true);

    const [errore, setErrore] =
        useState("");

    const [successo, setSuccesso] =
        useState("");

    useEffect(() => {
        caricaDati();
    }, [partitaId]);

    async function caricaDati() {
        try {
            setLoading(true);

            const auth =
                await getUtenteAutenticato();

            setUtente(auth);

            const lista =
                await getCommenti(partitaId);

            setCommenti(lista);
            setErrore("");

        } catch {
            setErrore(
                "Errore durante il caricamento dei commenti."
            );
        } finally {
            setLoading(false);
        }
    }

    async function gestisciModifica(
        commentoId: number,
        nuovoTesto: string
    ) {
        try {
            setErrore("");
            setSuccesso("");

            await modificaCommento(
                commentoId,
                {
                    testo: nuovoTesto
                }
            );

            await caricaDati();

            setSuccesso(
                "Commento modificato con successo."
            );

        } catch (error) {
            if (
                axios.isAxiosError<ErroreResponse>(
                    error
                )
            ) {
                setErrore(
                    error.response?.data?.messaggio ??
                    "Errore durante la modifica."
                );
            } else {
                setErrore(
                    "Errore durante la modifica."
                );
            }

            throw error;
        }
    }

    async function gestisciEliminazione(
        commentoId: number
    ) {
        try {
            setErrore("");
            setSuccesso("");

            await eliminaCommento(commentoId);

            await caricaDati();

            setSuccesso(
                "Commento eliminato con successo."
            );

        } catch (error) {
            if (
                axios.isAxiosError<ErroreResponse>(
                    error
                )
            ) {
                setErrore(
                    error.response?.data?.messaggio ??
                    "Errore durante l'eliminazione."
                );
            } else {
                setErrore(
                    "Errore durante l'eliminazione."
                );
            }

            throw error;
        }
    }

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <Stack spacing={3}>
            <Typography variant="h5">
                Commenti
            </Typography>

            {errore && (
                <Alert severity="error">
                    {errore}
                </Alert>
            )}

            {successo && (
                <Alert severity="success">
                    {successo}
                </Alert>
            )}

            {utente?.authenticated ? (
                <Alert severity="success">
                    Connesso come {utente.username}
                </Alert>
            ) : (
                <Alert severity="info">
                    Stai navigando come ospite.
                </Alert>
            )}

            <FormCommento
                partitaId={partitaId}
                autenticato={
                    utente?.authenticated ?? false
                }
                onCommentoCreato={caricaDati}
            />

            <ListaCommenti
                commenti={commenti}
                onModifica={gestisciModifica}
                onElimina={gestisciEliminazione}
            />
        </Stack>
    );
}