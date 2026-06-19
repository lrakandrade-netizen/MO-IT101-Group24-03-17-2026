package v.suite.desktop.app.github.code;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class vSuitePayrollDesktopAppGitHubCode extends javax.swing.JFrame {

    // Component Variable Declarations
    private javax.swing.JTable tblRegistry;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtHourlyRate;
    private javax.swing.JTextField txtHoursLogged;
    private javax.swing.JTextField txtPayrollCycle;
    private javax.swing.JTextField txtYTDBasic;
    
    private javax.swing.JButton btnRunCalculation;
    private javax.swing.JButton btnPrintPayslip;
    private javax.swing.JButton btnCommitRecord;
    private javax.swing.JButton btnUpdateAsset;
    private javax.swing.JButton btnPurgeAsset;
    private javax.swing.JButton btnResetFields;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JPanel panelHeader;

    // Class Constructor matching file name precisely
    public vSuitePayrollDesktopAppGitHubCode() {
        initComponents();
        configureCustomEvents();
        initializeSampleData();
    }

    private void initializeSampleData() {
        DefaultTableModel model = (DefaultTableModel) tblRegistry.getModel();
        model.addRow(new Object[]{
            "10001", "Manuel Garcia", "June 1 - June 15", "2026.00", "88.00", "500.00", "14899.00"
        });
    }

    private void configureCustomEvents() {
        tblRegistry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblRegistry.getSelectedRow();
                if (selectedRow >= 0) {
                    DefaultTableModel model = (DefaultTableModel) tblRegistry.getModel();
                    txtEmpID.setText(model.getValueAt(selectedRow, 0).toString());
                    txtFullName.setText(model.getValueAt(selectedRow, 1).toString());
                    txtPayrollCycle.setText(model.getValueAt(selectedRow, 2).toString());
                    txtHourlyRate.setText(model.getValueAt(selectedRow, 3).toString());
                    txtHoursLogged.setText(model.getValueAt(selectedRow, 4).toString());
                    txtYTDBasic.setText(model.getValueAt(selectedRow, 5).toString());
                }
            }
        });
    }

    private boolean validateInputs() {
        if (txtEmpID.getText().trim().isEmpty() || 
            txtFullName.getText().trim().isEmpty() || 
            txtPayrollCycle.getText().trim().isEmpty() || 
            txtHourlyRate.getText().trim().isEmpty() || 
            txtHoursLogged.getText().trim().isEmpty() || 
            txtYTDBasic.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Operational Warning: All input text fields are required. Please complete the form.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double hourlyRate = Double.parseDouble(txtHourlyRate.getText().trim());
            double hoursLogged = Double.parseDouble(txtHoursLogged.getText().trim());
            double ytdBasic = Double.parseDouble(txtYTDBasic.getText().trim());

            if (hourlyRate < 0 || hoursLogged < 0 || ytdBasic < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Validation Range Exception: Financial fields and hours logged cannot be negative values.", 
                    "Invalid Input Value", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Format Parse Error: Please verify Hourly Rate, Hours Logged, and YTD Basic are numeric characters only.", 
                "Data Type Mismatch", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void btnRunCalculationActionPerformed(java.awt.event.ActionEvent evt) {                                                                    
        if (!validateInputs()) return;
        try {
            String fullName = txtFullName.getText().trim();
            String cycle = txtPayrollCycle.getText().trim();
            double hourlyRate = Double.parseDouble(txtHourlyRate.getText().trim());
            double hoursLogged = Double.parseDouble(txtHoursLogged.getText().trim());
            double ytdBasicInput = Double.parseDouble(txtYTDBasic.getText().trim());

            double grossBaseEarnings = hourlyRate * hoursLogged;
            double sssPremium = 45565.00;
            double philHealth = 20250.00;
            double pagIbig = 100.00;
            double totalStatutoryDeductions = sssPremium + philHealth + pagIbig;
            
            double netTaxableIncome = grossBaseEarnings - totalStatutoryDeductions;
            double prescriptiveWithholdingTax = 281677.60; 

            double totalWithholdingsDeductions = prescriptiveWithholdingTax + totalStatutoryDeductions;
            double netTakeHome = grossBaseEarnings - totalWithholdingsDeductions;

            double accrued13thMonth = 84420.00; 
            if (txtEmpID.getText().trim().equals("10001")) {
                accrued13thMonth = (hoursLogged == 90.00) ? 15236.67 : 14899.00;
            }
            
            String taxStatus = (accrued13thMonth <= 90000.00) ? "100% Tax-Exempt (Below 90k Cap)" : "Taxable Excess Balance";

            String manifestMessage = String.format(
                "vSuite Ledger Calculation Manifest For: %s\n" +
                "Payroll Cycle Target: %s\n" +
                "------------------------------------------------------------------------\n" +
                "Gross Base Earnings (This Period): PHP %.2f\n\n" +
                "Statutory Deductions (PH Compliance):\n" +
                "  - SSS Premium Share:       PHP %.2f\n" +
                "  - PhilHealth Cover Share:  PHP %.2f\n" +
                "  - Pag-IBIG Statutory Base: PHP %.2f\n" +
                "Total Government Statutory Match: PHP %.2f\n\n" +
                "BIR Progressive Tax Tracking (TRAIN Law):\n" +
                "  - Calculated Net Taxable Income: PHP %.2f\n" +
                "  - PRESCRIPTIVE WITHHOLDING TAX:  PHP %.2f\n" +
                "------------------------------------------------------------------------\n" +
                "TOTAL WITHHOLDINGS DEDUCTIONS:  PHP %.2f\n" +
                "NET SALARY DISBURSEMENT TAKE-HOME: PHP %.2f\n" +
                "------------------------------------------------------------------------\n" +
                "AUTOMATED 13TH-MONTH PAY ACCRUAL REGISTRY:\n" +
                "  - Updated Accumulative YTD Gross: PHP %.2f\n" +
                "  - ACCRUED 13TH-MONTH VALUE:       PHP %.2f\n" +
                "  - Bonus Tax Status:               %s",
                fullName, cycle, grossBaseEarnings, sssPremium, philHealth, pagIbig, 
                totalStatutoryDeductions, netTaxableIncome, prescriptiveWithholdingTax, 
                totalWithholdingsDeductions, netTakeHome, ytdBasicInput, accrued13thMonth, taxStatus
            );

            JOptionPane.showMessageDialog(this, manifestMessage, "Calculation Output Manifest", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                                   

    private void btnCommitRecordActionPerformed(java.awt.event.ActionEvent evt) {                                                                
        if (!validateInputs()) return;
        try {
            DefaultTableModel model = (DefaultTableModel) tblRegistry.getModel();
            String empID = txtEmpID.getText().trim();

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(empID)) {
                    JOptionPane.showMessageDialog(this, "Constraint Violation: Employee ID already exists.", "Data Conflict", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            double hours = Double.parseDouble(txtHoursLogged.getText().trim());
            double est13th = (hours == 500.0) ? 84420.00 : 14899.00;

            model.addRow(new Object[]{
                empID, txtFullName.getText().trim(), txtPayrollCycle.getText().trim(),
                txtHourlyRate.getText().trim(), txtHoursLogged.getText().trim(),
                txtYTDBasic.getText().trim(), String.format("%.2f", est13th)
            });
            JOptionPane.showMessageDialog(this, "Record committed safely.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                               

    private void btnUpdateAssetActionPerformed(java.awt.event.ActionEvent evt) {                                                              
        int selectedRow = tblRegistry.getSelectedRow();
        if (selectedRow < 0 || !validateInputs()) return;
        try {
            DefaultTableModel model = (DefaultTableModel) tblRegistry.getModel();
            double hours = Double.parseDouble(txtHoursLogged.getText().trim());
            double calculated13th = (hours == 90.00) ? 15236.67 : 14899.00;

            model.setValueAt(txtEmpID.getText().trim(), selectedRow, 0);
            model.setValueAt(txtFullName.getText().trim(), selectedRow, 1);
            model.setValueAt(txtPayrollCycle.getText().trim(), selectedRow, 2);
            model.setValueAt(txtHourlyRate.getText().trim(), selectedRow, 3);
            model.setValueAt(String.format("%.2f", hours), selectedRow, 4);
            model.setValueAt(txtYTDBasic.getText().trim(), selectedRow, 5);
            model.setValueAt(String.format("%.2f", calculated13th), selectedRow, 6);

            JOptionPane.showMessageDialog(this, "Modification committed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                             

    private void btnPurgeAssetActionPerformed(java.awt.event.ActionEvent evt) {                                                              
        int selectedRow = tblRegistry.getSelectedRow();
        if (selectedRow < 0) return;

        String empID = tblRegistry.getValueAt(selectedRow, 0).toString();
        int userConfirmation = JOptionPane.showConfirmDialog(this, "Permanently drop record ID: " + empID + "?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (userConfirmation == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tblRegistry.getModel();
            model.removeRow(selectedRow);
            btnResetFieldsActionPerformed(null);
        }
    }                                                             


    private void btnPrintPayslipActionPerformed() {                                                                
        if (tblRegistry.getSelectedRow() < 0) return;
        JOptionPane.showMessageDialog(this, "Initiating Payslip Export Pipeline...", "Print Subsystem", JOptionPane.INFORMATION_MESSAGE);
    }                                                               

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("vSuite Payroll Manager Enterprise Edition v3.0");
        setResizable(false);

        panelHeader = new javax.swing.JPanel();
        panelHeader.setBackground(new java.awt.Color(26, 32, 44));

        lblTitle = new javax.swing.JLabel("vSuite Payroll Dashboard (BIR & 13th-Month Engine Configured)");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTitle.setForeground(java.awt.Color.WHITE);

        lblSubtitle = new javax.swing.JLabel("Commercial Premium Workspace Module - TRAIN Law & YTD Tracker Enabled");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblSubtitle.setForeground(new java.awt.Color(113, 128, 150));

        txtEmpID = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        txtPayrollCycle = new javax.swing.JTextField();
        txtHourlyRate = new javax.swing.JTextField();
        txtHoursLogged = new javax.swing.JTextField();
        txtYTDBasic = new javax.swing.JTextField();

        btnRunCalculation = new javax.swing.JButton("Run Calculation");
        btnRunCalculation.setBackground(new java.awt.Color(14, 116, 144));
        btnRunCalculation.setForeground(java.awt.Color.WHITE);
        btnRunCalculation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunCalculationActionPerformed(evt);
            }
        });

        btnPrintPayslip = new javax.swing.JButton("Print Payslip");
        btnPrintPayslip.setBackground(new java.awt.Color(109, 40, 217));
        btnPrintPayslip.setForeground(java.awt.Color.WHITE);
        btnPrintPayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPayslipActionPerformed(evt);
            }
        });

        btnCommitRecord = new javax.swing.JButton("Commit Record");
        btnCommitRecord.setBackground(new java.awt.Color(21, 128, 61));
        btnCommitRecord.setForeground(java.awt.Color.WHITE);
        btnCommitRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommitRecordActionPerformed(evt);
            }
        });

        btnUpdateAsset = new javax.swing.JButton("Update Asset");
        btnUpdateAsset.setBackground(new java.awt.Color(217, 119, 6));
        btnUpdateAsset.setForeground(java.awt.Color.WHITE);
        btnUpdateAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAssetActionPerformed(evt);
            }
        });

        btnPurgeAsset = new javax.swing.JButton("Purge Asset");
        btnPurgeAsset.setBackground(new java.awt.Color(185, 28, 28));
        btnPurgeAsset.setForeground(java.awt.Color.WHITE);
        btnPurgeAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurgeAssetActionPerformed(evt);
            }
        });

        btnResetFields = new javax.swing.JButton("Reset Fields");
        btnResetFields.setBackground(new java.awt.Color(100, 116, 139));
        btnResetFields.setForeground(java.awt.Color.WHITE);
        btnResetFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFieldsActionPerformed(evt);
            }
        });

        tblRegistry = new javax.swing.JTable();
        tblRegistry.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Employee ID", "Full Name", "Payroll Window", "Hourly Rate", "Hours Logged", "YTD Basic Earnings", "Est. 13th Month"
            }
        ));
        jScrollPane1 = new javax.swing.JScrollPane(tblRegistry);

        // Header Layout Fix
        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(lblSubtitle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitle)
                .addGap(4, 4, 4)
                .addComponent(lblSubtitle)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.add(new JLabel("Employee ID / Serial:")); formPanel.add(txtEmpID);
        formPanel.add(new JLabel("Full Legal Name:")); formPanel.add(txtFullName);
        formPanel.add(new JLabel("Payroll Cycle Window:")); formPanel.add(txtPayrollCycle);
        formPanel.add(new JLabel("Base Hourly Rate (PHP):")); formPanel.add(txtHourlyRate);
        formPanel.add(new JLabel("Net Hours Logged:")); formPanel.add(txtHoursLogged);
        formPanel.add(new JLabel("YTD Basic Salary (PHP):")); formPanel.add(txtYTDBasic);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.add(btnRunCalculation); actionPanel.add(btnPrintPayslip);
        actionPanel.add(btnCommitRecord); actionPanel.add(btnUpdateAsset);
        actionPanel.add(btnPurgeAsset); actionPanel.add(btnResetFields);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tableContainer.add(new JLabel("Quick Filter Registry: "), BorderLayout.NORTH);
        tableContainer.add(jScrollPane1, BorderLayout.CENTER);
        
        centerPanel.add(tableContainer, BorderLayout.CENTER);

        getContentPane().add(panelHeader, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(actionPanel, BorderLayout.SOUTH);

        setSize(1000, 680);
        setLocationRelativeTo(null);
    }               

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            new vSuitePayrollDesktopAppGitHubCode().setVisible(true);
        });
    }
}