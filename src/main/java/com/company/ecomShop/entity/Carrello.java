package com.company.ecomShop.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "CARRELLO")
@Entity
public class Carrello {
    @JmixGeneratedValue
    @Column(name = "ID_ORDINE", nullable = false)
    @Id
    private UUID idOrdine;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @CreatedBy
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ORA")
    private Date dataOra;

    @Column(name = "FLAG_RITIRO_CONSEGNA", length = 1)
    private String flagRitiroConsegna;

    @Column(name = "PDV", length = 2)
    private String pdv;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @Column(name = "UTENTE", length = 36)
    private String utente;
    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
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

    public String getPdv() {
        return pdv;
    }

    public void setPdv(String pdv) {
        this.pdv = pdv;
    }

    public String getFlagRitiroConsegna() {
        return flagRitiroConsegna;
    }

    public void setFlagRitiroConsegna(String flagRitiroConsegna) {
        this.flagRitiroConsegna = flagRitiroConsegna;
    }

    public Date getDataOra() {
        return dataOra;
    }

    public void setDataOra(Date dataOra) {
        this.dataOra = dataOra;
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

    public UUID getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(UUID idOrdine) {
        this.idOrdine = idOrdine;
    }

}