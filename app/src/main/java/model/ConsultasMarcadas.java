package model;

public class ConsultasMarcadas {

    private String Nome;
    private String Data;
    private String Especialidade;
    private String Hora;
    private String Local;
    private String Medico;
    private String Foto;

    public ConsultasMarcadas() {
    }

    public ConsultasMarcadas(String nome, String data, String especialidade, String hora, String local, String medico, String foto) {
        Nome = nome;
        Data = data;
        Especialidade = especialidade;
        Hora = hora;
        Local = local;
        Medico = medico;
        Foto = foto;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getEspecialidade() {
        return Especialidade;
    }

    public void setEspecialidade(String especialidade) {
        Especialidade = especialidade;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getMedico() {
        return Medico;
    }

    public void setMedico(String medico) {
        Medico = medico;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
