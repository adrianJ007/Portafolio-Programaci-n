import javax.swing.JOptionPane;

public class CostoAuto {
    public static void main(String[] args) {
        double kmPorDia = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el total de kilómetros conducidos por día:"));
        double costoLitro = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el costo por litro de combustible:"));
        double kmPorLitro = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el promedio de kilómetros por litro:"));
        double estacionamiento = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cuota de estacionamiento por día:"));
        double peaje = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el peaje por día:"));
        int personas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese entre cuántas personas se comparte el auto (1 si no comparte):"));

        double litrosConsumidos = kmPorDia / kmPorLitro;
        double costoCombustible = litrosConsumidos * costoLitro;
        double costoTotal = costoCombustible + estacionamiento + peaje;
        double costoCompartido = costoTotal / personas;
        double ahorro = costoTotal - costoCompartido;

        JOptionPane.showMessageDialog(null,
            String.format("Costo total diario sin compartir: B/. %.2f\n" +
                          "Costo diario por persona al compartir: B/. %.2f\n" +
                          "Ahorro estimado: B/. %.2f",
                          costoTotal, costoCompartido, ahorro));
    }
}
