// Define la clase principal del programa, llamada Factura
public class Factura {
    // El método principal desde donde se ejecuta el programa
    public static void main(String[] args) {

        // --- 1. Declarar variables para los productos ---
        String descripcion1 = "Lámpara de escritorio"; // Nombre del primer producto
        int cantidad1 = 2; // Cantidad del primer producto
        double costoUnitario1 = 15.50; // Costo por unidad

        String descripcion2 = "Cable HDMI"; // Nombre del segundo producto
        int cantidad2 = 1; // Cantidad del segundo producto
        double costoUnitario2 = 8.75; // Costo por unidad

        // --- 2. Realizar los cálculos ---
        double total1 = cantidad1 * costoUnitario1; // Total del primer producto
        double total2 = cantidad2 * costoUnitario2; // Total del segundo producto

        double subtotal = total1 + total2; // Suma de los totales individuales
        double impuesto = subtotal * 0.07; // 7% de impuesto (ITBMS)
        double totalFinal = subtotal + impuesto; // Total final

        // --- 3. Imprimir la factura en la consola ---
        System.out.println("-------------------------------------------------");
        System.out.println("               FACTURA DE VENTA");
        System.out.println("EMPRESA X");
        System.out.println("-------------------------------------------------");
        System.out.println("CANT.  DESCRIPCIÓN              COSTO U.   TOTAL");
        // Usa printf para alinear las columnas
        System.out.printf("%-6d %-25s $%.2f   $%.2f\n", cantidad1, descripcion1, costoUnitario1, total1);
        System.out.printf("%-6d %-25s $%.2f   $%.2f\n", cantidad2, descripcion2, costoUnitario2, total2);
        System.out.println("-------------------------------------------------");
        System.out.printf("SUBTOTAL:                            $%.2f\n", subtotal);
        System.out.printf("IMPUESTO (7%%):                      $%.2f\n", impuesto);
        System.out.printf("TOTAL FINAL:                         $%.2f\n", totalFinal);
        System.out.println("-------------------------------------------------");
    }
}