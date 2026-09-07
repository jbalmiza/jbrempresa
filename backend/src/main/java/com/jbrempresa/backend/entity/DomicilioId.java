package com.jbrempresa.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class DomicilioId implements Serializable {
    private Long domId;
    private Long domIdHis;

    public DomicilioId() {}
    public DomicilioId(Long domId, Long domIdHis) { this.domId = domId; this.domIdHis = domIdHis; }
    public Long getDomId() { return domId; }
    public void setDomId(Long domId) { this.domId = domId; }
    public Long getDomIdHis() { return domIdHis; }
    public void setDomIdHis(Long domIdHis) { this.domIdHis = domIdHis; }
    @Override public boolean equals(Object o) { return this == o || (o instanceof DomicilioId id && Objects.equals(domId, id.domId) && Objects.equals(domIdHis, id.domIdHis)); }
    @Override public int hashCode() { return Objects.hash(domId, domIdHis); }
}
