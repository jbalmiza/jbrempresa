package com.jbrempresa.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProductoId implements Serializable {
    private Long proId;
    private Long proIdHis;

    public ProductoId() {}
    public ProductoId(Long proId, Long proIdHis) { this.proId = proId; this.proIdHis = proIdHis; }
    public Long getProId() { return proId; }
    public void setProId(Long proId) { this.proId = proId; }
    public Long getProIdHis() { return proIdHis; }
    public void setProIdHis(Long proIdHis) { this.proIdHis = proIdHis; }
    @Override public boolean equals(Object o) { return this == o || (o instanceof ProductoId id && Objects.equals(proId, id.proId) && Objects.equals(proIdHis, id.proIdHis)); }
    @Override public int hashCode() { return Objects.hash(proId, proIdHis); }
}
