SELECT
    pt.PlaylistID,
    t.ID AS TrackID,
    t.Title AS TrackTitle,
    pt.AddedDate
FROM PlaylistTracks AS pt
JOIN Tracks AS t
    ON pt.TrackID = t.ID
WHERE pt.PlaylistID = 10
  AND pt.AddedDate = '2026-03-10';