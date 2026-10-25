SELECT 
   o.order_id,
   o.order_date,
   c.customer_id,
   c.first_name AS customer_first_name,
   c.last_name AS customer_last_name,
   s.store_id,
   s.store_name
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN stores s ON o.store_id = s.store_id 
WHERE s.store_name = 'Santa Cruz Bikes'
  AND YEAR(o.order_date) >= 2017
 

