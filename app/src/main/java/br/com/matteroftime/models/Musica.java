package br.com.matteroftime.models;

import java.io.Serializable;
import java.util.List;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class Musica extends RealmObject implements Serializable {

    @PrimaryKey
    private long id;
    private String nome;
    private List<Compasso> compassos;

    public Musica() {
    }

    public Musica(long id, String nome, List<Compasso> compassos) {
        this.id = id;
        this.nome = nome;
        this.compassos = compassos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Compasso> getCompassos() {
        return compassos;
    }

    public void setCompassos(List<Compasso> compassos) {
        this.compassos = compassos;
    }

    public void defineIntervalo(final List<Compasso> compassos) {
        for (Compasso compasso : compassos) {
            switch (compasso.getNota()){
                //semi-breve
                case 1:
                    compasso.setIntervalo((1.0 / compasso.getBpm() * 4 ) * 60000.0);
                    break;
                //mínima
                case 2:
                    compasso.setIntervalo((1.0 / compasso.getBpm() * 2 ) * 60000.0);
                    break;
                //semínima
                case 4:
                    compasso.setIntervalo(1.0 / compasso.getBpm() * 60000.0);
                    break;
                //colcheia
                case 8:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 2 ) * 60000.0);
                    break;
                //semicolcheia
                case 16:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 4 ) * 60000.0);
                    break;
                //fusa
                case 32:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 8 ) * 60000.0);
                    break;
                //semifusa
                case 64:
                    compasso.setIntervalo((1.0 / compasso.getBpm() / 16 ) * 60000.0);
                    break;
            }
        }
        //return compassos;
    }
}
