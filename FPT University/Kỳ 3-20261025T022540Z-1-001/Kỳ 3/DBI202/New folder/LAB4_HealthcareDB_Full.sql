
  
CREATE DATABASE HealthcareDB;

USE HealthcareDB;
GO


-- 1. DEPARTMENT
CREATE TABLE DEPARTMENT (
    department_id      INT IDENTITY(1,1) PRIMARY KEY,
    department_name    NVARCHAR(100)  NOT NULL UNIQUE,
    description         NVARCHAR(255) NULL,
    location             NVARCHAR(100) NULL
);
GO

-- 2. EMPLOYEE
CREATE TABLE EMPLOYEE (
    employee_id     INT IDENTITY(1,1) PRIMARY KEY,
    employee_code   VARCHAR(20)  NOT NULL UNIQUE,
    fname           VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    phone           VARCHAR(20)  NULL,
    hire_date       DATE         NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'Active'
                        CHECK (status IN ('Active','Inactive','On Leave')),
    employee_type   VARCHAR(30)  NOT NULL
                        CHECK (employee_type IN ('Doctor','Nurse','Receptionist','Admin','Staff')),
    department_id   INT NOT NULL,
    CONSTRAINT FK_Employee_Department FOREIGN KEY (department_id)
        REFERENCES DEPARTMENT(department_id)
);
GO

-- 3. DOCTOR (IS_A specialization of EMPLOYEE, 1-1)
CREATE TABLE DOCTOR (
    doctor_id        INT IDENTITY(1,1) PRIMARY KEY,
    lisence_number   VARCHAR(30)    NOT NULL UNIQUE,
    specialization   VARCHAR(100)   NULL,
    consulation_fee  DECIMAL(10,2)  NULL,
    employee_id      INT NOT NULL UNIQUE,
    CONSTRAINT FK_Doctor_Employee FOREIGN KEY (employee_id)
        REFERENCES EMPLOYEE(employee_id)
);
GO

-- 4. PATIENT
CREATE TABLE PATIENT (
    patient_id            INT IDENTITY(1,1) PRIMARY KEY,
    medical_record_number VARCHAR(30)   NOT NULL UNIQUE,
    fname                 NVARCHAR(50)  NOT NULL,
    lname                 NVARCHAR(50)  NOT NULL,
    date_of_birth         DATE          NOT NULL,
    gender                NVARCHAR(10)  NOT NULL,
    phone                 VARCHAR(20)   NULL,
    email                 VARCHAR(100)  NULL,
    blood_type            VARCHAR(5)    NULL
        CHECK (blood_type IN ('A+','A-','B+','B-','AB+','AB-','O+','O-')),
    emergency_name        VARCHAR(100)  NULL,
    emergency_contact     VARCHAR(20)   NULL,
    status                VARCHAR(20)   NOT NULL DEFAULT 'Active'
        CHECK (status IN ('Active','Inactive')),
    insurance_number      VARCHAR(30)   NULL UNIQUE,
    CONSTRAINT CK_Patient_ContactRequired
        CHECK (phone IS NOT NULL OR email IS NOT NULL)
);
GO

-- 5. APPOINTMENT
CREATE TABLE APPOINTMENT (
    appointment_id    INT IDENTITY(1,1) PRIMARY KEY,
    appointment_date  DATE NOT NULL,
    start_time        TIME NOT NULL,
    end_time          TIME NOT NULL,
    status            VARCHAR(20) NOT NULL DEFAULT 'Scheduled'
                        CHECK (status IN ('Scheduled','Completed','Cancelled','No-show')),
    appointment_type  VARCHAR(30) NOT NULL,
    reason            NVARCHAR(255) NULL,
    doctor_id         INT NOT NULL,
    patient_id        INT NOT NULL,
    CONSTRAINT FK_Appointment_Doctor FOREIGN KEY (doctor_id)
        REFERENCES DOCTOR(doctor_id),
    CONSTRAINT FK_Appointment_Patient FOREIGN KEY (patient_id)
        REFERENCES PATIENT(patient_id)
);
GO

