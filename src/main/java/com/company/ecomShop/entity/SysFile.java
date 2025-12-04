package com.company.ecomShop.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Table(name = "SYS_FILE")
@Entity
public class SysFile {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETE_TS")
    private Date deleteTs;
    @Column(name = "DELETED_BY", length = 50)
    private String deletedBy;
    @Column(name = "EXT", length = 20)
    private String ext;
    @Column(name = "FILE_SIZE")
    private Long fileSize;
    @InstanceName
    @Column(name = "NAME", nullable = false, length = 500)
    private String name;
    @Column(name = "SYS_TENANT_ID")
    private String sysTenant;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;
    @Column(name = "VERSION", nullable = false)
    private Integer version;
    @JoinTable(name = "ART_SCHEDA_N_FILE_DESCRIPTOR_LINK",
            joinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "CODICE_ARTICOLO"))
    @ManyToMany
    private List<MarketnewstoreArtImg> marketnewstoreArtImg;




    
    public List<MarketnewstoreArtImg> getMarketnewstoreArtImg() {
        return marketnewstoreArtImg;
    }

    public void setMarketnewstoreArtImg(List<MarketnewstoreArtImg> marketnewstoreArtImg) {
        this.marketnewstoreArtImg = marketnewstoreArtImg;
    }

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

    public String getSysTenant() {
        return sysTenant;
    }

    public void setSysTenant(String sysTenant) {
        this.sysTenant = sysTenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeleteTs() {
        return deleteTs;
    }

    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}