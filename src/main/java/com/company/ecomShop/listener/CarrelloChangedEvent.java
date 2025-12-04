package com.company.ecomShop.listener;

public class CarrelloChangedEvent {
    private final long numeroProdotti;

    public CarrelloChangedEvent(long numeroProdotti) {
        this.numeroProdotti = numeroProdotti;
    }

    public long getNumeroProdotti() {
        return numeroProdotti;
    }
}
