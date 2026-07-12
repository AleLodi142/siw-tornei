package siw_tornei.dto;

public class ErroreDto {

    private String messaggio;

    public ErroreDto() {
    }

    public ErroreDto(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}