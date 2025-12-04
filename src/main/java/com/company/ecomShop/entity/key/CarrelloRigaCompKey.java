package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;
import java.util.UUID;

@JmixEntity
@Embeddable
public class CarrelloRigaCompKey {
    @Column(name = "ID_ORDINE", nullable = false)
    private UUID idOrdine;

    @Column(name = "PROGRESSIVO", nullable = false, length = 3)
    private String progressivo;

    public String getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(String progressivo) {
        this.progressivo = progressivo;
    }

    public UUID getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(UUID idOrdine) {
        this.idOrdine = idOrdine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdine, progressivo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarrelloRigaCompKey entity = (CarrelloRigaCompKey) o;
        return Objects.equals(this.idOrdine, entity.idOrdine) &&
                Objects.equals(this.progressivo, entity.progressivo);
    }

}