SELECT
    al.Title AS AlbumTitle,
    al.ReleaseYear,
    t.ID AS TrackID,
    t.Title AS TrackTitle,
    g.Name AS GenreName,
    p.Title AS PlaylistTitle,
    pt.AddedDate
FROM Tracks AS t
JOIN Albums AS al
    ON t.AlbumID = al.ID
JOIN Genres AS g
    ON t.GenreID = g.ID
LEFT JOIN PlaylistTracks AS pt
    ON t.ID = pt.TrackID
LEFT JOIN Playlists AS p
    ON pt.PlaylistID = p.ID
WHERE al.ReleaseYear > 2020
  AND g.Name = N'Rock'
ORDER BY
    p.Title DESC,
    t.Title ASC;