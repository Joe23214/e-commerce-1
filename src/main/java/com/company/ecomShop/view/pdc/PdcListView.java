package com.company.ecomShop.view.pdc;

import com.company.ecomShop.entity.Pdc;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "pdcs")
@ViewController(id = "Pdc.list")
@ViewDescriptor(path = "pdc-list-view.xml")
@LookupComponent("pdcsDataGrid")
@DialogMode(width = "64em")
public class PdcListView extends StandardListView<Pdc> {
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }

}