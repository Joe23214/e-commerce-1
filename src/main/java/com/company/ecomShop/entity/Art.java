package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

import java.util.Date;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "ART")
@Entity
public class Art {
    @Column(name = "CODICE_ARTICOLO", nullable = false, length = 25)
    @Id
    private String codiceArticolo;
    @Column(name = "AGENTE_ABITUALE", length = 5)
    private String agenteAbituale;
    @Column(name = "CLASSE_ESTESA", length = 30)
    private String classeEstesa;
    @Column(name = "cod_classe_0", length = 10)
    private String codClasse0;
    @Column(name = "cod_classe_1", length = 10)
    private String codClasse1;
    @Column(name = "cod_classe_2", length = 10)
    private String codClasse2;
    @Column(name = "cod_classe_3", length = 10)
    private String codClasse3;
    @Column(name = "cod_classe_4", length = 10)
    private String codClasse4;
    @Column(name = "cod_classe_5", length = 10)
    private String codClasse5;
    @Column(name = "cod_classe_6", length = 10)
    private String codClasse6;
    @Column(name = "cod_classe_7", length = 10)
    private String codClasse7;
    @Column(name = "cod_classe_8", length = 10)
    private String codClasse8;
    @Column(name = "cod_classe_9", length = 10)
    private String codClasse9;
    @Column(name = "CODICE_CONFEZIONE", length = 25)
    private String codiceConfezione;
    @Column(name = "CODICE_IVA", length = 3)
    private String codiceIva;
    @Column(name = "CODICE_OMAGGIO", length = 25)
    private String codiceOmaggio;
    @Column(name = "COEF_UM_SEC")
    private Double coefUmSec;
    @Column(name = "COEFF_CONVERSIONE")
    private Double coeffConversione;
    @Column(name = "CONTO_VENDITA")
    private Integer contoVendita;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DATA_CREAZIONE", length = 10)
    private String dataCreazione;
    @Column(name = "DATA_ULTIMO_AGGIO", length = 8)
    private String dataUltimoAggio;
    @Column(name = "DESCRIZIONE", length = 40)
    private String descrizione;
    @Column(name = "DESCRIZIONE_CASSE", length = 20)
    private String descrizioneCasse;
    @Column(name = "DESCRIZIONE_SUPPL", length = 60)
    private String descrizioneSuppl;
    @Column(name = "FLAG_BILANCIA", length = 1)
    private String flagBilancia;
    @Column(name = "FLAG_LIBERO", length = 1)
    private String flagLibero;
    @Column(name = "FLAG_REGALO", length = 1)
    private String flagRegalo;
    @Column(name = "FORNITORE_ABITUALE", length = 13)
    private String fornitoreAbituale;
    @Column(name = "LISTINO_ACQUISTO", length = 1)
    private String listinoAcquisto;
    @Column(name = "LISTINO_VENDITA", length = 1)
    private String listinoVendita;
    @Column(name = "MULTIPLO_RIORDINO")
    private Double multiploRiordino;
    @Column(name = "N_RACCOLTA_PUNTI")
    private Integer nRaccoltaPunti;
    @Column(name = "NOME_IMMAGINE", length = 50)
    private String nomeImmagine;
    @Column(name = "NOTE", length = 11)
    private String note;
    @Column(name = "NUMERO_BOLLINI")
    private Double numeroBollini;
    @Column(name = "NUMERO_OFFERTA")
    private Integer numeroOfferta;
    @Column(name = "PERC_SCARTO")
    private Double percScarto;
    @Column(name = "PESO_LORDO")
    private Double pesoLordo;
    @Column(name = "PREZZO_CONS")
    private Double prezzoCons;
    @Column(name = "QUANT_MULTIPACK")
    private Integer quantMultipack;
    @Column(name = "RICAR_PREZZO")
    private Integer ricarPrezzo;
    @Column(name = "SCONTO_BASE")
    private Double scontoBase;
    @Column(name = "TIPO_FRONTALINI", length = 2)
    private String tipoFrontalini;
    @Column(name = "TIPO_OFFERTA", length = 1)
    private String tipoOfferta;
    @Column(name = "UM_CONFEZIONE", length = 3)
    private String umConfezione;
    @Column(name = "UM_PESO_LORDO", length = 3)
    private String umPesoLordo;
    @Column(name = "UM_PRIMARIA", length = 3)
    private String umPrimaria;
    @Column(name = "UM_SECONDARIA", length = 3)
    private String umSecondaria;
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

