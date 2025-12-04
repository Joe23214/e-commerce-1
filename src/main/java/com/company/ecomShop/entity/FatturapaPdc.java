package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "FatturaPA_PDC")
@Entity
public class FatturapaPdc {
    @Column(name = "CODICE_CONTO", nullable = false, length = 13)
    @Id
    private String codiceConto;
    @Column(name = "CODDESTINAZIONE", nullable = false, length = 7)
    private String coddestinazione;
    @Column(name = "CODICE_CIG", length = 10)
    private String codiceCig;
    @Column(name = "CODICE_CUP", length = 10)
    private String codiceCup;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PA")
    private Date dataPa;
    @Column(name = "EMAIL_PEC", nullable = false, length = 100)
    private String emailPec;
    @Column(name = "FLAG_PA", length = 1)
    private String flagPa;
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

    public String getFlagPa() {
        return flagPa;
    }

    public void setFlagPa(String flagPa) {
        this.flagPa = flagPa;
    }

    public String getEmailPec() {
        return emailPec;
    }

    public void setEmailPec(String emailPec) {
        this.emailPec = emailPec;
    }

    public Date getDataPa() {
        return dataPa;
    }

    public void setDataPa(Date dataPa) {
        this.dataPa = dataPa;
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

    public String getCodiceCup() {
        return codiceCup;
    }

    public void setCodiceCup(String codiceCup) {
        this.codiceCup = codiceCup;
    }

    public String getCodiceCig() {
        return codiceCig;
    }

    public void setCodiceCig(String codiceCig) {
        this.codiceCig = codiceCig;
    }

    public String getCoddestinazione() {
        return coddestinazione;
    }

    public void setCoddestinazione(String coddestinazione) {
        this.coddestinazione = coddestinazione;
    }

    public String getCodiceConto() {
        return codiceConto;
    }

    public void setCodiceConto(String codiceConto) {
        this.codiceConto = codiceConto;
    }
}