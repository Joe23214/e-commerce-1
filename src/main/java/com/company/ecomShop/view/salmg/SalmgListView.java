package com.company.ecomShop.view.salmg;

import com.company.ecomShop.entity.Salmg;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "salmgs", layout = MainView.class)
@ViewController(id = "Salmg.list")
@ViewDescriptor(path = "salmg-list-view.xml")
@LookupComponent("salmgsDataGrid")
@DialogMode(width = "64em")
public class SalmgListView extends StandardListView<Salmg> {
}