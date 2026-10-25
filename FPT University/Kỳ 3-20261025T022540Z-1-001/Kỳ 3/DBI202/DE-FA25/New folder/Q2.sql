SELECT * FROM Employees
WHERE Role IN (N'Chef', N'Manager') AND YEAR(HireDate) = 2021