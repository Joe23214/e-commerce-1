package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.PromCompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "PROM")
@Entity
public class Prom {
    @EmbeddedId
    private PromCompKey id;
    @Column(name = "COD_COUPON", length = 20)
    private String codCoupon;
    @Column(name = "COD_MESSAGGIO")
    private Integer codMessaggio;
    @Column(name = "COD_PANEL", length = 25)
    private String codPanel;
    @Column(name = "COD_TESSERA", length = 13)
    private String codTessera;
    @Column(name = "CODICE_INIZ", length = 10)
    private String codiceIniz;
    @Column(name = "COSTO", precision = 18, scale = 3)
    private BigDecimal costo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DATA_FIN", length = 8)
    private String dataFin;
    @Column(name = "DATA_INIZ", length = 8)
    private String dataIniz;
    @Column(name = "DATASELLINFIN", length = 8)
    private String datasellinfin;
    @Column(name = "DATASELLININ", length = 8)
    private String datasellinin;
    @Column(name = "DESCRIZIONE", length = 30)
    private String descrizione;
    @Column(name = "FLAG_APPL", length = 2)
    private String flagAppl;
    @Column(name = "FLAG_CALC", length = 2)
    private String flagCalc;
    @Column(name = "FLAG_PUNTI", length = 2)
    private String flagPunti;
    @Column(name = "GIORNI_SET", length = 7)
    private String giorniSet;
    @Column(name = "LIMITE_MAX_1", precision = 10, scale = 3)
    private BigDecimal limiteMax1;
    @Column(name = "LIMITE_MAX_2", precision = 10, scale = 3)
    private BigDecimal limiteMax2;
    @Column(name = "LIMITE_MAX_3", precision = 10, scale = 3)
    private BigDecimal limiteMax3;
    @Column(name = "LIMITE_MAX_4", precision = 10, scale = 3)
    private BigDecimal limiteMax4;
    @Column(name = "LIMITE_MAX_5", precision = 10, scale = 3)
    private BigDecimal limiteMax5;
    @Column(name = "LIMITE_MAX_6", precision = 10, scale = 3)
    private BigDecimal limiteMax6;
    @Column(name = "LIMITE_MAX_7", precision = 10, scale = 3)
    private BigDecimal limiteMax7;
    @Column(name = "LIMITE_MAX_8", precision = 10, scale = 3)
    private BigDecimal limiteMax8;
    @Column(name = "LIMITE_MAX_9", precision = 10, scale = 3)
    private BigDecimal limiteMax9;
    @Column(name = "LIMITE_MAX_PR", precision = 10, scale = 3)
    private BigDecimal limiteMaxPr;
    @Column(name = "LIMITE_MIN_1", precision = 10, scale = 3)
    private BigDecimal limiteMin1;
    @Column(name = "LIMITE_MIN_2", precision = 10, scale = 3)
    private BigDecimal limiteMin2;
    @Column(name = "LIMITE_MIN_3", precision = 10, scale = 3)
    private BigDecimal limiteMin3;
    @Column(name = "LIMITE_MIN_4", precision = 10, scale = 3)
    private BigDecimal limiteMin4;
    @Column(name = "LIMITE_MIN_5", precision = 10, scale = 3)
    private BigDecimal limiteMin5;
    @Column(name = "LIMITE_MIN_6", precision = 10, scale = 3)
    private BigDecimal limiteMin6;
    @Column(name = "LIMITE_MIN_7", precision = 10, scale = 3)
    private BigDecimal limiteMin7;
    @Column(name = "LIMITE_MIN_8", precision = 10, scale = 3)
    private BigDecimal limiteMin8;
    @Column(name = "LIMITE_MIN_9", precision = 10, scale = 3)
    private BigDecimal limiteMin9;
    @Column(name = "LIMITE_MIN_PR", precision = 10, scale = 3)
    private BigDecimal limiteMinPr;
    @Column(name = "N_RACCOLTA_PUNTI")
    private Integer nRaccoltaPunti;
    @Column(name = "NUM_PROM_PROTOTIPO")
    private Integer numPromPrototipo;
    @Column(name = "ORA_FIN")
    private Integer oraFin;
    @Column(name = "ORA_INIZ")
    private Integer oraIniz;
    @Column(name = "PASSO_1", precision = 10, scale = 3)
    private BigDecimal passo1;
    @Column(name = "PASSO_2", precision = 10, scale = 3)
    private BigDecimal passo2;
    @Column(name = "PASSO_3", precision = 10, scale = 3)
    private BigDecimal passo3;
    @Column(name = "PASSO_4", precision = 10, scale = 3)
    private BigDecimal passo4;
    @Column(name = "PASSO_5", precision = 10, scale = 3)
    private BigDecimal passo5;
    @Column(name = "PASSO_6", precision = 10, scale = 3)
    private BigDecimal passo6;
    @Column(name = "PASSO_7", precision = 10, scale = 3)
    private BigDecimal passo7;
    @Column(name = "PASSO_8", precision = 10, scale = 3)
    private BigDecimal passo8;
    @Column(name = "PASSO_9", precision = 10, scale = 3)
    private BigDecimal passo9;
    @Column(name = "QTA_1", precision = 10, scale = 3)
    private BigDecimal qta1;
    @Column(name = "QTA_2", precision = 10, scale = 3)
    private BigDecimal qta2;
    @Column(name = "QTA_3", precision = 10, scale = 3)
    private BigDecimal qta3;
    @Column(name = "QTA_4", precision = 10, scale = 3)
    private BigDecimal qta4;
    @Column(name = "QTA_5", precision = 10, scale = 3)
    private BigDecimal qta5;
    @Column(name = "QTA_6", precision = 10, scale = 3)
    private BigDecimal qta6;
    @Column(name = "QTA_7", precision = 10, scale = 3)
    private BigDecimal qta7;
    @Column(name = "QTA_8", precision = 10, scale = 3)
    private BigDecimal qta8;
    @Column(name = "QTA_9", precision = 10, scale = 3)
    private BigDecimal qta9;
    @Column(name = "TIPO_APPL", length = 3)
    private String tipoAppl;
    @Column(name = "TIPO_OFF", length = 2)
    private String tipoOff;
    @Column(name = "TIPO_PAG", length = 2)
    private String tipoPag;
    @Column(name = "TIPO_TES", length = 5)
    private String tipoTes;
    @Column(name = "TOLLERANZA", precision = 10, scale = 3)
    private BigDecimal tolleranza;
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