-- 6. DOCTOR_SCHEDULE
CREATE TABLE DOCTOR_SCHEDULE (
    schedule_id       INT IDENTITY(1,1) PRIMARY KEY,
    date_of_week      VARCHAR(10) NOT NULL,
    start_time        TIME NOT NULL,
    end_time          TIME NOT NULL,
    across_midnight   BIT NOT NULL DEFAULT 0,
    slot_duration     INT NOT NULL,          -- minutes
    available         BIT NOT NULL DEFAULT 1,
    doctor_id         INT NOT NULL,
    CONSTRAINT FK_Schedule_Doctor FOREIGN KEY (doctor_id)
        REFERENCES DOCTOR(doctor_id)
);
GO

-- 7. CONSULTATION
CREATE TABLE CONSULTATION (
    consultation_id     INT IDENTITY(1,1) PRIMARY KEY,
    consultation_date   DATE NOT NULL,
    symptom             NVARCHAR(255) NULL,
    examination_note    NVARCHAR(MAX) NULL,
    vital_signs         NVARCHAR(255) NULL,
    follow_up_required  BIT NOT NULL DEFAULT 0,
    follow_up_date      DATE NULL,
    appointment_id      INT NOT NULL UNIQUE,
    CONSTRAINT FK_Consultation_Appointment FOREIGN KEY (appointment_id)
        REFERENCES APPOINTMENT(appointment_id)
);
GO

-- 8. MEDICAL_RECORD
CREATE TABLE MEDICAL_RECORD (
    medical_record_id   INT IDENTITY(1,1) PRIMARY KEY,
    chief_complaint      NVARCHAR(255) NULL,
    medical_history       NVARCHAR(MAX) NULL,
    family_history        NVARCHAR(MAX) NULL,
    current_medication    NVARCHAR(255) NULL,
    height                 DECIMAL(5,2) NULL,   -- cm
    weight                 DECIMAL(5,2) NULL,   -- kg
    blood_presure          VARCHAR(20) NULL,
    access_lever            VARCHAR(20) NULL,    -- record access level
    last_update_by          INT NULL,             -- employee who last updated
    consultation_id         INT NOT NULL,
    CONSTRAINT FK_MedicalRecord_Consultation FOREIGN KEY (consultation_id)
        REFERENCES CONSULTATION(consultation_id),
    CONSTRAINT FK_MedicalRecord_Employee FOREIGN KEY (last_update_by)
        REFERENCES EMPLOYEE(employee_id)
);
GO

-- 9. DIAGNOSIS
CREATE TABLE DIAGNOSIS (
    diagnosis_id         INT IDENTITY(1,1) PRIMARY KEY,
    icd_code              VARCHAR(20) NULL,
    symptom_description   NVARCHAR(255) NULL,
    diagnosis_type        VARCHAR(30) NULL,
    severity               VARCHAR(20) NULL
                            CHECK (severity IN ('Low','Medium','High','Critical')),
    consultation_id        INT NOT NULL,
    CONSTRAINT FK_Diagnosis_Consultation FOREIGN KEY (consultation_id)
        REFERENCES CONSULTATION(consultation_id)
);
GO

-- 10. PRESCRIPTION
CREATE TABLE PRESCRIPTION (
    prescription_id   INT IDENTITY(1,1) PRIMARY KEY,
    prescription_date  DATE NOT NULL,
    status             VARCHAR(20) NOT NULL DEFAULT 'Active',
	      CHECK (status IN ('Active','Completed','Cancelled')),
    pharmacy_note       NVARCHAR(255) NULL,
    consultation_id     INT NOT NULL,
    CONSTRAINT FK_Prescription_Consultation FOREIGN KEY (consultation_id)
        REFERENCES CONSULTATION(consultation_id)
);
GO

