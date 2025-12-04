package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DbView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@DbView
@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "PDC_PASSWORD")
@Entity
public class PdcPassword {
    @Column(name = "CODICE", nullable = false, length = 10)
    @Id
    private String codice;
    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "USERNAME", length = 50)
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}