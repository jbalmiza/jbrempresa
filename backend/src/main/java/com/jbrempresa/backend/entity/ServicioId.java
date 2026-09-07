package com.jbrempresa.backend.entity;
import java.io.Serializable;
import java.util.Objects;
public class ServicioId implements Serializable {
    private Long serId; private Long serIdHis;
    public ServicioId() {}
    public ServicioId(Long serId, Long serIdHis) { this.serId=serId; this.serIdHis=serIdHis; }
    public Long getSerId(){return serId;} public void setSerId(Long v){serId=v;}
    public Long getSerIdHis(){return serIdHis;} public void setSerIdHis(Long v){serIdHis=v;}
    @Override public boolean equals(Object o){return this==o||(o instanceof ServicioId i&&Objects.equals(serId,i.serId)&&Objects.equals(serIdHis,i.serIdHis));}
    @Override public int hashCode(){return Objects.hash(serId,serIdHis);}
}
