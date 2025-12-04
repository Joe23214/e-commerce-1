package com.company.ecomShop.app;

import com.company.ecomShop.view.secondapagina.SecondaPagina;
import com.flowingcode.vaadin.addons.carousel.Slide;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.router.QueryParameters;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyClickListener
            implements ComponentEventListener<ClickEvent<MenuItem>> {
    private static final Logger log = LoggerFactory.getLogger(MyClickListener.class);
    private final Notifications notifications;
    private final ViewNavigators viewNavigators;
    int count = 0;
    private Map<String,String> stringStringMap = new HashMap<>();
    private String codice ;

    public MyClickListener(Notifications notifications, ViewNavigators viewNavigators) {
        this.notifications = notifications;
        this.viewNavigators = viewNavigators;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }


    public Map<String, String> getStringStringMap() {
        return stringStringMap;
    }

    public void setStringStringMap(Map<String, String> stringStringMap) {
        this.stringStringMap = stringStringMap;
    }

    @Override
    public void onComponentEvent(ClickEvent<MenuItem> event) {
//            event.getSource()
//                    .setText("You have clicked me " + (++count) + " times");
        notifications.create(event.getSource().getText());

        String codice = stringStringMap.get(event.getSource().getText());

        setCodice(codice);



    }





//    @Override0
//    public void onComponentEvent(ClickEvent<Slide> event) {
////            event.getSource()
////                    .setText("You have clicked me " + (++count) + " times");
//
//        String a = "";
//
//    }

    }