CREATE OR ALTER TRIGGER tr_insertPlaylist
ON Playlists
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @CurrentDateTime DATETIME = GETDATE();

    INSERT INTO Playlists (Title, UserID, CreatedAt)
    SELECT
        i.Title,
        i.UserID,
        CASE
            WHEN i.CreatedAt >= u.RegistrationDate
             AND i.CreatedAt <= @CurrentDateTime
                THEN i.CreatedAt
            ELSE @CurrentDateTime
        END AS CreatedAt
    FROM inserted AS i
    INNER JOIN Users AS u
        ON i.UserID = u.ID;
END;
GO


INSERT INTO Playlists (Title, UserID, CreatedAt)
VALUES
    (N'Playlist Test 1', 10, '2025-10-12'),
    (N'Playlist Test 2', 50, '2025-11-12'),
    (N'Playlist Test 3', 12, '2020-05-12'),
    (N'Playlist Test 4', 13, '2031-02-15');

SELECT *
FROM Playlists
WHERE Title LIKE N'Playlist Test %'
ORDER BY Title;