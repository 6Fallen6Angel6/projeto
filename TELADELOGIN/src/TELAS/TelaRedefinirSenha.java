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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class TelaRedefinirSenha extends JFrame {

    private JPanel contentPane;
    private JTextField tfUsuario;
    private JPasswordField pfSenhaAtual;
    private JPasswordField pfNovaSenha;
    private JPasswordField pfConfirmarSenha;
    private TelaPrincipal telaPrincipal;

    /**
     * Create the frame.
     */
    public TelaRedefinirSenha(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário");
        lblUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        lblUsuario.setBounds(10, 51, 129, 23);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        tfUsuario.setBounds(149, 51, 142, 24);
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        JLabel lblSenhaAtual = new JLabel("Senha Atual");
        lblSenhaAtual.setBounds(10, 94, 129, 14);
        contentPane.add(lblSenhaAtual);

        pfSenhaAtual = new JPasswordField();
        pfSenhaAtual.setBounds(149, 91, 142, 24);
        contentPane.add(pfSenhaAtual);

        JLabel lblNovaSenha = new JLabel("Nova Senha");
        lblNovaSenha.setBounds(10, 129, 129, 14);
        contentPane.add(lblNovaSenha);

        pfNovaSenha = new JPasswordField();
        pfNovaSenha.setBounds(149, 126, 142, 24);
        contentPane.add(pfNovaSenha);

        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha");
        lblConfirmarSenha.setBounds(10, 167, 129, 14);
        contentPane.add(lblConfirmarSenha);

        pfConfirmarSenha = new JPasswordField();
        pfConfirmarSenha.setBounds(149, 164, 142, 24);
        contentPane.add(pfConfirmarSenha);

        JButton btnRedefinirSenha = new JButton("Redefinir Senha");
        btnRedefinirSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redefinirSenha();
            }
        });
        btnRedefinirSenha.setBounds(149, 208, 142, 23);
        contentPane.add(btnRedefinirSenha);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltarParaTelaPrincipal();
            }
        });
        btnVoltar.setBounds(10, 208, 129, 23);
        contentPane.add(btnVoltar);
    }

      private void redefinirSenha() {
        try {
            Connection con = Conexao.conectar();
            if (con != null) {
                // Verificar a senha atual
                String sql = "SELECT senha FROM dados_senhas WHERE usuario = ?";
                PreparedStatement verificarSenha = con.prepareStatement(sql);
                verificarSenha.setString(1, tfUsuario.getText());

                // Execute a consulta
                ResultSet rs = verificarSenha.executeQuery();

                if (rs.next()) {
                    String senhaAtualArmazenada = rs.getString("senha");
                    String senhaAtualDigitada = new String(pfSenhaAtual.getPassword());

                    if (senhaAtualArmazenada.equals(senhaAtualDigitada)) {
                        // Senha atual está correta, verificar se as novas senhas coincidem
                        String novaSenha = new String(pfNovaSenha.getPassword());
                        String confirmarSenha = new String(pfConfirmarSenha.getPassword());

                        if (novaSenha.equals(confirmarSenha)) {
                            // Atualizar a senha no banco de dados
                            String updateSenha = "UPDATE dados_senhas SET senha = ? WHERE usuario = ?";
                            PreparedStatement atualizarSenha = con.prepareStatement(updateSenha);
                            atualizarSenha.setString(1, novaSenha);
                            atualizarSenha.setString(2, tfUsuario.getText());

                            // Execute a atualização
                            int resultado = atualizarSenha.executeUpdate();

                            if (resultado > 0) {
                                JOptionPane.showMessageDialog(null, "Senha redefinida com sucesso!");
                                limparCampos();
                                voltarParaTelaPrincipal();
                            } else {
                                JOptionPane.showMessageDialog(null, "Falha ao redefinir a senha!");
                            }

                            atualizarSenha.close();
                        } else {
                            JOptionPane.showMessageDialog(null, "As novas senhas não coincidem!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Senha atual incorreta!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não encontrado!");
                }

                verificarSenha.close();
                con.close();
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        tfUsuario.setText("");
        pfSenhaAtual.setText("");
        pfNovaSenha.setText("");
        pfConfirmarSenha.setText("");
    }
    private void voltarParaTelaPrincipal() {
        telaPrincipal.setVisible(true);
        setVisible(false);
    }
}
