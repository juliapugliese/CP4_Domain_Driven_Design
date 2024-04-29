package org.example.repositories;

import org.example.entities.Card;
import org.example.entities._BaseEntity;
import org.example.infraestructure.OracleDbConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CardRepository implements _BaseRepository<Card>, _Logger<String>{
    public static final String TB_NAME = "CARTAS";

    public boolean exists(String cardName) {
        try(var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + CardRepository.TB_NAME + " WHERE NOME = ?")){
            stmt.setString(1, cardName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e) {
            logError(e);
        }
        return false;
    }

    @Override
    public void create(Card carta) {
        try (var conn = new OracleDbConfiguration().getConnection()){
             String sql = carta.getColecao() != null ?
                     "INSERT INTO " + CardRepository.TB_NAME + " (NOME, TIPO, DESCRICAO, PODER, RESISTENCIA, PRECO, COD_COLECAO) VALUES (?,?,?,?,?,?,?)" :
                     "INSERT INTO " + CardRepository.TB_NAME + " (NOME, TIPO, DESCRICAO, PODER, RESISTENCIA, PRECO) VALUES (?,?,?,?,?,?)";
             PreparedStatement stmt = conn.prepareStatement(sql);
             stmt.setString(1, carta.getNome());
             stmt.setString(2, carta.getTipo());
             stmt.setString(3, carta.getDescricao());
             stmt.setInt(4, carta.getPoder());
             stmt.setInt(5, carta.getResistencia());
             stmt.setDouble(6, carta.getPreco());
            if (carta.getColecao() != null) {
                stmt.setInt(7, carta.getColecao().getId());
            }
            stmt.executeUpdate();
            logInfo("Carta adicionada com sucesso");
            conn.close();
        } catch (SQLException e) {
            logError(e);
        }
    }

    @Override

    public List<Card> getAll(){
        var cartas = new ArrayList<Card>();
        try{
            var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM " + CardRepository.TB_NAME +" ORDER BY COD_CARTAS");
            var rs = stmt.executeQuery();
            while(rs.next()){
                CollectionRepository collectionRepository = new CollectionRepository();
                int codColecao = rs.getInt("COD_COLECAO");
                if (rs.wasNull()) {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            null
                    ));
                } else {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            collectionRepository.get(codColecao).get()
                    ));
                }
            }
            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }
        logInfo("Lendo cartas: " + cartas);
        return cartas;
    }

    public List<Card> getAllFiltro(String orderBy, String direction, int limit, int offset) {
        var cartas = new ArrayList<Card>();
        try(var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement(
                    "SELECT * FROM "+ CardRepository.TB_NAME + " ORDER BY " + orderBy + " " +
                            (direction == null || direction.isEmpty() ? "ASC" : direction)
                            + " OFFSET "+offset+" ROWS FETCH NEXT "+ (limit == 0 ? 10 : limit) +" ROWS ONLY");){
            var rs = stmt.executeQuery();
            while (rs.next()){
                CollectionRepository collectionRepository = new CollectionRepository();
                int codColecao = rs.getInt("COD_COLECAO");
                if (rs.wasNull()) {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            null
                    ));
                } else {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            collectionRepository.get(codColecao).get()
                    ));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cartas;
    }

    public List<Card> getAllByCollection(int idCollection){
        var cartas = new ArrayList<Card>();
        try{var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM " + CardRepository.TB_NAME + " WHERE COD_COLECAO = ?");
            stmt.setInt(1, idCollection);
            var rs = stmt.executeQuery();
            while (rs.next()){
                CollectionRepository collectionRepository = new CollectionRepository();
                int codColecao = rs.getInt("COD_COLECAO");
                if (rs.wasNull()) {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            null
                    ));
                } else {
                    cartas.add(new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            collectionRepository.get(codColecao).get()
                    ));
                }
            }
            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }
        cartas.sort(Comparator.comparingInt(_BaseEntity::getId));
        logInfo("Lendo cartas: " + cartas);
        return cartas;
    }

    @Override
    public Optional<Card> get(int id){
        try(var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM " + CardRepository.TB_NAME + " WHERE COD_CARTAS = ?")){
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if(rs.next()){
                CollectionRepository collectionRepository = new CollectionRepository();
                int codColecao = rs.getInt("COD_COLECAO");
                if (rs.wasNull()) {
                    var carta = new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            null
                    );
                    logInfo("Lendo carta" + carta);
                    return Optional.of(carta);
                } else {
                    var carta = new Card(
                            rs.getInt("COD_CARTAS"),
                            rs.getString("NOME"),
                            rs.getString("TIPO"),
                            rs.getString("DESCRICAO"),
                            rs.getInt("PODER"),
                            rs.getInt("RESISTENCIA"),
                            rs.getDouble("PRECO"),
                            collectionRepository.get(codColecao).get()
                    );
                    logInfo("Lendo carta: " + carta);
                    return Optional.of(carta);
                }
            }
            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }

        return Optional.empty();
    }

    @Override
    public void update(int id, Card carta){
        try(var conn = new OracleDbConfiguration().getConnection()) {
            String sql = carta.getColecao() != null ?
                    "UPDATE " + CardRepository.TB_NAME + " SET NOME = ?, TIPO = ?, DESCRICAO = ?, PODER = ?, RESISTENCIA = ?, PRECO = ?, COD_COLECAO = ? WHERE COD_CARTAS = ?" :
                    "UPDATE " + CardRepository.TB_NAME + " SET NOME = ?, TIPO = ?, DESCRICAO = ?, PODER = ?, RESISTENCIA = ?, PRECO = ? WHERE COD_CARTAS = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, carta.getNome());
            stmt.setString(2, carta.getTipo());
            stmt.setString(3, carta.getDescricao());
            stmt.setInt(4, carta.getPoder());
            stmt.setInt(5, carta.getResistencia());
            stmt.setDouble(6, carta.getPreco());
            if (carta.getColecao() != null) {
                stmt.setInt(7, carta.getColecao().getId());
            }
            stmt.setInt(carta.getColecao() != null ? 8 : 7, id);
            stmt.executeUpdate();
            logInfo("Carta atualizada com sucesso");
        }
        catch (SQLException e) {
            logError(e);
        }
    }

    @Override
    public void delete(int id) {

        try{var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("DELETE FROM " + CardRepository.TB_NAME + " WHERE COD_CARTAS = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logWarn("Carta deletada com sucesso");
            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }
    }

}
