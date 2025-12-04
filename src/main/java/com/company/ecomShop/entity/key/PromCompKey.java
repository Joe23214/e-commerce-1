package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class PromCompKey {
    @Column(name = "NUMERO_OFFERTA", nullable = false)
    private Integer numeroOfferta;
    @Column(name = "PROGRESSIVO", nullable = false)
    private Integer progressivo;

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public Integer getNumeroOfferta() {
        return numeroOfferta;
    }

    public void setNumeroOfferta(Integer numeroOfferta) {
        this.numeroOfferta = numeroOfferta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroOfferta, progressivo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromCompKey entity = (PromCompKey) o;
        return Objects.equals(this.numeroOfferta, entity.numeroOfferta) &&
                Objects.equals(this.progressivo, entity.progressivo);
    }
}