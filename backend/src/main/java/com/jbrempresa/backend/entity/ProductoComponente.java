package com.jbrempresa.backend.entity;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="productos_componentes",uniqueConstraints=@UniqueConstraint(name="uk_producto_componente",columnNames={"emp_id","pro_id","pro_id_his","cmp_id"}))
public class ProductoComponente{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="prc_id") private Long prcId;
 @Column(name="emp_id",nullable=false) private Long empId;@Column(name="pro_id",nullable=false) private Long proId;@Column(name="pro_id_his",nullable=false) private Long proIdHis;@Column(name="cmp_id",nullable=false) private Long cmpId;@Column(name="prc_can",nullable=false,precision=14,scale=4) private BigDecimal prcCan;
 public Long getPrcId(){return prcId;}public void setPrcId(Long v){prcId=v;}public Long getEmpId(){return empId;}public void setEmpId(Long v){empId=v;}public Long getProId(){return proId;}public void setProId(Long v){proId=v;}public Long getProIdHis(){return proIdHis;}public void setProIdHis(Long v){proIdHis=v;}public Long getCmpId(){return cmpId;}public void setCmpId(Long v){cmpId=v;}public BigDecimal getPrcCan(){return prcCan;}public void setPrcCan(BigDecimal v){prcCan=v;}
}