    public BigDecimal getTolleranza() {
        return tolleranza;
    }

    public void setTolleranza(BigDecimal tolleranza) {
        this.tolleranza = tolleranza;
    }

    public String getTipoTes() {
        return tipoTes;
    }

    public void setTipoTes(String tipoTes) {
        this.tipoTes = tipoTes;
    }

    public String getTipoPag() {
        return tipoPag;
    }

    public void setTipoPag(String tipoPag) {
        this.tipoPag = tipoPag;
    }

    public String getTipoOff() {
        return tipoOff;
    }

    public void setTipoOff(String tipoOff) {
        this.tipoOff = tipoOff;
    }

    public String getTipoAppl() {
        return tipoAppl;
    }

    public void setTipoAppl(String tipoAppl) {
        this.tipoAppl = tipoAppl;
    }

    public BigDecimal getQta9() {
        return qta9;
    }

    public void setQta9(BigDecimal qta9) {
        this.qta9 = qta9;
    }

    public BigDecimal getQta8() {
        return qta8;
    }

    public void setQta8(BigDecimal qta8) {
        this.qta8 = qta8;
    }

    public BigDecimal getQta7() {
        return qta7;
    }

    public void setQta7(BigDecimal qta7) {
        this.qta7 = qta7;
    }

    public BigDecimal getQta6() {
        return qta6;
    }

    public void setQta6(BigDecimal qta6) {
        this.qta6 = qta6;
    }

    public BigDecimal getQta5() {
        return qta5;
    }

    public void setQta5(BigDecimal qta5) {
        this.qta5 = qta5;
    }

    public BigDecimal getQta4() {
        return qta4;
    }

    public void setQta4(BigDecimal qta4) {
        this.qta4 = qta4;
    }

    public BigDecimal getQta3() {
        return qta3;
    }

    public void setQta3(BigDecimal qta3) {
        this.qta3 = qta3;
    }

    public BigDecimal getQta2() {
        return qta2;
    }

    public void setQta2(BigDecimal qta2) {
        this.qta2 = qta2;
    }