-- 11. PRESCRIPTION_DETAIL
CREATE TABLE PRESCRIPTION_DETAIL (
    detail_id          INT IDENTITY(1,1) PRIMARY KEY,
    medication_name     NVARCHAR(100) NOT NULL,
    dosage               VARCHAR(50) NOT NULL,
    frequency            VARCHAR(50) NOT NULL,
    duration             VARCHAR(50) NOT NULL,
    quantity              INT NOT NULL,
    route                 VARCHAR(30) NOT NULL,
	    CHECK (route IN ('Oral','Topical','Injection','Intravenous','Inhalation','Sublingual','Rectal','Ophthalmic')),
    instruction           NVARCHAR(255) NOT NULL,
    prescription_id       INT NOT NULL,
    CONSTRAINT FK_PrescriptionDetail_Prescription FOREIGN KEY (prescription_id)
        REFERENCES PRESCRIPTION(prescription_id)
);
GO

-- 12. BILLING
CREATE TABLE BILLING (
    billing_id        INT IDENTITY(1,1) PRIMARY KEY,
    invoice_number       VARCHAR(30)   NOT NULL UNIQUE,
    consultation_fee     DECIMAL(10,2) NOT NULL DEFAULT 0,
    medication_fee       DECIMAL(10,2) NOT NULL DEFAULT 0,
    lab_test_fee         DECIMAL(10,2) NOT NULL DEFAULT 0,
    other_charges        DECIMAL(10,2) NOT NULL DEFAULT 0,
    discount             DECIMAL(4,3) NOT NULL DEFAULT 0,
	         CHECK (discount >= 0 AND discount <= 1),
    payment_status       VARCHAR(20)   NOT NULL DEFAULT 'Unpaid'
             CHECK (payment_status IN ('Unpaid','Paid','Partially Paid','Refunded')),
    billing_date         DATE NOT NULL,
    consultation_id      INT  NOT NULL,
    CONSTRAINT FK_Billing_Consultation FOREIGN KEY (consultation_id)
        REFERENCES CONSULTATION(consultation_id)
);
GO

-- 13. NOTIFICATION
CREATE TABLE NOTIFICATION (
    notification_id    INT IDENTITY(1,1) PRIMARY KEY,
    title                NVARCHAR(100) NOT NULL,
    message              NVARCHAR(255) NOT NULL,
    notification_type    VARCHAR(30) NOT NULL,
    channel              VARCHAR(30) NOT NULL,
    is_read              BIT NOT NULL DEFAULT 0,   -- is_read
    sent_at              DATETIME NOT NULL DEFAULT GETDATE(),
    employee_id          INT NOT NULL,
    patient_id           INT NOT NULL,
    CONSTRAINT FK_Notification_Employee FOREIGN KEY (employee_id)
        REFERENCES EMPLOYEE(employee_id),
    CONSTRAINT FK_Notification_Patient FOREIGN KEY (patient_id)
        REFERENCES PATIENT(patient_id)
);
GO


/* ============================================================
   PART 3: SAMPLE DATA
   Thu tu insert theo dependency (bang cha truoc, bang con sau)
   Khong khai bao ID (IDENTITY tu tang tu 1, dung thu tu insert)
   ============================================================ */

-- ===== 1. DEPARTMENT (5 rows) =====
INSERT INTO DEPARTMENT (department_name, description, location) VALUES
(N'Khoa Noi', N'Kham va dieu tri noi khoa tong quat', N'Toa A - Tang 1'),
(N'Khoa Ngoai', N'Phau thuat va chan thuong chinh hinh', N'Toa A - Tang 2'),
(N'Khoa Nhi', N'Kham va dieu tri cho tre em', N'Toa B - Tang 1'),
(N'Khoa San', N'San khoa va phu khoa', N'Toa B - Tang 2'),
(N'Khoa Cap cuu', N'Tiep nhan va xu ly cap cuu 24/7', N'Toa C - Tang tret');
GO

