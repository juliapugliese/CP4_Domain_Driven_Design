package org.example.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Collection extends _BaseEntity {
    private String nome;
    private LocalDate dataLancamento;
    private double preco;
    private long quantidade;
    private List<Card> cartas = new ArrayList<>();
    private double contador = 0;


    public Collection() {
    }

    public Collection(int id, String nome, LocalDate dataLancamento, List<Card> cartas) {
        super(id);
        this.nome = nome;
        this.dataLancamento = dataLancamento;
        this.quantidade = cartas.size();
        this.cartas = cartas;
        cartas.forEach(card -> contador += card.getPreco());
        this.preco = contador;
        cartas.forEach(card -> card.setColecao(this));
    }

    public Collection(String nome, LocalDate dataLancamento, List<Card> cartas) {
        this.nome = nome;
        this.dataLancamento = dataLancamento;
        this.quantidade = cartas.size();
        this.cartas = cartas;
        cartas.forEach(card -> contador += card.getPreco());
        this.preco = contador;
        cartas.forEach(card -> card.setColecao(this));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public List<Card> getCartas() {
        return cartas;
    }

    public void setCartas(List<Card> cartas) {
        this.cartas = cartas;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Collection.class.getSimpleName() + "[", "]")
                .add("nome='" + nome + "'")
                .add("id=" + getId())
                .add("dataLancamento=" + dataLancamento)
                .add("preco=" + preco)
                .add("quantidade=" + quantidade)
                .add("cartas=" + cartas)
                .toString();
    }


    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (nome == null || nome.isBlank())
            errors.add("Nome da coleção não pode ser vazio");
        if (dataLancamento == null)
            errors.add("A coleção tem que ter uma data de lançamento");

        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
}
