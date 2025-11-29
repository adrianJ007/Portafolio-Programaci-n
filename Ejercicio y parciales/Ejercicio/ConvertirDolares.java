import javax.swing.JOptionPane;

public class ConvertirDolares{
    public static void main(String[] args) {
            double euro = 0.92; 
            double peseta = 153.0;
            double yen = 147.0;
            double libra = 0.79;
            double franco = 0.89;
            double dolarCanadiense = 1.36;

            String cantidadStr=""; 
            double cantidadUSD=0;
        while (true) { 
            try {
            cantidadStr = JOptionPane.showInputDialog("Introduce la cantidad en dólares (USD):");
            cantidadUSD = Double.parseDouble(cantidadStr);

            if (cantidadUSD >= 0) {
                JOptionPane.showMessageDialog(null,String.format(
                    "Equivalente de $%.2f USD en otras monedas:\n\n" +
                    "- Euros: €%.2f\n" +
                    "- Pesetas: ₧%.2f\n" +
                    "- Yenes: ¥%.2f\n" +
                    "- Libras Esterlinas: £%.2f\n" +
                    "- Francos Suizos: CHF %.2f\n" +
                    "- Dólares Canadienses: CA$%.2f",
                    cantidadUSD,
                    (cantidadUSD * euro),
                    (cantidadUSD * peseta),
                    (cantidadUSD * yen),
                    (cantidadUSD * libra),
                    (cantidadUSD * franco),
                    (cantidadUSD * dolarCanadiense)
                )
            );
            break;
            } else {
                JOptionPane.showMessageDialog(null,"el valor no puede ser negativo");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, introduce una cantidad válida.");
        }
        }
    }
}
