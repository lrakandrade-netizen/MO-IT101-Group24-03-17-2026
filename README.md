MotorPH Payroll Management System
A Java Swing desktop application designed to manage employee records and perform payroll computations for MotorPH employees.

The application allows users to add, update, delete, and store employee records while automatically computing payroll information such as gross salary, deductions, withholding tax, and net pay. Employee data is saved to a CSV file for persistent storage.

Features
Employee record management Add employee records Update employee records Delete employee records Employee information validation Duplicate Employee ID prevention Payroll computation Gross salary calculation SSS deduction calculation PhilHealth deduction calculation Pag-IBIG deduction calculation Withholding tax calculation Net pay calculation Payroll receipt generation CSV file storage and retrieval Employee records displayed in a JTable Clear form functionality Confirmation and error messages using dialog boxes

Technologies Used
Java Java Swing JTable and DefaultTableModel AWT Event Handling File Handling CSV Data Storage Object-Oriented Programming (OOP)

Project Structure
MotorPH-Payroll-Management-System/

├── MotorPHEmployeeApp.java

├── SalaryComputationModule.java

├── employees.csv

├── README.md

├── LICENSE

└── .gitignore

Requirements
Before running the project, make sure you have:

Java Development Kit (JDK) 8 or later
A Java IDE such as NetBeans, IntelliJ IDEA, Eclipse, or VS Code
You can check your Java installation with:

java -version
javac -version
How to Run
Option 1: Run Using an IDE
Open the project folder in your preferred Java IDE.
Open MotorPHEmployeeApp.java.
Run the file.
The MotorPH Employee App window should appear.
Option 2: Run Using the Terminal
From the project folder, compile the Java files:

javac *.java

Then run the program:

java MotorPHEmployeeApp

How to Use
Managing Employee Records 1.Enter employee information: -Employee Number -Last Name -First Name -SSS Number -PhilHealth Number -TIN -Pag-IBIG Number -Rate Per Day -Days Worked

2.Click Add Record to save a new employee record. 3.Select an existing employee record from the table to load its information into the input fields. 4.Click Update Record to save changes to the selected employee. 5.Click Delete Record to remove the selected employee record.

Computing Payroll
1.Select or enter an employee record. 2.Click Compute Salary. 3.The payroll receipt will display: -Gross Salary -SSS Deduction -PhilHealth Deduction -Pag-IBIG Deduction -Total Deductions -Withholding Tax -Net Pay

Clearing Fields
Click Clear Fields to reset all input fields and the payroll receipt.
Data Storage
Employee records are automatically saved to employees.csv.
Existing records are automatically loaded when the application starts.
Changes made through Add, Update, and Delete operations are immediately saved to the CSV file.
Notes
-Employee IDs must be numeric. -Duplicate Employee IDs are not allowed. -Rate Per Day and Days Worked must be valid positive numbers. -The application performs input validation before processing records or payroll computations. -Payroll calculations are handled through the SalaryComputationModule class.

License
This project includes a LICENSE file. See the LICENSE file for more information.
