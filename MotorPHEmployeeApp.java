package pkgpublic.pkgclass.motorphemployeeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class MotorPHEmployeeApp extends JFrame {

    private final JTextField txtEmpId, txtLastName, txtFirstName, txtSss, txtPhilHealth, txtTin, txtPagIbig, txtRate, txtDays;
    private final JTextArea txtReceipt;
    private final JTable tblMasterList;
    private final DefaultTableModel tableModel;
    private final JButton btnAdd, btnUpdate, btnDelete, btnCompute, btnClear;
    private final String FILE_NAME = "employees.csv";

    public MotorPHEmployeeApp() {
        setTitle("MotorPH Payroll Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- UI Setup ---
        JPanel pnlFields = new JPanel(new GridLayout(9, 2, 8, 8));
        pnlFields.add(new JLabel(" Employee Number:")); txtEmpId = new JTextField(); pnlFields.add(txtEmpId);
        pnlFields.add(new JLabel(" Last Name:"));       txtLastName = new JTextField(); pnlFields.add(txtLastName);
        pnlFields.add(new JLabel(" First Name:"));      txtFirstName = new JTextField(); pnlFields.add(txtFirstName);
        pnlFields.add(new JLabel(" SSS Number:"));      txtSss = new JTextField(); pnlFields.add(txtSss);
        pnlFields.add(new JLabel(" PhilHealth No:"));   txtPhilHealth = new JTextField(); pnlFields.add(txtPhilHealth);
        pnlFields.add(new JLabel(" TIN:"));             txtTin = new JTextField(); pnlFields.add(txtTin);
        pnlFields.add(new JLabel(" Pag-IBIG Number:")); txtPagIbig = new JTextField(); pnlFields.add(txtPagIbig);
        pnlFields.add(new JLabel(" Rate Per Day (P):"));txtRate = new JTextField(); pnlFields.add(txtRate);
        pnlFields.add(new JLabel(" Days Worked:"));     txtDays = new JTextField(); pnlFields.add(txtDays);
        add(pnlFields, BorderLayout.WEST);

        txtReceipt = new JTextArea(15, 30);
        txtReceipt.setEditable(false);
        add(new JScrollPane(txtReceipt), BorderLayout.EAST);

        String[] columns = {"Emp ID", "Last Name", "First Name", "SSS No", "PhilHealth No", "TIN", "Pag-IBIG No", "Rate", "Days"};
        tableModel = new DefaultTableModel(columns, 0);
        tblMasterList = new JTable(tableModel);
       
        tblMasterList.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMasterList.getSelectedRow() != -1) {
                int row = tblMasterList.getSelectedRow();
                txtEmpId.setText(tableModel.getValueAt(row, 0).toString());
                txtLastName.setText(tableModel.getValueAt(row, 1).toString());
                txtFirstName.setText(tableModel.getValueAt(row, 2).toString());
                txtSss.setText(tableModel.getValueAt(row, 3).toString());
                txtPhilHealth.setText(tableModel.getValueAt(row, 4).toString());
                txtTin.setText(tableModel.getValueAt(row, 5).toString());
                txtPagIbig.setText(tableModel.getValueAt(row, 6).toString());
                txtRate.setText(tableModel.getValueAt(row, 7).toString());
                txtDays.setText(tableModel.getValueAt(row, 8).toString());
            }
        });
       
        add(new JScrollPane(tblMasterList), BorderLayout.CENTER);
        loadFromCSV();

        JPanel pnlButtons = new JPanel();
        btnAdd = new JButton("Add Record");
        btnUpdate = new JButton("Update Record");
        btnDelete = new JButton("Delete Record");
        btnCompute = new JButton("Compute Salary");
        btnClear = new JButton("Clear Fields");
       
        // Add this temporarily to your constructor for testing
        JButton btnTest = new JButton("Run Exception Test");
        btnTest.addActionListener(e -> {
        txtRate.setText("INVALID_DATA"); // Force a validation error
        executePayrollComputation();    // This will trigger the catch block
         });
       
        pnlButtons.add(btnTest);
        pnlButtons.add(btnAdd); pnlButtons.add(btnUpdate); pnlButtons.add(btnDelete);
        pnlButtons.add(btnCompute); pnlButtons.add(btnClear);
        add(pnlButtons, BorderLayout.SOUTH);

        btnCompute.addActionListener(e -> executePayrollComputation());
        btnClear.addActionListener(e -> clearInputForm());
        btnAdd.addActionListener(e -> addRecord());
        btnUpdate.addActionListener(e -> updateRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        setVisible(true);
    }

    private boolean validateInput() {
        if (txtEmpId.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty() ||
            txtFirstName.getText().trim().isEmpty() || txtRate.getText().trim().isEmpty() ||
            txtDays.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double rate = Double.parseDouble(txtRate.getText().trim());
            double days = Double.parseDouble(txtDays.getText().trim());
            if (rate < 0 || days < 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Rate and Days must be non-negative numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void executePayrollComputation() {
    try {
        // 1. Get values from UI
        double rate = Double.parseDouble(txtRate.getText().trim());
        double days = Double.parseDouble(txtDays.getText().trim());

        // 2. Perform Calculations via Module
        double gross = SalaryComputationModule.computeGrossPay(rate, days);
        double sss = SalaryComputationModule.computeSSS(gross);
        double ph = SalaryComputationModule.computePhilHealth(gross);
        double pi = SalaryComputationModule.computePagIBIG(gross);
        double totalDeductions = SalaryComputationModule.computeDeductions(sss, ph, pi);
        double tax = SalaryComputationModule.computeWithholdingTax(gross, totalDeductions);
        double net = SalaryComputationModule.computeNetPay(gross, totalDeductions, tax);

        // 3. Display in Receipt
        txtReceipt.setText("--- PAYROLL RECEIPT ---\n" +
                           "Employee: " + txtLastName.getText() + ", " + txtFirstName.getText() + "\n" +
                           "ID: " + txtEmpId.getText() + "\n\n" +
                           "Gross Salary: P" + String.format("%.2f", gross) + "\n" +
                           "SSS: P" + String.format("%.2f", sss) + "\n" +
                           "PhilHealth: P" + String.format("%.2f", ph) + "\n" +
                           "Pag-IBIG: P" + String.format("%.2f", pi) + "\n" +
                           "Withholding Tax: P" + String.format("%.2f", tax) + "\n\n" +
                           "NET PAY: P" + String.format("%.2f", net));
                           
        JOptionPane.showMessageDialog(this, "Payroll computed successfully!");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error: Please enter valid numeric values for Rate and Days.");
    }
}
    private void addRecord() {
    String newId = txtEmpId.getText().trim();
   
    // 1. Check for Duplicate ID
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        if (tableModel.getValueAt(i, 0).toString().equals(newId)) {
            JOptionPane.showMessageDialog(this, "Error: Employee ID " + newId + " already exists.");
            return; // Stop the method here so the record isn't added
        }
    }

    // 2. Add the record only if no duplicate was found
    tableModel.addRow(new Object[]{
        txtEmpId.getText(), txtLastName.getText(), txtFirstName.getText(),
        txtSss.getText(), txtPhilHealth.getText(), txtTin.getText(),
        txtPagIbig.getText(), txtRate.getText(), txtDays.getText()
    });
   
    saveToCSV();
    clearInputForm(); // Optional: clears the form after successful addition
}

    private void updateRecord() {
        int row = tblMasterList.getSelectedRow();
        if (row != -1 && validateInput()) {
            tableModel.setValueAt(txtEmpId.getText(), row, 0);
            tableModel.setValueAt(txtLastName.getText(), row, 1);
            tableModel.setValueAt(txtFirstName.getText(), row, 2);
            tableModel.setValueAt(txtSss.getText(), row, 3);
            tableModel.setValueAt(txtPhilHealth.getText(), row, 4);
            tableModel.setValueAt(txtTin.getText(), row, 5);
            tableModel.setValueAt(txtPagIbig.getText(), row, 6);
            tableModel.setValueAt(txtRate.getText(), row, 7);
            tableModel.setValueAt(txtDays.getText(), row, 8);
            saveToCSV();
            JOptionPane.showMessageDialog(this, "Record updated successfully!");
        } else if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
        }
    }

    private void deleteRecord() {
    int r = tblMasterList.getSelectedRow();
   
    // 1. Validation: Ensure a row is selected
    if (r == -1) {
        JOptionPane.showMessageDialog(this, "Please select a record to delete.");
        return;
    }
   
    // 2. Confirmation: Required by Feature 4
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
   
    if (confirm == JOptionPane.YES_OPTION) {
        // 3. Remove from UI
        tableModel.removeRow(r);
       
        // 4. Persist to CSV: Crucial for Feature 2 and 4 integration
        saveToCSV();
       
        JOptionPane.showMessageDialog(this, "Record deleted successfully.");
    }
}
    private void clearInputForm() {
        txtEmpId.setText(""); txtLastName.setText(""); txtFirstName.setText("");
        txtSss.setText(""); txtPhilHealth.setText(""); txtTin.setText("");
        txtPagIbig.setText(""); txtRate.setText(""); txtDays.setText("");
        txtReceipt.setText("");
        txtEmpId.requestFocusInWindow();
    }

    private void saveToCSV() {
    // 1. Define the file path clearly (using project root)
    java.io.File file = new java.io.File("employees.csv");
   
    // 2. Use try-with-resources to ensure the file closes automatically
    try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
       
        // 3. Loop through your table model to get the data
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(i, j);
                row.append(value != null ? value.toString() : "");
               
                // Add comma only between columns, not at the end
                if (j < tableModel.getColumnCount() - 1) {
                    row.append(",");
                }
            }
            writer.println(row.toString());
        }
       
    } catch (java.io.FileNotFoundException e) {
        // 4. Proper error handling
        javax.swing.JOptionPane.showMessageDialog(this, "Error: Could not save data. File not found: " + e.getMessage());
    }
}
    private void loadFromCSV() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tableModel.addRow(line.split(","));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MotorPHEmployeeApp::new);
    }

    private static class SalaryComputationModule {

        private static double computeGrossPay(double rate, double days) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computeSSS(double gross) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computePhilHealth(double gross) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computePagIBIG(double gross) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computeDeductions(double sss, double ph, double pi) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computeWithholdingTax(double gross, double totalDeductions) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static double computeNetPay(double gross, double totalDeductions, double tax) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public SalaryComputationModule() {
        }
    }
}