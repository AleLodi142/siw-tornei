package siw_tornei.model;

public class Classifica {

    private Squadra squadra;

    private int punti;

    private int giocate;

    private int vinte;

    private int pareggiate;

    private int perse;

    private int golFatti;

    private int golSubiti;

    public Classifica() {

    }

    public int getDifferenzaReti() {
        return this.golFatti - this.golSubiti;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public int getGiocate() {
        return giocate;
    }

    public void setGiocate(int giocate) {
        this.giocate = giocate;
    }

    public int getVinte() {
        return vinte;
    }

    public void setVinte(int vinte) {
        this.vinte = vinte;
    }

    public int getPareggiate() {
        return pareggiate;
    }

    public void setPareggiate(int pareggiate) {
        this.pareggiate = pareggiate;
    }

    public int getPerse() {
        return perse;
    }

    public void setPerse(int perse) {
        this.perse = perse;
    }

    public int getGolFatti() {
        return golFatti;
    }

    public void setGolFatti(int golFatti) {
        this.golFatti = golFatti;
    }

    public int getGolSubiti() {
        return golSubiti;
    }

    public void setGolSubiti(int golSubiti) {
        this.golSubiti = golSubiti;
    }

}