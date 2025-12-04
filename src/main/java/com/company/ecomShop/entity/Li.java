package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.LiCompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "LIS")
@Entity
public class Li {
    @EmbeddedId
    private LiCompKey id;
    @Column(name = "COSTI_AGGIUNTIVI")
    private Double costiAggiuntivi;
    @Column(name = "COSTO_MED")
    private Double costoMed;
    @Column(name = "COSTO_ULT")
    private Double costoUlt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DATA_ELAB_COS_ULTL", length = 8)
    private String dataElabCosUltl;
    @Column(name = "DATA_ULTIMO_AGGIOL", length = 8)
    private String dataUltimoAggiol;
    @Column(name = "FLAG_ARTI_ESA", length = 1)
    private String flagArtiEsa;
    @Column(name = "FLAG_DISTINTA", length = 1)
    private String flagDistinta;
    @Column(name = "FLAG_GESTITO_RI", length = 1)
    private String flagGestitoRi;
    @Column(name = "FLAG_LIS_GESTITO", length = 1)
    private String flagLisGestito;
    @Column(name = "FLAG_PREZZO_V", length = 1)
    private String flagPrezzoV;
    @Column(name = "FLAG_PROD_IN_OF", length = 1)
    private String flagProdInOf;
    @Column(name = "FLAG_SERVIZIO", length = 1)
    private String flagServizio;
    @Column(name = "FLAG_STAMPA_ETI", length = 1)
    private String flagStampaEti;
    @Column(name = "FLAG_STAMPA_FRO", length = 1)
    private String flagStampaFro;
    @Column(name = "FLAG_STATO", length = 1)
    private String flagStato;
    @Column(name = "PER_RIC_MARG2")
    private Double perRicMarg2;
    @Column(name = "PER_RIC_MARG3")
    private Double perRicMarg3;
    @Column(name = "PERC_RIC_MARG10")
    private Double percRicMarg10;
    @Column(name = "PERC_RIC_MARG4")
    private Double percRicMarg4;
    @Column(name = "PERC_RIC_MARG5")
    private Double percRicMarg5;
    @Column(name = "PERC_RIC_MARG6")
    private Double percRicMarg6;
    @Column(name = "PERC_RIC_MARG7")
    private Double percRicMarg7;
    @Column(name = "PERC_RIC_MARG8")
    private Double percRicMarg8;
    @Column(name = "PERC_RIC_MARG9")
    private Double percRicMarg9;
    @Column(name = "PERC_RICAR_MAR")
    private Double percRicarMar;
    @Column(name = "PREZZO10")
    private Double prezzo10;
    @Column(name = "PREZZO4")
    private Double prezzo4;
    @Column(name = "PREZZO5")
    private Double prezzo5;
    @Column(name = "PREZZO6")
    private Double prezzo6;
    @Column(name = "PREZZO7")
    private Double prezzo7;
    @Column(name = "PREZZO8")
    private Double prezzo8;
    @Column(name = "PREZZO9")
    private Double prezzo9;
    @Column(name = "PREZZO_VEND")
    private Double prezzoVend;
    @Column(name = "PREZZOPRECL")
    private Double prezzoprecl;
    @Column(name = "PROGRES_VENDITA")
    private Double progresVendita;
    @Column(name = "QUANT_RIORD_PREC")
    private Double quantRiordPrec;
    @Column(name = "QUANTITA_VENDUTA")
    private Double quantitaVenduta;
    @Column(name = "SCORTA_MINIMAL")
    private Double scortaMinimal;
    @Column(name = "SEC_PREZZO_VE")
    private Double secPrezzoVe;
    @Column(name = "TERZO_PREZZO_VE")
    private Double terzoPrezzoVe;
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

    public Double getTerzoPrezzoVe() {
        return terzoPrezzoVe;
    }

    public void setTerzoPrezzoVe(Double terzoPrezzoVe) {
        this.terzoPrezzoVe = terzoPrezzoVe;
    }

    public Double getSecPrezzoVe() {
        return secPrezzoVe;
    }

    public void setSecPrezzoVe(Double secPrezzoVe) {
        this.secPrezzoVe = secPrezzoVe;
    }

    public Double getScortaMinimal() {
        return scortaMinimal;
    }

    public void setScortaMinimal(Double scortaMinimal) {
        this.scortaMinimal = scortaMinimal;
    }

    public Double getQuantitaVenduta() {
        return quantitaVenduta;
    }

    public void setQuantitaVenduta(Double quantitaVenduta) {
        this.quantitaVenduta = quantitaVenduta;
    }

    public Double getQuantRiordPrec() {
        return quantRiordPrec;
    }

    public void setQuantRiordPrec(Double quantRiordPrec) {
        this.quantRiordPrec = quantRiordPrec;
    }

    public Double getProgresVendita() {
        return progresVendita;
    }

    public void setProgresVendita(Double progresVendita) {
        this.progresVendita = progresVendita;
    }

    public Double getPrezzoprecl() {
        return prezzoprecl;
    }

    public void setPrezzoprecl(Double prezzoprecl) {
        this.prezzoprecl = prezzoprecl;
    }

    public Double getPrezzoVend() {
        return prezzoVend;
    }

    public void setPrezzoVend(Double prezzoVend) {
        this.prezzoVend = prezzoVend;
    }

    public Double getPrezzo9() {
        return prezzo9;
    }

    public void setPrezzo9(Double prezzo9) {
        this.prezzo9 = prezzo9;
    }

    public Double getPrezzo8() {
        return prezzo8;
    }

    public void setPrezzo8(Double prezzo8) {
        this.prezzo8 = prezzo8;
    }

    public Double getPrezzo7() {
        return prezzo7;
    }

