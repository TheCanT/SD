import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class Musica {
    private String titulo;
    private String artista;
    private Integer ano;
    private Collection<String> etiquetas;
    private Collection<String> donos;

    public void setDonos(Collection<String> donos) {
        this.donos = new HashSet<>(donos);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Musica(String titulo, String artista, Integer ano, Collection<String> etiquetas, Collection<String> donos) {
        this.titulo = titulo;
        this.artista = artista;
        this.ano = ano;
        setEtiquetas(etiquetas);
        this.setDonos(donos);
    }

    public void setEtiquetas(Collection<String> etiquetas) {
        this.etiquetas = new HashSet<>(etiquetas);
    }

    public Collection<String> getDonos() {
        return new HashSet<>(donos);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public Integer getAno() {
        return ano;
    }

    public Collection<String> getEtiquetas() {
        return new HashSet<>(etiquetas);
    }

    public String getKey(){
        return this.getArtista()+"_"+this.getTitulo();
    }
}
