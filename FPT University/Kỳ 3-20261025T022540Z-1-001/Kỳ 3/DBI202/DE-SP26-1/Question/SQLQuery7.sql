WITH AlbumTrackCounts AS
(
    SELECT
        ar.ID AS ArtistID,
        ar.Name AS ArtistName,
        al.ID AS AlbumID,
        al.Title AS AlbumTitle,
        COUNT(t.ID) AS NumberOfTracks
    FROM Artists AS ar
    INNER JOIN Albums AS al
        ON ar.ID = al.ArtistID
    LEFT JOIN Tracks AS t
        ON al.ID = t.AlbumID
    WHERE ar.Country = N'Canada'
    GROUP BY
        ar.ID,
        ar.Name,
        al.ID,
        al.Title
),
RankedAlbums AS
(
    SELECT *,
        DENSE_RANK() OVER (
            PARTITION BY ArtistID
            ORDER BY NumberOfTracks DESC
        ) AS AlbumRank
    FROM AlbumTrackCounts
)
SELECT
    ArtistID,
    ArtistName,
    AlbumID,
    AlbumTitle,
    NumberOfTracks
FROM RankedAlbums
WHERE AlbumRank = 1
ORDER BY ArtistID, AlbumID;