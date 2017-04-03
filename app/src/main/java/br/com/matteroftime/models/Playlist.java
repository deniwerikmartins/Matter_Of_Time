package br.com.matteroftime.models;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class Playlist extends RealmObject {

    @PrimaryKey
    private long id;
    private String nome;
    @Ignore
    private List<Musica> musicas;

    public Playlist() {
    }

    public Playlist(long id, String nome, List<Musica> musicas) {
        this.id = id;
        this.nome = nome;
        this.musicas = musicas;
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

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }
}
