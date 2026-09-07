package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "mensajes")
public class Mensaje {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "men_id") private Long menId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "com_id") private Long comId;
    @Column(name = "men_sec") private Long menSec;
    @Column(name = "men_dir", nullable = false, length = 10) private String menDir;
    @Column(name = "men_aut", nullable = false, length = 15) private String menAut;
    @Column(name = "men_con", nullable = false, columnDefinition = "TEXT") private String menCon;
    @Column(name = "men_asu", length = 200) private String menAsu;
    @Column(name = "men_est", nullable = false, length = 30) private String menEst;
    @Column(name = "men_int", length = 30) private String menInt;
    @Column(name = "men_con_int") private Double menConInt;
    @Column(name = "men_ori_cla", length = 20) private String menOriCla;
    @Column(name = "men_est_cla", length = 20) private String menEstCla;
    @Column(name = "men_id_ext", length = 200) private String menIdExt;
    @Column(name = "men_rem", length = 200) private String menRem;
    @Column(name = "men_des", length = 200) private String menDes;
    @Column(name = "coc_id") private Long cocId;
    @Column(name = "men_can", length = 20) private String menCan;
    @Column(name = "per_id") private Long perId;
    @Column(name = "men_nom_rem", length = 150) private String menNomRem;
    @Column(name = "men_dni_rem", length = 30) private String menDniRem;
    @Column(name = "men_tel_rem", length = 50) private String menTelRem;
    @Column(name = "men_ema_rem", length = 150) private String menEmaRem;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @Column(name = "men_fec_rec") private LocalDateTime menFecRec;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @Column(name = "men_fec_pro") private LocalDateTime menFecPro;
    @Column(name = "men_con_per") private Double menConPer;
    @Column(name = "men_usu_mov", nullable = false, length = 50) private String menUsuMov;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "men_fec_mov", nullable = false) private LocalDateTime menFecMov;
    @Column(name = "men_act", nullable = false) private Boolean menAct;

    public Long getMenId(){return menId;} public void setMenId(Long v){menId=v;} public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;} public Long getComId(){return comId;} public void setComId(Long v){comId=v;} public Long getMenSec(){return menSec;} public void setMenSec(Long v){menSec=v;} public String getMenDir(){return menDir;} public void setMenDir(String v){menDir=v;} public String getMenAut(){return menAut;} public void setMenAut(String v){menAut=v;} public String getMenCon(){return menCon;} public void setMenCon(String v){menCon=v;} public String getMenAsu(){return menAsu;} public void setMenAsu(String v){menAsu=v;} public String getMenEst(){return menEst;} public void setMenEst(String v){menEst=v;} public String getMenInt(){return menInt;} public void setMenInt(String v){menInt=v;} public Double getMenConInt(){return menConInt;} public void setMenConInt(Double v){menConInt=v;} public String getMenOriCla(){return menOriCla;} public void setMenOriCla(String v){menOriCla=v;} public String getMenEstCla(){return menEstCla;} public void setMenEstCla(String v){menEstCla=v;} public String getMenIdExt(){return menIdExt;} public void setMenIdExt(String v){menIdExt=v;} public String getMenRem(){return menRem;} public void setMenRem(String v){menRem=v;} public String getMenDes(){return menDes;} public void setMenDes(String v){menDes=v;} public Long getCocId(){return cocId;}public void setCocId(Long v){cocId=v;}public String getMenCan(){return menCan;}public void setMenCan(String v){menCan=v;} public Long getPerId(){return perId;} public void setPerId(Long v){perId=v;} public String getMenNomRem(){return menNomRem;} public void setMenNomRem(String v){menNomRem=v;} public String getMenDniRem(){return menDniRem;} public void setMenDniRem(String v){menDniRem=v;} public String getMenTelRem(){return menTelRem;} public void setMenTelRem(String v){menTelRem=v;} public String getMenEmaRem(){return menEmaRem;} public void setMenEmaRem(String v){menEmaRem=v;} public LocalDateTime getMenFecRec(){return menFecRec;} public void setMenFecRec(LocalDateTime v){menFecRec=v;} public LocalDateTime getMenFecPro(){return menFecPro;} public void setMenFecPro(LocalDateTime v){menFecPro=v;} public Double getMenConPer(){return menConPer;} public void setMenConPer(Double v){menConPer=v;} public String getMenUsuMov(){return menUsuMov;} public void setMenUsuMov(String v){menUsuMov=v;} public LocalDateTime getMenFecMov(){return menFecMov;} public void setMenFecMov(LocalDateTime v){menFecMov=v;} public Boolean getMenAct(){return menAct;} public void setMenAct(Boolean v){menAct=v;}
}
