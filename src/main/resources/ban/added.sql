CREATE TABLE t_ban_added AS
SELECT b.id
FROM t_ban b
WHERE NOT EXISTS (
    SELECT 1
    FROM t_ban_prec p
    WHERE p.id = b.id
);