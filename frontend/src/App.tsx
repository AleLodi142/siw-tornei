import CommentiPartita
    from "./components/CommentiPartita";

interface Props {
    partitaId: number;
}

function App({ partitaId }: Props) {
    return (
        <div
            style={{
                maxWidth: 700,
                margin: "30px auto"
            }}
        >
            <CommentiPartita
                partitaId={partitaId}
            />
        </div>
    );
}

export default App;