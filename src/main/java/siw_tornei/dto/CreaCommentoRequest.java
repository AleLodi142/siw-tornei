package siw_tornei.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreaCommentoRequest {

    @NotBlank(message = "Il testo del commento è obbligatorio")
    @Size(
        max = 1000,
        message = "Il commento non può superare 1000 caratteri"
    )
    private String testo;

    public CreaCommentoRequest() {
    }

    public CreaCommentoRequest(String testo) {
        this.testo = testo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}