    public String getUmSecondaria() {
        return umSecondaria;
    }

    public void setUmSecondaria(String umSecondaria) {
        this.umSecondaria = umSecondaria;
    }

    public String getUmPrimaria() {
        return umPrimaria;
    }

    public void setUmPrimaria(String umPrimaria) {
        this.umPrimaria = umPrimaria;
    }

    public String getUmPesoLordo() {
        return umPesoLordo;
    }

    public void setUmPesoLordo(String umPesoLordo) {
        this.umPesoLordo = umPesoLordo;
    }

    public String getUmConfezione() {
        return umConfezione;
    }

    public void setUmConfezione(String umConfezione) {
        this.umConfezione = umConfezione;
    }

    public String getTipoOfferta() {
        return tipoOfferta;
    }

    public void setTipoOfferta(String tipoOfferta) {
        this.tipoOfferta = tipoOfferta;
    }

    public String getTipoFrontalini() {
        return tipoFrontalini;
    }

    public void setTipoFrontalini(String tipoFrontalini) {
        this.tipoFrontalini = tipoFrontalini;
    }

    public Double getScontoBase() {
        return scontoBase;
    }

    public void setScontoBase(Double scontoBase) {
        this.scontoBase = scontoBase;
    }

    public Integer getRicarPrezzo() {
        return ricarPrezzo;
    }

    public void setRicarPrezzo(Integer ricarPrezzo) {
        this.ricarPrezzo = ricarPrezzo;
    }

    public Integer getQuantMultipack() {
        return quantMultipack;
    }

    public void setQuantMultipack(Integer quantMultipack) {
        this.quantMultipack = quantMultipack;
    }

    public Double getPrezzoCons() {
        return prezzoCons;
    }

    public void setPrezzoCons(Double prezzoCons) {
        this.prezzoCons = prezzoCons;
    }

    public Double getPesoLordo() {
        return pesoLordo;
    }

    public void setPesoLordo(Double pesoLordo) {
        this.pesoLordo = pesoLordo;
    }

    public Double getPercScarto() {
        return percScarto;
    }

    public void setPercScarto(Double percScarto) {
        this.percScarto = percScarto;
    }

    public Integer getNumeroOfferta() {
        return numeroOfferta;
    }

    public void setNumeroOfferta(Integer numeroOfferta) {
        this.numeroOfferta = numeroOfferta;
    }

    public Double getNumeroBollini() {
        return numeroBollini;
    }

