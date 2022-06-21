package model;

public class Remedios {

    private String Nome;
    private String Descricao;
    private String Quantidade;
    private String Imagem;

    public Remedios() {
    }

    public Remedios(String nome, String descricao, String quantidade, String imagem) {
        Nome = nome;
        Descricao = descricao;
        Quantidade = quantidade;
        Imagem = imagem;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(String quantidade) {
        Quantidade = quantidade;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }
}
