CREATE OR ALTER PROCEDURE listTopArtistByYear
    @year INT
AS
BEGIN
    SET NOCOUNT ON;

    WITH ArtistStatistics AS
    (
        SELECT
            ar.Country,
            ar.ID AS ArtistID,
            ar.Name AS ArtistName,
            SUM(
                CASE
                    WHEN YEAR(pt.AddedDate) = @year THEN 1
                    ELSE 0
                END
            ) AS TotalPlaylistAdditionsInYear
        FROM Artists AS ar
        LEFT JOIN Albums AS al
            ON ar.ID = al.ArtistID
        LEFT JOIN Tracks AS t
            ON al.ID = t.AlbumID
        LEFT JOIN PlaylistTracks AS pt
            ON t.ID = pt.TrackID
        GROUP BY
            ar.Country,
            ar.ID,
            ar.Name
    ),
    RankedArtists AS
    (
        SELECT *,
            DENSE_RANK() OVER (
                PARTITION BY Country
                ORDER BY TotalPlaylistAdditionsInYear DESC
            ) AS ArtistRank
        FROM ArtistStatistics
    )
    SELECT
        Country,
        ArtistID,
        ArtistName,
        TotalPlaylistAdditionsInYear
    FROM RankedArtists
    WHERE ArtistRank = 1
    ORDER BY ArtistID;
END;
Go

EXECUTE listTopArtistByYear @year = 2023;