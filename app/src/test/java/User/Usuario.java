package User;

public class Usuario {

    private final String id;
    private final String dtsnc;
    private final String cpf;

    public Usuario(String id, String dtsnc, String cpf) {
        this.id = id;
        this.dtsnc = dtsnc;
        this.cpf = cpf;
    }

    public String getId() {
        return id;
    }

    public String getDtsnc() {
        return dtsnc;
    }

    public String getCpf() {
        return cpf;
    }
}
