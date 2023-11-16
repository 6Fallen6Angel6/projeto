package TELAS;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CONEXAO.Conexao;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaCardapio extends JFrame {

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaCardapio frame = new TelaCardapio();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaCardapio() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1));

        // Carrega o cardápio do banco de dados
        carregarCardapio();
    }

    private void carregarCardapio() {
        try {
            Connection con = Conexao.conectar();
            if (con != null) {
                String sql = "SELECT nome, preco FROM cardapio";
                PreparedStatement statement = con.prepareStatement(sql);

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");

                    JButton btnPizza = new JButton(nome + " - R$" + preco);
                    contentPane.add(btnPizza);

                    btnPizza.addActionListener(e -> fazerPedido(nome, preco));
                }

                statement.close();
                con.close();
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fazerPedido(String nomePizza, double precoPizza) {
        // Configura a pizza selecionada
        PizzaSelecionada.setNomePizza(nomePizza);
        PizzaSelecionada.setPrecoPizza(precoPizza);
        PizzaSelecionada.setQuantidade(1); // Quantidade fixa

        // Defina o ID da pizza selecionada (substitua 1 pelo valor correto)
        PizzaSelecionada.setIdPizza(1);

        // Abre a tela de pedido
        TelaPedido telaPedido = new TelaPedido();
        telaPedido.setVisible(true);
        this.setVisible(false);
    }
}