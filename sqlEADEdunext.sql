USE Sem4DB;
GO

-- Bệnh nhân
CREATE TABLE EADPatients (
    PatientID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100) NOT NULL,
	Address NVARCHAR(100),
    Email NVARCHAR(100) UNIQUE,
    Phone NVARCHAR(20) NOT NULL,
    Gender NVARCHAR(10),
    DateOfBirth DATE,
);

-- Bác sĩ
CREATE TABLE EADDoctors (
    DoctorID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) NOT NULL UNIQUE,
    Specialization NVARCHAR(100),
    Phone NVARCHAR(20),
	DateOfBirth DATE,
);

-- Lịch hẹn
CREATE TABLE EADAppointments (
    AppointmentID INT PRIMARY KEY IDENTITY(1,1),
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentDate DATE NOT NULL,
    AppointmentTime TIME NOT NULL,
    Status NVARCHAR(20) DEFAULT 'Scheduled',  -- Scheduled, Cancelled, Completed
    Notes NVARCHAR(500),

    FOREIGN KEY (PatientID) REFERENCES EADPatients(PatientID),
    FOREIGN KEY (DoctorID) REFERENCES EADDoctors(DoctorID)
);

-- Hồ sơ khám bệnh
CREATE TABLE EADMedicalRecords (
    RecordID INT PRIMARY KEY IDENTITY(1,1),
    AppointmentID INT NOT NULL,
    Symptoms NVARCHAR(255),	--	Triệu chứng
    Diagnosis NVARCHAR(255),	-- Chẩn đoán
    CreatedAt DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (AppointmentID) REFERENCES EADAppointments(AppointmentID) ON DELETE CASCADE
);

-- Bảng thuốc
CREATE TABLE EADMedicines (
    MedicineID INT PRIMARY KEY IDENTITY(1,1),
    MedicineName NVARCHAR(100) NOT NULL,
    Unit NVARCHAR(20),         -- Viên, Gói, Chai,...
    Price DECIMAL(10,2)
);

-- Đơn thuốc
CREATE TABLE EADPrescriptions (
    PrescriptionID INT PRIMARY KEY IDENTITY(1,1),
    RecordID INT NOT NULL,
    MedicineID INT NOT NULL,
	Quantity INT NOT NULL,   -- Số lượng 
    Dosage NVARCHAR(100),    -- Liều dùng
    Duration INT,  -- Số ngày

    FOREIGN KEY (RecordID) REFERENCES EADMedicalRecords(RecordID) ON DELETE CASCADE,
    FOREIGN KEY (MedicineID) REFERENCES EADMedicines(MedicineID)
);

-- Thanh toán
CREATE TABLE EADBills (
    BillID INT PRIMARY KEY IDENTITY(1,1),
    AppointmentID INT NOT NULL,
    Amount DECIMAL(10, 2),
    PaymentDate DATETIME DEFAULT GETDATE(),
    PaymentMethod NVARCHAR(50),
    Status NVARCHAR(50),

    FOREIGN KEY (AppointmentID) REFERENCES EADAppointments(AppointmentID) ON DELETE CASCADE
);

-- Trigger tính Amount và tự động tạo Bill
CREATE OR ALTER TRIGGER trg_CalculateBillAmount
ON EADPrescriptions
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    MERGE EADBills AS target
    USING (
        SELECT a.AppointmentID, SUM(m.Price * p.Quantity) AS TotalAmount
        FROM EADAppointments a
        JOIN EADMedicalRecords mr ON a.AppointmentID = mr.AppointmentID
        JOIN EADPrescriptions p ON mr.RecordID = p.RecordID
        JOIN EADMedicines m ON p.MedicineID = m.MedicineID
        GROUP BY a.AppointmentID
    ) AS source
    ON target.AppointmentID = source.AppointmentID

    WHEN MATCHED THEN
        UPDATE SET Amount = source.TotalAmount

    WHEN NOT MATCHED THEN
        INSERT (AppointmentID, Amount, PaymentMethod, Status)
        VALUES (source.AppointmentID, source.TotalAmount, 'Chua xac dinh', 'Chua thanh toan');
END



-------- INSERT -----------

INSERT INTO EADPatients (FullName, Email, Phone, Gender, DateOfBirth)
VALUES
('Nguyen Van An', 'vana@example.com', '0909123456', 'Nam', '1990-05-12'),
('Tran Thi Bich', 'thib@example.com', '0911223344', 'Nu', '1985-08-21'),
('Le Van Cuong', 'le.c@example.com', '0933445566', 'Nam', '1978-11-30'),
('Pham Thi Doanh', 'pham.d@example.com', '0922334455', 'Nu', '1995-04-10'),
('Do Van E', 'doe@example.com', '0988776655', 'Nam', '2000-02-17');


