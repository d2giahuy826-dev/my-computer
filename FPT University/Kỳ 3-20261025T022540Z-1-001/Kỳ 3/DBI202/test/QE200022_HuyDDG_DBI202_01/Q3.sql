SELECT 
      p.product_id,
	  p.product_name,
	  p.Brand,
	  p.Category,
	  s.store_id,
	  s.store_name,
	  st.quantity
FROM 
     products p

JOIN 
     stocks st ON p.product_id = st.product_id
JOIN 
     stores s ON st.store_id = s.store_id
WHERE
    p.Category = 'Electric Bikes'
	AND p.Brand = 'Trek';