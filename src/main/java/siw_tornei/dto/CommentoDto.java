package siw_tornei.dto;

public class CommentoDto {

    private Long id;
    private String testo;
    private String autoreUsername;
    private Long partitaId;
    private boolean proprietario;

    public CommentoDto() {
    }

    public CommentoDto(
            Long id,
            String testo,
            String autoreUsername,
            Long partitaId,
            boolean proprietario) {

        this.id = id;
        this.testo = testo;
        this.autoreUsername = autoreUsername;
        this.partitaId = partitaId;
        this.proprietario = proprietario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getAutoreUsername() {
        return autoreUsername;
    }

    public void setAutoreUsername(String autoreUsername) {
        this.autoreUsername = autoreUsername;
    }

    public Long getPartitaId() {
        return partitaId;
    }

    public void setPartitaId(Long partitaId) {
        this.partitaId = partitaId;
    }

    public boolean isProprietario() {
        return proprietario;
    }

    public void setProprietario(boolean proprietario) {
        this.proprietario = proprietario;
    }
}