INSERT INTO EADDoctors (FullName, Email, Specialization, Phone)
VALUES
('Tran Van Binh', 'binh@abc.com', 'Noi tong quat', '0909888777'),
('Nguyen Thi Chi', 'chi@abc.com', 'Nhi khoa', '0911445566'),
('Hoang Van Dung', 'dung@abc.com', 'Da lieu', '0933557799'),
('Vu Thi En', 'en@abc.com', 'Mat', '0966554433'),
('Le Van Hanh', 'hanh@abc.com', 'Noi tong quat', '0909111222'),
('Pham Thi Hoa', 'hoa@abc.com', 'Noi tong quat', '0909222333'),
('Ngo Van Kien', 'kien@abc.com', 'Noi tong quat', '0909333444'),
('Nguyen Van Lam', 'lam@abc.com', 'Nhi khoa', '0911555666'),
('Tran Thi Mai', 'mai@abc.com', 'Nhi khoa', '0911666777'),
('Bui Van Nam', 'nam@abc.com', 'Nhi khoa', '0911777888'),
('Dang Thi Oanh', 'oanh@abc.com', 'Da lieu', '0933666888'),
('Trinh Van Phuc', 'phuc@abc.com', 'Da lieu', '0933777999'),
('Ly Thi Quyen', 'quyen@abc.com', 'Da lieu', '0933888000'),
('Do Van Son', 'son@abc.com', 'Mat', '0966666777'),
('Nguyen Thi Trang', 'trang@abc.com', 'Mat', '0966777888'),
('Phan Van Vinh', 'vinh@abc.com', 'Mat', '0966888999');

INSERT INTO EADAppointments (PatientID, DoctorID, AppointmentDate, AppointmentTime, Status, Notes)
VALUES
(1, 1, '2025-04-06', '08:00:00', 'Scheduled', 'Kham lan dau'),
(2, 2, '2025-04-06', '09:00:00', 'Scheduled', 'Tai kham'),
(3, 3, '2025-04-06', '10:00:00', 'Scheduled', 'Kham da lieu'),
(4, 1, '2025-04-06', '11:00:00', 'Scheduled', 'Kiem tra huyet ap'),
(5, 4, '2025-04-06', '14:00:00', 'Scheduled', 'Kiem tra mat'),
(2, 1, '2025-04-07', '08:00:00', 'Scheduled', 'Kham tong quat'),
(1, 2, '2025-04-07', '09:00:00', 'Scheduled', 'Kham nhi cho con'),
(3, 4, '2025-04-07', '10:00:00', 'Scheduled', 'Kiem tra mat dinh ky'),
(4, 3, '2025-04-07', '11:00:00', 'Scheduled', 'Da lieu dinh ky'),
(5, 1, '2025-04-07', '14:00:00', 'Scheduled', 'Theo doi huyet ap'),
(1, 3, '2025-04-08', '08:00:00', 'Scheduled', 'Da lieu cho nguoi gia'),
(2, 2, '2025-04-08', '09:00:00', 'Scheduled', 'Tai kham ho hap'),
(3, 1, '2025-04-08', '10:00:00', 'Scheduled', 'Kiem tra suc khoe tong quat'),
(4, 4, '2025-04-08', '11:00:00', 'Scheduled', 'Kiem tra thi luc'),
(5, 2, '2025-04-08', '14:00:00', 'Scheduled', 'Kham dinh ky cho tre'),
(1, 4, '2025-04-08', '15:00:00', 'Scheduled', 'Tu van mat');

INSERT INTO EADMedicalRecords (AppointmentID, Symptoms, Diagnosis)
VALUES
(1, 'Dau dau, met moi', 'Cam lanh'),
(2, 'Dau bung, buon non', 'Roi loan tieu hoa'),
(3, 'Ho kho, ngat mui', 'Viem hong cap'),
(4, 'Dau lung keo dai', 'Dau cot song'),
(5, 'Chay mui, ho co dom', 'Cam cum'),
(6, 'Dau co, mo hoi nhieu', 'Cang co vai gay');

INSERT INTO EADMedicines (MedicineName, Unit, Price)
VALUES
('Paracetamol 500mg', 'Hop', 2000),
('Amoxicillin 500mg', 'Hop', 3000),
('Vitamin C 500mg', 'Chai', 1500),
('Smecta', 'Chai', 6000),
('Decolgen', 'Chai', 4000),
('Salonpas', 'Hop', 5000);

INSERT INTO EADPrescriptions (RecordID, MedicineID, Quantity, Dosage, Duration)
VALUES 
(1, 1, 2, '2 lan/ngay', 3),
(1, 3, 2, '1 lan/ngay', 3),
(2, 4, 3, '2 lan/ngay', 2),
(3, 2, 1, '3 lan/ngay', 4),
(3, 3, 2, '1 lan/ngay', 3),
(4, 6, 1, '2 mieng/ngay', 3),
(5, 5, 3, '2 lan/ngay', 4),
(5, 1, 1, '2 lan/ngay', 2),
(6, 6, 2, '2 lan/ngay', 3);
