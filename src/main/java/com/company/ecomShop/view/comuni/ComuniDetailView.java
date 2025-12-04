package com.company.ecomShop.view.comuni;

import com.company.ecomShop.entity.Comuni;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "comunis/:id", layout = MainView.class)
@ViewController(id = "Comuni.detail")
@ViewDescriptor(path = "comuni-detail-view.xml")
@EditedEntityContainer("comuniDc")
public class ComuniDetailView extends StandardDetailView<Comuni> {
}