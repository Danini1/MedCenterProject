package model;

import android.widget.Button;

public class Vacinas {

    private String Nome;
    private String Uid;
    private String Descricao;
    private String Indicacao;
    private String Imagem;
    private String Video;

    public Vacinas() {
    }

    public Vacinas(String nome, String uid, String descricao, String indicacao, String imagem, String video) {
        Nome = nome;
        Uid = uid;
        Descricao = descricao;
        Indicacao = indicacao;
        Imagem = imagem;
        Video = video;
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

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getIndicacao() {
        return Indicacao;
    }

    public void setIndicacao(String indicacao) {
        Indicacao = indicacao;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }
}

