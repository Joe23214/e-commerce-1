package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.PanelnewCompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "PANELNEW")
@Entity
public class Panelnew {
    @EmbeddedId
    private PanelnewCompKey id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "DESCRIZIONE", length = 15)
    private String descrizione;

    @Column(name = "FLAG_CONDIZIONE")
    private Integer flagCondizione;

    @Column(name = "FLAG_GRUPPO")
    private Integer flagGruppo;

    @Column(name = "FLAG_QTA_IMP", length = 2)
    private String flagQtaImp;

    @Column(name = "FLAG_RIP_SCONTO_VEN", length = 2)
    private String flagRipScontoVen;

    @Column(name = "FLAG_RIPETITIVITA")
    private Integer flagRipetitivita;

    @Column(name = "IMPORTO", precision = 10, scale = 3)
    private BigDecimal importo;

    @Column(name = "PROGRESSIVO")
    private Integer progressivo;

    @Column(name = "QTA_NEC", precision = 10, scale = 3)
    private BigDecimal qtaNec;

    @Column(name = "TIPO_PAN", length = 2)
    private String tipoPan;

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

    public String getTipoPan() {
        return tipoPan;
    }

    public void setTipoPan(String tipoPan) {
        this.tipoPan = tipoPan;
    }

    public BigDecimal getQtaNec() {
        return qtaNec;
    }

    public void setQtaNec(BigDecimal qtaNec) {
        this.qtaNec = qtaNec;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public Integer getFlagRipetitivita() {
        return flagRipetitivita;
    }

    public void setFlagRipetitivita(Integer flagRipetitivita) {
        this.flagRipetitivita = flagRipetitivita;
    }

    public String getFlagRipScontoVen() {
        return flagRipScontoVen;
    }

    public void setFlagRipScontoVen(String flagRipScontoVen) {
        this.flagRipScontoVen = flagRipScontoVen;
    }

    public String getFlagQtaImp() {
        return flagQtaImp;
    }

    public void setFlagQtaImp(String flagQtaImp) {
        this.flagQtaImp = flagQtaImp;
    }

    public Integer getFlagGruppo() {
        return flagGruppo;
    }

    public void setFlagGruppo(Integer flagGruppo) {
        this.flagGruppo = flagGruppo;
    }

    public Integer getFlagCondizione() {
        return flagCondizione;
    }

    public void setFlagCondizione(Integer flagCondizione) {
        this.flagCondizione = flagCondizione;
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

    public PanelnewCompKey getId() {
        return id;
    }

    public void setId(PanelnewCompKey id) {
        this.id = id;
    }

}