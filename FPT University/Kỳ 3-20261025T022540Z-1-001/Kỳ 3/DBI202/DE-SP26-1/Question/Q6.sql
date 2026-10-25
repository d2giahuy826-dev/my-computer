WITH AlbumStatistics AS
(
    SELECT
        ar.Country,
        al.ID AS AlbumID,
        al.Title AS AlbumTitle,
        COUNT(pt.TrackID) AS TotalPlaylistAdditions,
        SUM(
            CASE
                WHEN YEAR(pt.AddedDate) = 2023 THEN 1
                ELSE 0
            END
        ) AS TotalPlaylistAdditionsIn2023
    FROM Artists AS ar
    INNER JOIN Albums AS al
        ON ar.ID = al.ArtistID
    LEFT JOIN Tracks AS t
        ON al.ID = t.AlbumID
    LEFT JOIN PlaylistTracks AS pt
        ON t.ID = pt.TrackID
    GROUP BY
        ar.Country,
        al.ID,
        al.Title
),
RankedAlbums AS
(
    SELECT *,
        DENSE_RANK() OVER (
            PARTITION BY Country
            ORDER BY TotalPlaylistAdditions DESC
        ) AS TotalRank,

        DENSE_RANK() OVER (
            PARTITION BY Country
            ORDER BY TotalPlaylistAdditionsIn2023 DESC
        ) AS RankIn2023
    FROM AlbumStatistics
)
SELECT
    Country,
    AlbumID,
    AlbumTitle,
    TotalPlaylistAdditions,
    TotalPlaylistAdditionsIn2023
FROM RankedAlbums
WHERE TotalRank = 1
  AND RankIn2023 = 1
ORDER BY
    Country ASC,
    AlbumTitle ASC;