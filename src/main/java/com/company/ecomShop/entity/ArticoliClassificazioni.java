package com.company.ecomShop.entity;

import com.vaadin.flow.component.html.Image;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Entity;

@JmixEntity
public class ArticoliClassificazioni {

    private String classi;

    private String codiceArticolo;

    private String descrizione;

    private String prezzo;

    private String immagine;


    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getClassi() {
        return classi;
    }

    public void setClassi(String classi) {
        this.classi = classi;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }
}