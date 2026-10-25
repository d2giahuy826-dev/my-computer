SELECT t.ID, t.Title AS TrackTitle,al.Title AS AlbumTitle,al.ReleaseYear,ar.Name AS ArtistName,ar.Gender,ar.Country
FROM Tracks AS t 
JOIN Albums AS al ON t.AlbumID = al.ID
JOIN Artists AS ar ON al.ArtistID = ar.ID
WHERE al.ReleaseYear = 2020
   AND ar.Gender = N'Female'
   AND ar.Country = N'USA'
ORDER BY t.ID;

     