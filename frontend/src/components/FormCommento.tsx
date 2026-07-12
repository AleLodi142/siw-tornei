import { useState } from "react";

import {
    Alert,
    Button,
    Stack,
    TextField
} from "@mui/material";

import { creaCommento } from "../services/commentoService";

import type { FormCommentoProps } from "../types";

export default function FormCommento({
    partitaId,
    autenticato,
    onCommentoCreato
}: FormCommentoProps) {

    const [testo, setTesto] = useState("");

    const [errore, setErrore] = useState("");

    const [successo, setSuccesso] = useState("");

    const [loading, setLoading] = useState(false);

    async function inviaCommento() {

        try {

            setLoading(true);

            setErrore("");

            setSuccesso("");

            await creaCommento(partitaId, {
                testo
            });

            setTesto("");

            setSuccesso("Commento pubblicato con successo.");

            await onCommentoCreato();

        } catch (errore: any) {

            if (errore.response?.data?.messaggio) {

                setErrore(
                    errore.response.data.messaggio
                );

            } else {

                setErrore(
                    "Errore durante il salvataggio."
                );
            }

        } finally {

            setLoading(false);
        }
    }

    if (!autenticato) {

        return (
            <Alert severity="info">

                Effettua il login per scrivere un commento.

            </Alert>
        );
    }

    return (

        <Stack spacing={2}>

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

        <TextField
            multiline
            rows={4}
            label="Scrivi un commento"
            value={testo}
            onChange={(e) => setTesto(e.target.value)}

            sx={{
                "& .MuiOutlinedInput-root": {
                    backgroundColor: "#ffffff"
                }
            }}
         />

            <Button

                variant="contained"

                disabled={loading}

                onClick={inviaCommento}

            >

                Pubblica commento

            </Button>

        </Stack>
    );
}