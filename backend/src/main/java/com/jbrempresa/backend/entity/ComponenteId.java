package com.jbrempresa.backend.entity;
import java.io.Serializable;import java.util.Objects;
public class ComponenteId implements Serializable { private Long empId; private Long cmpId; private Long cmpIdHis; public ComponenteId(){} public boolean equals(Object o){if(this==o)return true;if(!(o instanceof ComponenteId i))return false;return Objects.equals(empId,i.empId)&&Objects.equals(cmpId,i.cmpId)&&Objects.equals(cmpIdHis,i.cmpIdHis);}public int hashCode(){return Objects.hash(empId,cmpId,cmpIdHis);} }
