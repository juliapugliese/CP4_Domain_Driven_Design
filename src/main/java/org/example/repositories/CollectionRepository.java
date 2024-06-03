package org.example.repositories;
import org.example.entities.Card;
import org.example.entities.Collection;
import org.example.infraestructure.OracleDbConfiguration;

import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionRepository implements _BaseRepository<Collection>, _Logger<String> {
    public static final String TB_NAME = "COLECAO";
    @Override
    public void create(Collection colecao) {
        try(var conn = new OracleDbConfiguration().getConnection();
            var stmt = conn.prepareStatement("INSERT INTO " + CollectionRepository.TB_NAME + " (NOME, DATA_LANCAMENTO, PRECO, QUANTIDADE) VALUES (?,?,?,?)")){
            stmt.setString(1, colecao.getNome());
            stmt.setDate(2, Date.valueOf(colecao.getDataLancamento()));
            stmt.setDouble(3, colecao.getPreco());
            stmt.setLong(4, colecao.getQuantidade());
            stmt.executeUpdate();

        colecao.getCartas().forEach(carta -> {
            try (var stmtCarta = conn.prepareStatement("INSERT INTO " + CardRepository.TB_NAME + " (NOME, TIPO, DESCRICAO, PODER, RESISTENCIA, PRECO, COD_COLECAO) VALUES (?,?,?,?,?,?,?)")){
                stmtCarta.setString(1, carta.getNome());
                stmtCarta.setString(2, carta.getTipo());
                stmtCarta.setString(3, carta.getDescricao());
                stmtCarta.setInt(4, carta.getPoder());
                stmtCarta.setInt(5, carta.getResistencia());
                stmtCarta.setDouble(6, carta.getPreco());
                stmtCarta.setInt(7, new CardRepository().getIdColecao(colecao).get(0));
                stmtCarta.executeUpdate();
                logInfo("Carta atualizada com sucesso");
            }catch (SQLException e) {
                logError(e);
            }


        });

            logInfo("Coleção adicionada com sucesso");
            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }
    }

    public List<Collection> getAll(String orderBy, String direction, int limit, int offset) {
        var colecoes = new ArrayList<Collection>();
        try(var conn = new OracleDbConfiguration().getConnection()){
            var stmt = conn.prepareStatement(
                    "SELECT * FROM "+ TB_NAME + " ORDER BY " + orderBy + " " +
                            (direction == null || direction.isEmpty() ? "ASC" : direction)
                            + " OFFSET "+offset+" ROWS FETCH NEXT "+ (limit == 0 ? 10 : limit) +" ROWS ONLY");{
            var rs = stmt.executeQuery();
            while (rs.next()){
                int codColecao = rs.getInt("COD_COLECAO");

                var listaCartas = new ArrayList<Card>();
                try (var stmtCartas = conn.prepareStatement("SELECT * FROM " + CardRepository.TB_NAME + " WHERE COD_COLECAO = ? ORDER BY COD_CARTAS")) {
                    stmtCartas.setInt(1, codColecao);
                    var rsCartas = stmtCartas.executeQuery();
                    while (rsCartas.next()) {
                        listaCartas.add(new Card(
                                rsCartas.getInt("COD_CARTAS"),
                                rsCartas.getString("NOME"),
                                rsCartas.getString("TIPO"),
                                rsCartas.getString("DESCRICAO"),
                                rsCartas.getInt("PODER"),
                                rsCartas.getInt("RESISTENCIA"),
                                rsCartas.getDouble("PRECO")
                        ));
                    }
                }
                colecoes.add(new Collection(
                        codColecao,
                        rs.getString("NOME"),
                        rs.getDate("DATA_LANCAMENTO").toLocalDate(),
                        listaCartas
                ));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return colecoes;
    }

    @Override
    public Optional<Collection> get(int id){
        try(var conn = new OracleDbConfiguration().getConnection()) {
            var stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME + " WHERE COD_COLECAO = ?");
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                int codColecao = rs.getInt("COD_COLECAO");
                var listaCartas = new ArrayList<Card>();
                try (var stmtCartas = conn.prepareStatement("SELECT * FROM " + CardRepository.TB_NAME + " WHERE COD_COLECAO = ? ORDER BY COD_CARTAS")) {
                    stmtCartas.setInt(1, codColecao);
                    var rsCartas = stmtCartas.executeQuery();
                    while (rsCartas.next()) {
                        listaCartas.add(new Card(
                                rsCartas.getInt("COD_CARTAS"),
                                rsCartas.getString("NOME"),
                                rsCartas.getString("TIPO"),
                                rsCartas.getString("DESCRICAO"),
                                rsCartas.getInt("PODER"),
                                rsCartas.getInt("RESISTENCIA"),
                                rsCartas.getDouble("PRECO")
                        ));
                    }
                }
                var colecao = new Collection(
                        codColecao,
                        rs.getString("NOME"),
                        rs.getDate("DATA_LANCAMENTO").toLocalDate(),
                        listaCartas
                );
                logInfo("Lendo coleção: " + colecao);
                return Optional.of(colecao);
            }
        }
        catch (SQLException e) {
            logError(e);
        }
        return Optional.empty();
    }


    @Override
    public void update(int id, Collection colecao) {
        try(var conn = new OracleDbConfiguration().getConnection()){
            var stmt = conn.prepareStatement("UPDATE "+ TB_NAME +
                    " SET NOME = ?, DATA_LANCAMENTO = ?, PRECO = ?, QUANTIDADE = ? WHERE COD_COLECAO = ?");
            stmt.setString(1, colecao.getNome());
            stmt.setDate(2, Date.valueOf(colecao.getDataLancamento()));
            stmt.setDouble(3, colecao.getPreco());
            stmt.setLong(4, colecao.getQuantidade());
            stmt.setInt(5, id);
            stmt.executeUpdate();
            logWarn("Coleção atualizada com sucesso!");


            colecao.getCartas().forEach(carta -> {
                try (var stmtCarta = conn.prepareStatement("UPDATE " + CardRepository.TB_NAME + " SET NOME = ?, TIPO = ?, DESCRICAO = ?, PODER = ?, RESISTENCIA = ?, PRECO = ? WHERE COD_COLECAO = ?")){
                stmtCarta.setString(1, carta.getNome());
                stmtCarta.setString(2, carta.getTipo());
                stmtCarta.setString(3, carta.getDescricao());
                stmtCarta.setInt(4, carta.getPoder());
                stmtCarta.setInt(5, carta.getResistencia());
                stmtCarta.setDouble(6, carta.getPreco());
                stmtCarta.setInt(7, id);
                stmtCarta.executeUpdate();
                logInfo("Carta atualizada com sucesso");
            }catch (SQLException e) {
                logError(e);
            }

            });
            conn.close();
        }catch (SQLException e){
            logError(e);
        }
    }

    @Override
    public void delete(int id) {
        try (var conn = new OracleDbConfiguration().getConnection()) {
            try (var deleteStmt = conn.prepareStatement("DELETE FROM " + TB_NAME + " WHERE COD_COLECAO = ?")) {
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();
                logWarn("Coleção deletada com sucesso");
            } catch (SQLException e) {
                logError(e);
            }
        } catch (SQLException e) {
            logError(e);
        }
    }
}





