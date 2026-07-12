import React from "react";
import ReactDOM from "react-dom/client";

import App from "./App";
import "./index.css";

const contenitoreReact =
    document.getElementById("react-commenti")
    ?? document.getElementById("root");

if (!contenitoreReact) {
    throw new Error(
        "Contenitore React non trovato"
    );
}

const partitaIdTesto =
    contenitoreReact.dataset.partitaId ?? "1";

const partitaId =
    Number(partitaIdTesto);

if (Number.isNaN(partitaId)) {
    throw new Error(
        "Id della partita non valido"
    );
}

ReactDOM.createRoot(
    contenitoreReact
).render(
    <React.StrictMode>
        <App partitaId={partitaId} />
    </React.StrictMode>
);