-- ===== 2. EMPLOYEE (10 rows) : id 1-10 =====
INSERT INTO EMPLOYEE (employee_code, fname, last_name, email, phone, hire_date, status, employee_type, department_id) VALUES
('EMP001', 'An',    'Nguyen Van',  'an.nguyen@healthcare.vn',    '0901000001', '2019-03-01', 'Active', 'Doctor',       1),
('EMP002', 'Binh',  'Tran Thi',    'binh.tran@healthcare.vn',    '0901000002', '2018-06-15', 'Active', 'Doctor',       2),
('EMP003', 'Cuong', 'Le Van',      'cuong.le@healthcare.vn',     '0901000003', '2020-01-10', 'Active', 'Doctor',       3),
('EMP004', 'Dung',  'Pham Thi',    'dung.pham@healthcare.vn',    '0901000004', '2017-09-20', 'Active', 'Doctor',       4),
('EMP005', 'Em',    'Hoang Van',   'em.hoang@healthcare.vn',     '0901000005', '2021-02-05', 'Active', 'Doctor',       5),
('EMP006', 'Hoa',   'Vu Thi',      'hoa.vu@healthcare.vn',       '0901000006', '2019-07-01', 'Active', 'Nurse',        1),
('EMP007', 'Khoa',  'Dang Van',    'khoa.dang@healthcare.vn',    '0901000007', '2020-04-12', 'Active', 'Nurse',        2),
('EMP008', 'Lan',   'Bui Thi',     'lan.bui@healthcare.vn',      '0901000008', '2021-08-01', 'Active', 'Receptionist', 1),
('EMP009', 'Minh',  'Ngo Van',     'minh.ngo@healthcare.vn',     '0901000009', '2016-11-11', 'Active', 'Admin',        1),
('EMP010', 'Nga',   'Do Thi',      'nga.do@healthcare.vn',       '0901000010', '2022-03-01', 'On Leave', 'Staff',     3);
GO

-- ===== 3. DOCTOR (5 rows) : id 1-5, ung voi employee 1-5 =====
INSERT INTO DOCTOR (lisence_number, specialization, consulation_fee, employee_id) VALUES
('BS-0001', N'Noi tong quat',        200000, 1),
('BS-0002', N'Ngoai chan thuong',    300000, 2),
('BS-0003', N'Nhi khoa',             150000, 3),
('BS-0004', N'San phu khoa',         250000, 4),
('BS-0005', N'Cap cuu',              350000, 5);
GO

-- ===== 4. PATIENT (8 rows) : id 1-8 =====
INSERT INTO PATIENT (medical_record_number, fname, lname, date_of_birth, gender, phone, email, blood_type, emergency_name, emergency_contact, status, insurance_number) VALUES
('MRN0001', N'Mai',   N'Nguyen Thi', '1990-05-12', N'Nu',  '0912000001', 'mai.nguyen@gmail.com', 'A+',  N'Nguyen Van Bo', '0912000011', 'Active', 'INS0001'),
('MRN0002', N'Nam',   N'Tran Van',   '1985-11-02', N'Nam', '0912000002', 'nam.tran@gmail.com',   'O+',  N'Tran Thi Ba',   '0912000012', 'Active', 'INS0002'),
('MRN0003', N'Oanh',  N'Le Thi',     '1998-01-20', N'Nu',  '0912000003', NULL,                    'B+',  N'Le Van Ba',     '0912000013', 'Active', 'INS0003'),
('MRN0004', N'Phuc',  N'Pham Van',   '1975-07-08', N'Nam', '0912000004', 'phuc.pham@gmail.com',  'AB+', N'Pham Thi Ha',   '0912000014', 'Active', 'INS0004'),
('MRN0005', N'Quyen', N'Hoang Thi',  '2001-09-30', N'Nu',  NULL,          'quyen.hoang@gmail.com','A-',  N'Hoang Van Y',   '0912000015', 'Active', 'INS0005'),
('MRN0006', N'Son',   N'Vu Van',     '1965-03-17', N'Nam', '0912000006', 'son.vu@gmail.com',     'O-',  N'Vu Thi Kim',    '0912000016', 'Active', 'INS0006'),
('MRN0007', N'Thu',   N'Dang Thi',   '1993-12-25', N'Nu',  '0912000007', 'thu.dang@gmail.com',   'B-',  N'Dang Van Loi',  '0912000017', 'Active', 'INS0007'),
('MRN0008', N'Tung',  N'Bui Van',    '2010-04-04', N'Nam', '0912000008', NULL,                    'A+',  N'Bui Thi Nga',   '0912000018', 'Active', 'INS0008');
GO

