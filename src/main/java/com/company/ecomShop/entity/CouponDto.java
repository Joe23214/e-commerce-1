package com.company.ecomShop.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity
public class CouponDto {

    private String codiceCupon;
    private String messaggio;
    private String dataInizioPromo;
    private String dataFinePromo;

    public CouponDto() {
    }

    public CouponDto(String codiceCupon, String messaggio, String dataInizioPromo, String dataFinePromo) {
        this.codiceCupon = codiceCupon;
        this.messaggio = messaggio;
        this.dataInizioPromo = dataInizioPromo;
        this.dataFinePromo = dataFinePromo;
    }

    public String getCodiceCupon() {
        return codiceCupon;
    }

    public void setCodiceCupon(String codiceCupon) {
        this.codiceCupon = codiceCupon;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
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
}