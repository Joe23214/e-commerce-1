package com.company.ecomShop.view.ag;

import com.company.ecomShop.entity.Ag;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "ags/:id", layout = MainView.class)
@ViewController(id = "Ag.detail")
@ViewDescriptor(path = "ag-detail-view.xml")
@EditedEntityContainer("agDc")
public class AgDetailView extends StandardDetailView<Ag> {
}