CREATE TABLE t_dvf_update AS
SELECT b.id_mutation
FROM t_dvf b
WHERE EXISTS (
    SELECT 1
    FROM t_dvf_old p
    WHERE p.id_mutation = b.id_mutation
      AND p.hash <> b.hash
);