package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class TabellaFasceCompKey {
    @Column(name = "COD_FASCIA", nullable = false, length = 2)
    private String codFascia;

    @Column(name = "COD_NEGOZIO", nullable = false, length = 3)
    private String codNegozio;

    public String getCodNegozio() {
        return codNegozio;
    }

    public void setCodNegozio(String codNegozio) {
        this.codNegozio = codNegozio;
    }

    public String getCodFascia() {
        return codFascia;
    }

    public void setCodFascia(String codFascia) {
        this.codFascia = codFascia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codFascia, codNegozio);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabellaFasceCompKey entity = (TabellaFasceCompKey) o;
        return Objects.equals(this.codFascia, entity.codFascia) &&
                Objects.equals(this.codNegozio, entity.codNegozio);
    }

}