package TELAS;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CONEXAO.Conexao;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class TelaCadastro extends JFrame {

    private JPanel contentPane;
    private JTextField tfNovoUsuario;
    private JPasswordField pfNovaSenha;
    private JPasswordField pfConfirmarSenha;
    private TelaPrincipal telaPrincipal;

    /**
     * Create the frame.
     */
    public TelaCadastro(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNovoUsuario = new JLabel("Novo Usuário");
        lblNovoUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        lblNovoUsuario.setBounds(10, 51, 129, 23);
        contentPane.add(lblNovoUsuario);

        tfNovoUsuario = new JTextField();
        tfNovoUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        tfNovoUsuario.setBounds(149, 51, 142, 24);
        contentPane.add(tfNovoUsuario);
        tfNovoUsuario.setColumns(10);

        JLabel lblNovaSenha = new JLabel("Nova Senha");
        lblNovaSenha.setBounds(10, 94, 129, 14);
        contentPane.add(lblNovaSenha);

        pfNovaSenha = new JPasswordField();
        pfNovaSenha.setBounds(149, 91, 142, 24);
        contentPane.add(pfNovaSenha);

        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha");
        lblConfirmarSenha.setBounds(10, 129, 129, 14);
        contentPane.add(lblConfirmarSenha);

        pfConfirmarSenha = new JPasswordField();
        pfConfirmarSenha.setBounds(149, 126, 142, 24);
        contentPane.add(pfConfirmarSenha);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarNovoUsuario();
            }
        });
        btnCadastrar.setBounds(149, 167, 142, 23);
        contentPane.add(btnCadastrar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltarParaTelaPrincipal();
            }
        });
        btnVoltar.setBounds(149, 208, 142, 23);
        contentPane.add(btnVoltar);
    }

    private void cadastrarNovoUsuario() {
        String novoUsuario = tfNovoUsuario.getText();
        String novaSenha = new String(pfNovaSenha.getPassword());
        String confirmarSenha = new String(pfConfirmarSenha.getPassword());

        if (novaSenha.equals(confirmarSenha)) {
            try {
                Connection con = Conexao.conectar();
                if (con != null) {
                    String sql = "INSERT INTO dados_senhas (usuario, senha) VALUES (?, ?)";
                    PreparedStatement cadastrar = con.prepareStatement(sql);

                    cadastrar.setString(1, novoUsuario);
                    cadastrar.setString(2, novaSenha);

                    int linhasAfetadas = cadastrar.executeUpdate();

                    if (linhasAfetadas > 0) {
                        JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                        limparCampos();
                        telaPrincipal.setVisible(true);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Falha ao cadastrar usuário!");
                    }

                    cadastrar.close();
                    con.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "As senhas não coincidem. Tente novamente.");
            limparCampos();
        }
    }

    private void limparCampos() {         
        tfNovoUsuario.setText("");
        pfNovaSenha.setText("");
        pfConfirmarSenha.setText("");
    }

    private void voltarParaTelaPrincipal() {
        telaPrincipal.setVisible(true);
        setVisible(false);
    }
}
