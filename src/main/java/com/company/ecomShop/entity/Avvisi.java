package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.*;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "AVVISI")
@Entity
public class Avvisi {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "COD_ART", nullable = false, length = 15)
    private String codArt;

    @Column(name = "COD_CONTO", nullable = false, length = 20)
    private String codConto;

    @Column(name = "ID_UTENTE", nullable = false, length = 50)
    private String idUtente;

    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public String getCodConto() {
        return codConto;
    }

    public void setCodConto(String codConto) {
        this.codConto = codConto;
    }

    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codArt) {
        this.codArt = codArt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}