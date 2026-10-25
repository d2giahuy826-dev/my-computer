SELECT 
   ID,
   Title,
   Duration,
   Language,
   AlbumID,
   GenreID
FROM Tracks
WHERE Title LIKE N'%love%'
ORDER BY ID;