package com.company.ecomShop.view.art;

import com.company.ecomShop.entity.Art;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "arts/:id", layout = MainView.class)
@ViewController(id = "Art.detail")
@ViewDescriptor(path = "art-detail-view.xml")
@EditedEntityContainer("artDc")
public class ArtDetailView extends StandardDetailView<Art> {

}