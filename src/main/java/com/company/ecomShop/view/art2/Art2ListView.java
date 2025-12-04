package com.company.ecomShop.view.art2;

import com.company.ecomShop.entity.Art2;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "art2s", layout = MainView.class)
@ViewController(id = "Art2.list")
@ViewDescriptor(path = "art2-list-view.xml")
@LookupComponent("art2sDataGrid")
@DialogMode(width = "64em")
public class Art2ListView extends StandardListView<Art2> {
}