package TELAS;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import CONEXAO.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class TelaPedido extends JFrame {

    private JPanel contentPane;
    private JTextField tfTempoEspera;
    private JTextField tfQuantidade;  // Adicionado campo de quantidade
    private JComboBox<String> cbFormaPagamento;
    private JTextField tfBairro;
    private JTextField tfCidade;
    private JTextField tfNumeroCasa;
    private JTextField tfComplemento;
    private JLabel lblSubtotal;
    private JLabel lblFrete;

    private double subtotal = 0;

    public TelaPedido() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
                contentPane.setLayout(null);
        
        
                JLabel lblQuantidade = new JLabel("Quantidade:");
                lblQuantidade.setBounds(5, 8, 237, 37);
                contentPane.add(lblQuantidade);
        
                tfQuantidade = new JTextField();
                tfQuantidade.setBounds(242, 8, 237, 37);
                contentPane.add(tfQuantidade);
                
                        // Adiciona um ouvinte de alteração ao campo de texto tfQuantidade
                        tfQuantidade.getDocument().addDocumentListener(new DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                calcularSubtotal();
                            }
                
                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                calcularSubtotal();
                            }
                
                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                calcularSubtotal();
                            }
                        });
        
                JLabel lblFormaPagamento = new JLabel("Forma de Pagamento:");
                lblFormaPagamento.setBounds(5, 45, 237, 37);
                contentPane.add(lblFormaPagamento);
        
                cbFormaPagamento = new JComboBox<>();
                cbFormaPagamento.setBounds(242, 45, 237, 37);
                cbFormaPagamento.addItem("Forma de pagamento");
                cbFormaPagamento.addItem("Dinheiro");
                cbFormaPagamento.addItem("Cartão");
                cbFormaPagamento.addItem("PIX");
                contentPane.add(cbFormaPagamento);
                
                        // Adiciona ouvintes para a mudança de seleção no JComboBox
                        cbFormaPagamento.addActionListener(e -> {
                            calcularSubtotal();
                            calcularFrete();
                        });

                JLabel lblSubtotalLabel = new JLabel("Subtotal:");
                lblSubtotalLabel.setBounds(5, 82, 237, 37);
                contentPane.add(lblSubtotalLabel);
        
                lblSubtotal = new JLabel("R$ 0.00");
                lblSubtotal.setBounds(242, 82, 237, 37);
                contentPane.add(lblSubtotal);
        
                JLabel lblFreteLabel = new JLabel("Frete:");
                lblFreteLabel.setBounds(5, 119, 237, 37);
                contentPane.add(lblFreteLabel);
        
                lblFrete = new JLabel("R$ 0.00");
                lblFrete.setBounds(242, 119, 237, 37);
                contentPane.add(lblFrete);

                JLabel lblBairro = new JLabel("Bairro:");
                lblBairro.setBounds(5, 156, 237, 37);
                contentPane.add(lblBairro);
                
                tfBairro = new JTextField();
                tfBairro.setBounds(242, 156, 237, 37);
                contentPane.add(tfBairro);
        
                JLabel lblCidade = new JLabel("Cidade:");
                lblCidade.setBounds(5, 193, 237, 37);
                contentPane.add(lblCidade);
        
                tfCidade = new JTextField();
                tfCidade.setBounds(242, 193, 237, 37);
                contentPane.add(tfCidade);
        
                JLabel lblNumeroCasa = new JLabel("Número da Casa:");
                lblNumeroCasa.setBounds(5, 230, 237, 37);
                contentPane.add(lblNumeroCasa);
        
                tfNumeroCasa = new JTextField();
                tfNumeroCasa.setBounds(242, 230, 237, 37);
                contentPane.add(tfNumeroCasa);
        
                JLabel lblComplemento = new JLabel("Complemento:");
                lblComplemento.setBounds(5, 267, 237, 37);
                contentPane.add(lblComplemento);
        
                tfComplemento = new JTextField();
                tfComplemento.setBounds(242, 267, 237, 37);
                contentPane.add(tfComplemento);
        
                JLabel lblTempoEspera = new JLabel("Tempo de Espera (minutos):");
                lblTempoEspera.setBounds(5, 304, 237, 37);
                contentPane.add(lblTempoEspera);
        
                tfTempoEspera = new JTextField();
                tfTempoEspera.setBounds(242, 304, 237, 37);
                tfTempoEspera.setText("45"); // Tempo de espera fixo
                tfTempoEspera.setEditable(false);
                contentPane.add(tfTempoEspera);
        
                JButton btnFinalizarPedido = new JButton("Finalizar Pedido");
                btnFinalizarPedido.setBounds(5, 341, 237, 37);
                btnFinalizarPedido.addActionListener(e -> finalizarPedido());
                contentPane.add(btnFinalizarPedido);
        
                JButton btnCancelar = new JButton("Cancelar");
                btnCancelar.setBounds(242, 341, 237, 37);
                btnCancelar.addActionListener(e -> cancelarPedido());
                contentPane.add(btnCancelar);
        
                JButton btnVoltar = new JButton("Voltar");
                btnVoltar.setBounds(123, 378, 237, 37);
                btnVoltar.addActionListener(e -> voltarAoCardapio());
                contentPane.add(btnVoltar);
        
        JLabel label = new JLabel("");
        label.setBounds(242, 378, 237, 37);
        contentPane.add(label);
        
        JLabel label_1 = new JLabel("");
        label_1.setBounds(5, 415, 237, 37);
        contentPane.add(label_1);
        
        JLabel label_2 = new JLabel("");
        label_2.setBounds(242, 415, 237, 37);
        contentPane.add(label_2);

        atualizarSubtotal(); // Inicializa o subtotal
    }

    private void finalizarPedido() {
        try {
            Connection con = Conexao.conectar();
            if (con != null) {
                int idPizzaSelecionada = PizzaSelecionada.getIdPizza();
    
                // Verifica se a pizza selecionada existe no cardápio
                if (pizzaExisteNoCardapio(con, idPizzaSelecionada)) {
                    int quantidade = PizzaSelecionada.getQuantidade();
    
                    String sql = "INSERT INTO pedidos (id_pizza, forma_pagamento, tempo_espera, quantidade, forma_entrega, bairro, cidade, numero_casa, complemento, subtotal, frete, total, forma_pagamento_entrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = con.prepareStatement(sql);

                    statement.setInt(1, idPizzaSelecionada);
                    statement.setString(2, cbFormaPagamento.getSelectedItem().toString());
                    statement.setInt(3, Integer.parseInt(tfTempoEspera.getText()));
                    statement.setInt(4, quantidade);
                    statement.setString(5, cbFormaPagamento.getSelectedItem().toString());
                    statement.setString(6, tfBairro.getText());
                    statement.setString(7, tfCidade.getText());
                    statement.setString(8, tfNumeroCasa.getText());
                    statement.setString(9, tfComplemento.getText());
                    statement.setDouble(10, subtotal);
                    statement.setDouble(11, calcularFrete());
                    statement.setDouble(12, subtotal + calcularFrete());
                    statement.setString(13, "Dinheiro");
    
                    statement.executeUpdate();
    
                    JOptionPane.showMessageDialog(
                            null,
                            "Pedido finalizado com sucesso! Total: R$ " + (subtotal + calcularFrete()),
                            "Informação",
                            JOptionPane.INFORMATION_MESSAGE
                    );
    
                    statement.close();
                    con.close();
    
                    // Reinicializa o subtotal e a tela volta ao cardápio
                    subtotal = 0;
                    voltarAoCardapio();
                } else {
                    JOptionPane.showMessageDialog(null, "A pizza selecionada não existe no cardápio!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    private void cancelarPedido() {
        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Tem certeza que deseja cancelar o pedido?",
                "Cancelar Pedido",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            // Lógica para cancelar o pedido
            // ...

            // Reinicializa o subtotal
            subtotal = 0;

            // Volta ao cardápio
            voltarAoCardapio();
        }
    }

    private void voltarAoCardapio() {
        // Volta à tela do cardápio
        TelaCardapio telaCardapio = new TelaCardapio();
        telaCardapio.setVisible(true);
        this.setVisible(false);
    }

    private void calcularSubtotal() {
        try {
            int quantidade = Integer.parseInt(tfQuantidade.getText());
            
            // Supondo que PizzaSelecionada contém informações sobre a pizza
            double precoPizza = PizzaSelecionada.getPrecoPizza();

            // Lógica para calcular o subtotal com base na forma de pagamento
            subtotal = quantidade * precoPizza;

            // Adiciona juros de 2% para pagamento com cartão
            if (cbFormaPagamento.getSelectedItem().equals("Cartão")) {
                subtotal *= 1.02;
            }

            // Atualiza o rótulo do subtotal
            atualizarSubtotal();
        } catch (NumberFormatException e) {
            // Tratar caso o texto não seja um número
            JOptionPane.showMessageDialog(null, "A quantidade deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            tfQuantidade.setText("");  // Limpa o campo
        }
    }

    private void atualizarSubtotal() {
        // Atualiza o rótulo do subtotal com o valor formatado
        DecimalFormat df = new DecimalFormat("0.00");
        lblSubtotal.setText("R$ " + df.format(subtotal));
    }

    private double calcularFrete() {
        // Lógica para calcular o frete com base no subtotal
        double frete = 0;

        if (subtotal > 0 && subtotal <= 30) {
            frete = 5.0;
        } else if (subtotal > 30 && subtotal <= 50) {
            frete = 8.0;
        } else if (subtotal > 50) {
            frete = 10.0;
        }

        // Atualiza o rótulo do frete
        DecimalFormat df = new DecimalFormat("0.00");
        lblFrete.setText("R$ " + df.format(frete));

        return frete;
    }

    private boolean pizzaExisteNoCardapio(Connection con, int idPizza) throws SQLException {
        String sql = "SELECT * FROM cardapio WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, idPizza);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}