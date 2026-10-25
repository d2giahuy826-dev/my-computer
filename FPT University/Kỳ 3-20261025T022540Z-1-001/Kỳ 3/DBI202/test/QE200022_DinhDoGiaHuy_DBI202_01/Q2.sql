
SELECT
  product_id,
  product_name,
  price
FROM
  products
WHERE
  price < (SELECT AVG(price) FROM products)
ORDER BY
  product_id ASC
