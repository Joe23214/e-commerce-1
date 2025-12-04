package com.company.ecomShop.view.artscheda;

import com.company.ecomShop.entity.ArtScheda;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "artSchedas/:id", layout = MainView.class)
@ViewController(id = "ArtScheda.detail")
@ViewDescriptor(path = "art-scheda-detail-view.xml")
@EditedEntityContainer("artSchedaDc")
public class ArtSchedaDetailView extends StandardDetailView<ArtScheda> {
}