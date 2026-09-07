package com.jbrempresa.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "propuestas_respuesta")
public class PropuestaRespuesta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prr_id") private Long prrId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "com_id", nullable = false) private Long comId;
    @Column(name = "prr_con", nullable = false, columnDefinition = "TEXT") private String prrCon;
    @Column(name = "prr_con_apr", columnDefinition = "TEXT") private String prrConApr;
    @Column(name = "prr_ori", nullable = false, length = 30) private String prrOri;
    @Column(name = "prr_est", nullable = false, length = 20) private String prrEst;
    @Column(name = "prr_usu_gen", nullable = false, length = 50) private String prrUsuGen;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @Column(name = "prr_fec_gen", nullable = false) private LocalDateTime prrFecGen;
    @Column(name = "prr_usu_apr", length = 50) private String prrUsuApr;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") @Column(name = "prr_fec_apr") private LocalDateTime prrFecApr;

    public Long getPrrId(){return prrId;} public void setPrrId(Long v){prrId=v;}
    public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
    public Long getComId(){return comId;} public void setComId(Long v){comId=v;}
    public String getPrrCon(){return prrCon;} public void setPrrCon(String v){prrCon=v;}
    public String getPrrConApr(){return prrConApr;} public void setPrrConApr(String v){prrConApr=v;}
    public String getPrrOri(){return prrOri;} public void setPrrOri(String v){prrOri=v;}
    public String getPrrEst(){return prrEst;} public void setPrrEst(String v){prrEst=v;}
    public String getPrrUsuGen(){return prrUsuGen;} public void setPrrUsuGen(String v){prrUsuGen=v;}
    public LocalDateTime getPrrFecGen(){return prrFecGen;} public void setPrrFecGen(LocalDateTime v){prrFecGen=v;}
    public String getPrrUsuApr(){return prrUsuApr;} public void setPrrUsuApr(String v){prrUsuApr=v;}
    public LocalDateTime getPrrFecApr(){return prrFecApr;} public void setPrrFecApr(LocalDateTime v){prrFecApr=v;}
}
