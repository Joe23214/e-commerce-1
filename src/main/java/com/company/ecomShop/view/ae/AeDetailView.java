package com.company.ecomShop.view.ae;

import com.company.ecomShop.entity.Ae;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "aes/:id", layout = MainView.class)
@ViewController(id = "Ae.detail")
@ViewDescriptor(path = "ae-detail-view.xml")
@EditedEntityContainer("aeDc")
public class AeDetailView extends StandardDetailView<Ae> {



}