package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "comunicaciones")
public class Comunicacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id") private Long comId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "com_tip", nullable = false, length = 20) private String comTip;
    @Column(name = "com_ori", nullable = false, length = 10) private String comOri;
    @Column(name = "com_can", nullable = false, length = 20) private String comCan;
    @Column(name = "com_est", nullable = false, length = 30) private String comEst;
    @Column(name = "com_asu", nullable = false, length = 200) private String comAsu;
    @Column(name = "com_int", length = 30) private String comInt;
    @Column(name = "per_id") private Long perId;
    @Column(name = "usu_id") private Long usuId;
    @Column(name = "are_id") private Long areId;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "com_fec_ult", nullable = false) private LocalDateTime comFecUlt;
    @Column(name = "com_usu_mov", nullable = false, length = 50) private String comUsuMov;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "com_fec_mov", nullable = false) private LocalDateTime comFecMov;
    @Column(name = "com_act", nullable = false) private Boolean comAct;

    public Long getComId(){return comId;} public void setComId(Long v){comId=v;}
    public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
    public String getComTip(){return comTip;} public void setComTip(String v){comTip=v;}
    public String getComOri(){return comOri;} public void setComOri(String v){comOri=v;}
    public String getComCan(){return comCan;} public void setComCan(String v){comCan=v;}
    public String getComEst(){return comEst;} public void setComEst(String v){comEst=v;}
    public String getComAsu(){return comAsu;} public void setComAsu(String v){comAsu=v;}
    public String getComInt(){return comInt;} public void setComInt(String v){comInt=v;}
    public Long getPerId(){return perId;} public void setPerId(Long v){perId=v;}
    public Long getUsuId(){return usuId;} public void setUsuId(Long v){usuId=v;}
    public Long getAreId(){return areId;} public void setAreId(Long v){areId=v;}
    public LocalDateTime getComFecUlt(){return comFecUlt;} public void setComFecUlt(LocalDateTime v){comFecUlt=v;}
    public String getComUsuMov(){return comUsuMov;} public void setComUsuMov(String v){comUsuMov=v;}
    public LocalDateTime getComFecMov(){return comFecMov;} public void setComFecMov(LocalDateTime v){comFecMov=v;}
    public Boolean getComAct(){return comAct;} public void setComAct(Boolean v){comAct=v;}
}
