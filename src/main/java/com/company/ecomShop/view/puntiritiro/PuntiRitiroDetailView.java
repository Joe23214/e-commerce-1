package com.company.ecomShop.view.puntiritiro;

import com.company.ecomShop.entity.PuntiRitiro;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "punti-ritiroes/:id", layout = MainView.class)
@ViewController(id = "PuntiRitiro.detail")
@ViewDescriptor(path = "punti-ritiro-detail-view.xml")
@EditedEntityContainer("puntiRitiroDc")
public class PuntiRitiroDetailView extends StandardDetailView<PuntiRitiro> {
}