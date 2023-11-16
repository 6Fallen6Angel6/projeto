package TELAS;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import CONEXAO.Conexao;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.SwingConstants;
import java.awt.Color;

public class TelaPrincipal extends JFrame {

    private JPanel contentPane;
    private JTextField tfUsuario;
    private JPasswordField pfSenha;
    private JButton btnLogin;
    private JButton btnCadastrar;
    private JButton btnRedefinirSenha;
    private JButton btnExcluirConta;
    private TelaCadastro telaCadastro;
    private TelaRedefinirSenha telaRedefinirSenha;
    private TelaExcluirConta telaExcluirConta;
    private TelaCardapio telaCardapio;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaPrincipal frame = new TelaPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TelaPrincipal() {
    	setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setForeground(new Color(128, 128, 128));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel Usuario = new JLabel("Usuário");
        Usuario.setHorizontalAlignment(SwingConstants.CENTER);
        Usuario.setBounds(83, 57, 78, 23);
        Usuario.setFont(new Font("Dialog", Usuario.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 16));
        contentPane.add(Usuario);

        tfUsuario = new JTextField();
        tfUsuario.setBounds(166, 56, 101, 24);
        tfUsuario.setFont(new Font("Dialog", Font.BOLD, 16));
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        JLabel Senha = new JLabel("Senha");
        Senha.setHorizontalAlignment(SwingConstants.CENTER);
        Senha.setFont(new Font("Dialog", Font.BOLD, 16));
        Senha.setBounds(83, 114, 78, 24);
        contentPane.add(Senha);

        pfSenha = new JPasswordField();
        pfSenha.setFont(new Font("Tahoma", Font.BOLD, 16));
        pfSenha.setBounds(166, 114, 101, 24);
        contentPane.add(pfSenha);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnLogin.setBounds(66, 172, 140, 31);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                realizarLogin();
            }
        });
        contentPane.add(btnLogin);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnCadastrar.setBounds(235, 172, 145, 31);
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exibirTelaCadastro();
            }
        });
        contentPane.add(btnCadastrar);

        btnRedefinirSenha = new JButton("Redefinir Senha");
        btnRedefinirSenha.setBounds(66, 216, 145, 23);
        btnRedefinirSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exibirTelaRedefinirSenha();
            }
        });
        contentPane.add(btnRedefinirSenha);

        btnExcluirConta = new JButton("Excluir Conta");
        btnExcluirConta.setBounds(235, 216, 145, 23);
        btnExcluirConta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exibirTelaExcluirConta();
            }
        });
        contentPane.add(btnExcluirConta);

        

        // Inicializa as telas
        telaCadastro = new TelaCadastro(this);
        telaRedefinirSenha = new TelaRedefinirSenha(this);
        telaExcluirConta = new TelaExcluirConta(this);
        telaCardapio = new TelaCardapio();

    }

    private void realizarLogin() {
        try {
            Connection con = Conexao.conectar();
            if (con != null) {
                String sql = "SELECT * from dados_senhas Where usuario=? and senha=?";
                PreparedStatement login = con.prepareStatement(sql);

                login.setString(1, tfUsuario.getText());
                login.setString(2, new String(pfSenha.getPassword()));

                ResultSet rs = login.executeQuery();

                if (rs.next()) {
                    JOptionPane.showConfirmDialog(null, "Logado com sucesso!", "Informação", JOptionPane.DEFAULT_OPTION);
                    exibirTelaCardapio();
                } else {
                    JOptionPane.showMessageDialog(null, "Senha incorreta!");
                }

                login.close();
                con.close();
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void exibirTelaCardapio() {
        telaCardapio.setVisible(true);
        this.setVisible(false);
    }

    private void exibirTelaCadastro() {
        telaCadastro.setVisible(true);
        this.setVisible(false);
    }

    private void exibirTelaRedefinirSenha() {
        telaRedefinirSenha.setVisible(true);
        this.setVisible(false);
    }

    private void exibirTelaExcluirConta() {
        telaExcluirConta.setVisible(true);
        this.setVisible(false);
    }
}
