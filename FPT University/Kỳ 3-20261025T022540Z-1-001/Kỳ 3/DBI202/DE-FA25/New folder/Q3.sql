SELECT c.CustomerID, c.FullName, r.ReservationTime, t.TableID, t.Location
FROM Customers c
JOIN Reservations r ON c.CustomerID = r.CustomerID
JOIN ReservationTables re ON r.ReservationID = re.ReservationID
JOIN Tables t ON t.TableID= re.TableID
WHERE YEAR(r.ReservationTime) = 2025 AND t.Location LIKE N'%Khu A%'