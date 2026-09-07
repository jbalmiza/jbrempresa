package com.jbrempresa.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class PersonaId implements Serializable {
    private Long perId;
    private Long perIdHis;
    public PersonaId() {}
    public PersonaId(Long perId, Long perIdHis) { this.perId = perId; this.perIdHis = perIdHis; }
    public Long getPerId() { return perId; }
    public void setPerId(Long perId) { this.perId = perId; }
    public Long getPerIdHis() { return perIdHis; }
    public void setPerIdHis(Long perIdHis) { this.perIdHis = perIdHis; }
    @Override public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof PersonaId otro)) return false;
        return Objects.equals(perId, otro.perId) && Objects.equals(perIdHis, otro.perIdHis);
    }
    @Override public int hashCode() { return Objects.hash(perId, perIdHis); }
}
