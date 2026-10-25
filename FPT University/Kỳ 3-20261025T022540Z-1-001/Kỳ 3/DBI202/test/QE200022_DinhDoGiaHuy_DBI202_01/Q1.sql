
SELECT
   o.order_id,
   CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
   s.store_name,
   o.order_date,
   o.status AS status
FROM
   orders o
JOIN
   customers c ON o.customer_id = c.customer_id
JOIN 
   stores s ON o.store_id = s.store_id
WHERE
   o.order_date BETWEEN '2016-03-22' AND '2016-03-31'
ORDER BY
   o.order_id ASC
