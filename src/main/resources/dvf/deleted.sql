CREATE TABLE t_dvf_del AS
SELECT p.id_mutation
FROM t_dvf_old p
WHERE NOT EXISTS(SELECT 1 FROM t_dvf b WHERE b.id_mutation = p.id_mutation);