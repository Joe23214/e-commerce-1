package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "AG")
@Entity
public class Ag {
    @Column(name = "CODICE", nullable = false, length = 5)
    @Id
    private String codice;
    @Column(name = "CAP", length = 5)
    private String cap;
    @Column(name = "CESS_ATT_GMA")
    private Integer cessAttGma;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DESCRIZIONE", length = 50)
    private String descrizione;
    @Column(name = "FLAG_DEPO", length = 1)
    private String flagDepo;
    @Column(name = "INDIRIZZO", length = 40)
    private String indirizzo;
    @Column(name = "LOCALITA", length = 20)
    private String localita;
    @Column(name = "P_IVA", length = 12)
    private String pIva;
    @Column(name = "PROV_STAND")
    private Double provStand;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public Double getProvStand() {
        return provStand;
    }

    public void setProvStand(Double provStand) {
        this.provStand = provStand;
    }

    public String getPIva() {
        return pIva;
    }

    public void setPIva(String pIva) {
        this.pIva = pIva;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getFlagDepo() {
        return flagDepo;
    }

    public void setFlagDepo(String flagDepo) {
        this.flagDepo = flagDepo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public Integer getCessAttGma() {
        return cessAttGma;
    }

    public void setCessAttGma(Integer cessAttGma) {
        this.cessAttGma = cessAttGma;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}