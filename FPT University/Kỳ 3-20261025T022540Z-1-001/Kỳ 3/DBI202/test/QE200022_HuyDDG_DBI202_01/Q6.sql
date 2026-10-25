SELECT
   c.customer_id,
   CONCAT(c.first_name, ' ', c.last_name) AS custommer_name,
   COUNT(o.order_id) AS total_orders
FROM 
  customers c
JOIN 
   orders o ON c.customer_id = o.customer_id
GROUP BY
   c.customer_id,
   c.first_name,
   c.last_name
HAVING 
   COUNT(o.order_id) >= 2;