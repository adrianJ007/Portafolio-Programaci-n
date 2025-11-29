/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nibarra;

/**
 *
 * @author adria
 */


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class NIBARRA {

    public static void main(String[] args) {
        try {
            // Usar un Look and Feel limpio para mejorar la apariencia
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignorar
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: No se encontró el driver de MySQL (JDBC). Asegúrate de añadir el .jar al proyecto.", "Error de Configuración", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

    // ===============================================================
    // CONEXIÓN A BASE DE DATOS
    // ===============================================================
    public static class DB {
        private static final String URL = "jdbc:mysql://localhost:3306/NIBARRA_MANTENIMIENTOS?serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASS = "JIMENEZ123456";

        public static Connection conectar() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASS);
        }
    }

    // ===============================================================
    // LOGIN (MODIFICADO: Diseño Vistoso)
    // ===============================================================
    public static class Login extends JFrame {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        public Login() {
            setTitle("🔐 Acceso al Sistema NIBARRA");
            setSize(450, 350); // Aumentado para mejor distribución
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            
            // --- Panel principal (Center) ---
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(new Color(245, 245, 250)); // Fondo claro

            // --- Header / Title ---
            JLabel icon = new JLabel("🛠️ NIBARRA MANTENIMIENTOS", SwingConstants.CENTER);
            icon.setFont(new Font("Arial", Font.BOLD, 22));
            icon.setForeground(new Color(40, 40, 150));
            icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            mainPanel.add(icon, BorderLayout.NORTH);

            // --- Form Panel (Center) ---
            JPanel formPanel = new JPanel(new GridBagLayout()); // Usamos GridBagLayout para mejor control de alineación
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 5, 10, 5);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.setOpaque(false);

            // Estilo de campos
            user.setFont(new Font("Arial", Font.PLAIN, 14));
            pass.setFont(new Font("Arial", Font.PLAIN, 14));
            user.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 250), 1), 
                    BorderFactory.createEmptyBorder(5, 5, 5, 5) 
            ));
            pass.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 250), 1), 
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            // Agregar componentes al formulario
            int y = 0;
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0; 
            formPanel.add(new JLabel("👤 Usuario:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1.0; 
            formPanel.add(user, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0; 
            formPanel.add(new JLabel("🔑 Contraseña:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1.0; 
            formPanel.add(pass, gbc);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            // --- Button Panel (South) ---
            JButton ingresar = new JButton("INGRESAR");
            JButton registrar = new JButton("REGISTRAR USUARIO"); 

            ingresar.setFont(new Font("Arial", Font.BOLD, 14));
            registrar.setFont(new Font("Arial", Font.BOLD, 12));

            ingresar.setBackground(new Color(50, 150, 250)); // Azul
            ingresar.setForeground(Color.WHITE);
            registrar.setBackground(new Color(40, 167, 69)); // Verde
            registrar.setForeground(Color.WHITE); 
            
            ingresar.setFocusPainted(false);
            registrar.setFocusPainted(false);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
            btnPanel.setOpaque(false);
            btnPanel.add(ingresar);
            btnPanel.add(registrar);
            
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            
            add(mainPanel, BorderLayout.CENTER);
            
            ingresar.addActionListener(e -> validar());
            registrar.addActionListener(e -> registrarUsuario());
        }

        private void validar() {
            String rolObtenido = null;
            try (Connection cn = DB.conectar();
                 // SELECCIONA EL ROL para pasarlo al Menu
                 PreparedStatement ps = cn.prepareStatement(
                                     "SELECT usuario, rol FROM usuarios WHERE usuario=? AND contrasena=?"
                            )) {

                ps.setString(1, user.getText().trim());
                ps.setString(2, new String(pass.getPassword()).trim());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    rolObtenido = rs.getString("rol");
                    
                    JOptionPane.showMessageDialog(this, "Bienvenido " + user.getText() + " (Rol: " + rolObtenido + ")");
                    
                    // PASAMOS EL ROL AL MENÚ
                    new Menu(rolObtenido).setVisible(true); 
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Verifique usuario y contraseña.", "Error de Login", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de conexión/validación: " + ex.getMessage());
            }
        }
        
        // ===============================================================
        // MÉTODO PARA REGISTRAR NUEVO USUARIO (MODIFICADO: Título/Mensaje de Rol)
        // ===============================================================
        private void registrarUsuario() {
            JTextField newUser = new JTextField();
            JPasswordField newPass = new JPasswordField();
            JTextField newName = new JTextField();
            
            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Nombre Completo (Opcional):"));
            panel.add(newName);
            panel.add(new JLabel("Nuevo Usuario:"));
            panel.add(newUser);
            panel.add(new JLabel("Nueva Contraseña:"));
            panel.add(newPass);

            // Título claro sobre el rol asignado
            int result = JOptionPane.showConfirmDialog(this, panel, 
                    "⭐ Registro de Nuevo Usuario (Rol ASIGNADO: Técnico)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String usuario = newUser.getText().trim();
                String contrasena = new String(newPass.getPassword()).trim();
                String nombre = newName.getText().trim(); 
                final String ROL_ASIGNADO = "Técnico"; // Rol por defecto

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El usuario y la contraseña no pueden estar vacíos.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String nombreAInsertar = nombre.isEmpty() ? usuario : nombre;

                try (Connection cn = DB.conectar();
                     PreparedStatement ps = cn.prepareStatement(
                                         "INSERT INTO usuarios (usuario, contrasena, nombre, rol) VALUES (?, ?, ?, ?)" 
                                )) {

                    ps.setString(1, usuario);
                    ps.setString(2, contrasena);
                    ps.setString(3, nombreAInsertar); 
                    ps.setString(4, ROL_ASIGNADO); // Se inserta el rol por defecto

                    ps.executeUpdate();

                    // Mensaje de éxito con confirmación explícita del rol
                    JOptionPane.showMessageDialog(this, 
                            "✔️ Usuario **" + usuario + "** registrado con éxito con el ROL **" + ROL_ASIGNADO + "**.\n¡Ya puedes ingresar!", 
                            "Registro Exitoso", 
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(this, "Error: El nombre de usuario '" + usuario + "' ya existe.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error de conexión/registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // ===============================================================
    // PANEL REGISTRO DE USUARIO (MANTENIDO)
    // ===============================================================
    public static class PanelRegistroUsuario extends JPanel {
        
        JTextField newUser = new JTextField(15);
        JPasswordField newPass = new JPasswordField(15);
        JTextField newName = new JTextField(15);
        // Selección de Rol
        JComboBox<String> newRol = new JComboBox<>(new String[]{"Administrador", "Técnico"}); 
        JButton btnRegistrar = new JButton("GUARDAR USUARIO");

        public PanelRegistroUsuario() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Creación de Cuentas de Usuario"));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            
            btnRegistrar.setBackground(new Color(50, 150, 250));
            btnRegistrar.setForeground(Color.WHITE);
            btnRegistrar.setFocusPainted(false);
            btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
            
            int y = 0;
            
            gbc.gridx = 0; gbc.gridy = y; this.add(new JLabel("Nombre Completo:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.fill = GridBagConstraints.HORIZONTAL; this.add(newName, gbc);

            gbc.gridx = 0; gbc.gridy = y; this.add(new JLabel("Usuario (Login):"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.fill = GridBagConstraints.HORIZONTAL; this.add(newUser, gbc);

            gbc.gridx = 0; gbc.gridy = y; this.add(new JLabel("Contraseña:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; this.add(newPass, gbc);

            gbc.gridx = 0; gbc.gridy = y; this.add(new JLabel("Rol a Asignar:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; this.add(newRol, gbc);

            gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; this.add(Box.createVerticalStrut(10), gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; this.add(btnRegistrar, gbc);
            
            btnRegistrar.addActionListener(e -> registrar());
        }

        private void registrar() {
            String usuario = newUser.getText().trim();
            String contrasena = new String(newPass.getPassword()).trim();
            String nombre = newName.getText().trim();
            String rol = newRol.getSelectedItem().toString();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El usuario y la contraseña no pueden estar vacíos.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection cn = DB.conectar();
                 PreparedStatement ps = cn.prepareStatement(
                                     "INSERT INTO usuarios (usuario, contrasena, nombre, rol) VALUES (?, ?, ?, ?)"
                            )) {

                ps.setString(1, usuario);
                ps.setString(2, contrasena);
                ps.setString(3, nombre.isEmpty() ? usuario : nombre);
                ps.setString(4, rol);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✔️ Usuario **" + usuario + "** registrado correctamente con el rol **" + rol + "**.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                newUser.setText("");
                newPass.setText("");
                newName.setText("");

            } catch (SQLIntegrityConstraintViolationException ex) {
                 JOptionPane.showMessageDialog(this, "El usuario **" + usuario + "** ya existe. Por favor, elige otro.", "Error de Duplicado", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // ===============================================================
    // MENÚ (MANTENIDO)
    // ===============================================================
    public static class Menu extends JFrame {
        
        private final String userRol; 
        
        public Menu(String rol) {
            this.userRol = rol;
            
            setTitle("Sistema de Mantenimiento - NIBARRA (Rol: " + rol + ")");
            setSize(1000, 650); 
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JTabbedPane tabs = new JTabbedPane();
            tabs.setFont(new Font("Arial", Font.BOLD, 14));
            
            PanelEquipos panelEquipos = new PanelEquipos();
            PanelMantenimiento panelMantenimiento = new PanelMantenimiento();
            PanelCalendario panelCalendario = new PanelCalendario();
            PanelChatBot panelChatBot = new PanelChatBot();
            PanelFacturas panelFacturas = new PanelFacturas();

            // Pestañas comunes (o restringidas en la lógica interna, no en la visibilidad de la pestaña)
            tabs.addTab("💻 Equipos", panelEquipos);
            tabs.addTab("🛠️ Mantenimientos", panelMantenimiento);
            tabs.addTab("📅 Calendario", panelCalendario);
            tabs.addTab("🤖 ChatBot", panelChatBot);
            tabs.addTab("🧾 Facturas", panelFacturas);
            
            // Si quieres que el Admin tenga un panel de administración más avanzado:
            // if (rol.equals("Administrador")) {
            //     tabs.addTab("🔑 Administración Avanzada", new PanelRegistroUsuario());
            // }


            // LISTENER PARA SINCRONIZACIÓN AUTOMÁTICA
            tabs.addChangeListener(e -> {
                int selectedIndex = tabs.getSelectedIndex();
                // Usamos el título de la pestaña para evitar errores de índice si se añade o quita una pestaña
                String title = tabs.getTitleAt(selectedIndex);
                
                if (title.contains("Equipos")) { 
                    panelEquipos.cargar();
                } else if (title.contains("Mantenimientos")) { 
                    panelMantenimiento.cargar();
                } else if (title.contains("Calendario")) { 
                    panelCalendario.cargarEventos();
                } else if (title.contains("Facturas")) { 
                    panelFacturas.cargar();
                }
            });

            add(tabs);
        }
    }

    // ===============================================================
    // PANEL EQUIPOS (MANTENIDO)
    // ===============================================================
    public static class PanelEquipos extends JPanel {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Equipo", "Marca", "Empresa", "Serie", "Servicio", "F. Ingreso", "F. Salida", "C. Inicial", "C. Final", "Estado"}, 0);

        JTable table = new JTable(model);

        public PanelEquipos() {
            setLayout(new BorderLayout(10, 10)); 
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton agregar = new JButton("➕ Registrar Dispositivo");
            JButton eliminar = new JButton("❌ Eliminar");
            JButton refrescar = new JButton("🔄 Refrescar");
            
            agregar.setBackground(new Color(60, 179, 113));
            agregar.setForeground(Color.BLACK); 
            
            eliminar.setBackground(new Color(220, 20, 60));
            eliminar.setForeground(Color.WHITE);
            refrescar.setBackground(Color.LIGHT_GRAY);
            refrescar.setForeground(Color.BLACK); 


            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            top.add(agregar);
            top.add(eliminar);
            top.add(refrescar);

            add(top, BorderLayout.NORTH);
            add(new JScrollPane(table), BorderLayout.CENTER);

            refrescar.addActionListener(e -> cargar());
            agregar.addActionListener(e -> registrarDispositivo());
            eliminar.addActionListener(e -> eliminar());

            cargar();
        }

        public void cargar() { 
            model.setRowCount(0);
            
            String sql = "SELECT d.id, d.equipo, d.marca, d.serie, d.tipo_servicio, d.fecha_ingreso, d.fecha_salida, d.costo_inicial, d.costo_final, d.empresa, d.estado, " +
                         "       COALESCE(m.porcentaje, 0) AS porcentaje_actual " + 
                         "FROM dispositivos d " +
                         "LEFT JOIN equipos e ON d.id = e.dispositivo_id " +
                         "LEFT JOIN mantenimientos m ON e.id = m.equipo_id AND m.porcentaje < 100 " + 
                         "WHERE d.estado <> 'Terminado' " + 
                         "GROUP BY d.id, d.equipo, d.marca, d.serie, d.tipo_servicio, d.fecha_ingreso, d.fecha_salida, d.costo_inicial, d.costo_final, d.empresa, d.estado, m.porcentaje " + 
                         "ORDER BY d.id";

            try (Connection cn = DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    int porcentaje = rs.getInt("porcentaje_actual");
                    String estadoDb = rs.getString("estado");
                    
                    String estadoDisplay = estadoDb + " (" + porcentaje + "%)";
                    
                    model.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("equipo"),
                                rs.getString("marca"),
                                rs.getString("empresa"), 
                                rs.getString("serie"),
                                rs.getString("tipo_servicio"),
                                rs.getDate("fecha_ingreso"),
                                rs.getDate("fecha_salida"),
                                rs.getDouble("costo_inicial"),
                                rs.getDouble("costo_final"),
                                estadoDisplay 
                            });
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar dispositivos: " + ex.getMessage());
            }
        }

        private void registrarDispositivo() {
            JTextField equipo = new JTextField();
            JTextField marca = new JTextField();
            JTextField empresa = new JTextField(); 
            JTextField serie = new JTextField();
            
            JComboBox<String> tipo = new JComboBox<>(new String[]{
                            "Predictivo", "Preventivo", "Correctivo" 
            });
            JTextField fechaIngreso = new JTextField(LocalDate.now().toString());
            JTextField fechaSalida = new JTextField(LocalDate.now().plusDays(3).toString());
            JTextField costoInicial = new JTextField("0.00");
            JTextField costoFinal = new JTextField("0.00"); 
            JTextArea observacion = new JTextArea(4, 15);
            JScrollPane scrollObs = new JScrollPane(observacion);

            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.insets = new Insets(5, 5, 5, 5); 
            gbc.anchor = GridBagConstraints.WEST; 
            gbc.fill = GridBagConstraints.HORIZONTAL; 

            int y = 0;
            
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Fecha de Ingreso:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(fechaIngreso, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Equipo:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(equipo, gbc);
            
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Marca:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(marca, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Empresa:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(empresa, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Serie:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(serie, gbc);
            
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Tipo de Servicios:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(tipo, gbc);
            
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Fecha de Salida (Estimada):"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(fechaSalida, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Costo Inicial:"), gbc);
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(costoInicial, gbc);
            
            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            formPanel.add(new JLabel("Costo Final (Subtotal de Factura):"), gbc); 
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            formPanel.add(costoFinal, gbc);

            gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST; 
            formPanel.add(new JLabel("Observación:"), gbc);
            
            gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1;
            gbc.weighty = 1.0; 
            gbc.fill = GridBagConstraints.BOTH; 
            formPanel.add(scrollObs, gbc);

            if (JOptionPane.showConfirmDialog(this, formPanel, "Registrar Nuevo Dispositivo",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

            Connection cn = null;
            try {
                cn = DB.conectar();
                cn.setAutoCommit(false); 

                int dispositivoId = -1;

                String insertDispSql = "INSERT INTO dispositivos(fecha_ingreso, equipo, marca, serie, tipo_servicio, fecha_salida, costo_inicial, costo_final, observacion, estado, empresa)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pendiente', ?)";

                try (PreparedStatement psDisp = cn.prepareStatement(insertDispSql, Statement.RETURN_GENERATED_KEYS)) {
                    psDisp.setDate(1, java.sql.Date.valueOf(fechaIngreso.getText()));
                    psDisp.setString(2, equipo.getText());
                    psDisp.setString(3, marca.getText());
                    psDisp.setString(4, serie.getText());
                    psDisp.setString(5, tipo.getSelectedItem().toString());
                    psDisp.setDate(6, java.sql.Date.valueOf(fechaSalida.getText())); 
                    psDisp.setDouble(7, Double.parseDouble(costoInicial.getText()));
                    psDisp.setDouble(8, Double.parseDouble(costoFinal.getText())); 
                    psDisp.setString(9, observacion.getText());
                    psDisp.setString(10, empresa.getText()); 

                    psDisp.executeUpdate();

                    ResultSet rsKeys = psDisp.getGeneratedKeys();
                    if (rsKeys.next()) {
                        dispositivoId = rsKeys.getInt(1);
                    }
                }

                if (dispositivoId != -1) {
                    try (PreparedStatement psEq = cn.prepareStatement(
                                "INSERT INTO equipos(dispositivo_id) VALUES (?)")) {
                        psEq.setInt(1, dispositivoId);
                        psEq.executeUpdate();
                    }
                } else {
                    throw new SQLException("No se pudo obtener el ID del dispositivo insertado.");
                }

                cn.commit(); 
                JOptionPane.showMessageDialog(this, "Dispositivo y Equipo de mantenimiento registrado correctamente");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
                if (cn != null) {
                    try {
                        cn.rollback(); 
                    } catch (SQLException rollbackEx) {
                        JOptionPane.showMessageDialog(this, "Error durante el Rollback: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                try { if (cn != null) cn.close(); } catch (SQLException closeEx) { /* ignored */ }
            }

            cargar();
        }

        private void eliminar() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            if (JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el dispositivo ID: " + id + "? Se eliminarán todos los registros asociados (mantenimientos y facturas). Los registros de Historial/Calendario NO terminados se perderán.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

            Connection cn = null;
            try {
                cn = DB.conectar();
                cn.setAutoCommit(false);
                
                // 1. Obtener ID del equipo asociado
                int equipoId = -1;
                try (PreparedStatement ps = cn.prepareStatement("SELECT id FROM equipos WHERE dispositivo_id = ?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        equipoId = rs.getInt(1);
                    }
                }
                
                // 2. Eliminar Facturas (si existen)
                if (equipoId != -1) {
                    try (PreparedStatement ps = cn.prepareStatement("DELETE FROM facturas WHERE equipo_id = ?")) {
                        ps.setInt(1, equipoId);
                        ps.executeUpdate();
                    }
                }
                
                // 3. Eliminar Mantenimientos (si existen)
                if (equipoId != -1) {
                    try (PreparedStatement ps = cn.prepareStatement("DELETE FROM mantenimientos WHERE equipo_id = ?")) {
                        ps.setInt(1, equipoId);
                        ps.executeUpdate();
                    }
                }

                // 4. Eliminar Equipos (relación)
                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM equipos WHERE dispositivo_id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                // 5. Eliminar Dispositivo
                try (PreparedStatement ps = cn.prepareStatement("DELETE FROM dispositivos WHERE id=?")) {
                    ps.setInt(1, id);
                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(this, "Dispositivo y registros asociados eliminados correctamente. El historial facturado permanece en el ChatBot/Calendario.");
                    } else {
                        throw new SQLException("No se encontró el dispositivo para eliminar.");
                    }
                }
                
                cn.commit();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Detalle: " + ex.getMessage());
                 if (cn != null) {
                    try {
                        cn.rollback(); 
                    } catch (SQLException rollbackEx) {
                        System.err.println("Rollback fallido: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                 try { if (cn != null) cn.close(); } catch (SQLException closeEx) { /* ignored */ }
            }

            cargar();
        }
    }

    // ===============================================================
    // PANEL DE MANTENIMIENTOS (MANTENIDO)
    // ===============================================================
    public static class PanelMantenimiento extends JPanel {

        JPanel col0 = new JPanel();
        JPanel col50 = new JPanel();
        JPanel col75 = new JPanel();
        JPanel col100 = new JPanel(); 

        public PanelMantenimiento() {
            setLayout(new BorderLayout());

            JPanel board = new JPanel(new GridLayout(1, 4, 10, 10));
            board.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Títulos de paneles verificados para reflejar el porcentaje de avance
            configurar(col0, "Por Hacer (0%)", board, new Color(255, 192, 203)); 
            configurar(col50, "En Espera (50%)", board, new Color(255, 255, 150)); 
            configurar(col75, "En Proceso (75%)", board, new Color(173, 216, 230)); 
            configurar(col100, "Terminado (100%)", board, new Color(144, 238, 144)); 

            JButton btnNuevoMantenimiento = new JButton("➕ Registrar Mantenimiento");
            JButton btnActualizarMantenimiento = new JButton("🔄 Actualizar Progreso");
            
            btnNuevoMantenimiento.setBackground(new Color(60, 179, 113));
            btnNuevoMantenimiento.setForeground(Color.BLACK); 
            
            btnActualizarMantenimiento.setBackground(new Color(255, 165, 0));
            btnActualizarMantenimiento.setForeground(Color.WHITE);


            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            topPanel.add(btnNuevoMantenimiento);
            topPanel.add(btnActualizarMantenimiento);

            add(topPanel, BorderLayout.NORTH);
            add(board, BorderLayout.CENTER);

            btnNuevoMantenimiento.addActionListener(e -> registrarMantenimiento());
            btnActualizarMantenimiento.addActionListener(e -> actualizarProgreso());

            cargar();
        }

        private void configurar(JPanel p, String t, JPanel parent, Color colorFondo) {
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                t, 0, 2, new Font("Arial", Font.BOLD, 12), Color.BLACK
            ));
            p.setBackground(colorFondo);
            p.setPreferredSize(new Dimension(220, 500));
            parent.add(new JScrollPane(p)); 
        }
        
        private int obtenerEquipoID(String equipoNombre) throws SQLException {
            String sql = "SELECT e.id FROM equipos e INNER JOIN dispositivos d ON e.dispositivo_id = d.id WHERE d.equipo = ?";
            try (Connection cn = NIBARRA.DB.conectar();
                 PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, equipoNombre);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Equipo de mantenimiento no encontrado para el dispositivo: " + equipoNombre);
            }
        }
        
        private int obtenerDispositivoID(String equipoNombre) throws SQLException {
            String sql = "SELECT id FROM dispositivos WHERE equipo = ?";
            try (Connection cn = NIBARRA.DB.conectar();
                 PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, equipoNombre);
                ResultSet rs = ps.executeQuery(); 
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Dispositivo no encontrado: " + equipoNombre);
            }
        }

        public String[] obtenerNombresEquipos() {
            // Filtro: Solo equipos que NO estén terminados para asignación
            java.util.List<String> equipos = new java.util.ArrayList<>();
            String sql = "SELECT equipo FROM dispositivos WHERE estado <> 'Terminado'"; 
            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    equipos.add(rs.getString("equipo"));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar equipos disponibles: " + ex.getMessage());
            }
            return equipos.toArray(new String[0]);
        }

        private void registrarMantenimiento() {
            String[] equipos = obtenerNombresEquipos();
            if (equipos.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay dispositivos activos para asignar mantenimiento.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> cbEquipo = new JComboBox<>(equipos);
            JTextArea detalle = new JTextArea(5, 20);
            detalle.setLineWrap(true);
            detalle.setWrapStyleWord(true);
            JScrollPane scrollDetalle = new JScrollPane(detalle);

            Object[] campos = {
                            "Seleccionar Equipo:", cbEquipo,
                            "Detalle del trabajo:", scrollDetalle,
            };

            if (JOptionPane.showConfirmDialog(this, campos, "Registrar Nuevo Mantenimiento (0%)",
                            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

            Connection cn = null;
            try {
                cn = NIBARRA.DB.conectar();
                cn.setAutoCommit(false);
                
                String equipoNombre = cbEquipo.getSelectedItem().toString();
                int equipoId = obtenerEquipoID(equipoNombre);
                int dispositivoId = obtenerDispositivoID(equipoNombre); // Nuevo ID para Dispositivos

                // 1. Insertar Mantenimiento
                try (PreparedStatement ps = cn.prepareStatement(
                                     "INSERT INTO mantenimientos(equipo_id, detalle, porcentaje, estado, fecha_inicio) VALUES (?, ?, 0, 'Pendiente', NOW())")) {
                    
                    ps.setInt(1, equipoId);
                    ps.setString(2, detalle.getText());
                    ps.executeUpdate();
                }
                
                // 2. Actualizar estado del Dispositivo a 'En Curso' (REQUERIMIENTO CUMPLIDO)
                 try (PreparedStatement psDisp = cn.prepareStatement("UPDATE dispositivos SET estado = 'En Curso' WHERE id = ?")) {
                    psDisp.setInt(1, dispositivoId);
                    psDisp.executeUpdate();
                }
                
                cn.commit();
                JOptionPane.showMessageDialog(this, "Mantenimiento registrado y estado de Dispositivo actualizado a 'En Curso'.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar mantenimiento: " + ex.getMessage()); 
                if (cn != null) {
                    try {
                        cn.rollback(); 
                    } catch (SQLException rollbackEx) {
                        System.err.println("Rollback fallido: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                 try { if (cn != null) cn.close(); } catch (SQLException closeEx) { /* ignored */ }
            }

            cargar();
        }

        private void actualizarProgreso() {
            java.util.Map<String, Integer> mantenimientosMap = new java.util.HashMap<>();
            String[] nombresMantenimientos;

            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT m.id, d.equipo, m.detalle, m.porcentaje FROM mantenimientos m INNER JOIN equipos e ON m.equipo_id = e.id INNER JOIN dispositivos d ON e.dispositivo_id = d.id WHERE m.porcentaje < 100")) {

                java.util.List<String> nombres = new java.util.ArrayList<>();
                while (rs.next()) {
                    String nombre = rs.getString("equipo") + " - " + rs.getString("detalle") + " (" + rs.getInt("porcentaje") + "%)";
                    mantenimientosMap.put(nombre, rs.getInt("id"));
                    nombres.add(nombre);
                }
                nombresMantenimientos = nombres.toArray(new String[0]);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar mantenimientos: " + ex.getMessage());
                return;
            }

            if (nombresMantenimientos.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay mantenimientos pendientes para actualizar.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JComboBox<String> cbMantenimiento = new JComboBox<>(nombresMantenimientos);
            JComboBox<Integer> cbPorcentaje = new JComboBox<>(new Integer[]{50, 75, 100});
            
            Object[] campos = {
                            "Seleccione Mantenimiento:", cbMantenimiento,
                            "Nuevo Porcentaje:", cbPorcentaje
            };

            if (JOptionPane.showConfirmDialog(this, campos, "Actualizar Progreso",
                            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;


            int mantenimientoId = mantenimientosMap.get(cbMantenimiento.getSelectedItem().toString());
            int nuevoPorcentaje = (int) cbPorcentaje.getSelectedItem();
            
            Connection cn = null;
            try {
                cn = NIBARRA.DB.conectar();
                cn.setAutoCommit(false);
                
                String nuevoEstado = "En Proceso";
                if (nuevoPorcentaje == 50) nuevoEstado = "En Espera";
                else if (nuevoPorcentaje == 75) nuevoEstado = "En Proceso";
                else if (nuevoPorcentaje == 100) nuevoEstado = "Terminado";
                
                // 1. Actualizar mantenimiento
                try (PreparedStatement ps = cn.prepareStatement(
                                     "UPDATE mantenimientos SET porcentaje = ?, estado = ? WHERE id = ?")) {

                    ps.setInt(1, nuevoPorcentaje);
                    ps.setString(2, nuevoEstado);
                    ps.setInt(3, mantenimientoId);
                    ps.executeUpdate();
                }

                if (nuevoPorcentaje == 100) {
                    // Actualizar el estado del DISPOSITIVO a 'Terminado'
                    actualizarEstadoDispositivo(cn, mantenimientoId, "Terminado");
                    actualizarFechaFinMantenimiento(cn, mantenimientoId);
                    // GENERAR FACTURA Y REGISTRAR HISTORIAL CHATBOT
                    generarFacturaYRegistrarHistorial(cn, mantenimientoId); 
                } else {
                    // Actualizar el estado del DISPOSITIVO si no está terminado (por ejemplo, a 'En Curso')
                    actualizarEstadoDispositivo(cn, mantenimientoId, "En Curso");
                }
                
                cn.commit();
                JOptionPane.showMessageDialog(this, "Progreso de mantenimiento actualizado a " + nuevoPorcentaje + "%.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar progreso: " + ex.getMessage());
                if (cn != null) {
                    try {
                        cn.rollback(); 
                    } catch (SQLException rollbackEx) {
                        System.err.println("Rollback fallido: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                 try { if (cn != null) cn.close(); } catch (SQLException closeEx) { /* ignored */ }
            }

            cargar();
        }
        
        private void actualizarEstadoDispositivo(Connection cn, int mantenimientoId, String nuevoEstado) throws SQLException {
             try (PreparedStatement ps = cn.prepareStatement(
                                     "UPDATE dispositivos d " +
                                     "INNER JOIN equipos e ON d.id = e.dispositivo_id " +
                                     "INNER JOIN mantenimientos m ON e.id = m.equipo_id " +
                                     "SET d.estado = ? " +
                                     "WHERE m.id = ?")) {
                ps.setString(1, nuevoEstado);
                ps.setInt(2, mantenimientoId);
                ps.executeUpdate();
            }
        }
        
        private void actualizarFechaFinMantenimiento(Connection cn, int mantenimientoId) throws SQLException {
            try (PreparedStatement ps = cn.prepareStatement("UPDATE mantenimientos SET fecha_fin = NOW() WHERE id = ?")) {
                ps.setInt(1, mantenimientoId);
                ps.executeUpdate();
            }
        }

        private void generarFacturaYRegistrarHistorial(Connection cn, int mantenimientoId) throws SQLException {
            int equipoId = -1;
            double costoFinalDispositivo = 0; 
            String detalleMantenimiento = ""; 
            String nombreEquipo = ""; 
            String serieEquipo = ""; 

            // Se obtiene d.costo_final como base para el subtotal de la factura.
            String sqlDatos = "SELECT m.equipo_id, d.costo_final, m.detalle, d.equipo, d.serie " + 
                              "FROM mantenimientos m " +
                              "JOIN equipos e ON m.equipo_id = e.id " +
                              "JOIN dispositivos d ON e.dispositivo_id = d.id " +
                              "WHERE m.id = ?";
            
            try (PreparedStatement ps = cn.prepareStatement(sqlDatos)) {
                ps.setInt(1, mantenimientoId);
                ResultSet rsDatos = ps.executeQuery();
                if (rsDatos.next()) {
                    equipoId = rsDatos.getInt("equipo_id");
                    costoFinalDispositivo = rsDatos.getDouble("costo_final"); 
                    detalleMantenimiento = rsDatos.getString("detalle"); 
                    nombreEquipo = rsDatos.getString("equipo"); 
                    serieEquipo = rsDatos.getString("serie"); 
                } else {
                    throw new SQLException("No se encontraron datos del mantenimiento para facturar.");
                }
            }

            double subtotal = costoFinalDispositivo; 
            double impuestos = subtotal * 0.07; // 7% de impuesto
            double total = subtotal + impuestos; 
            
            // 1. REGISTRAR FACTURA
            String sqlInsertFactura = "INSERT INTO facturas(equipo_id, trabajo, subtotal, impuestos, total, fecha) VALUES(?,?,?,?,?,NOW())";
            
            try (PreparedStatement psFactura = cn.prepareStatement(sqlInsertFactura)) {
                psFactura.setInt(1, equipoId);
                psFactura.setString(2, detalleMantenimiento);
                psFactura.setDouble(3, subtotal);
                psFactura.setDouble(4, impuestos);
                psFactura.setDouble(5, total);
                psFactura.executeUpdate();
            }
            
            // 2. REGISTRAR HISTORIAL CHATBOT
            String pregunta = "Historial del equipo " + nombreEquipo + " (Serie: " + serieEquipo + ")";
            String respuesta = "Trabajo Realizado: " + detalleMantenimiento + 
                               " | Mantenimiento ID " + mantenimientoId + 
                               " | Subtotal (Costo Final): $" + String.format("%.2f", subtotal) + 
                               " | Impuestos (7%): $" + String.format("%.2f", impuestos) + 
                               " | Costo Total: $" + String.format("%.2f", total) + 
                               " | Fecha de finalización: " + java.time.LocalDate.now().toString();

            String sqlInsertChatbot = "INSERT INTO historial_chatbot (pregunta, respuesta) VALUES (?, ?)";
            try (PreparedStatement psChatbot = cn.prepareStatement(sqlInsertChatbot)) {
                psChatbot.setString(1, pregunta);
                psChatbot.setString(2, respuesta);
                psChatbot.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Factura generada y Historial registrado. Subtotal (Costo Final): $" + String.format("%.2f", subtotal) + " | Total: $" + String.format("%.2f", total), "Facturación", JOptionPane.INFORMATION_MESSAGE);
        }


        private void editarDetalle(int id, String equipo, String detalleActual) {
            JTextArea nuevoDetalleArea = new JTextArea(detalleActual, 5, 25);
            nuevoDetalleArea.setLineWrap(true);
            nuevoDetalleArea.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(nuevoDetalleArea);

            Object[] campos = {
                            "Editando Detalle para: " + equipo,
                            scroll
            };

            if (JOptionPane.showConfirmDialog(this, campos, "Editar Detalle de Mantenimiento (ID: " + id + ")",
                            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

            String nuevoDetalle = nuevoDetalleArea.getText();

            try (Connection cn = NIBARRA.DB.conectar();
                 PreparedStatement ps = cn.prepareStatement(
                                     "UPDATE mantenimientos SET detalle = ? WHERE id = ?")) {

                ps.setString(1, nuevoDetalle);
                ps.setInt(2, id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Detalle actualizado correctamente.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al editar detalle: " + ex.getMessage());
            }

            cargar();
        }
        
        public void cargar() { 
            col0.removeAll();
            col50.removeAll();
            col75.removeAll();
            col100.removeAll(); 

            String sql = "SELECT m.id, m.detalle, m.porcentaje, d.equipo, d.tipo_servicio " + 
                            "FROM mantenimientos m " +
                            "INNER JOIN equipos e ON m.equipo_id = e.id " +
                            "INNER JOIN dispositivos d ON e.dispositivo_id = d.id " +
                            "WHERE m.porcentaje < 100 " + 
                            "ORDER BY d.tipo_servicio, d.equipo"; 

            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                String currentTipoServicio = "";
                JPanel targetPanel = null;

                while (rs.next()) {
                    final int mantenimientoId = rs.getInt("id");
                    final String detalleActual = rs.getString("detalle");
                    final String equipo = rs.getString("equipo"); 
                    final String tipoServicio = rs.getString("tipo_servicio");
                    int pc = rs.getInt("porcentaje");

                    // 1. DETERMINAR EL PANEL OBJETIVO POR PORCENTAJE
                    if (pc == 0) targetPanel = col0;
                    else if (pc == 50) targetPanel = col50;
                    else if (pc == 75) targetPanel = col75;
                    else continue; 

                    // 2. AÑADIR ENCABEZADO DE SERVICIO SI CAMBIA
                    if (!tipoServicio.equals(currentTipoServicio) || targetPanel.getComponentCount() == 0) {
                        JLabel header = new JLabel("--- Servicio: " + tipoServicio.toUpperCase() + " ---");
                        header.setFont(new Font("Arial", Font.BOLD, 12));
                        header.setForeground(new Color(10, 10, 130));
                        header.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
                        header.setAlignmentX(Component.LEFT_ALIGNMENT);
                        targetPanel.add(header);
                        currentTipoServicio = tipoServicio; 
                    }
                    
                    // 3. CREAR Y AÑADIR LA TARJETA DEL MANTENIMIENTO
                    String detalleCompleto = equipo + " - " + (detalleActual != null ? detalleActual : "Sin detalle");

                    JLabel tarjeta = new JLabel("• " + detalleCompleto);
                    tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    tarjeta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    tarjeta.setBackground(Color.WHITE);
                    tarjeta.setOpaque(true);
                    tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);


                    tarjeta.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON3) { // Clic derecho
                                JPopupMenu menu = new JPopupMenu();
                                JMenuItem editItem = new JMenuItem("✏️ Editar Detalle");

                                editItem.addActionListener(ae -> editarDetalle(mantenimientoId, equipo, detalleActual));

                                menu.add(editItem);
                                menu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    });

                    JPanel cardPanel = new JPanel();
                    cardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    cardPanel.setBackground(Color.WHITE);
                    cardPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); 
                    cardPanel.add(tarjeta);
                    cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    targetPanel.add(cardPanel);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el panel de mantenimiento: " + ex.getMessage());
            }

            revalidate();
            repaint();
        }
    }

    // ===============================================================
    // RENDEREADOR DE CELDAS PARA MONEDA (Facturas)
    // ===============================================================
    public static class CurrencyRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");

        public CurrencyRenderer() {
            // Asegura que el texto se alinee a la derecha
            setHorizontalAlignment(RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Double) {
                value = CURRENCY_FORMAT.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    // ===============================================================
    // PANEL FACTURAS (MANTENIDO)
    // ===============================================================
    public static class PanelFacturas extends JPanel {

        Vector<String> columnNames = new Vector<>(java.util.Arrays.asList("ID", "Equipo", "Descripción", "Subtotal", "Impuestos (7%)", "Total", "Fecha"));
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 3: 
                    case 4: 
                    case 5: 
                        return Double.class; 
                    default:
                        return Object.class;
                }
            }
             @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable table = new JTable(model);

        public PanelFacturas() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton nueva = new JButton("📝 Generar Factura (Manual)");
            JButton refrescar = new JButton("🔄 Refrescar");
            
            nueva.setBackground(new Color(30, 144, 255));
            nueva.setForeground(Color.WHITE);
            refrescar.setBackground(Color.LIGHT_GRAY);
            refrescar.setForeground(Color.BLACK);


            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            top.add(nueva);
            top.add(refrescar);

            add(top, BorderLayout.NORTH);
            add(new JScrollPane(table), BorderLayout.CENTER);

            nueva.addActionListener(e -> generarFactura());
            refrescar.addActionListener(e -> cargar());
            
            table.setDefaultRenderer(Double.class, new CurrencyRenderer());
            table.getColumnModel().getColumn(3).setPreferredWidth(80); 
            table.getColumnModel().getColumn(4).setPreferredWidth(80); 
            table.getColumnModel().getColumn(5).setPreferredWidth(80);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 

            cargar();
        }

        public void cargar() { 
            model.setRowCount(0);

            try (Connection cn = DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(
                                     "SELECT f.id AS factura_id, " +
                                     "d.equipo AS nombre_equipo, " + 
                                     "f.trabajo AS trabajo_realizado, " +
                                     "f.subtotal, f.impuestos, f.total, " + 
                                     "f.fecha AS fecha_factura " +
                                     "FROM facturas f " +
                                     "INNER JOIN equipos e ON f.equipo_id = e.id " +
                                     "INNER JOIN dispositivos d ON e.dispositivo_id = d.id " +
                                     "ORDER BY f.fecha DESC"
                             )) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                                rs.getInt("factura_id"),
                                rs.getString("nombre_equipo"),
                                rs.getString("trabajo_realizado"),
                                rs.getDouble("subtotal"), 
                                rs.getDouble("impuestos"), 
                                rs.getDouble("total"), 
                                rs.getDate("fecha_factura")
                            });
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar facturas: " + ex.getMessage());
            }
        }
        
        private void generarFactura() {
            PanelMantenimiento pm = new PanelMantenimiento();
            String[] equipos = pm.obtenerNombresEquipos();
            if (equipos.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay dispositivos activos para facturar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> cbEquipo = new JComboBox<>(equipos);
            JTextArea trabajo = new JTextArea("Trabajo Manual", 3, 20);
            trabajo.setLineWrap(true);
            trabajo.setWrapStyleWord(true);
            JTextField subtotalField = new JTextField("0.00"); 
            JTextField fecha = new JTextField(LocalDate.now().toString());

            JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
            p.add(new JLabel("Seleccionar Equipo:")); p.add(cbEquipo);
            p.add(new JLabel("Descripción del Servicio:")); p.add(new JScrollPane(trabajo)); 
            p.add(new JLabel("Costo Base (Subtotal):")); p.add(subtotalField); 
            p.add(new JLabel("Fecha (YYYY-MM-DD):")); p.add(fecha);

            if (JOptionPane.showConfirmDialog(this, p, "Registrar Factura Manual",
                            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

            try {
                int equipoId = new PanelMantenimiento().obtenerEquipoID(cbEquipo.getSelectedItem().toString()); 
                double subtotal = Double.parseDouble(subtotalField.getText()); 

                double impuestosCalc = subtotal * 0.07; // 7%
                double total = subtotal + impuestosCalc; 
                
                try (Connection cn = DB.conectar();
                     PreparedStatement ps = cn.prepareStatement(
                                     "INSERT INTO facturas(equipo_id, subtotal, impuestos, total, fecha, trabajo) VALUES(?,?,?,?,?,?)")) {

                    ps.setInt(1, equipoId);
                    ps.setDouble(2, subtotal);
                    ps.setDouble(3, impuestosCalc);
                    ps.setDouble(4, total);
                    ps.setDate(5, java.sql.Date.valueOf(fecha.getText()));
                    ps.setString(6, trabajo.getText());

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✔️ Factura manual registrada correctamente. Subtotal: $" + String.format("%.2f", subtotal) + " | Total: $" + String.format("%.2f", total));

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de DB al registrar factura: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(this, "Error de formato de número en Costo. Asegúrate de usar un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
            }

            cargar();
        }
    }
    
    // ===============================================================
    // PANEL CALENDARIO (MANTENIDO)
    // ===============================================================
    public static class PanelCalendario extends JPanel {
        
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Fecha", "Evento (Mantenimiento / Manual)"}, 0);

        JTable table = new JTable(model);

        public PanelCalendario() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(new Color(245, 245, 245)); 

            JLabel titulo = new JLabel("📅 Vista de Eventos", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 18));
            titulo.setForeground(new Color(0, 100, 0)); 

            JButton refrescar = new JButton("🔄 Recargar Eventos");
            refrescar.addActionListener(e -> cargarEventos());
            refrescar.setBackground(new Color(152, 251, 152));
            refrescar.setForeground(Color.BLACK); 
            
            JPanel top = new JPanel(new BorderLayout());
            top.add(titulo, BorderLayout.CENTER);
            top.add(refrescar, BorderLayout.EAST);
            
            table.setRowHeight(25);
            table.setShowGrid(true);
            table.setGridColor(Color.LIGHT_GRAY);
            table.getTableHeader().setBackground(new Color(220, 220, 220));

            add(top, BorderLayout.NORTH);
            add(new JScrollPane(table), BorderLayout.CENTER);
            
            // Panel para agregar eventos manuales
            JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField fechaField = new JTextField(LocalDate.now().toString(), 10);
            JTextField eventoField = new JTextField(30);
            JButton btnAgregarManual = new JButton("Agregar Evento Manual");
            btnAgregarManual.setBackground(Color.LIGHT_GRAY);
            btnAgregarManual.setForeground(Color.BLACK);
            
            panelAgregar.add(new JLabel("Fecha (YYYY-MM-DD):"));
            panelAgregar.add(fechaField);
            panelAgregar.add(new JLabel("Descripción:"));
            panelAgregar.add(eventoField);
            panelAgregar.add(btnAgregarManual);
            
            add(panelAgregar, BorderLayout.SOUTH);
            
            btnAgregarManual.addActionListener(e -> {
                try (Connection cn = DB.conectar();
                     PreparedStatement ps = cn.prepareStatement("INSERT INTO calendario (fecha, evento) VALUES (?, ?)")) {
                    ps.setDate(1, java.sql.Date.valueOf(fechaField.getText()));
                    ps.setString(2, eventoField.getText());
                    ps.executeUpdate();
                    cargarEventos();
                    eventoField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al agregar evento: " + ex.getMessage());
                }
            });
            
            cargarEventos();
        }
        
        public void cargarEventos() { 
            model.setRowCount(0);

            // Cargar eventos manuales
            String sqlManual = "SELECT fecha, evento FROM calendario ORDER BY fecha DESC";
            try (Connection cn = DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sqlManual)) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                                rs.getDate("fecha"),
                                rs.getString("evento") + " (Manual)"
                            });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar eventos manuales: " + ex.getMessage());
            }
            
            // Cargar fechas de mantenimientos (AHORA INCLUYE TERMINADOS)
            String sqlMantenimientos = "SELECT d.equipo, m.detalle, m.fecha_inicio, m.fecha_fin, d.estado AS estado_dispositivo " +
                                       "FROM mantenimientos m " +
                                       "JOIN equipos e ON m.equipo_id = e.id " +
                                       "JOIN dispositivos d ON e.dispositivo_id = d.id " +
                                       "WHERE m.fecha_inicio IS NOT NULL"; 

            try (Connection cn = DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sqlMantenimientos)) {
                
                while (rs.next()) {
                    String equipo = rs.getString("equipo");
                    String estado = rs.getString("estado_dispositivo");
                    
                    // Evento de Inicio
                    if (rs.getDate("fecha_inicio") != null) {
                         model.addRow(new Object[]{
                                rs.getDate("fecha_inicio"),
                                "Inicio Mantenimiento: " + equipo
                            });
                    }
                    
                    // Evento de Fin (si existe fecha de fin prevista O TERMINADA)
                    if (rs.getDate("fecha_fin") != null) {
                        String tipoEvento = estado.equals("Terminado") ? "Fin Mantenimiento (COMPLETADO): " : "Fin Mantenimiento (Previsto): ";
                         model.addRow(new Object[]{
                                rs.getDate("fecha_fin"),
                                tipoEvento + equipo
                            });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar eventos de mantenimiento: " + ex.getMessage());
            }
        }
    }

    // ===============================================================
    // PANEL CHATBOT (MANTENIDO)
    // ===============================================================
    public static class PanelChatBot extends JPanel {

        JTextArea chatArea = new JTextArea();
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Enviar");

        public PanelChatBot() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(new Color(240, 248, 255)); 

            chatArea.setEditable(false);
            chatArea.setLineWrap(true);
            chatArea.setWrapStyleWord(true);
            chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            chatArea.setForeground(Color.BLACK); 

            JScrollPane scrollPane = new JScrollPane(chatArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);
            
            sendButton.setBackground(new Color(65, 105, 225));
            sendButton.setForeground(Color.WHITE);

            JLabel titulo = new JLabel("🤖 ChatBot de Asistencia NIBARRA", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 18));
            titulo.setForeground(new Color(65, 105, 225));

            add(titulo, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(inputPanel, BorderLayout.SOUTH);

            appendMessage("🤖", "Hola, soy el ChatBot de NIBARRA. Puedes preguntarme sobre **'Historial'**, **'Calendario'** o los otros paneles.");
            
            ActionListener sendMessage = e -> {
                String userMessage = inputField.getText().trim();
                if (!userMessage.isEmpty()) {
                    appendMessage("👤 Tú", userMessage);
                    inputField.setText("");
                    simulateResponse(userMessage);
                }
            };

            sendButton.addActionListener(sendMessage);
            inputField.addActionListener(sendMessage);
        }

        private void appendMessage(String user, String message) {
            chatArea.append(user + ": " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength()); 
        }

        private String consultarHistorialGeneral() {
            StringBuilder sb = new StringBuilder();
            
            // 1. Historial de Mantenimientos Terminados (Facturados) - Últimos 3
            sb.append("\n--- Historial de Mantenimientos Terminados (Facturados) ---\n");
            String sqlTerminado = "SELECT respuesta, fecha_registro FROM historial_chatbot ORDER BY fecha_registro DESC LIMIT 3";
            
            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sqlTerminado)) {

                int count = 1;
                while (rs.next()) {
                    sb.append("\n[Terminado #").append(count++).append("]\n");
                    sb.append("   Fecha de Registro: ").append(rs.getTimestamp("fecha_registro")).append("\n");
                    sb.append("   Registro de Factura / Trabajo: ").append(rs.getString("respuesta")).append("\n"); 
                }
            } catch (Exception ex) {
                sb.append("Error al consultar historial terminado: ").append(ex.getMessage()).append("\n");
            }
            
            // 2. Eventos Manuales de Calendario - Últimos 3
            sb.append("\n--- Eventos Manuales de Calendario ---\n");
            String sqlManual = "SELECT fecha, evento FROM calendario ORDER BY fecha DESC LIMIT 3";
            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sqlManual)) {
                
                int count = 1;
                while (rs.next()) {
                    sb.append("\n[Manual #").append(count++).append("]\n");
                    sb.append("   Fecha: ").append(rs.getDate("fecha")).append("\n");
                    sb.append("   Detalle: ").append(rs.getString("evento")).append("\n");
                }
            } catch (Exception ex) {
                sb.append("Error al consultar calendario manual: ").append(ex.getMessage()).append("\n");
            }

            // 3. Fechas Clave de Mantenimiento (Inicio/Fin) - Últimos 3
            sb.append("\n--- Fechas Clave de Mantenimientos (Inicio/Fin) ---\n");
            String sqlMantenimientos = "SELECT d.equipo, m.detalle, m.fecha_inicio, m.fecha_fin, d.estado AS estado_dispositivo " +
                                       "FROM mantenimientos m " +
                                       "JOIN equipos e ON m.equipo_id = e.id " +
                                       "JOIN dispositivos d ON e.dispositivo_id = d.id " +
                                       "WHERE m.fecha_inicio IS NOT NULL " + 
                                       "ORDER BY m.fecha_inicio DESC LIMIT 3"; 

            try (Connection cn = NIBARRA.DB.conectar();
                 Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sqlMantenimientos)) {
                
                int count = 1;
                while (rs.next()) {
                    String equipo = rs.getString("equipo");
                    String estado = rs.getString("estado_dispositivo");
                    
                    sb.append("\n[Mantenimiento #").append(count++).append(" - ").append(equipo).append("]\n");

                    if (rs.getDate("fecha_inicio") != null) {
                         sb.append("   Inicio: ").append(rs.getDate("fecha_inicio")).append("\n");
                    }
                    
                    if (rs.getDate("fecha_fin") != null) {
                        String tipoEvento = estado.equals("Terminado") ? "Fin (COMPLETADO): " : "Fin (Previsto): ";
                         sb.append("   ").append(tipoEvento).append(rs.getDate("fecha_fin")).append("\n");
                    } else {
                         sb.append("   Estado: ").append(estado).append(" (En curso, sin fecha fin establecida)").append("\n");
                    }
                }
            } catch (Exception ex) {
                sb.append("Error al consultar eventos de mantenimiento: ").append(ex.getMessage()).append("\n");
            }
            
            return sb.toString();
        }
        
        private void simulateResponse(String userMessage) {
            String response;
            String lowerMsg = userMessage.toLowerCase();
            
            if (lowerMsg.contains("historial") || lowerMsg.contains("terminado") || lowerMsg.contains("calendario") || lowerMsg.contains("agenda") || lowerMsg.contains("general")) {
                String historial = consultarHistorialGeneral();
                if (historial.trim().isEmpty()) {
                    response = "No hay información de historial o eventos de calendario para mostrar.";
                } else {
                    response = "Informe Histórico y de Calendario General (Últimos Registros):\n" + historial;
                }
            } else if (lowerMsg.contains("estado") || lowerMsg.contains("progreso")) {
                response = "Para ver el estado y progreso de los mantenimientos activos (no terminados), consulta el panel '🛠️ Mantenimientos'.";
            } else if (lowerMsg.contains("factura") || lowerMsg.contains("costo") || lowerMsg.contains("total")) {
                response = "Toda la información de facturación (costos, impuestos del 7% y totales) se encuentra en el panel '🧾 Facturas'.";
            } else if (lowerMsg.contains("equipo") || lowerMsg.contains("dispositivo")) {
                response = "Puedes registrar y ver los detalles de los equipos activos en el panel '💻 Equipos'.";
            } else if (lowerMsg.contains("hola") || lowerMsg.contains("gracias")) {
                 response = "De nada. Recuerda que siempre estoy aquí para ayudarte con las dudas generales del sistema.";
            } else {
                response = "Mi función es ayudarte con información básica de navegación. Por favor, sé más específico sobre qué parte del sistema tienes dudas (Equipos, Mantenimientos, Facturas, Historial/Calendario).";
            }
            
            Timer timer = new Timer(500, e -> appendMessage("🤖 NIBARRA Bot", response));
            timer.setRepeats(false);
            timer.start();
        }
    }
}
