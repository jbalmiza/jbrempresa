BEGIN;

DELETE FROM personas;

WITH nuevas(per_id,dom_id,nombre,apellido1,apellido2,documento,nacimiento,telefono,email) AS (VALUES
 (1,1,'Alejandro','Moreno','Ruiz','25673418L','1985-03-14'::date,'600100001','alejandro.moreno@example.com'),
 (2,2,'Lucía','Navarro','Santos','34892157R','1991-07-22'::date,'600100002','lucia.navarro@example.com'),
 (3,3,'Daniel','Romero','Vega','51734682W','1979-11-05'::date,'600100003','daniel.romero@example.com'),
 (4,4,'Carmen','Ortega','Molina','28945173P','1988-01-18'::date,'600100004','carmen.ortega@example.com'),
 (5,5,'Javier','Castro','Iglesias','43678219S','1983-09-27'::date,'600100005','javier.castro@example.com'),
 (6,6,'Elena','Vargas','Campos','36521948K','1994-04-11'::date,'600100006','elena.vargas@example.com'),
 (7,7,'Pablo','Serrano','Lozano','59281746M','1976-12-30'::date,'600100007','pablo.serrano@example.com'),
 (8,8,'Marta','Delgado','Prieto','47361825T','1990-06-09'::date,'600100008','marta.delgado@example.com'),
 (9,9,'Sergio','Herrera','Cano','31865924J','1986-02-23'::date,'600100009','sergio.herrera@example.com'),
 (10,10,'Natalia','Gil','Ramos','68423195A','1996-10-16'::date,'600100010','natalia.gil@example.com'),
 (11,11,'Álvaro','Méndez','Fuentes','42793518D','1982-05-07'::date,'600100011','alvaro.mendez@example.com'),
 (12,12,'Sara','Reyes','Blanco','53917642F','1989-08-25'::date,'600100012','sara.reyes@example.com'),
 (13,13,'Miguel','Cabrera','Pastor','29684371N','1978-03-19'::date,'600100013','miguel.cabrera@example.com'),
 (14,14,'Laura','Vidal','Peña','75132864G','1993-01-04'::date,'600100014','laura.vidal@example.com'),
 (15,15,'Rubén','Marín','Soler','38276491B','1984-07-13'::date,'600100015','ruben.marin@example.com'),
 (16,16,'Irene','Campos','Nieto','61542938H','1997-09-21'::date,'600100016','irene.campos@example.com'),
 (17,17,'Adrián','Sáez','Pardo','29417653C','1981-11-29'::date,'600100017','adrian.saez@example.com'),
 (18,18,'Beatriz','León','Díaz','46372815E','1992-04-02'::date,'600100018','beatriz.leon@example.com')
)
INSERT INTO personas(per_id,per_id_his,cli_id,dom_id,per_tip_mov,per_cau_mov,per_tip_per,per_tip_doc,per_doc,per_nom_com,per_nom,per_ape1,per_ape2,per_fec_nac,per_tel,per_ema,per_cox,per_coy,per_hus,per_act,per_usu_mov,per_fec_mov)
SELECT n.per_id,1,1,n.dom_id,'A','Carga de personas de prueba','FISICA','DNI',n.documento,
       concat_ws(' ',n.nombre,n.apellido1,n.apellido2),n.nombre,n.apellido1,n.apellido2,n.nacimiento,n.telefono,n.email,
       d.dom_cox,d.dom_coy,d.dom_hus,TRUE,'carga_prueba',CURRENT_TIMESTAMP
FROM nuevas n JOIN domicilios d ON d.cli_id=1 AND d.dom_id=n.dom_id AND d.dom_act=TRUE;

INSERT INTO personas(per_id,per_id_his,cli_id,dom_id,per_tip_mov,per_cau_mov,per_tip_per,per_tip_doc,per_doc,per_raz_soc_cor,per_raz_soc_lar,per_nom_com,per_tel,per_ema,per_cox,per_coy,per_hus,per_act,per_usu_mov,per_fec_mov)
SELECT 19,1,1,19,'A','Carga de personas de prueba','JURIDICA','C.I.F.','B93625147','Transportes Costa Sur','Transportes Costa Sur, Sociedad Limitada','B93625147 - Transportes Costa Sur, Sociedad Limitada','600100019','administracion@costasur.example.com',dom_cox,dom_coy,dom_hus,TRUE,'carga_prueba',CURRENT_TIMESTAMP FROM domicilios WHERE cli_id=1 AND dom_id=19 AND dom_act=TRUE
UNION ALL
SELECT 20,1,1,20,'A','Carga de personas de prueba','JURIDICA','C.I.F.','B29741863','Panadería La Plaza','Panadería La Plaza, Sociedad Limitada','B29741863 - Panadería La Plaza, Sociedad Limitada','600100020','contacto@panaderialaplaza.example.com',dom_cox,dom_coy,dom_hus,TRUE,'carga_prueba',CURRENT_TIMESTAMP FROM domicilios WHERE cli_id=1 AND dom_id=20 AND dom_act=TRUE;

COMMIT;
