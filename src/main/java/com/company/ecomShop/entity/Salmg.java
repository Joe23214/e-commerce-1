package com.company.ecomShop.entity;

import com.company.ecomShop.entity.key.SalmgCompKey;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


@JmixEntity
@Store(name = "marketnewstoredb")
@Table(name = "SALMG")
@Entity
public class Salmg {
    @EmbeddedId
    private SalmgCompKey id;
    @Column(name = "COSTO_ULTIMO", precision = 10, scale = 3)
    private BigDecimal costoUltimo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    @Column(name = "DATA_ULTIMA_ARC", length = 10)
    private String dataUltimaArc;
    @Column(name = "DATA_ULTIMO_CAR", length = 10)
    private String dataUltimoCar;
    @Column(name = "DATA_ULTIMO_SCAR", length = 10)
    private String dataUltimoScar;
    @Column(name = "FLAG1", length = 1)
    private String flag1;
    @Column(name = "FLAG10", length = 1)
    private String flag10;
    @Column(name = "FLAG2", length = 1)
    private String flag2;
    @Column(name = "FLAG3", length = 1)
    private String flag3;
    @Column(name = "FLAG4", length = 1)
    private String flag4;
    @Column(name = "FLAG5", length = 1)
    private String flag5;
    @Column(name = "FLAG6", length = 1)
    private String flag6;
    @Column(name = "FLAG7", length = 1)
    private String flag7;
    @Column(name = "FLAG8", length = 1)
    private String flag8;
    @Column(name = "FLAG9", length = 1)
    private String flag9;
    @Column(name = "GIAC_DINAM_AT", precision = 14, scale = 3)
    private BigDecimal giacDinamAt;
    @Column(name = "GIAC_ULT_ARCHIV", precision = 14, scale = 3)
    private BigDecimal giacUltArchiv;
    @Column(name = "QUA_BOLLE_FATT", precision = 14, scale = 3)
    private BigDecimal quaBolleFatt;
    @Column(name = "QUA_BOLLE_FATT_UA", precision = 14, scale = 3)
    private BigDecimal quaBolleFattUa;
    @Column(name = "QUA_CAR_ULT_ARC", precision = 14, scale = 3)
    private BigDecimal quaCarUltArc;
    @Column(name = "QUA_IMPEGN_CLI_UA", precision = 14, scale = 3)
    private BigDecimal quaImpegnCliUa;
    @Column(name = "QUA_ORD_FORNIT_UA", precision = 14, scale = 3)
    private BigDecimal quaOrdFornitUa;
    @Column(name = "QUA_SCA_ULT_ARC", precision = 14, scale = 3)
    private BigDecimal quaScaUltArc;
    @Column(name = "QUANT_IMPEGN_CLI", precision = 14, scale = 3)
    private BigDecimal quantImpegnCli;
    @Column(name = "QUANT_ORD_FORNIT", precision = 14, scale = 3)
    private BigDecimal quantOrdFornit;
    @Column(name = "TOTALE_QUA_CARIC", precision = 14, scale = 3)
    private BigDecimal totaleQuaCaric;
    @Column(name = "TOTALE_QUA_SCARIC", precision = 14, scale = 3)
    private BigDecimal totaleQuaScaric;
    @Column(name = "TOTALE_VAL_CARIC", precision = 14, scale = 3)
    private BigDecimal totaleValCaric;
    @Column(name = "TOTALE_VAL_SCARIC", precision = 14, scale = 3)
    private BigDecimal totaleValScaric;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    private Date updateTs;
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;
    @Column(name = "VAL_CARIC_ULT_ARC", precision = 14, scale = 3)
    private BigDecimal valCaricUltArc;
    @Column(name = "VAL_SCA_ULT_ARC", precision = 14, scale = 3)
    private BigDecimal valScaUltArc;
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getValScaUltArc() {
        return valScaUltArc;
    }

    public void setValScaUltArc(BigDecimal valScaUltArc) {
        this.valScaUltArc = valScaUltArc;
    }

    public BigDecimal getValCaricUltArc() {
        return valCaricUltArc;
    }

