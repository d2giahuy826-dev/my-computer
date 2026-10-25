CREATE TABLE Universities
(
    universityID INT PRIMARY KEY,
    name NVARCHAR(200),
    street NVARCHAR(50),
    city NVARCHAR(50)
);

CREATE TABLE Departments
(
    deptID INT PRIMARY KEY,
    name NVARCHAR(100)
);

CREATE TABLE Employees
(
    empID INT PRIMARY KEY,
    FullName NVARCHAR(500),
    gender NVARCHAR(10),
    universityID INT,
    
    CONSTRAINT FK_Employees_Universities
        FOREIGN KEY (universityID)
        REFERENCES Universities(universityID)
);

CREATE TABLE EmployeesPhone
(
    empID INT,
    phone NVARCHAR(20),

    CONSTRAINT PK_EmployeesPhone
        PRIMARY KEY (empID, phone),

    CONSTRAINT FK_EmployeesPhone_Employees
        FOREIGN KEY (empID)
        REFERENCES Employees(empID)
);

CREATE TABLE Professors
(
    empID INT PRIMARY KEY,
    field NVARCHAR(100),
    deptID INT,

    CONSTRAINT FK_Professors_Employees
        FOREIGN KEY (empID)
        REFERENCES Employees(empID),

    CONSTRAINT FK_Professors_Departments
        FOREIGN KEY (deptID)
        REFERENCES Departments(deptID)
);