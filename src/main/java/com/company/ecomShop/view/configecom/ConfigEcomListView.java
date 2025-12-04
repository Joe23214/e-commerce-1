package com.company.ecomShop.view.configecom;

import com.company.ecomShop.entity.ConfigEcom;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "config-ecoms", layout = MainView.class)
@ViewController(id = "ConfigEcom.list")
@ViewDescriptor(path = "config-ecom-list-view.xml")
@LookupComponent("configEcomsDataGrid")
@DialogMode(width = "64em")
public class ConfigEcomListView extends StandardListView<ConfigEcom> {
}