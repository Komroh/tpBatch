CREATE TABLE t_ban_del AS
SELECT p.id
FROM t_ban_prec p
WHERE NOT EXISTS(SELECT 1 FROM t_ban b WHERE b.id = p.id);