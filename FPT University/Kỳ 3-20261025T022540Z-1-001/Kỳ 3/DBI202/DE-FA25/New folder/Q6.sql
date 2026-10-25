WITH ItemSales AS
(
    SELECT
        mi.ItemID,
        mi.ItemName,
        mi.Category,
        COALESCE(
            SUM(
                CASE
                    WHEN o.Status = N'Completed'
                     AND o.OrderTime >= '2023-01-01'
                     AND o.OrderTime <  '2024-01-01'
                    THEN od.Quantity * mi.Price
                    ELSE 0
                END
            ), 0
        ) AS TotalAmount
    FROM MenuItems AS mi
    LEFT JOIN OrderDetails AS od
        ON mi.ItemID = od.ItemID
    LEFT JOIN Orders AS o
        ON od.OrderID = o.OrderID
    GROUP BY
        mi.ItemID,
        mi.ItemName,
        mi.Category
)
SELECT
    ItemID,
    ItemName,
    Category,
    TotalAmount
FROM ItemSales
WHERE TotalAmount < (
    SELECT AVG(CAST(TotalAmount AS DECIMAL(18,2)))
    FROM ItemSales
)
ORDER BY
    TotalAmount ASC,
    ItemID ASC;