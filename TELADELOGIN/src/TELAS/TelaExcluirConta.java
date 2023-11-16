package TELAS;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import CONEXAO.Conexao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaExcluirConta extends JFrame {

    private JPanel contentPane;
    private JTextField tfUsuario;
    private TelaPrincipal telaPrincipal;

    public TelaExcluirConta(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(10, 51, 78, 23);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setBounds(87, 51, 150, 24);
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        JButton btnExcluirConta = new JButton("Excluir Conta");
        btnExcluirConta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirConta();
            }
        });
        btnExcluirConta.setBounds(87, 167, 150, 23);
        contentPane.add(btnExcluirConta);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltarTelaAnterior();
            }
        });
        btnVoltar.setBounds(87, 200, 150, 23);
        contentPane.add(btnVoltar);
    }

    private void excluirConta() {
        try {
            Connection con = Conexao.conectar();
            if (con != null) {
                String sql = "DELETE FROM dados_senhas WHERE usuario=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, tfUsuario.getText());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Conta excluída com sucesso!", "Informação", JOptionPane.DEFAULT_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
                con.close();
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void voltarTelaAnterior() {
        this.setVisible(false);
        telaPrincipal.setVisible(true);
    }
}