    public BigDecimal getQta1() {
        return qta1;
    }

    public void setQta1(BigDecimal qta1) {
        this.qta1 = qta1;
    }

    public BigDecimal getPasso9() {
        return passo9;
    }

    public void setPasso9(BigDecimal passo9) {
        this.passo9 = passo9;
    }

    public BigDecimal getPasso8() {
        return passo8;
    }

    public void setPasso8(BigDecimal passo8) {
        this.passo8 = passo8;
    }

    public BigDecimal getPasso7() {
        return passo7;
    }

    public void setPasso7(BigDecimal passo7) {
        this.passo7 = passo7;
    }

    public BigDecimal getPasso6() {
        return passo6;
    }

    public void setPasso6(BigDecimal passo6) {
        this.passo6 = passo6;
    }

    public BigDecimal getPasso5() {
        return passo5;
    }

    public void setPasso5(BigDecimal passo5) {
        this.passo5 = passo5;
    }

    public BigDecimal getPasso4() {
        return passo4;
    }

    public void setPasso4(BigDecimal passo4) {
        this.passo4 = passo4;
    }

    public BigDecimal getPasso3() {
        return passo3;
    }

    public void setPasso3(BigDecimal passo3) {
        this.passo3 = passo3;
    }

    public BigDecimal getPasso2() {
        return passo2;
    }

    public void setPasso2(BigDecimal passo2) {
        this.passo2 = passo2;
    }

    public BigDecimal getPasso1() {
        return passo1;
    }

    public void setPasso1(BigDecimal passo1) {
        this.passo1 = passo1;
    }

    public Integer getOraIniz() {
        return oraIniz;
    }

    public void setOraIniz(Integer oraIniz) {
        this.oraIniz = oraIniz;
    }

    public Integer getOraFin() {
        return oraFin;
    }

    public void setOraFin(Integer oraFin) {
        this.oraFin = oraFin;
    }

    public Integer getNumPromPrototipo() {
        return numPromPrototipo;
    }

    public void setNumPromPrototipo(Integer numPromPrototipo) {
        this.numPromPrototipo = numPromPrototipo;
    }

    public Integer getNRaccoltaPunti() {
        return nRaccoltaPunti;
    }

    public void setNRaccoltaPunti(Integer nRaccoltaPunti) {
        this.nRaccoltaPunti = nRaccoltaPunti;
    }

    public BigDecimal getLimiteMinPr() {
        return limiteMinPr;
    }

    public void setLimiteMinPr(BigDecimal limiteMinPr) {
        this.limiteMinPr = limiteMinPr;
    }

    public BigDecimal getLimiteMin9() {
        return limiteMin9;
    }

    public void setLimiteMin9(BigDecimal limiteMin9) {
        this.limiteMin9 = limiteMin9;
    }

    public BigDecimal getLimiteMin8() {
        return limiteMin8;
    }

    public void setLimiteMin8(BigDecimal limiteMin8) {
        this.limiteMin8 = limiteMin8;
    }

    public BigDecimal getLimiteMin7() {
        return limiteMin7;
    }

    public void setLimiteMin7(BigDecimal limiteMin7) {
        this.limiteMin7 = limiteMin7;
    }

    public BigDecimal getLimiteMin6() {
        return limiteMin6;
    }

    public void setLimiteMin6(BigDecimal limiteMin6) {
        this.limiteMin6 = limiteMin6;
    }

    public BigDecimal getLimiteMin5() {
        return limiteMin5;
    }

    public void setLimiteMin5(BigDecimal limiteMin5) {
        this.limiteMin5 = limiteMin5;
    }

    public BigDecimal getLimiteMin4() {
        return limiteMin4;
    }

    public void setLimiteMin4(BigDecimal limiteMin4) {
        this.limiteMin4 = limiteMin4;
    }

    public BigDecimal getLimiteMin3() {
        return limiteMin3;
    }

    public void setLimiteMin3(BigDecimal limiteMin3) {
        this.limiteMin3 = limiteMin3;
    }

    public BigDecimal getLimiteMin2() {
        return limiteMin2;
    }

    public void setLimiteMin2(BigDecimal limiteMin2) {
        this.limiteMin2 = limiteMin2;
    }

    public BigDecimal getLimiteMin1() {
        return limiteMin1;
    }

