import javax.swing.JOptionPane;

public class CalculoDeVenta {
    public static void main(String[] args) {

        double precioUnitario = 0;
        double cantidad = 0;

        while (true) {
            String inputPrecio = JOptionPane.showInputDialog("Ingrese el precio unitario del producto:");
            if (inputPrecio == null) return;
            try {
                precioUnitario = Double.parseDouble(inputPrecio);
                if (precioUnitario >= 0) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "El precio no puede ser negativo. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada no válida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        while (true) {
            String inputCantidad = JOptionPane.showInputDialog("Ingrese la cantidad de venta:");
            if (inputCantidad == null) return; 
            try {
                cantidad = Double.parseDouble(inputCantidad);
                if (cantidad >= 0) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada no válida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        double precioParcial = precioUnitario * cantidad;
        JOptionPane.showMessageDialog(null, String.format("El precio parcial es: $%.2f", precioParcial), "Precio Parcial", JOptionPane.INFORMATION_MESSAGE);

        double descuento = 0;
        int opcionDescuento = JOptionPane.showConfirmDialog(null, "¿Desea aplicar un descuento?", "Aplicar Descuento", JOptionPane.YES_NO_OPTION);

        if (opcionDescuento == JOptionPane.YES_OPTION) {
            int[] descuentosDisponibles = {5, 10, 20, 30, 40, 50};
            
            StringBuilder sb = new StringBuilder("Seleccione un descuento (1-6):\n");
            for (int i = 0; i < descuentosDisponibles.length; i++) {
                sb.append(String.format("%d. %d%%%n", i + 1, descuentosDisponibles[i]));
            }
            while (true) {
                String inputOpcion = JOptionPane.showInputDialog(sb.toString());
                if (inputOpcion == null) break; 
                try {
                    int opcion = Integer.parseInt(inputOpcion);
                    if (opcion >= 1 && opcion <= descuentosDisponibles.length) {
                        int porcentajeDescuento = descuentosDisponibles[opcion - 1];
                        descuento = (precioParcial * porcentajeDescuento) / 100.0;
                        JOptionPane.showMessageDialog(null, String.format("Descuento aplicado: $%.2f", descuento), "Descuento Aplicado", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione un número de la lista.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada no válida. Por favor, ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        double precioNeto = precioParcial - descuento;
        JOptionPane.showMessageDialog(null, String.format("El precio neto a pagar es: $%.2f", precioNeto), "Precio Neto", JOptionPane.INFORMATION_MESSAGE);
    }
}