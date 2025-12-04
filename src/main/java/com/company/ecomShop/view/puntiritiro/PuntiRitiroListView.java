package com.company.ecomShop.view.puntiritiro;

import com.company.ecomShop.entity.PuntiRitiro;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "punti-ritiroes", layout = MainView.class)
@ViewController(id = "PuntiRitiro.list")
@ViewDescriptor(path = "punti-ritiro-list-view.xml")
@LookupComponent("puntiRitiroesDataGrid")
@DialogMode(width = "64em")
public class PuntiRitiroListView extends StandardListView<PuntiRitiro> {
}