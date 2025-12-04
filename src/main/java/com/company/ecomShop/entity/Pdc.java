package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "PDC")
@Entity
public class Pdc {
    @Column(name = "CODICE_CONTO", nullable = false, length = 13)
    @Id
    private String codiceConto;
    @Column(name = "CAP", length = 5)
    private String cap;
    @Column(name = "CATEGORIA_SCONTO", length = 3)
    private String categoriaSconto;
    @Column(name = "COD_CLIENTE_DEST", length = 13)
    private String codClienteDest;
    @Column(name = "COD_PORTO", length = 3)
    private String codPorto;
    @Column(name = "COD_VETTORE", length = 5)
    private String codVettore;
    @Column(name = "CODICE_AGENTE", length = 5)
    private String codiceAgente;
    @Column(name = "CODICE_ATTIVITA")
    private Integer codiceAttivita;
    @Column(name = "CODICE_BANCA", length = 11)
    private String codiceBanca;
    @Column(name = "CODICE_FISCALE", length = 20)
    private String codiceFiscale;
    @Column(name = "CODICE_PADRE", length = 13)
    private String codicePadre;
    @Column(name = "CODICE_PAGAMENTO", length = 4)
    private String codicePagamento;
    @Column(name = "CODVAL", length = 4)
    private String codval;
    @Column(name = "COGNOME_CLI")
    private String cognomeCli;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "CREDITO")
    private Double credito;
    @Column(name = "DATA10", length = 10)
    private String data10;
    @Column(name = "DATA_CREA", length = 8)
    private String dataCrea;
    @Column(name = "DATA_UL_EC", length = 10)
    private String dataUlEc;
    @Column(name = "DATAULTSCO", length = 10)
    private String dataultsco;
    @Column(name = "DESCRIZIONE", length = 50)
    private String descrizione;
    @Column(name = "DESCRIZIONE_SUPPL", length = 30)
    private String descrizioneSuppl;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FAX", length = 30)
    private String fax;
    @Column(name = "FLAG_1", length = 1)
    private String flag1;
    @Column(name = "FLAG_2", length = 1)
    private String flag2;
    @Column(name = "FLAG_3", length = 1)
    private String flag3;
    @Column(name = "FLAG_CLI_FORNIT", length = 1)
    private String flagCliFornit;
    @Column(name = "FLAG_PERSONA_FIS", length = 1)
    private String flagPersonaFis;
    @Column(name = "GRUPPO", length = 3)
    private String gruppo;
    @Column(name = "INDIRIZZO", length = 40)
    private String indirizzo;
    @Column(name = "INDIRIZZO2", length = 40)
    private String indirizzo2;
    @Column(name = "LISTINO_CL")
    private Integer listinoCl;
    @Column(name = "LOCALITA", length = 40)
    private String localita;
    @Column(name = "MIN_ORDINE")
    private Double minOrdine;
    @Column(name = "NAZIONE", length = 50)
    private String nazione;
    @Column(name = "NOME_CLI")
    private String nomeCli;
    @Column(name = "NOTE", length = 40)
    private String note;
    @Column(name = "PARTITA_IVA", length = 12)
    private String partitaIva;
    @Column(name = "PDV_CREA", length = 3)
    private String pdvCrea;
    @Column(name = "PEC")
    private String pec;
    @Column(name = "PREF_FAX", length = 10)
    private String prefFax;
    @Column(name = "PREF_TEL", length = 10)
    private String prefTel;
    @Column(name = "PROG_SPES_PREC")
    private Double progSpesPrec;
    @Column(name = "PROGR_SPESA")
    private Double progrSpesa;
    @Column(name = "PROVINCIA", length = 5)
    private String provincia;
    @Column(name = "SALDO")
    private Double saldo;
    @Column(name = "SCONTO_CATEG")
    private Double scontoCateg;
    @Column(name = "SCONTO_CLI")
    private Double scontoCli;
    @Column(name = "SCONTO_CLI_2")
    private Double scontoCli2;
    @Column(name = "TELEFONO", length = 30)
    private String telefono;
    @Column(name = "TOT_ACC_PREC")
    private Double totAccPrec;
    @Column(name = "TOT_BOLL")
    private Double totBoll;
    @Column(name = "ULTIMO_AGGIORN", length = 10)
    private String ultimoAggiorn;
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

    public String getUltimoAggiorn() {
        return ultimoAggiorn;
    }

    public void setUltimoAggiorn(String ultimoAggiorn) {
        this.ultimoAggiorn = ultimoAggiorn;
    }

    public Double getTotBoll() {
        return totBoll;
    }

    public void setTotBoll(Double totBoll) {
        this.totBoll = totBoll;
    }

    public Double getTotAccPrec() {
        return totAccPrec;
    }

    public void setTotAccPrec(Double totAccPrec) {
        this.totAccPrec = totAccPrec;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getScontoCli2() {
        return scontoCli2;
    }

    public void setScontoCli2(Double scontoCli2) {
        this.scontoCli2 = scontoCli2;
    }

    public Double getScontoCli() {
        return scontoCli;
    }

    public void setScontoCli(Double scontoCli) {
        this.scontoCli = scontoCli;
    }

    public Double getScontoCateg() {
        return scontoCateg;
    }

    public void setScontoCateg(Double scontoCateg) {
        this.scontoCateg = scontoCateg;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Double getProgrSpesa() {
        return progrSpesa;
    }

    public void setProgrSpesa(Double progrSpesa) {
        this.progrSpesa = progrSpesa;
    }

    public Double getProgSpesPrec() {
        return progSpesPrec;
    }

    public void setProgSpesPrec(Double progSpesPrec) {
        this.progSpesPrec = progSpesPrec;
    }

    public String getPrefTel() {
        return prefTel;
    }

    public void setPrefTel(String prefTel) {
        this.prefTel = prefTel;
    }

    public String getPrefFax() {
        return prefFax;
    }

    public void setPrefFax(String prefFax) {
        this.prefFax = prefFax;
    }

    public String getPec() {
        return pec;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public String getPdvCrea() {
        return pdvCrea;
    }

    public void setPdvCrea(String pdvCrea) {
        this.pdvCrea = pdvCrea;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNomeCli() {
        return nomeCli;
    }

    public void setNomeCli(String nomeCli) {
        this.nomeCli = nomeCli;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public Double getMinOrdine() {
        return minOrdine;
    }

    public void setMinOrdine(Double minOrdine) {
        this.minOrdine = minOrdine;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public Integer getListinoCl() {
        return listinoCl;
    }

    public void setListinoCl(Integer listinoCl) {
        this.listinoCl = listinoCl;
    }

    public String getIndirizzo2() {
        return indirizzo2;
    }

    public void setIndirizzo2(String indirizzo2) {
        this.indirizzo2 = indirizzo2;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getGruppo() {
        return gruppo;
    }

    public void setGruppo(String gruppo) {
        this.gruppo = gruppo;
    }

    public String getFlagPersonaFis() {
        return flagPersonaFis;
    }

    public void setFlagPersonaFis(String flagPersonaFis) {
        this.flagPersonaFis = flagPersonaFis;
    }

    public String getFlagCliFornit() {
        return flagCliFornit;
    }

    public void setFlagCliFornit(String flagCliFornit) {
        this.flagCliFornit = flagCliFornit;
    }

    public String getFlag3() {
        return flag3;
    }

    public void setFlag3(String flag3) {
        this.flag3 = flag3;
    }

    public String getFlag2() {
        return flag2;
    }

    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }

    public String getFlag1() {
        return flag1;
    }

    public void setFlag1(String flag1) {
        this.flag1 = flag1;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescrizioneSuppl() {
        return descrizioneSuppl;
    }

    public void setDescrizioneSuppl(String descrizioneSuppl) {
        this.descrizioneSuppl = descrizioneSuppl;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDataultsco() {
        return dataultsco;
    }

    public void setDataultsco(String dataultsco) {
        this.dataultsco = dataultsco;
    }

    public String getDataUlEc() {
        return dataUlEc;
    }

    public void setDataUlEc(String dataUlEc) {
        this.dataUlEc = dataUlEc;
    }

    public String getDataCrea() {
        return dataCrea;
    }

    public void setDataCrea(String dataCrea) {
        this.dataCrea = dataCrea;
    }

    public String getData10() {
        return data10;
    }

    public void setData10(String data10) {
        this.data10 = data10;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
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

    public String getCognomeCli() {
        return cognomeCli;
    }

    public void setCognomeCli(String cognomeCli) {
        this.cognomeCli = cognomeCli;
    }

    public String getCodval() {
        return codval;
    }

    public void setCodval(String codval) {
        this.codval = codval;
    }

    public String getCodicePagamento() {
        return codicePagamento;
    }

    public void setCodicePagamento(String codicePagamento) {
        this.codicePagamento = codicePagamento;
    }

    public String getCodicePadre() {
        return codicePadre;
    }

    public void setCodicePadre(String codicePadre) {
        this.codicePadre = codicePadre;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCodiceBanca() {
        return codiceBanca;
    }

    public void setCodiceBanca(String codiceBanca) {
        this.codiceBanca = codiceBanca;
    }

    public Integer getCodiceAttivita() {
        return codiceAttivita;
    }

    public void setCodiceAttivita(Integer codiceAttivita) {
        this.codiceAttivita = codiceAttivita;
    }

    public String getCodiceAgente() {
        return codiceAgente;
    }

    public void setCodiceAgente(String codiceAgente) {
        this.codiceAgente = codiceAgente;
    }

    public String getCodVettore() {
        return codVettore;
    }

    public void setCodVettore(String codVettore) {
        this.codVettore = codVettore;
    }

    public String getCodPorto() {
        return codPorto;
    }

    public void setCodPorto(String codPorto) {
        this.codPorto = codPorto;
    }

    public String getCodClienteDest() {
        return codClienteDest;
    }

    public void setCodClienteDest(String codClienteDest) {
        this.codClienteDest = codClienteDest;
    }

    public String getCategoriaSconto() {
        return categoriaSconto;
    }

    public void setCategoriaSconto(String categoriaSconto) {
        this.categoriaSconto = categoriaSconto;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCodiceConto() {
        return codiceConto;
    }

    public void setCodiceConto(String codiceConto) {
        this.codiceConto = codiceConto;
    }
}