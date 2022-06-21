package model;

public class AgendaMedicos {

   private  String FotoPaciente;
   private  String Nome;
   private  String Local;
   private String Data;
   private  String Hora;

    public AgendaMedicos() {
    }

    public AgendaMedicos(String fotoPaciente, String nome, String local, String data, String hora) {
        FotoPaciente = fotoPaciente;
        Nome = nome;
        Local = local;
        Data = data;
        Hora = hora;
    }

    public String getFotoPaciente() {
        return FotoPaciente;
    }

    public void setFotoPaciente(String fotoPaciente) {
        FotoPaciente = fotoPaciente;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }
}
