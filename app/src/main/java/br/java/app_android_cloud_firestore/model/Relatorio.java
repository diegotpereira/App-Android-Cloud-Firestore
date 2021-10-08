package br.java.app_android_cloud_firestore.model;

import java.util.HashMap;
import java.util.Map;

public class Relatorio {

    private String id;
    private String titulo;
    private String conteudo;

    public Relatorio() {
    }

    public Relatorio(String id, String titulo, String conteudo) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public Relatorio(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Map<String, Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("titulo", this.titulo);
        result.put("conteudo", this.conteudo);

        return result;
    }
}
