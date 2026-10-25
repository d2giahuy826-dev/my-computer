WITH CustomerOrderAverages AS
(
    SELECT
        c.CustomerID,
        c.FullName,

        AVG(
            CASE
                WHEN o.OrderTime >= '2024-01-01'
                 AND o.OrderTime <  '2025-01-01'
                THEN o.TotalAmount
            END
        ) AS Avg2024,

        AVG(
            CASE
                WHEN o.OrderTime >= '2025-01-01'
                 AND o.OrderTime <  '2026-01-01'
                THEN o.TotalAmount
            END
        ) AS Avg2025
    FROM Customers AS c
    JOIN Orders AS o
        ON c.CustomerID = o.CustomerID
    WHERE o.Status = N'Completed'
      AND o.OrderTime >= '2024-01-01'
      AND o.OrderTime <  '2026-01-01'
    GROUP BY
        c.CustomerID,
        c.FullName
    HAVING
        SUM(CASE
                WHEN o.OrderTime >= '2024-01-01'
                 AND o.OrderTime <  '2025-01-01'
                THEN 1 ELSE 0
            END) > 0
        AND
        SUM(CASE
                WHEN o.OrderTime >= '2025-01-01'
                 AND o.OrderTime <  '2026-01-01'
                THEN 1 ELSE 0
            END) > 0
)
SELECT
    CustomerID,
    FullName,
    CAST(ROUND(Avg2024, 2) AS DECIMAL(18,2))
        AS AvgTotalOrderValue2024,
    CAST(ROUND(Avg2025, 2) AS DECIMAL(18,2))
        AS AvgTotalOrderValue2025,
    CONCAT(
        CAST(
            ROUND(
                (Avg2025 - Avg2024)
                / NULLIF(Avg2024, 0) * 100.0,
                2
            ) AS DECIMAL(10,2)
        ),
        N' %'
    ) AS PercentageChange
FROM CustomerOrderAverages
ORDER BY CustomerID ASC;