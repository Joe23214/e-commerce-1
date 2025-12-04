package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class PromoDto {
    private String numeroOfferta;
    private String descrizione;
    private String dataInizioPromo;
    private String dataFinePromo;
    private String jsonData; // contiene il JSON completo (col_1, col_2, ecc.)

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public PromoDto(String numeroOfferta, String descrizione,
                    String dataInizioPromo, String dataFinePromo) {
        this.numeroOfferta = numeroOfferta;
        this.descrizione = descrizione;
        this.dataInizioPromo = dataInizioPromo;
        this.dataFinePromo = dataFinePromo;
    }

    public String getNumeroOfferta() {
        return numeroOfferta;
    }

    public void setNumeroOfferta(String numeroOfferta) {
        this.numeroOfferta = numeroOfferta;
    }


    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDataInizioPromo() {
        return dataInizioPromo;
    }

    public void setDataInizioPromo(String dataInizioPromo) {
        this.dataInizioPromo = dataInizioPromo;
    }

    public String getDataFinePromo() {
        return dataFinePromo;
    }

    public void setDataFinePromo(String dataFinePromo) {
        this.dataFinePromo = dataFinePromo;
    }

    @Override
    public String toString() {
        return "Promo{" +
                "numeroOfferta='" + numeroOfferta + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataInizio='" + dataInizioPromo + '\'' +
                ", dataFine='" + dataFinePromo + '\'' +
                '}';
    }
}
