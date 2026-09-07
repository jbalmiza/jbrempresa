package com.jbrempresa.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class RecursoOperativoId implements Serializable {
 private Long reoId;
 private Long reoIdHis;
 public RecursoOperativoId(){}
 public RecursoOperativoId(Long reoId,Long reoIdHis){this.reoId=reoId;this.reoIdHis=reoIdHis;}
 public boolean equals(Object o){if(this==o)return true;if(!(o instanceof RecursoOperativoId x))return false;return Objects.equals(reoId,x.reoId)&&Objects.equals(reoIdHis,x.reoIdHis);}
 public int hashCode(){return Objects.hash(reoId,reoIdHis);}
}
