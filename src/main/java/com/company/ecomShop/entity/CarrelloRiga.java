package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.CarrelloRigaCompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "CARRELLO_RIGA")
@Entity
public class CarrelloRiga {
    @EmbeddedId
    private CarrelloRigaCompKey id;

    @Column(name = "CODICE_ARTICOLO", length = 25)
    private String codiceArticolo;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @CreatedBy
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "FLAG_PESO", length = 1)
    private String flagPeso;

    @Column(name = "QTA_PESO")
    private Double qtaPeso;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @Column(name = "VALORE")
    private Double valore;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getValore() {
        return valore;
    }

    public void setValore(Double valore) {
        this.valore = valore;
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

    public Double getQtaPeso() {
        return qtaPeso;
    }

    public void setQtaPeso(Double qtaPeso) {
        this.qtaPeso = qtaPeso;
    }

    public String getFlagPeso() {
        return flagPeso;
    }

    public void setFlagPeso(String flagPeso) {
        this.flagPeso = flagPeso;
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

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public CarrelloRigaCompKey getId() {
        return id;
    }

    public void setId(CarrelloRigaCompKey id) {
        this.id = id;
    }

}