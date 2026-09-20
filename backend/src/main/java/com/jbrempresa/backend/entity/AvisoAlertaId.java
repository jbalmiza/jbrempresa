package com.jbrempresa.backend.entity;
import java.io.Serializable;
import java.util.Objects;
public class AvisoAlertaId implements Serializable {
 private Long empId; private Long aviId; private Long aviIdHis;
 public AvisoAlertaId(){} public AvisoAlertaId(Long emp,Long id,Long his){empId=emp;aviId=id;aviIdHis=his;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
 public Long getAviId(){return aviId;} public void setAviId(Long v){aviId=v;}
 public Long getAviIdHis(){return aviIdHis;} public void setAviIdHis(Long v){aviIdHis=v;}
 @Override public boolean equals(Object o){return o instanceof AvisoAlertaId x&&Objects.equals(empId,x.empId)&&Objects.equals(aviId,x.aviId)&&Objects.equals(aviIdHis,x.aviIdHis);}
 @Override public int hashCode(){return Objects.hash(empId,aviId,aviIdHis);}
}
