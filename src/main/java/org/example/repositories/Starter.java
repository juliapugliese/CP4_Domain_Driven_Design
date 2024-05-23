package org.example.repositories;


import org.example.infraestructure.OracleDbConfiguration;

import java.sql.SQLException;


public class Starter implements _Logger<String>{

    public void initialize() {
        try (var conn = new OracleDbConfiguration().getConnection()) {


            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + CardRepository.TB_NAME + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }

            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + CollectionRepository.TB_NAME + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }


            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE " + CardRepository.TB_NAME + " (COD_CARTAS NUMBER GENERATED AS IDENTITY CONSTRAINT CARDS_PK PRIMARY KEY, " +
                            "NOME VARCHAR2(80) NOT NULL, " +
                            "TIPO VARCHAR2(60) NOT NULL, " +
                            "DESCRICAO VARCHAR2(150) NOT NULL, " +
                            "PODER NUMBER, " +
                            "RESISTENCIA NUMBER, " +
                            "PRECO DECIMAL(9,2), "+
                            "COD_COLECAO NUMBER)")){
                stmt.executeUpdate();
                logInfo("Tabela " + CardRepository.TB_NAME + " criada com sucesso!");
            } catch (SQLException e) {
                logError(e);
            }

            try (var stmt = conn.prepareStatement(
                    "CREATE TABLE " + CollectionRepository.TB_NAME + " (COD_COLECAO NUMBER GENERATED AS IDENTITY CONSTRAINT COLLECTION_PK PRIMARY KEY, " +
                            "NOME VARCHAR2(80) NOT NULL, " +
                            "DATA_LANCAMENTO DATE NOT NULL, " +
                            "PRECO DECIMAL(9,2), " +
                            "QUANTIDADE NUMBER)")){
                stmt.executeUpdate();
                logInfo("Tabela " + CollectionRepository.TB_NAME + " criada com sucesso!");
            } catch (SQLException e) {
                logError(e);
            }

            try (var stmt = conn.prepareStatement("ALTER TABLE "+CardRepository.TB_NAME+" ADD CONSTRAINT CARD_COLLECTION_FK FOREIGN KEY(COD_COLECAO) REFERENCES "+CollectionRepository.TB_NAME+"(COD_COLECAO) ON DELETE SET NULL")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }



            conn.close();
        } catch (SQLException e) {
            logError(e);
        }
    }

}
