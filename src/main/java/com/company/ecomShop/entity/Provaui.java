package com.company.ecomShop.entity;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Entity;

@JmixEntity
public class Provaui  {
    @JmixId
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}