package com.company.ecomShop.entity.key;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.util.Objects;

@JmixEntity
@Embeddable
public class PanelnewCompKey {
    @Column(name = "CODICE_PAN", nullable = false, length = 10)
    private String codicePan;

    @Column(name = "CODICE_PLU", nullable = false, length = 25)
    private String codicePlu;

    @Column(name = "FLAG_ART_REP", nullable = false, length = 2)
    private String flagArtRep;

    @Column(name = "GRUPPO", nullable = false)
    private Integer gruppo;

    public Integer getGruppo() {
        return gruppo;
    }

    public void setGruppo(Integer gruppo) {
        this.gruppo = gruppo;
    }

    public String getFlagArtRep() {
        return flagArtRep;
    }

    public void setFlagArtRep(String flagArtRep) {
        this.flagArtRep = flagArtRep;
    }

    public String getCodicePlu() {
        return codicePlu;
    }

    public void setCodicePlu(String codicePlu) {
        this.codicePlu = codicePlu;
    }

    public String getCodicePan() {
        return codicePan;
    }

    public void setCodicePan(String codicePan) {
        this.codicePan = codicePan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePlu, gruppo, flagArtRep, codicePan);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelnewCompKey entity = (PanelnewCompKey) o;
        return Objects.equals(this.codicePlu, entity.codicePlu) &&
                Objects.equals(this.gruppo, entity.gruppo) &&
                Objects.equals(this.flagArtRep, entity.flagArtRep) &&
                Objects.equals(this.codicePan, entity.codicePan);
    }

}