package model;

public class VacinasUBS {

    private String Uid;
    private String Nome;
    private String quantindade;

    public VacinasUBS() {
    }

    public VacinasUBS(String uid, String nome, String quantindade) {
        Uid = uid;
        Nome = nome;
        this.quantindade = quantindade;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getQuantindade() {
        return quantindade;
    }

    public void setQuantindade(String quantindade) {
        this.quantindade = quantindade;
    }
}
