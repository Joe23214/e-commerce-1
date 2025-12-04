package com.company.ecomShop.view.salmg;

import com.company.ecomShop.entity.Salmg;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "salmgs/:id", layout = MainView.class)
@ViewController(id = "Salmg.detail")
@ViewDescriptor(path = "salmg-detail-view.xml")
@EditedEntityContainer("salmgDc")
public class SalmgDetailView extends StandardDetailView<Salmg> {
}