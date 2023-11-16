package TELAS;

public class PizzaSelecionada {
    private static String nomePizza;
    private static double precoPizza;
    private static int quantidade;
    private static double subtotal;
    private static int idPizza; // Adicionado o atributo idPizza

    public static String getNomePizza() {
        return nomePizza;
    }

    public static void setNomePizza(String nomePizza) {
        PizzaSelecionada.nomePizza = nomePizza;
    }

    public static double getPrecoPizza() {
        return precoPizza;
    }

    public static void setPrecoPizza(double precoPizza) {
        PizzaSelecionada.precoPizza = precoPizza;
    }

    public static int getQuantidade() {
        return quantidade;
    }

    public static void setQuantidade(int quantidade) {
        PizzaSelecionada.quantidade = quantidade;
        calcularSubtotal();
    }

    public static double getSubtotal() {
        return subtotal;
    }

    private static void calcularSubtotal() {
        subtotal = quantidade * precoPizza;
    }

    public static int getIdPizza() {
        return idPizza;
    }

    public static void setIdPizza(int idPizza) {
        PizzaSelecionada.idPizza = idPizza;
    }
}
