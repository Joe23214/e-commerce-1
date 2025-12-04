package com.company.ecomShop.view.comuni;

import com.company.ecomShop.entity.Comuni;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "comunis", layout = MainView.class)
@ViewController(id = "Comuni.list")
@ViewDescriptor(path = "comuni-list-view.xml")
@LookupComponent("comunisDataGrid")
@DialogMode(width = "64em")
public class ComuniListView extends StandardListView<Comuni> {
}