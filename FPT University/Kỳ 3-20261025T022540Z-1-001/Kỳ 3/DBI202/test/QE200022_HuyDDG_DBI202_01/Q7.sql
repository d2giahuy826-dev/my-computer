CREATE PROCEDURE p1
  @staff_id INT,
  @NumberOfOrders INT OUTPUT
AS
BEGIN
    SELECT @NumberOfOrders = COUNT(order_id)
	FROM orders
	WHERE staff_id = @staff_id;
END;


