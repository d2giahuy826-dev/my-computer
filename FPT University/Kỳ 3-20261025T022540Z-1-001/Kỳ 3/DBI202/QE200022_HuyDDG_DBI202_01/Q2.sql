SELECT c.customer_id, c.first_name, c.last_name, c.city, c.state
FROM customers c
WHERE c.last_name LIKE 'O%' OR c.last_name LIKE N'A%'
  AND c.state LIKE N'%CA%' OR c.state LIKE N'%NY%'
ORDER BY 
  c.customer_id ASC

