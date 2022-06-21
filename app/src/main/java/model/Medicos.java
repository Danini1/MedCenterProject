package model;

public class Medicos {
    private String Nome;
    private String Uid;
    private String Email;
    private String Senha;
    private String Tipo;
    private String Especialidade;
    private String Local;
    private String Imagem;

    public Medicos() {
    }

    public Medicos(String nome, String uid, String email, String senha, String tipo, String especialidade, String tocando, String local, String imagem) {
        Nome = nome;
        Uid = uid;
        Email = email;
        Senha = senha;
        Tipo = tipo;
        Especialidade = especialidade;
        Local = local;
        Imagem = imagem;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getEspecialidade() {
        return Especialidade;
    }

    public void setEspecialidade(String especialidade) {
        Especialidade = especialidade;
    }



    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }
}
