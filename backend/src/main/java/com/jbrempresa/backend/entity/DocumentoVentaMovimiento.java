package com.jbrempresa.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "documentos_venta_movimientos")
public class DocumentoVentaMovimiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "dvm_id") private Long dvmId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "dov_id", nullable = false) private Long dovId;
    @Column(name = "dov_tip", nullable = false, length = 3) private String dovTip;
    @Column(name = "dov_num", nullable = false, length = 30) private String dovNum;
    @Column(name = "per_id", nullable = false) private Long perId;
    @Column(name = "dov_fec", nullable = false) private LocalDate dovFec;
    @Column(name = "dov_est", nullable = false, length = 20) private String dovEst;
    @Column(name = "dov_ubi", length = 100) private String dovUbi;
    @Column(name = "dov_ori", length = 20) private String dovOri;
    @Column(name = "dov_mod", length = 20) private String dovMod;
    @Column(name = "dov_dir_env", length = 500) private String dovDirEnv;
    @Column(name = "dov_fil_mal") private Integer dovFilMal;
    @Column(name = "dov_col_mal") private Integer dovColMal;
    @Column(name = "dov_imp_tot") private BigDecimal dovImpTot;
    @Column(name = "dvm_tip_mov", nullable = false, length = 20) private String dvmTipMov;
    @Column(name = "dvm_cau_mov", length = 500) private String dvmCauMov;
    @Column(name = "dvm_usu_mov", nullable = false, length = 100) private String dvmUsuMov;
    @Column(name = "dvm_fec_mov", nullable = false) private LocalDateTime dvmFecMov;

    public Long getDvmId(){return dvmId;} public void setDvmId(Long v){dvmId=v;}
    public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
    public Long getDovId(){return dovId;} public void setDovId(Long v){dovId=v;}
    public String getDovTip(){return dovTip;} public void setDovTip(String v){dovTip=v;}
    public String getDovNum(){return dovNum;} public void setDovNum(String v){dovNum=v;}
    public Long getPerId(){return perId;} public void setPerId(Long v){perId=v;}
    public LocalDate getDovFec(){return dovFec;} public void setDovFec(LocalDate v){dovFec=v;}
    public String getDovEst(){return dovEst;} public void setDovEst(String v){dovEst=v;}
    public String getDovUbi(){return dovUbi;} public void setDovUbi(String v){dovUbi=v;}
    public String getDovOri(){return dovOri;} public void setDovOri(String v){dovOri=v;}
    public String getDovMod(){return dovMod;} public void setDovMod(String v){dovMod=v;}
    public String getDovDirEnv(){return dovDirEnv;} public void setDovDirEnv(String v){dovDirEnv=v;}
    public Integer getDovFilMal(){return dovFilMal;} public void setDovFilMal(Integer v){dovFilMal=v;}
    public Integer getDovColMal(){return dovColMal;} public void setDovColMal(Integer v){dovColMal=v;}
    public BigDecimal getDovImpTot(){return dovImpTot;} public void setDovImpTot(BigDecimal v){dovImpTot=v;}
    public String getDvmTipMov(){return dvmTipMov;} public void setDvmTipMov(String v){dvmTipMov=v;}
    public String getDvmCauMov(){return dvmCauMov;} public void setDvmCauMov(String v){dvmCauMov=v;}
    public String getDvmUsuMov(){return dvmUsuMov;} public void setDvmUsuMov(String v){dvmUsuMov=v;}
    public LocalDateTime getDvmFecMov(){return dvmFecMov;} public void setDvmFecMov(LocalDateTime v){dvmFecMov=v;}
}
