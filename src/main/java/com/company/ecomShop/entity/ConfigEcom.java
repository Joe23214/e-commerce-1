package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "CONFIG_ECOM")
@Entity
public class ConfigEcom {
    @Column(name = "CFG", nullable = false, length = 1)
    @Id
    private String cfg;

    @Column(name = "CAP", length = 6)
    private String cap;

    @Column(name = "CITTA", length = 30)
    private String citta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "INDIRIZZO", length = 200)
    private String indirizzo;

    @Column(name = "LINK_FACEBOOK", length = 200)
    private String linkFacebook;

    @Column(name = "LINK_INSTAGRAM", length = 200)
    private String linkInstagram;

    @Column(name = "LINK_TIKTOK", length = 200)
    private String linkTiktok;

    @Column(name = "MAIL", nullable = false, length = 200)
    private String mail;

    @Column(name = "ORE_SCARTO")
    private Integer oreScarto;

    @Column(name = "P_IVA", length = 12)
    private String pIva;

    @Column(name = "PEC", length = 30)
    private String pec;

    @Column(name = "PROVINCIA", length = 10)
    private String provincia;

    @Column(name = "QTA_GIACENZA_GIALLO")
    private Integer qtaGiacenzaGiallo;

    @Column(name = "QTA_GIACENZA_ROSSO")
    private Integer qtaGiacenzaRosso;

    @Column(name = "RAG_SOC", length = 200)
    private String ragSoc;

    @Column(name = "SDI", length = 30)
    private String sdi;

    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    @Column(name = "TESTO_FOOTER", length = 250)
    private String testoFooter;

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

    public String getTestoFooter() {
        return testoFooter;
    }

    public void setTestoFooter(String testoFooter) {
        this.testoFooter = testoFooter;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSdi() {
        return sdi;
    }

    public void setSdi(String sdi) {
        this.sdi = sdi;
    }

    public String getRagSoc() {
        return ragSoc;
    }

    public void setRagSoc(String ragSoc) {
        this.ragSoc = ragSoc;
    }

    public Integer getQtaGiacenzaRosso() {
        return qtaGiacenzaRosso;
    }

    public void setQtaGiacenzaRosso(Integer qtaGiacenzaRosso) {
        this.qtaGiacenzaRosso = qtaGiacenzaRosso;
    }

    public Integer getQtaGiacenzaGiallo() {
        return qtaGiacenzaGiallo;
    }

    public void setQtaGiacenzaGiallo(Integer qtaGiacenzaGiallo) {
        this.qtaGiacenzaGiallo = qtaGiacenzaGiallo;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPec() {
        return pec;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public String getPIva() {
        return pIva;
    }

    public void setPIva(String pIva) {
        this.pIva = pIva;
    }

    public Integer getOreScarto() {
        return oreScarto;
    }

    public void setOreScarto(Integer oreScarto) {
        this.oreScarto = oreScarto;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLinkTiktok() {
        return linkTiktok;
    }

    public void setLinkTiktok(String linkTiktok) {
        this.linkTiktok = linkTiktok;
    }

    public String getLinkInstagram() {
        return linkInstagram;
    }

    public void setLinkInstagram(String linkInstagram) {
        this.linkInstagram = linkInstagram;
    }

    public String getLinkFacebook() {
        return linkFacebook;
    }

    public void setLinkFacebook(String linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

}