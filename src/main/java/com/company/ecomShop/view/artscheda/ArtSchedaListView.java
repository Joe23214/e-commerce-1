package com.company.ecomShop.view.artscheda;

import com.company.ecomShop.entity.ArtScheda;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "artSchedas", layout = MainView.class)
@ViewController(id = "ArtScheda.list")
@ViewDescriptor(path = "art-scheda-list-view.xml")
@LookupComponent("artSchedasDataGrid")
@DialogMode(width = "64em")
public class ArtSchedaListView extends StandardListView<ArtScheda> {
}