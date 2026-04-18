
package views;

import domain.Marca;
import domain.Sucursal;
import domain.Vehiculo;
import domain.VehiculoCombustible;
import domain.VehiculoElectrico;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AgregarVehiculoView extends JFrame {

    private JTextField patenteField;
    private JTextField marcaField;
    private JTextField paisField;
    private JTextField modeloField;
    private JTextField anioField;
    private JTextField capacidadCargaField;

    private JComboBox<Sucursal> sucursalCombo;
    private JComboBox<String> tipoCombo;

    private JTextField kwhBaseField;
    private JTextField kmPorLitroField;
    private JTextField litrosExtraField;

    private JLabel kwhBaseLabel;
    private JLabel kmPorLitroLabel;
    private JLabel litrosExtraLabel;

    private JButton guardarButton;
    private JButton cancelarButton;

    public AgregarVehiculoView() {
        setTitle("Agregar Vehículo");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        int y = 20;

        panel.add(createLabel("Patente:", 20, y));
        patenteField = createTextField(180, y);
        panel.add(patenteField);
        y += 35;

        panel.add(createLabel("Marca:", 20, y));
        marcaField = createTextField(180, y);
        panel.add(marcaField);
        y += 35;

        panel.add(createLabel("País de marca:", 20, y));
        paisField = createTextField(180, y);
        panel.add(paisField);
        y += 35;

        panel.add(createLabel("Modelo:", 20, y));
        modeloField = createTextField(180, y);
        panel.add(modeloField);
        y += 35;

        panel.add(createLabel("Año:", 20, y));
        anioField = createTextField(180, y);
        panel.add(anioField);
        y += 35;

        panel.add(createLabel("Capacidad de carga:", 20, y));
        capacidadCargaField = createTextField(180, y);
        panel.add(capacidadCargaField);
        y += 35;

        panel.add(createLabel("Sucursal:", 20, y));
        sucursalCombo = new JComboBox<>();
        sucursalCombo.setBounds(180, y, 200, 25);
        cargarSucursales();
        panel.add(sucursalCombo);
        y += 35;

        panel.add(createLabel("Tipo:", 20, y));
        tipoCombo = new JComboBox<>(new String[]{"ELECTRICO", "COMBUSTIBLE"});
        tipoCombo.setBounds(180, y, 200, 25);
        tipoCombo.addActionListener(e -> actualizarCamposPorTipo());
        panel.add(tipoCombo);
        y += 40;

        kwhBaseLabel = createLabel("kWh base:", 20, y);
        panel.add(kwhBaseLabel);
        kwhBaseField = createTextField(180, y);
        panel.add(kwhBaseField);
        y += 35;

        kmPorLitroLabel = createLabel("Km por litro:", 20, y);
        panel.add(kmPorLitroLabel);
        kmPorLitroField = createTextField(180, y);
        panel.add(kmPorLitroField);
        y += 35;

        litrosExtraLabel = createLabel("Litros extra:", 20, y);
        panel.add(litrosExtraLabel);
        litrosExtraField = createTextField(180, y);
        panel.add(litrosExtraField);
        y += 50;

        guardarButton = new JButton("Guardar");
        guardarButton.setBounds(100, y, 120, 30);
        guardarButton.addActionListener(e -> guardarVehiculo());
        panel.add(guardarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(250, y, 120, 30);
        cancelarButton.addActionListener(e -> dispose());
        panel.add(cancelarButton);

        add(panel);

        actualizarCamposPorTipo();
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 25);
        return label;
    }

    private JTextField createTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 25);
        return field;
    }

    private void cargarSucursales() {
        ArrayList<Sucursal> sucursales = Controlador.getSucursales();
        for (Sucursal sucursal : sucursales) {
            sucursalCombo.addItem(sucursal);
        }
    }

    private void actualizarCamposPorTipo() {
        String tipo = (String) tipoCombo.getSelectedItem();
        boolean esElectrico = "ELECTRICO".equals(tipo);

        kwhBaseLabel.setVisible(esElectrico);
        kwhBaseField.setVisible(esElectrico);

        kmPorLitroLabel.setVisible(!esElectrico);
        kmPorLitroField.setVisible(!esElectrico);

        litrosExtraLabel.setVisible(!esElectrico);
        litrosExtraField.setVisible(!esElectrico);
    }

    private void guardarVehiculo() {
        try {
            String patente = patenteField.getText().trim();
            String nombreMarca = marcaField.getText().trim();
            String paisMarca = paisField.getText().trim();
            String modelo = modeloField.getText().trim();

            int anio = Integer.parseInt(anioField.getText().trim());
            double capacidadCarga = Double.parseDouble(capacidadCargaField.getText().trim());

            Sucursal sucursal = (Sucursal) sucursalCombo.getSelectedItem();
            String tipo = (String) tipoCombo.getSelectedItem();

            Marca marca = new Marca(nombreMarca, paisMarca);
            Vehiculo vehiculo;

            if ("ELECTRICO".equals(tipo)) {
                double kwhBase = Double.parseDouble(kwhBaseField.getText().trim());
                vehiculo = new VehiculoElectrico(patente, marca, modelo, anio, capacidadCarga, sucursal, kwhBase);
            } else {
                double kmPorLitro = Double.parseDouble(kmPorLitroField.getText().trim());
                double litrosExtra = Double.parseDouble(litrosExtraField.getText().trim());
                vehiculo = new VehiculoCombustible(patente, marca, modelo, anio, capacidadCarga, sucursal, kmPorLitro, litrosExtra);
            }

            Controlador.agregarVehiculo(vehiculo);
            JOptionPane.showMessageDialog(this, "Vehículo agregado correctamente.");
            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisá los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar el vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        patenteField.setText("");
        marcaField.setText("");
        paisField.setText("");
        modeloField.setText("");
        anioField.setText("");
        capacidadCargaField.setText("");
        kwhBaseField.setText("");
        kmPorLitroField.setText("");
        litrosExtraField.setText("");
        tipoCombo.setSelectedIndex(0);
        if (sucursalCombo.getItemCount() > 0) {
            sucursalCombo.setSelectedIndex(0);
        }
        actualizarCamposPorTipo();
    }
}