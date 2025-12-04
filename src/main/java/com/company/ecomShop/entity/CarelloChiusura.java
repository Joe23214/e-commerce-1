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
@Table(name = "CARELLO_CHIUSURA")
@Entity
public class CarelloChiusura {
    @JmixGeneratedValue
    @Column(name = "ID_ORDINE", nullable = false)
    @Id
    private UUID idOrdine;

    @Column(name = "CITOFONO", length = 20)
    private String citofono;

    @Column(name = "CODICE_FASCIA_ORARIA", length = 2)
    private String codiceFasciaOraria;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @CreatedBy
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ORA", nullable = false)
    private Date dataOra;

    @Column(name = "FLAG_STATO", length = 1)
    private String flagStato;

    @Column(name = "INDIRIZZO", length = 150)
    private String indirizzo;

    @Column(name = "NOTE", length = 250)
    private String note;

    @Column(name = "PIANO", length = 2)
    private String piano;

    @Column(name = "SCALA", length = 2)
    private String scala;

    @Column(name = "TIPO_PAGAMENTO", length = 3)
    private String tipoPagamento;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;
    @Version
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

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getScala() {
        return scala;
    }

    public void setScala(String scala) {
        this.scala = scala;
    }

    public String getPiano() {
        return piano;
    }

    public void setPiano(String piano) {
        this.piano = piano;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getFlagStato() {
        return flagStato;
    }

    public void setFlagStato(String flagStato) {
        this.flagStato = flagStato;
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

    public String getCodiceFasciaOraria() {
        return codiceFasciaOraria;
    }

    public void setCodiceFasciaOraria(String codiceFasciaOraria) {
        this.codiceFasciaOraria = codiceFasciaOraria;
    }

    public String getCitofono() {
        return citofono;
    }

    public void setCitofono(String citofono) {
        this.citofono = citofono;
    }

    public UUID getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(UUID idOrdine) {
        this.idOrdine = idOrdine;
    }

}