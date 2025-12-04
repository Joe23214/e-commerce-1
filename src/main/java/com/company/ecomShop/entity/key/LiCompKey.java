package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class LiCompKey {
    @Column(name = "CODICE_ARTICOLO", nullable = false, length = 25)
    private String codiceArticolo;
    @Column(name = "LISTINO", nullable = false)
    private Integer listino;

    public Integer getListino() {
        return listino;
    }

    public void setListino(Integer listino) {
        this.listino = listino;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceArticolo, listino);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiCompKey entity = (LiCompKey) o;
        return Objects.equals(this.codiceArticolo, entity.codiceArticolo) &&
                Objects.equals(this.listino, entity.listino);
    }
}