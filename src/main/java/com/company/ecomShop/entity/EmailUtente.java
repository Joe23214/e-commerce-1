package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "EMAIL_UTENTE")
@Entity
public class EmailUtente {
    @Column(name = "CODICE", nullable = false, length = 15)
    @Id
    private String codice;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ORA", nullable = false)
    private Date dataOra;

    public Date getDataOra() {
        return dataOra;
    }

    public void setDataOra(Date dataOra) {
        this.dataOra = dataOra;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}