package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.Art2CompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "ART2")
@Entity
public class Art2 {
    @EmbeddedId
    private Art2CompKey id;
    @Column(name = "COEFFICIENTE")
    private Double coefficiente;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DESCR_BARRE", length = 30)
    private String descrBarre;
    @Column(name = "FORNITORE", length = 13)
    private String fornitore;
    @Column(name = "PERC_PREZZO_CONF")
    private Double percPrezzoConf;
    @Column(name = "PREZZO")
    private Double prezzo;
    @Column(name = "PREZZO_CONFEZIONE", length = 1)
    private String prezzoConfezione;
    @Column(name = "TIPO_PESO")
    private Integer tipoPeso;
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

    public Integer getTipoPeso() {
        return tipoPeso;
    }

    public void setTipoPeso(Integer tipoPeso) {
        this.tipoPeso = tipoPeso;
    }

    public String getPrezzoConfezione() {
        return prezzoConfezione;
    }

    public void setPrezzoConfezione(String prezzoConfezione) {
        this.prezzoConfezione = prezzoConfezione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Double getPercPrezzoConf() {
        return percPrezzoConf;
    }

    public void setPercPrezzoConf(Double percPrezzoConf) {
        this.percPrezzoConf = percPrezzoConf;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getDescrBarre() {
        return descrBarre;
    }

    public void setDescrBarre(String descrBarre) {
        this.descrBarre = descrBarre;
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

    public Double getCoefficiente() {
        return coefficiente;
    }

    public void setCoefficiente(Double coefficiente) {
        this.coefficiente = coefficiente;
    }

    public Art2CompKey getId() {
        return id;
    }

    public void setId(Art2CompKey id) {
        this.id = id;
    }
}