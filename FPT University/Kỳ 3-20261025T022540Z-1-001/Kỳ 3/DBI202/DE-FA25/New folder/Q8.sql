CREATE OR ALTER PROCEDURE MonthlySalesSummary
    @year INT
AS
BEGIN
    SET NOCOUNT ON;

    WITH Months AS
    (
        SELECT MonthNumber
        FROM (VALUES
            (1), (2), (3), (4), (5), (6),
            (7), (8), (9), (10), (11), (12)
        ) AS m(MonthNumber)
    )
    SELECT
        CONCAT(m.MonthNumber, N'/', @year) AS [Month],

        COUNT(DISTINCT o.OrderID) AS NumberOfOrders,

        CAST(
            COALESCE(
                SUM(od.Quantity * mi.Price),
                0
            ) AS DECIMAL(18,2)
        ) AS TotalRevenue
    FROM Months AS m
    LEFT JOIN Orders AS o
        ON o.OrderTime >= DATEFROMPARTS(@year, m.MonthNumber, 1)
       AND o.OrderTime < DATEADD(
            MONTH,
            1,
            DATEFROMPARTS(@year, m.MonthNumber, 1)
       )
       AND o.Status = N'Completed'
    LEFT JOIN OrderDetails AS od
        ON o.OrderID = od.OrderID
    LEFT JOIN MenuItems AS mi
        ON od.ItemID = mi.ItemID
    GROUP BY
        m.MonthNumber
    ORDER BY
        m.MonthNumber;
END;
GO

EXECUTE MonthlySalesSummary 2023;