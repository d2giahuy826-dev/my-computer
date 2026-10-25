SELECT
  YEAR(o.order_date) AS order_year,
  SUM(oi.quantity * oi.price * (1- oi.discount)) AS total_revenue
FROM 
   orders o
JOIN
   order_items oi ON o.order_id = oi.order_id
GROUP BY
   YEAR(o.order_date);