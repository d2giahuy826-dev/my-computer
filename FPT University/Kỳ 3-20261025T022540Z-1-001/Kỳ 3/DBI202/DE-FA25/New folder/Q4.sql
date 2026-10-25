WITH SeptemberReservations AS
(
    SELECT
        rt.TableID,
        r.CustomerID
    FROM ReservationTables AS rt
    JOIN Reservations AS r
        ON rt.ReservationID = r.ReservationID
    WHERE r.ReservationTime >= '2025-09-01'
      AND r.ReservationTime <  '2025-10-01'
)
SELECT
    t.TableID,
    t.Capacity,
    t.Location,
    c.CustomerID,
    c.FullName
FROM Tables AS t
LEFT JOIN SeptemberReservations AS sr
    ON t.TableID = sr.TableID
LEFT JOIN Customers AS c
    ON sr.CustomerID = c.CustomerID
WHERE t.Location LIKE N'%Khu A%'
   OR t.Location LIKE N'%Khu B%'
   OR t.Location LIKE N'%Khu C%'
ORDER BY
    t.Capacity DESC,
    t.Location ASC;