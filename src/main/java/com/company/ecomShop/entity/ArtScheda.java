package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;


@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "ART_SCHEDA")
@Entity
public class ArtScheda {
    @Column(name = "CODICE_ARTICOLO", nullable = false, length = 25)
    @Id
    private String codiceArticolo;
    @Column(name = "ALTEZZA")
    private Integer altezza;
    @Column(name = "ARTCONPACKAGE", length = 2)
    private String artconpackage;
    @Column(name = "ARTPACKAGE", length = 2)
    private String artpackage;
    @Column(name = "CODICE_ARTICOLO_PADRE", length = 25)
    private String codiceArticoloPadre;
    @Column(name = "CODICE_ARTICOLO_SOSTITUTO", length = 25)
    private String codiceArticoloSostituto;
    @Column(name = "CODIFICAGS1", length = 10)
    private String codificags1;
    @Column(name = "COLORE", length = 3)
    private String colore;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "CTXSTR")
    private Integer ctxstr;
    @Column(name = "FLAG_ARTICOLO_DI_SERVIZIO", length = 3)
    private String flagArticoloDiServizio;
    @Column(name = "FLAG_DISTINTA_VENDITA", length = 2)
    private String flagDistintaVendita;
    @Column(name = "FLAG_ESPOSITORE_CARICHI", length = 2)
    private String flagEspositoreCarichi;
    @Column(name = "FLAG_PESO_ARTICOLO", length = 10)
    private String flagPesoArticolo;
    @Column(name = "FLAG_SCONFEZIONAMENTO", length = 2)
    private String flagSconfezionamento;
    @Column(name = "FLAG_SOSTITUTO", length = 2)
    private String flagSostituto;
    @Column(name = "GG_ALLERT_SCADENZA")
    private Integer ggAllertScadenza;
    @Column(name = "GGCONSEGNA")
    private Integer ggconsegna;
    @Column(name = "LARGHEZZA")
    private Integer larghezza;
    @Column(name = "LUNGHEZZA")
    private Integer lunghezza;
    @Column(name = "NOTE2", length = 200)
    private String note2;
    @Column(name = "PERCORSODEFAULT", length = 200)
    private String percorsodefault;
    @Column(name = "PZXCT")
    private Integer pzxct;
    @Column(name = "STAGIONE", length = 6)
    private String stagione;
    @Column(name = "STRXPED")
    private Integer strxped;
    @Column(name = "TAGLIA", length = 5)
    private String taglia;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

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

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

    public Integer getStrxped() {
        return strxped;
    }

    public void setStrxped(Integer strxped) {
        this.strxped = strxped;
    }

    public String getStagione() {
        return stagione;
    }

    public void setStagione(String stagione) {
        this.stagione = stagione;
    }

    public Integer getPzxct() {
        return pzxct;
    }

    public void setPzxct(Integer pzxct) {
        this.pzxct = pzxct;
    }

    public String getPercorsodefault() {
        return percorsodefault;
    }

    public void setPercorsodefault(String percorsodefault) {
        this.percorsodefault = percorsodefault;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public Integer getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(Integer lunghezza) {
        this.lunghezza = lunghezza;
    }

    public Integer getLarghezza() {
        return larghezza;
    }

    public void setLarghezza(Integer larghezza) {
        this.larghezza = larghezza;
    }

    public Integer getGgconsegna() {
        return ggconsegna;
    }

    public void setGgconsegna(Integer ggconsegna) {
        this.ggconsegna = ggconsegna;
    }

    public Integer getGgAllertScadenza() {
        return ggAllertScadenza;
    }

    public void setGgAllertScadenza(Integer ggAllertScadenza) {
        this.ggAllertScadenza = ggAllertScadenza;
    }

    public String getFlagSostituto() {
        return flagSostituto;
    }

    public void setFlagSostituto(String flagSostituto) {
        this.flagSostituto = flagSostituto;
    }

    public String getFlagSconfezionamento() {
        return flagSconfezionamento;
    }

    public void setFlagSconfezionamento(String flagSconfezionamento) {
        this.flagSconfezionamento = flagSconfezionamento;
    }

    public String getFlagPesoArticolo() {
        return flagPesoArticolo;
    }

    public void setFlagPesoArticolo(String flagPesoArticolo) {
        this.flagPesoArticolo = flagPesoArticolo;
    }

    public String getFlagEspositoreCarichi() {
        return flagEspositoreCarichi;
    }

    public void setFlagEspositoreCarichi(String flagEspositoreCarichi) {
        this.flagEspositoreCarichi = flagEspositoreCarichi;
    }

    public String getFlagDistintaVendita() {
        return flagDistintaVendita;
    }

    public void setFlagDistintaVendita(String flagDistintaVendita) {
        this.flagDistintaVendita = flagDistintaVendita;
    }

    public String getFlagArticoloDiServizio() {
        return flagArticoloDiServizio;
    }

    public void setFlagArticoloDiServizio(String flagArticoloDiServizio) {
        this.flagArticoloDiServizio = flagArticoloDiServizio;
    }

    public Integer getCtxstr() {
        return ctxstr;
    }

    public void setCtxstr(Integer ctxstr) {
        this.ctxstr = ctxstr;
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

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getCodificags1() {
        return codificags1;
    }

    public void setCodificags1(String codificags1) {
        this.codificags1 = codificags1;
    }

    public String getCodiceArticoloSostituto() {
        return codiceArticoloSostituto;
    }

    public void setCodiceArticoloSostituto(String codiceArticoloSostituto) {
        this.codiceArticoloSostituto = codiceArticoloSostituto;
    }

    public String getCodiceArticoloPadre() {
        return codiceArticoloPadre;
    }

    public void setCodiceArticoloPadre(String codiceArticoloPadre) {
        this.codiceArticoloPadre = codiceArticoloPadre;
    }

    public String getArtpackage() {
        return artpackage;
    }

    public void setArtpackage(String artpackage) {
        this.artpackage = artpackage;
    }

    public String getArtconpackage() {
        return artconpackage;
    }

    public void setArtconpackage(String artconpackage) {
        this.artconpackage = artconpackage;
    }

    public Integer getAltezza() {
        return altezza;
    }

    public void setAltezza(Integer altezza) {
        this.altezza = altezza;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }
}