-- ===== 5. APPOINTMENT (10 rows) : id 1-10 =====
INSERT INTO APPOINTMENT (appointment_date, start_time, end_time, status, appointment_type, reason, doctor_id, patient_id) VALUES
('2026-07-01', '08:00', '08:30', 'Completed', 'Kham benh',   N'Dau bung',           1, 1),
('2026-07-01', '09:00', '09:30', 'Completed', 'Tai kham',    N'Theo doi huyet ap',  1, 2),
('2026-07-02', '08:30', '09:00', 'Completed', 'Kham benh',   N'Chan thuong dau goi',2, 3),
('2026-07-02', '10:00', '10:30', 'Completed', 'Phau thuat',  N'Gay xuong tay',      2, 4),
('2026-07-03', '08:00', '08:30', 'Completed', 'Kham benh',   N'Sot cao',            3, 5),
('2026-07-03', '09:30', '10:00', 'Completed', 'Kham benh',   N'Ho keo dai',         3, 6),
('2026-07-04', '08:00', '08:30', 'Completed', 'Kham thai',   N'Kham thai dinh ky',  4, 7),
('2026-07-04', '10:00', '10:30', 'Completed', 'Kham benh',   N'Dau bung duoi',      4, 8),
('2026-07-05', '13:00', '13:30', 'Scheduled', 'Cap cuu',     N'Tai nan giao thong', 5, 1),
('2026-07-05', '14:00', '14:30', 'Cancelled', 'Tai kham',    N'Benh nhan huy lich', 5, 2);
GO

-- ===== 6. DOCTOR_SCHEDULE (10 rows: 2 slot / bac si) =====
INSERT INTO DOCTOR_SCHEDULE (date_of_week, start_time, end_time, across_midnight, slot_duration, available, doctor_id) VALUES
(N'Monday',    '08:00', '12:00', 0, 30, 1, 1),
(N'Wednesday', '08:00', '12:00', 0, 30, 1, 1),
(N'Tuesday',   '08:00', '12:00', 0, 30, 1, 2),
(N'Thursday',  '13:00', '17:00', 0, 30, 1, 2),
(N'Monday',    '13:00', '17:00', 0, 20, 1, 3),
(N'Friday',    '08:00', '12:00', 0, 20, 1, 3),
(N'Tuesday',   '13:00', '17:00', 0, 30, 1, 4),
(N'Thursday',  '08:00', '12:00', 0, 30, 1, 4),
(N'Sunday',    '20:00', '08:00', 1, 30, 1, 5),
(N'Saturday',  '20:00', '08:00', 1, 30, 1, 5);
GO

-- ===== 7. CONSULTATION (8 rows) : ung voi 8 appointment da Completed (id 1-8) =====
INSERT INTO CONSULTATION (consultation_date, symptom, examination_note, vital_signs, follow_up_required, follow_up_date, appointment_id) VALUES
('2026-07-01', N'Dau bung am i',  N'Bung mem, an khong dau chan',       N'HA 120/80, Nhiet 37.0', 0, NULL,         1),
('2026-07-01', N'Theo doi HA',    N'Huyet ap on dinh',                  N'HA 130/85, Nhiet 36.8', 1, '2026-08-01', 2),
('2026-07-02', N'Dau khop goi',   N'Sung nhe khop goi trai',            N'HA 118/76, Nhiet 36.9', 1, '2026-07-16', 3),
('2026-07-02', N'Gay xuong canh tay', N'X-quang xac nhan gay kin',      N'HA 125/80, Nhiet 37.1', 1, '2026-07-09', 4),
('2026-07-03', N'Sot 39 do',      N'Hong do, amidan sung',              N'HA 110/70, Nhiet 39.0', 0, NULL,         5),
('2026-07-03', N'Ho keo dai 2 tuan', N'Phoi co ran am nhe',             N'HA 115/75, Nhiet 37.3', 1, '2026-07-17', 6),
('2026-07-04', N'Kham thai dinh ky', N'Thai 24 tuan, phat trien binh thuong', N'HA 112/72, Nhiet 36.7', 1, '2026-08-04', 7),
('2026-07-04', N'Dau bung duoi',  N'Nghi ngo viem phu khoa',            N'HA 118/78, Nhiet 37.2', 1, '2026-07-11', 8);
GO

