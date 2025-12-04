package com.company.ecomShop.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "MESSAGGI")
@Entity
public class Messaggi {
    @JmixGeneratedValue
    @Column(name = "PROGRESSIVO", nullable = false)
    @Id
    private Integer progressivo;

    @Column(name = "CODICE_CAT", length = 13)
    private String codiceCat;

    @Column(name = "CODICE_CLI", length = 13)
    private String codiceCli;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "DATA_FIN", length = 8)
    private String dataFin;

    @Column(name = "DATA_INI", length = 8)
    private String dataIni;

    @Column(name = "FLAG_MESSAGGIO_COUPON", length = 2)
    private String flagMessaggioCoupon;

    @Column(name = "MESSAGGIO", length = 512)
    private String messaggio;

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

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getFlagMessaggioCoupon() {
        return flagMessaggioCoupon;
    }

    public void setFlagMessaggioCoupon(String flagMessaggioCoupon) {
        this.flagMessaggioCoupon = flagMessaggioCoupon;
    }

    public String getDataIni() {
        return dataIni;
    }

    public void setDataIni(String dataIni) {
        this.dataIni = dataIni;
    }

    public String getDataFin() {
        return dataFin;
    }

    public void setDataFin(String dataFin) {
        this.dataFin = dataFin;
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

    public String getCodiceCli() {
        return codiceCli;
    }

    public void setCodiceCli(String codiceCli) {
        this.codiceCli = codiceCli;
    }

    public String getCodiceCat() {
        return codiceCat;
    }

    public void setCodiceCat(String codiceCat) {
        this.codiceCat = codiceCat;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

}