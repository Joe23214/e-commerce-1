package com.company.ecomShop.view.ag;

import com.company.ecomShop.entity.Ag;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "ags", layout = MainView.class)
@ViewController(id = "Ag.list")
@ViewDescriptor(path = "ag-list-view.xml")
@LookupComponent("agsDataGrid")
@DialogMode(width = "64em")
public class AgListView extends StandardListView<Ag> {
}