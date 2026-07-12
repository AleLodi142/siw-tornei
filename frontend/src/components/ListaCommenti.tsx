import {
    Stack,
    Typography
} from "@mui/material";

import CommentoCard from "./CommentoCard";

import type { Commento } from "../types";

interface Props {
    commenti: Commento[];

    onModifica: (
        commentoId: number,
        nuovoTesto: string
    ) => Promise<void>;

    onElimina: (
        commentoId: number
    ) => Promise<void>;
}

export default function ListaCommenti({
    commenti,
    onModifica,
    onElimina
}: Props) {

    if (commenti.length === 0) {
        return (
            <Typography>
                Nessun commento presente.
            </Typography>
        );
    }

    return (
        <Stack spacing={2}>
            {commenti.map((commento) => (
                <CommentoCard
                    key={commento.id}
                    commento={commento}
                    onModifica={onModifica}
                    onElimina={onElimina}
                />
            ))}
        </Stack>
    );
}