    public void setValCaricUltArc(BigDecimal valCaricUltArc) {
        this.valCaricUltArc = valCaricUltArc;
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

    public BigDecimal getTotaleValScaric() {
        return totaleValScaric;
    }

    public void setTotaleValScaric(BigDecimal totaleValScaric) {
        this.totaleValScaric = totaleValScaric;
    }

    public BigDecimal getTotaleValCaric() {
        return totaleValCaric;
    }

    public void setTotaleValCaric(BigDecimal totaleValCaric) {
        this.totaleValCaric = totaleValCaric;
    }

    public BigDecimal getTotaleQuaScaric() {
        return totaleQuaScaric;
    }

    public void setTotaleQuaScaric(BigDecimal totaleQuaScaric) {
        this.totaleQuaScaric = totaleQuaScaric;
    }

    public BigDecimal getTotaleQuaCaric() {
        return totaleQuaCaric;
    }

    public void setTotaleQuaCaric(BigDecimal totaleQuaCaric) {
        this.totaleQuaCaric = totaleQuaCaric;
    }

    public BigDecimal getQuantOrdFornit() {
        return quantOrdFornit;
    }

    public void setQuantOrdFornit(BigDecimal quantOrdFornit) {
        this.quantOrdFornit = quantOrdFornit;
    }

    public BigDecimal getQuantImpegnCli() {
        return quantImpegnCli;
    }

    public void setQuantImpegnCli(BigDecimal quantImpegnCli) {
        this.quantImpegnCli = quantImpegnCli;
    }

    public BigDecimal getQuaScaUltArc() {
        return quaScaUltArc;
    }

    public void setQuaScaUltArc(BigDecimal quaScaUltArc) {
        this.quaScaUltArc = quaScaUltArc;
    }

    public BigDecimal getQuaOrdFornitUa() {
        return quaOrdFornitUa;
    }

    public void setQuaOrdFornitUa(BigDecimal quaOrdFornitUa) {
        this.quaOrdFornitUa = quaOrdFornitUa;
    }

    public BigDecimal getQuaImpegnCliUa() {
        return quaImpegnCliUa;
    }

    public void setQuaImpegnCliUa(BigDecimal quaImpegnCliUa) {
        this.quaImpegnCliUa = quaImpegnCliUa;
    }

    public BigDecimal getQuaCarUltArc() {
        return quaCarUltArc;
    }

    public void setQuaCarUltArc(BigDecimal quaCarUltArc) {
        this.quaCarUltArc = quaCarUltArc;
    }

    public BigDecimal getQuaBolleFattUa() {
        return quaBolleFattUa;
    }

    public void setQuaBolleFattUa(BigDecimal quaBolleFattUa) {
        this.quaBolleFattUa = quaBolleFattUa;
    }

    public BigDecimal getQuaBolleFatt() {
        return quaBolleFatt;
    }

    public void setQuaBolleFatt(BigDecimal quaBolleFatt) {
        this.quaBolleFatt = quaBolleFatt;
    }

    public BigDecimal getGiacUltArchiv() {
        return giacUltArchiv;
    }

    public void setGiacUltArchiv(BigDecimal giacUltArchiv) {
        this.giacUltArchiv = giacUltArchiv;
    }

    public BigDecimal getGiacDinamAt() {
        return giacDinamAt;
    }

    public void setGiacDinamAt(BigDecimal giacDinamAt) {
        this.giacDinamAt = giacDinamAt;
    }

    public String getFlag9() {
        return flag9;
    }

    public void setFlag9(String flag9) {
        this.flag9 = flag9;
    }

    public String getFlag8() {
        return flag8;
    }

    public void setFlag8(String flag8) {
        this.flag8 = flag8;
    }

    public String getFlag7() {
        return flag7;
    }

    public void setFlag7(String flag7) {
        this.flag7 = flag7;
    }

    public String getFlag6() {
        return flag6;
    }

    public void setFlag6(String flag6) {
        this.flag6 = flag6;
    }

    public String getFlag5() {
        return flag5;
    }

    public void setFlag5(String flag5) {
        this.flag5 = flag5;
    }

    public String getFlag4() {
        return flag4;
    }

    public void setFlag4(String flag4) {
        this.flag4 = flag4;
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

    public String getFlag10() {
        return flag10;
    }

    public void setFlag10(String flag10) {
        this.flag10 = flag10;
    }

    public String getFlag1() {
        return flag1;
    }

    public void setFlag1(String flag1) {
        this.flag1 = flag1;
    }

    public String getDataUltimoScar() {
        return dataUltimoScar;
    }

    public void setDataUltimoScar(String dataUltimoScar) {
        this.dataUltimoScar = dataUltimoScar;
    }

    public String getDataUltimoCar() {
        return dataUltimoCar;
    }

    public void setDataUltimoCar(String dataUltimoCar) {
        this.dataUltimoCar = dataUltimoCar;
    }

    public String getDataUltimaArc() {
        return dataUltimaArc;
    }

    public void setDataUltimaArc(String dataUltimaArc) {
        this.dataUltimaArc = dataUltimaArc;
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

    public BigDecimal getCostoUltimo() {
        return costoUltimo;
    }

    public void setCostoUltimo(BigDecimal costoUltimo) {
        this.costoUltimo = costoUltimo;
    }

    public SalmgCompKey getId() {
        return id;
    }

    public void setId(SalmgCompKey id) {
        this.id = id;
    }
}