package Mapa;

public class MapModel {
    private String Nome;
    private String Endereco;
    private String Dia;
    private String Horario;
    private String Telefone;
    private double Latitude;
    private double Longitude;

    public MapModel() {
    }

    public MapModel(String nome, String endereco, String dia, String horario, String telefone, double latitude, double longitude) {
        Nome = nome;
        Endereco = endereco;
        Dia = dia;
        Horario = horario;
        Telefone = telefone;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
