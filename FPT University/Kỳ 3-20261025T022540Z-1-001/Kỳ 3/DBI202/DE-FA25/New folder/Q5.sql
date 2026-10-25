SELECT
    c.CustomerID,
    c.FullName,
    COUNT(o.OrderID) AS NumberOfOrders,
    COALESCE(SUM(o.TotalAmount), 0) AS TotalAmount
FROM Customers AS c
LEFT JOIN Orders AS o
    ON c.CustomerID = o.CustomerID
   AND o.OrderTime >= '2025-01-01'
   AND o.OrderTime <  '2026-01-01'
   AND o.Status = N'Completed'
WHERE c.FullName LIKE N'Nguy?n%'
   OR c.FullName LIKE N'Bùi%'
GROUP BY
    c.CustomerID,
    c.FullName
ORDER BY c.CustomerID;