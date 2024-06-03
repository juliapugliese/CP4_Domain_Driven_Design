package org.example.entities;


import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Card extends _BaseEntity {
    private String nome;
    private String tipo;
    private String descricao;
    private int poder;
    private int resistencia;
    private double preco;
    private transient Collection colecao;

    public Card() {
    }

    public Card(int id, String nome, String tipo, String descricao, int poder, int resistencia, double preco, Collection colecao) {
        super(id);
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.poder = poder;
        this.resistencia = resistencia;
        this.preco = preco;
        this.colecao = colecao;
    }

    public Card(int id, String nome, String tipo, String descricao, int poder, int resistencia, double preco) {
        super(id);
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.poder = poder;
        this.resistencia = resistencia;
        this.preco = preco;
    }

    public Card(String nome, String tipo, String descricao, int poder, int resistencia, double preco, Collection colecao) {
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.poder = poder;
        this.resistencia = resistencia;
        this.preco = preco;
        this.colecao = colecao;
    }

    public Card(String nome, String tipo, String descricao, int poder, int resistencia, double preco) {
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.poder = poder;
        this.resistencia = resistencia;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Collection getColecao() {
        return colecao;
    }

    public void setColecao(Collection colecao) {
        this.colecao = colecao;
    }

    @Override
    public String toString() {

        if (colecao != null) {
            return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                    .add("id=" + getId())
                    .add("nome='" + nome + "'")
                    .add("tipo='" + tipo + "'")
                    .add("descricao='" + descricao + "'")
                    .add("poder=" + poder)
                    .add("resistencia=" + resistencia)
                    .add("preco=" + preco)
                    .add("colecao=" + colecao.getId())
                    .toString();
        }
        else {
            return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                    .add("id=" + getId())
                    .add("nome='" + nome + "'")
                    .add("tipo='" + tipo + "'")
                    .add("descricao='" + descricao + "'")
                    .add("poder=" + poder)
                    .add("resistencia=" + resistencia)
                    .add("preco=" + preco)
                    .toString();
        }
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (nome == null || nome.isBlank())
            errors.add("Nome da carta não pode ser vazio");
        if (tipo == null || tipo.isBlank())
            errors.add("A carta tem que ter um tipo");
        if (descricao == null || descricao.isBlank())
            errors.add("A carta tem que ter descrição");
        if (poder < 0)
            errors.add("A carta não pode ter poder negativo");
        if (resistencia < 0)
            errors.add("A carta não pode ter resistencia negativa");
        if (preco < 0)
            errors.add("Preço da carta não pode ser menor que zero");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

}
