package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class Art2CompKey {
    @Column(name = "COD_BAR_FOR", nullable = false, length = 25)
    private String codBarFor;
    @Column(name = "CODICE_ARTICOLO", nullable = false, length = 25)
    private String codiceArticolo;
    @Column(name = "FLAG_B_F", nullable = false, length = 1)
    private String flagBF;

    public String getFlagBF() {
        return flagBF;
    }

    public void setFlagBF(String flagBF) {
        this.flagBF = flagBF;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getCodBarFor() {
        return codBarFor;
    }

    public void setCodBarFor(String codBarFor) {
        this.codBarFor = codBarFor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceArticolo, codBarFor, flagBF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Art2CompKey entity = (Art2CompKey) o;
        return Objects.equals(this.codiceArticolo, entity.codiceArticolo) &&
                Objects.equals(this.codBarFor, entity.codBarFor) &&
                Objects.equals(this.flagBF, entity.flagBF);
    }
}