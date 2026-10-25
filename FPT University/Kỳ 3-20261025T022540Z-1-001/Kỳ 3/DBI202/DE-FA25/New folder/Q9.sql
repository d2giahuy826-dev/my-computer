CREATE OR ALTER TRIGGER trg_UpdateTotalAmount
ON OrderDetails
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    ;WITH AffectedOrders AS
    (
        SELECT OrderID FROM inserted
        UNION
        SELECT OrderID FROM deleted
    )
    UPDATE o
    SET o.TotalAmount =
        COALESCE(
            (
                SELECT SUM(od.Quantity * od.Price)
                FROM OrderDetails AS od
                WHERE od.OrderID = o.OrderID
            ),
            0
        )
    FROM Orders AS o
    INNER JOIN AffectedOrders AS ao
        ON o.OrderID = ao.OrderID;
END;
GO