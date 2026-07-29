CREATE TABLE t_ban_update AS
SELECT b.id
FROM t_ban b
WHERE EXISTS (
    SELECT 1
    FROM t_ban_prec p
    WHERE p.id = b.id
      AND p.hash <> b.hash
);