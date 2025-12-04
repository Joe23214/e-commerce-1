package com.company.ecomShop.view.art;

import com.company.ecomShop.entity.Art;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "arts", layout = MainView.class)
@ViewController(id = "Art.list")
@ViewDescriptor(path = "art-list-view.xml")
@LookupComponent("artsDataGrid")
@DialogMode(width = "64em")
public class ArtListView extends StandardListView<Art> {

}