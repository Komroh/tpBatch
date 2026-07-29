CREATE TABLE t_dvf_added AS
SELECT b.id_mutation
FROM t_dvf b
WHERE NOT EXISTS (
    SELECT 1
    FROM t_dvf_old p
    WHERE p.id_mutation = b.id_mutation
);