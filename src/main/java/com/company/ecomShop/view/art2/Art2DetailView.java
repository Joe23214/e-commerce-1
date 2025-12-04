package com.company.ecomShop.view.art2;

import com.company.ecomShop.entity.Art2;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "art2s/:id", layout = MainView.class)
@ViewController(id = "Art2.detail")
@ViewDescriptor(path = "art2-detail-view.xml")
@EditedEntityContainer("art2Dc")
public class Art2DetailView extends StandardDetailView<Art2> {
}