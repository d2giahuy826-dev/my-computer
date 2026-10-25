SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Reservations'
ORDER BY ORDINAL_POSITION;

-- 1. Tạo reservation
INSERT INTO Reservations
    (ReservationID, CustomerID, ReservationTime, GuestCount)
VALUES
    (210, 7, '2025-11-02T19:15:00', 5);

-- 2. Gán bàn có TableID nhỏ nhất và sức chứa ít nhất 5 người
INSERT INTO ReservationTables (ReservationID, TableID)
SELECT
    210,
    MIN(TableID)
FROM Tables
WHERE Capacity >= 5;

-- 3. Kiểm tra kết quả
SELECT
    r.*,
    rt.TableID
FROM Reservations AS r
JOIN ReservationTables AS rt
    ON r.ReservationID = rt.ReservationID
WHERE r.ReservationID = 210;