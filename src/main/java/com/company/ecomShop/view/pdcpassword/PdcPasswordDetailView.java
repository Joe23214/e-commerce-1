package com.company.ecomShop.view.pdcpassword;

import com.company.ecomShop.entity.PdcPassword;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "pdcPasswords/:id", layout = MainView.class)
@ViewController(id = "PdcPassword.detail")
@ViewDescriptor(path = "pdc-password-detail-view.xml")
@EditedEntityContainer("pdcPasswordDc")
public class PdcPasswordDetailView extends StandardDetailView<PdcPassword> {
}