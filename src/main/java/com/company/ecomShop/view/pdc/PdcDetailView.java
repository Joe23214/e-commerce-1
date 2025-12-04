package com.company.ecomShop.view.pdc;

import com.company.ecomShop.entity.Pdc;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "pdcs/:id", layout = MainView.class)
@ViewController(id = "Pdc.detail")
@ViewDescriptor(path = "pdc-detail-view.xml")
@EditedEntityContainer("pdcDc")
public class PdcDetailView extends StandardDetailView<Pdc> {
}