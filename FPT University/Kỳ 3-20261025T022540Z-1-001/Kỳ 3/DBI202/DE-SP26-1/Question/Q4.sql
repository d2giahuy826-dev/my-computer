SELECT 
   ar.ID AS ArtistID,
   ar.Name,
   ar.Gender,
   ar.Country,
   COUNT(DISTINCT al.ID) AS NumberOfAlbums,
   COUNT(DISTINCT CASE WHEN g.Name = N'Rock' THEN t.ID END) AS NumberOfTracks
FROM Artists AS ar
LEFT JOIN Albums AS al ON ar.ID = al.ArtistID AND al.ReleaseYear BETWEEN 2020 AND 2024
LEFT JOIN Tracks AS t ON al.ID = t.AlbumID 
LEFT JOIN Genres AS g ON t.GenreID = g.ID
GROUP BY ar.ID, ar.Name, ar.Gender, ar.Country
ORDER BY ar.ID;


