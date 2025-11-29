import javax.swing.JOptionPane;

public class CalculoHipotenusa {
    public static void main(String[] args) {
        String catetoAString = JOptionPane.showInputDialog("Ingresa la longitud del primer cateto:");
        String catetoBString = JOptionPane.showInputDialog("Ingresa la longitud del segundo cateto:");

        try {
            double catetoA = Double.parseDouble(catetoAString);
            double catetoB = Double.parseDouble(catetoBString);

            double hipotenusa = Math.sqrt(Math.pow(catetoA, 2) + Math.pow(catetoB, 2));

            JOptionPane.showMessageDialog(null, "La hipotenusa es: " + hipotenusa);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Por favor, introduce solo números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}