package CONEXAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    
    private static String caminho = "jdbc:mysql://localhost:3306/db_senhas";
    private static String usuario = "root";
    private static String senha = "";

    private static Connection conectar;

    // Construtor privado para evitar instâncias múltiplas
    private Conexao() {}

    public static Connection conectar() {
        try {
            if (conectar == null || conectar.isClosed()) {
                conectar = DriverManager.getConnection(caminho, usuario, senha);
                System.out.println("Banco de dados conectado com sucesso!");
            }
        } catch (SQLException erro_conectar_banco) {
            System.out.println("Erro ao conectar no banco de dados!\n" + erro_conectar_banco.getMessage());
        }
        return conectar;
    }

    public static void finalizarConexao() {
        try {
            if (conectar != null && !conectar.isClosed()) {
                conectar.close();
                System.out.println("Conexão fechada com sucesso!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Script para criar o banco de dados e a tabela
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", usuario, senha);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS db_senhas");

            statement.executeUpdate("USE db_senhas");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS dados_senhas ("
                                    + "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,"
                                    + "usuario VARCHAR(45) NOT NULL,"
                                    + "senha VARCHAR(45) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS cardapio ("
                                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                                    +"nome VARCHAR(50) NOT NULL,"
                                    +"preco DECIMAL (8,2) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pedidos ("
                                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                                    + "id_pizza INT,"
                                    + "forma_pagamento VARCHAR(255) NOT NULL,"
                                    + "tempo_espera INT,"
                                    + "quantidade INT,"
                                    + "forma_entrega VARCHAR(255) NOT NULL,"
                                    + "bairro VARCHAR(50) NOT NULL,"
                                    + "cidade VARCHAR(50) NOT NULL,"
                                    + "numero_casa VARCHAR(10) NOT NULL,"
                                    + "complemento VARCHAR(100),"
                                    + "subtotal DECIMAL(8,2) NOT NULL,"
                                    + "frete DECIMAL(8,2) NOT NULL,"
                                    + "total DECIMAL(8,2) NOT NULL,"
                                    + "forma_pagamento_entrega VARCHAR(255) NOT NULL DEFAULT 'Dinheiro',"
                                    + "FOREIGN KEY (id_pizza) REFERENCES cardapio(id))");
            
    
            statement.executeUpdate("INSERT INTO cardapio (nome, preco) VALUES ('Pizza Margherita', 25.99)");
            statement.executeUpdate("INSERT INTO cardapio (nome, preco) VALUES ('Pizza Pepperoni', 29.99)");
            statement.executeUpdate("INSERT INTO cardapio (nome, preco) VALUES ('Pizza Quatro Queijos', 27.99)");

            System.out.println("Banco de dados e tabela criados com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar banco de dados e tabela:\n" + e.getMessage());
        }
    }
}
