package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.List;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Table(name = "MARKETNEWSTORE_ART_IMG")
@Entity
public class MarketnewstoreArtImg {
    @Column(name = "CODICE_ARTICOLO", nullable = false, length = 25)
    @Id
    private String codiceArticolo;
    @JoinTable(name = "ART_SCHEDA_N_FILE_DESCRIPTOR_LINK",
            joinColumns = @JoinColumn(name = "CODICE_ARTICOLO", referencedColumnName = "CODICE_ARTICOLO"),
            inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID", referencedColumnName = "ID"))
    @ManyToMany


    private List<SysFile> sysFile;
    @JmixProperty
    @Transient
    private SysFile artImgPrincipale;

    public void setArtImgPrincipale(SysFile artImgPrincipale) {
        this.artImgPrincipale = artImgPrincipale;
    }

    public SysFile getArtImgPrincipale() {
        return artImgPrincipale;
    }

    public List<SysFile> getSysFile() {
        return sysFile;
    }

    public void setSysFile(List<SysFile> sysFile) {
        this.sysFile = sysFile;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }
}