    public void setNumeroBollini(Double numeroBollini) {
        this.numeroBollini = numeroBollini;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNomeImmagine() {
        return nomeImmagine;
    }

    public void setNomeImmagine(String nomeImmagine) {
        this.nomeImmagine = nomeImmagine;
    }

    public Integer getNRaccoltaPunti() {
        return nRaccoltaPunti;
    }

    public void setNRaccoltaPunti(Integer nRaccoltaPunti) {
        this.nRaccoltaPunti = nRaccoltaPunti;
    }

    public Double getMultiploRiordino() {
        return multiploRiordino;
    }

    public void setMultiploRiordino(Double multiploRiordino) {
        this.multiploRiordino = multiploRiordino;
    }

    public String getListinoVendita() {
        return listinoVendita;
    }

    public void setListinoVendita(String listinoVendita) {
        this.listinoVendita = listinoVendita;
    }

    public String getListinoAcquisto() {
        return listinoAcquisto;
    }

    public void setListinoAcquisto(String listinoAcquisto) {
        this.listinoAcquisto = listinoAcquisto;
    }

    public String getFornitoreAbituale() {
        return fornitoreAbituale;
    }

    public void setFornitoreAbituale(String fornitoreAbituale) {
        this.fornitoreAbituale = fornitoreAbituale;
    }

    public String getFlagRegalo() {
        return flagRegalo;
    }

    public void setFlagRegalo(String flagRegalo) {
        this.flagRegalo = flagRegalo;
    }

    public String getFlagLibero() {
        return flagLibero;
    }

    public void setFlagLibero(String flagLibero) {
        this.flagLibero = flagLibero;
    }

    public String getFlagBilancia() {
        return flagBilancia;
    }

    public void setFlagBilancia(String flagBilancia) {
        this.flagBilancia = flagBilancia;
    }

    public String getDescrizioneSuppl() {
        return descrizioneSuppl;
    }

    public void setDescrizioneSuppl(String descrizioneSuppl) {
        this.descrizioneSuppl = descrizioneSuppl;
    }

    public String getDescrizioneCasse() {
        return descrizioneCasse;
    }

    public void setDescrizioneCasse(String descrizioneCasse) {
        this.descrizioneCasse = descrizioneCasse;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDataUltimoAggio() {
        return dataUltimoAggio;
    }

    public void setDataUltimoAggio(String dataUltimoAggio) {
        this.dataUltimoAggio = dataUltimoAggio;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
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

    public Integer getContoVendita() {
        return contoVendita;
    }

    public void setContoVendita(Integer contoVendita) {
        this.contoVendita = contoVendita;
    }

    public Double getCoeffConversione() {
        return coeffConversione;
    }

    public void setCoeffConversione(Double coeffConversione) {
        this.coeffConversione = coeffConversione;
    }

    public Double getCoefUmSec() {
        return coefUmSec;
    }

    public void setCoefUmSec(Double coefUmSec) {
        this.coefUmSec = coefUmSec;
    }

    public String getCodiceOmaggio() {
        return codiceOmaggio;
    }

    public void setCodiceOmaggio(String codiceOmaggio) {
        this.codiceOmaggio = codiceOmaggio;
    }

    public String getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(String codiceIva) {
        this.codiceIva = codiceIva;
    }

    public String getCodiceConfezione() {
        return codiceConfezione;
    }

    public void setCodiceConfezione(String codiceConfezione) {
        this.codiceConfezione = codiceConfezione;
    }

    public String getCodClasse9() {
        return codClasse9;
    }

    public void setCodClasse9(String codClasse9) {
        this.codClasse9 = codClasse9;
    }

    public String getCodClasse8() {
        return codClasse8;
    }

    public void setCodClasse8(String codClasse8) {
        this.codClasse8 = codClasse8;
    }

    public String getCodClasse7() {
        return codClasse7;
    }

    public void setCodClasse7(String codClasse7) {
        this.codClasse7 = codClasse7;
    }

    public String getCodClasse6() {
        return codClasse6;
    }

    public void setCodClasse6(String codClasse6) {
        this.codClasse6 = codClasse6;
    }

    public String getCodClasse5() {
        return codClasse5;
    }

    public void setCodClasse5(String codClasse5) {
        this.codClasse5 = codClasse5;
    }

    public String getCodClasse4() {
        return codClasse4;
    }

    public void setCodClasse4(String codClasse4) {
        this.codClasse4 = codClasse4;
    }

    public String getCodClasse3() {
        return codClasse3;
    }

    public void setCodClasse3(String codClasse3) {
        this.codClasse3 = codClasse3;
    }

    public String getCodClasse2() {
        return codClasse2;
    }

    public void setCodClasse2(String codClasse2) {
        this.codClasse2 = codClasse2;
    }

    public String getCodClasse1() {
        return codClasse1;
    }

    public void setCodClasse1(String codClasse1) {
        this.codClasse1 = codClasse1;
    }

    public String getCodClasse0() {
        return codClasse0;
    }

    public void setCodClasse0(String codClasse0) {
        this.codClasse0 = codClasse0;
    }

    public String getClasseEstesa() {
        return classeEstesa;
    }

    public void setClasseEstesa(String classeEstesa) {
        this.classeEstesa = classeEstesa;
    }

    public String getAgenteAbituale() {
        return agenteAbituale;
    }

    public void setAgenteAbituale(String agenteAbituale) {
        this.agenteAbituale = agenteAbituale;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }
}