-- ===== 8. MEDICAL_RECORD (8 rows) =====
INSERT INTO MEDICAL_RECORD (chief_complaint, medical_history, family_history, current_medication, height, weight, blood_presure, access_lever, last_update_by, consultation_id) VALUES
(N'Dau bung', N'Khong co tien su benh ly',  N'Khong', N'Khong',                165, 55, '120/80', 'Standard', 1, 1),
(N'Theo doi HA', N'Cao huyet ap 2 nam',      N'Bo bi cao huyet ap', N'Amlodipine 5mg', 170, 70, '130/85', 'Standard', 1, 2),
(N'Dau khop goi', N'Chan thuong the thao',   N'Khong', N'Khong', 175, 68, '118/76', 'Standard', 2, 3),
(N'Gay xuong', N'Tai nan xe may',            N'Khong', N'Paracetamol', 160, 60, '125/80', 'Standard', 2, 4),
(N'Sot cao', N'Khong co tien su benh ly',    N'Khong', N'Khong', 155, 48, '110/70', 'Standard', 3, 5),
(N'Ho keo dai', N'Hen suyen nhe',            N'Me bi hen suyen', N'Ventolin', 168, 58, '115/75', 'Standard', 3, 6),
(N'Kham thai', N'Mang thai lan 1',           N'Khong', N'Vitamin tong hop', 158, 62, '112/72', 'Restricted', 4, 7),
(N'Dau bung duoi', N'Khong co tien su benh ly', N'Khong', N'Khong', 162, 50, '118/78', 'Restricted', 4, 8);
GO

-- ===== 9. DIAGNOSIS (8 rows) =====
INSERT INTO DIAGNOSIS (icd_code, symptom_description, diagnosis_type, severity, consultation_id) VALUES
('K30',  N'Kho tieu chuc nang',        'Primary',   'Low',      1),
('I10',  N'Tang huyet ap vo can',       'Primary',   'Medium',   2),
('S83',  N'Bong gan khop goi',          'Primary',   'Medium',   3),
('S42',  N'Gay xuong canh tay',         'Primary',   'High',     4),
('J02',  N'Viem hong cap',              'Primary',   'Low',      5),
('J45',  N'Hen suyen',                  'Chronic',   'Medium',   6),
('Z34',  N'Theo doi thai ky binh thuong','Primary',  'Low',      7),
('N94',  N'Viem phu khoa nghi ngo',     'Primary',   'Medium',   8);
GO

-- ===== 10. PRESCRIPTION (6 rows) : cho consultation 1,2,3,4,5,6 =====
INSERT INTO PRESCRIPTION (prescription_date, status, pharmacy_note, consultation_id) VALUES
('2026-07-01', 'Active',    N'Uong sau an',              1),
('2026-07-01', 'Active',    N'Uong buoi sang',           2),
('2026-07-02', 'Active',    N'Uong khi dau',             3),
('2026-07-02', 'Active',    N'Uong dung gio',            4),
('2026-07-03', 'Completed', N'Da phat thuoc',            5),
('2026-07-03', 'Active',    N'Xit khi kho tho',          6);
GO

