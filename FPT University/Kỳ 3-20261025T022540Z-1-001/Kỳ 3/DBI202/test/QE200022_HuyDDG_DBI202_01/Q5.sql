SELECT 
   sf.staff_id,
   sf.first_name,
   sf.last_name,
   sf.store_id,
   st.store_name
FROM
   staffs sf
JOIN 
  stores st ON sf.store_id = st.store_id
WHERE
   sf.staff_id NOT IN ( SELECT manager_id
                        FROM staffs
						WHERE manager_id IS NOT NULL);