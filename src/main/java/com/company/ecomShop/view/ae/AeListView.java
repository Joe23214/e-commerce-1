package com.company.ecomShop.view.ae;

import com.company.ecomShop.entity.Ae;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "aes", layout = MainView.class)
@ViewController(id = "Ae.list")
@ViewDescriptor(path = "ae-list-view.xml")
@LookupComponent("aesDataGrid")
@DialogMode(width = "64em")
public class AeListView extends StandardListView<Ae> {
}