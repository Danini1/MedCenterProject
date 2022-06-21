package model;

public class ConsultasAntigas {

    private String Data;
    private String Descricao;
    private String Horario;
    private String Local;
    private String Doutor;

    public ConsultasAntigas() {
    }

    public ConsultasAntigas(String data, String descricao, String horario, String local, String doutor) {
        Data = data;
        Descricao = descricao;
        Horario = horario;
        Local = local;
        Doutor = doutor;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getDoutor() {
        return Doutor;
    }

    public void setDoutor(String doutor) {
        Doutor = doutor;
    }
}
