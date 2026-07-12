import { useState } from "react";

import {
    Button,
    Card,
    CardActions,
    CardContent,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Stack,
    TextField,
    Typography
} from "@mui/material";

import type { Commento } from "../types";

interface Props {
    commento: Commento;

    onModifica: (
        commentoId: number,
        nuovoTesto: string
    ) => Promise<void>;

    onElimina: (
        commentoId: number
    ) => Promise<void>;
}

export default function CommentoCard({
    commento,
    onModifica,
    onElimina
}: Props) {

    const [dialogModificaAperto, setDialogModificaAperto] =
        useState(false);

    const [dialogEliminaAperto, setDialogEliminaAperto] =
        useState(false);

    const [testoModificato, setTestoModificato] =
        useState(commento.testo);

    const [loading, setLoading] =
        useState(false);

    function apriDialogModifica() {
        setTestoModificato(commento.testo);
        setDialogModificaAperto(true);
    }

    function chiudiDialogModifica() {
        if (!loading) {
            setDialogModificaAperto(false);
        }
    }

    function apriDialogElimina() {
        setDialogEliminaAperto(true);
    }

    function chiudiDialogElimina() {
        if (!loading) {
            setDialogEliminaAperto(false);
        }
    }

    async function confermaModifica() {
        try {
            setLoading(true);

            await onModifica(
                commento.id,
                testoModificato
            );

            setDialogModificaAperto(false);

        } finally {
            setLoading(false);
        }
    }

    async function confermaEliminazione() {
        try {
            setLoading(true);

            await onElimina(commento.id);

            setDialogEliminaAperto(false);

        } finally {
            setLoading(false);
        }
    }

    return (
        <>
            <Card>
                <CardContent>
                    <Stack spacing={1}>
                        <Typography>
                            {commento.testo}
                        </Typography>

                        <Typography
                            variant="body2"
                            color="text.secondary"
                        >
                            Autore: {commento.autoreUsername}
                        </Typography>
                    </Stack>
                </CardContent>

                {commento.proprietario && (
                    <CardActions>
                        <Button
                            size="small"
                            variant="outlined"
                            onClick={apriDialogModifica}
                        >
                            Modifica
                        </Button>

                        <Button
                            size="small"
                            color="error"
                            variant="outlined"
                            onClick={apriDialogElimina}
                        >
                            Elimina
                        </Button>
                    </CardActions>
                )}
            </Card>

            <Dialog
                open={dialogModificaAperto}
                onClose={chiudiDialogModifica}
                fullWidth
                maxWidth="sm"
            >
                <DialogTitle>
                    Modifica commento
                </DialogTitle>

                <DialogContent>
                    <TextField
                        autoFocus
                        fullWidth
                        multiline
                        rows={4}
                        margin="normal"
                        label="Testo del commento"
                        value={testoModificato}
                        onChange={(event) =>
                            setTestoModificato(
                                event.target.value
                            )
                        }
                    />
                </DialogContent>

                <DialogActions>
                    <Button
                        onClick={chiudiDialogModifica}
                        disabled={loading}
                    >
                        Annulla
                    </Button>

                    <Button
                        variant="contained"
                        onClick={confermaModifica}
                        disabled={
                            loading ||
                            testoModificato.trim() === ""
                        }
                    >
                        Salva modifica
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog
                open={dialogEliminaAperto}
                onClose={chiudiDialogElimina}
            >
                <DialogTitle>
                    Elimina commento
                </DialogTitle>

                <DialogContent>
                    <DialogContentText>
                        Sei sicuro di voler eliminare questo commento?
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button
                        onClick={chiudiDialogElimina}
                        disabled={loading}
                    >
                        Annulla
                    </Button>

                    <Button
                        color="error"
                        variant="contained"
                        onClick={confermaEliminazione}
                        disabled={loading}
                    >
                        Elimina
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}