-- ===== 11. PRESCRIPTION_DETAIL (10 rows) =====
INSERT INTO PRESCRIPTION_DETAIL (medication_name, dosage, frequency, duration, quantity, route, instruction, prescription_id) VALUES
(N'Omeprazole',    '20mg',  N'1 lan/ngay', N'7 ngay',  7,  'Oral', N'Uong truoc an sang', 1),
(N'Amlodipine',    '5mg',   N'1 lan/ngay', N'30 ngay', 30, 'Oral', N'Uong buoi sang',     2),
(N'Paracetamol',   '500mg', N'3 lan/ngay', N'5 ngay',  15, 'Oral', N'Uong khi dau',       3),
(N'Ibuprofen',     '400mg', N'2 lan/ngay', N'5 ngay',  10, 'Oral', N'Uong sau an',        3),
(N'Paracetamol',   '500mg', N'3 lan/ngay', N'7 ngay',  21, 'Oral', N'Uong dung gio',      4),
(N'Amoxicillin',   '500mg', N'2 lan/ngay', N'10 ngay', 20, 'Oral', N'Uong sau an',        4),
(N'Paracetamol',   '500mg', N'3 lan/ngay', N'3 ngay',  9,  'Oral', N'Uong khi sot',       5),
(N'Ventolin',      '100mcg','Khi can',      N'30 ngay', 1,  'Inhalation', N'Xit khi kho tho', 6),
(N'Prednisolone',  '5mg',   '1 lan/ngay',   N'5 ngay',  5,  'Oral', N'Uong buoi sang',    6),
(N'Vitamin C',     '500mg', '1 lan/ngay',   N'30 ngay', 30, 'Oral', N'Uong sau an sang',  6);
GO

-- ===== 12. BILLING (8 rows) =====
INSERT INTO BILLING (invoice_number, consultation_fee, medication_fee, lab_test_fee, other_charges, discount, payment_status, billing_date, consultation_id) VALUES
('INV0001', 200000, 50000,  0,      0, 0.00, 'Paid',           '2026-07-01', 1),
('INV0002', 200000, 80000,  100000, 0, 0.05, 'Paid',           '2026-07-01', 2),
('INV0003', 300000, 60000,  150000, 0, 0.00, 'Unpaid',         '2026-07-02', 3),
('INV0004', 300000, 200000, 500000, 0, 0.10, 'Partially Paid', '2026-07-02', 4),
('INV0005', 150000, 30000,  50000,  0, 0.00, 'Paid',           '2026-07-03', 5),
('INV0006', 150000, 90000,  80000,  0, 0.00, 'Unpaid',         '2026-07-03', 6),
('INV0007', 250000, 40000,  120000, 0, 0.00, 'Paid',           '2026-07-04', 7),
('INV0008', 250000, 0,      100000, 0, 0.00, 'Unpaid',         '2026-07-04', 8);
GO

-- ===== 13. NOTIFICATION (10 rows) =====
INSERT INTO NOTIFICATION (title, message, notification_type, channel, is_read, sent_at, employee_id, patient_id) VALUES
(N'Nhac lich hen', N'Ban co lich hen vao 08:00 ngay 01/07/2026', 'Reminder', 'SMS',   1, '2026-06-30 09:00', 8, 1),
(N'Nhac lich hen', N'Ban co lich hen vao 09:00 ngay 01/07/2026', 'Reminder', 'SMS',   1, '2026-06-30 09:05', 8, 2),
(N'Ket qua kham',  N'Ket qua kham cua ban da san sang',           'Result',   'Email', 0, '2026-07-01 15:00', 1, 1),
(N'Nhac tai kham',  N'Vui long tai kham vao 01/08/2026',           'Reminder', 'SMS',   0, '2026-07-01 16:00', 1, 2),
(N'Nhac lich hen', N'Ban co lich hen vao 08:30 ngay 02/07/2026', 'Reminder', 'SMS',   1, '2026-07-01 09:00', 8, 3),
(N'Canh bao huy lich', N'Lich hen ngay 05/07/2026 da bi huy',      'Alert',    'App',   0, '2026-07-04 10:00', 8, 2),
(N'Nhac uong thuoc', N'Nho uong thuoc dung gio theo don',          'Reminder', 'App',   0, '2026-07-02 08:00', 6, 3),
(N'Ket qua xet nghiem', N'Ket qua xet nghiem mau da co',           'Result',   'Email', 0, '2026-07-03 14:00', 9, 5),
(N'Nhac lich hen', N'Ban co lich hen vao 08:00 ngay 04/07/2026', 'Reminder', 'SMS',   1, '2026-07-03 09:00', 8, 7),
(N'Nhac tai kham', N'Vui long tai kham theo lich hen bac si',      'Reminder', 'Email', 0, '2026-07-04 17:00', 4, 8);
GO