    public void setLimiteMin1(BigDecimal limiteMin1) {
        this.limiteMin1 = limiteMin1;
    }

    public BigDecimal getLimiteMaxPr() {
        return limiteMaxPr;
    }

    public void setLimiteMaxPr(BigDecimal limiteMaxPr) {
        this.limiteMaxPr = limiteMaxPr;
    }

    public BigDecimal getLimiteMax9() {
        return limiteMax9;
    }

    public void setLimiteMax9(BigDecimal limiteMax9) {
        this.limiteMax9 = limiteMax9;
    }

    public BigDecimal getLimiteMax8() {
        return limiteMax8;
    }

    public void setLimiteMax8(BigDecimal limiteMax8) {
        this.limiteMax8 = limiteMax8;
    }

    public BigDecimal getLimiteMax7() {
        return limiteMax7;
    }

    public void setLimiteMax7(BigDecimal limiteMax7) {
        this.limiteMax7 = limiteMax7;
    }

    public BigDecimal getLimiteMax6() {
        return limiteMax6;
    }

    public void setLimiteMax6(BigDecimal limiteMax6) {
        this.limiteMax6 = limiteMax6;
    }

    public BigDecimal getLimiteMax5() {
        return limiteMax5;
    }

    public void setLimiteMax5(BigDecimal limiteMax5) {
        this.limiteMax5 = limiteMax5;
    }

    public BigDecimal getLimiteMax4() {
        return limiteMax4;
    }

    public void setLimiteMax4(BigDecimal limiteMax4) {
        this.limiteMax4 = limiteMax4;
    }

    public BigDecimal getLimiteMax3() {
        return limiteMax3;
    }

    public void setLimiteMax3(BigDecimal limiteMax3) {
        this.limiteMax3 = limiteMax3;
    }

    public BigDecimal getLimiteMax2() {
        return limiteMax2;
    }

    public void setLimiteMax2(BigDecimal limiteMax2) {
        this.limiteMax2 = limiteMax2;
    }

    public BigDecimal getLimiteMax1() {
        return limiteMax1;
    }

    public void setLimiteMax1(BigDecimal limiteMax1) {
        this.limiteMax1 = limiteMax1;
    }

    public String getGiorniSet() {
        return giorniSet;
    }

    public void setGiorniSet(String giorniSet) {
        this.giorniSet = giorniSet;
    }

    public String getFlagPunti() {
        return flagPunti;
    }

    public void setFlagPunti(String flagPunti) {
        this.flagPunti = flagPunti;
    }

    public String getFlagCalc() {
        return flagCalc;
    }

    public void setFlagCalc(String flagCalc) {
        this.flagCalc = flagCalc;
    }

    public String getFlagAppl() {
        return flagAppl;
    }

    public void setFlagAppl(String flagAppl) {
        this.flagAppl = flagAppl;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDatasellinin() {
        return datasellinin;
    }

    public void setDatasellinin(String datasellinin) {
        this.datasellinin = datasellinin;
    }

    public String getDatasellinfin() {
        return datasellinfin;
    }

    public void setDatasellinfin(String datasellinfin) {
        this.datasellinfin = datasellinfin;
    }

    public String getDataIniz() {
        return dataIniz;
    }

    public void setDataIniz(String dataIniz) {
        this.dataIniz = dataIniz;
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

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getCodiceIniz() {
        return codiceIniz;
    }

    public void setCodiceIniz(String codiceIniz) {
        this.codiceIniz = codiceIniz;
    }

    public String getCodTessera() {
        return codTessera;
    }

    public void setCodTessera(String codTessera) {
        this.codTessera = codTessera;
    }

    public String getCodPanel() {
        return codPanel;
    }

    public void setCodPanel(String codPanel) {
        this.codPanel = codPanel;
    }

    public Integer getCodMessaggio() {
        return codMessaggio;
    }

    public void setCodMessaggio(Integer codMessaggio) {
        this.codMessaggio = codMessaggio;
    }

    public String getCodCoupon() {
        return codCoupon;
    }

    public void setCodCoupon(String codCoupon) {
        this.codCoupon = codCoupon;
    }

    public PromCompKey getId() {
        return id;
    }

    public void setId(PromCompKey id) {
        this.id = id;
    }
}