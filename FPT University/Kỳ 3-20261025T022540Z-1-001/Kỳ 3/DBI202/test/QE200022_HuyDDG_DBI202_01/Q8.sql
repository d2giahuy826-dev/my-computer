CREATE TRIGGER T1
ON stocks 
AFTER DELETE
AS
BEGIN
   SELECT
     d.product_id,
	 p.product_name,
	 d.store_id,
	 s.store_name,
	 d.quantity
FROM deleted d
JOIN products p ON d.product_id = p.product_id
JOIN stores S ON d.store_id = s.store_id;
END