    public void setPrezzo7(Double prezzo7) {
        this.prezzo7 = prezzo7;
    }

    public Double getPrezzo6() {
        return prezzo6;
    }

    public void setPrezzo6(Double prezzo6) {
        this.prezzo6 = prezzo6;
    }

    public Double getPrezzo5() {
        return prezzo5;
    }

    public void setPrezzo5(Double prezzo5) {
        this.prezzo5 = prezzo5;
    }

    public Double getPrezzo4() {
        return prezzo4;
    }

    public void setPrezzo4(Double prezzo4) {
        this.prezzo4 = prezzo4;
    }

    public Double getPrezzo10() {
        return prezzo10;
    }

    public void setPrezzo10(Double prezzo10) {
        this.prezzo10 = prezzo10;
    }

    public Double getPercRicarMar() {
        return percRicarMar;
    }

    public void setPercRicarMar(Double percRicarMar) {
        this.percRicarMar = percRicarMar;
    }

    public Double getPercRicMarg9() {
        return percRicMarg9;
    }

    public void setPercRicMarg9(Double percRicMarg9) {
        this.percRicMarg9 = percRicMarg9;
    }

    public Double getPercRicMarg8() {
        return percRicMarg8;
    }

    public void setPercRicMarg8(Double percRicMarg8) {
        this.percRicMarg8 = percRicMarg8;
    }

    public Double getPercRicMarg7() {
        return percRicMarg7;
    }

    public void setPercRicMarg7(Double percRicMarg7) {
        this.percRicMarg7 = percRicMarg7;
    }

    public Double getPercRicMarg6() {
        return percRicMarg6;
    }

    public void setPercRicMarg6(Double percRicMarg6) {
        this.percRicMarg6 = percRicMarg6;
    }

    public Double getPercRicMarg5() {
        return percRicMarg5;
    }

    public void setPercRicMarg5(Double percRicMarg5) {
        this.percRicMarg5 = percRicMarg5;
    }

    public Double getPercRicMarg4() {
        return percRicMarg4;
    }

    public void setPercRicMarg4(Double percRicMarg4) {
        this.percRicMarg4 = percRicMarg4;
    }

    public Double getPercRicMarg10() {
        return percRicMarg10;
    }

    public void setPercRicMarg10(Double percRicMarg10) {
        this.percRicMarg10 = percRicMarg10;
    }

    public Double getPerRicMarg3() {
        return perRicMarg3;
    }

    public void setPerRicMarg3(Double perRicMarg3) {
        this.perRicMarg3 = perRicMarg3;
    }

    public Double getPerRicMarg2() {
        return perRicMarg2;
    }

    public void setPerRicMarg2(Double perRicMarg2) {
        this.perRicMarg2 = perRicMarg2;
    }

    public String getFlagStato() {
        return flagStato;
    }

    public void setFlagStato(String flagStato) {
        this.flagStato = flagStato;
    }

    public String getFlagStampaFro() {
        return flagStampaFro;
    }

    public void setFlagStampaFro(String flagStampaFro) {
        this.flagStampaFro = flagStampaFro;
    }

    public String getFlagStampaEti() {
        return flagStampaEti;
    }

    public void setFlagStampaEti(String flagStampaEti) {
        this.flagStampaEti = flagStampaEti;
    }

    public String getFlagServizio() {
        return flagServizio;
    }

    public void setFlagServizio(String flagServizio) {
        this.flagServizio = flagServizio;
    }

    public String getFlagProdInOf() {
        return flagProdInOf;
    }

    public void setFlagProdInOf(String flagProdInOf) {
        this.flagProdInOf = flagProdInOf;
    }

    public String getFlagPrezzoV() {
        return flagPrezzoV;
    }

    public void setFlagPrezzoV(String flagPrezzoV) {
        this.flagPrezzoV = flagPrezzoV;
    }

    public String getFlagLisGestito() {
        return flagLisGestito;
    }

    public void setFlagLisGestito(String flagLisGestito) {
        this.flagLisGestito = flagLisGestito;
    }

    public String getFlagGestitoRi() {
        return flagGestitoRi;
    }

    public void setFlagGestitoRi(String flagGestitoRi) {
        this.flagGestitoRi = flagGestitoRi;
    }

    public String getFlagDistinta() {
        return flagDistinta;
    }

    public void setFlagDistinta(String flagDistinta) {
        this.flagDistinta = flagDistinta;
    }

    public String getFlagArtiEsa() {
        return flagArtiEsa;
    }

    public void setFlagArtiEsa(String flagArtiEsa) {
        this.flagArtiEsa = flagArtiEsa;
    }

    public String getDataUltimoAggiol() {
        return dataUltimoAggiol;
    }

    public void setDataUltimoAggiol(String dataUltimoAggiol) {
        this.dataUltimoAggiol = dataUltimoAggiol;
    }

    public String getDataElabCosUltl() {
        return dataElabCosUltl;
    }

    public void setDataElabCosUltl(String dataElabCosUltl) {
        this.dataElabCosUltl = dataElabCosUltl;
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

    public Double getCostoUlt() {
        return costoUlt;
    }

    public void setCostoUlt(Double costoUlt) {
        this.costoUlt = costoUlt;
    }

    public Double getCostoMed() {
        return costoMed;
    }

    public void setCostoMed(Double costoMed) {
        this.costoMed = costoMed;
    }

    public Double getCostiAggiuntivi() {
        return costiAggiuntivi;
    }

    public void setCostiAggiuntivi(Double costiAggiuntivi) {
        this.costiAggiuntivi = costiAggiuntivi;
    }

    public LiCompKey getId() {
        return id;
    }

    public void setId(LiCompKey id) {
        this.id = id;
    }
}