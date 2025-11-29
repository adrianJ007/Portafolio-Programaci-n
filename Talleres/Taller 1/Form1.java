import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Form1 extends JFrame { 
    private final JTextField[] txtVentas = new JTextField[3];
    private final JTextField[] txtPrecios = new JTextField[3];
    private final String[] PRODUCTOS = {"Soda de Cola", "Soda de Naranja", "Soda de Limon"};
    private DefaultTableModel tableModel;
    private JLabel lblTotalGlobal;
    private static final double MAX_VENTAS = 100000000.0;
    private static final DecimalFormat DF = new DecimalFormat("#,##0.00");
    private static final DecimalFormat DF_CANTIDAD = new DecimalFormat("#,##0");
    private JButton botonLimpio = new JButton("Limpio");

    public Form1() { 
        setTitle("Calculo de ventas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel panelEntrada = new JPanel(new GridLayout(3, 4, 10, 5));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Datos de Ventas"));
        
        for (int i = 0; i < PRODUCTOS.length; i++) {
            panelEntrada.add(new JLabel(PRODUCTOS[i] + " - Cantidad (Máx 100M):"));
            txtVentas[i] = new JTextField(5);
            panelEntrada.add(txtVentas[i]);

            panelEntrada.add(new JLabel("Precio (B/.):"));
            txtPrecios[i] = new JTextField(5);
            panelEntrada.add(txtPrecios[i]);
        }
        
        JButton btnCalcular = new JButton("Calcular y Mostrar Informe");
        btnCalcular.addActionListener(e -> calcularVentas()); 
        lblTotalGlobal = new JLabel("TOTAL VENTAS: B/. 0.00");
        lblTotalGlobal.setFont(new Font("Arial", Font.BOLD, 14));

        botonLimpio.addActionListener((actionEvent) -> {
         
        });
        

        
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(btnCalcular, BorderLayout.WEST);
        panelSur.add(lblTotalGlobal, BorderLayout.EAST);
        panelSur.add(botonLimpio,BorderLayout.SOUTH);
        
        String[] nombresColumnas = {"Producto", "Ventas", "Precio", "Total"};
        tableModel = new DefaultTableModel(nombresColumnas, 3) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        for(int i = 0; i < PRODUCTOS.length; i++) {
            tableModel.setValueAt(PRODUCTOS[i], i, 0);
        }
        
        JTable tablaInforme = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaInforme);
        
        add(panelEntrada, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        cargarDatosEjemplo();
        calcularVentas();
    }
    private void cargarDatosEjemplo() {
        txtVentas[0].setText("1"); txtPrecios[0].setText(".0");
        txtVentas[1].setText("1"); txtPrecios[1].setText(".0");
        txtVentas[2].setText("1"); txtPrecios[2].setText(".0");
    }
    
    private void calcularVentas() {
        double totalGlobal = 0.0;
        for (int i = 0; i < PRODUCTOS.length; i++) {
            try {
                double cantidadVendida = Double.parseDouble(txtVentas[i].getText().replace(",", ""));
                double precio = Double.parseDouble(txtPrecios[i].getText().replace(",", ""));
                
                if (cantidadVendida < 0 || cantidadVendida > MAX_VENTAS) {
                    JOptionPane.showMessageDialog(this, 
                        "ERROR en " + PRODUCTOS[i] + ": La cantidad debe ser entre 0 y 100,000,000.", 
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                if (precio < 0) {
                     JOptionPane.showMessageDialog(this, 
                         "ERROR en " + PRODUCTOS[i] + ": El precio no puede ser negativo.", 
                         "Error de Validación", JOptionPane.ERROR_MESSAGE);
                     return; 
                }
                
                double totalProducto = cantidadVendida * precio;
                totalGlobal += totalProducto;
                
                tableModel.setValueAt(DF_CANTIDAD.format(cantidadVendida), i, 1);
                tableModel.setValueAt(DF.format(precio), i, 2);
                tableModel.setValueAt(DF.format(totalProducto), i, 3);

                


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "ERROR: Ingrese solo valores numéricos válidos en la fila de " + PRODUCTOS[i] + ".", 
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        lblTotalGlobal.setText("TOTAL VENTAS: B/. " + DF.format(totalGlobal));
    } 
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Form1::new); 
        
    }
}