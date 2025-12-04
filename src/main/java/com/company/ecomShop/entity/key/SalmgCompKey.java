package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class SalmgCompKey {
    @Column(name = "CODICE_ARTICOLO", nullable = false, unique = true, length = 25)
    private String codiceArticolo;
    @Column(name = "CODICE_MAGAZZINO", nullable = false, unique = true, length = 2)
    private String codiceMagazzino;
    @Column(name = "PROPRIETA_MAGAZ", nullable = false, unique = true, length = 2)
    private String proprietaMagaz;

    public String getProprietaMagaz() {
        return proprietaMagaz;
    }

    public void setProprietaMagaz(String proprietaMagaz) {
        this.proprietaMagaz = proprietaMagaz;
    }

    public String getCodiceMagazzino() {
        return codiceMagazzino;
    }

    public void setCodiceMagazzino(String codiceMagazzino) {
        this.codiceMagazzino = codiceMagazzino;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceArticolo, proprietaMagaz, codiceMagazzino);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalmgCompKey entity = (SalmgCompKey) o;
        return Objects.equals(this.codiceArticolo, entity.codiceArticolo) &&
                Objects.equals(this.proprietaMagaz, entity.proprietaMagaz) &&
                Objects.equals(this.codiceMagazzino, entity.codiceMagazzino);
    }
}