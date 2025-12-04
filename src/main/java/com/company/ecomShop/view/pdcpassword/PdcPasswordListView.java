package com.company.ecomShop.view.pdcpassword;

import com.company.ecomShop.entity.PdcPassword;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "pdcPasswords", layout = MainView.class)
@ViewController(id = "PdcPassword.list")
@ViewDescriptor(path = "pdc-password-list-view.xml")
@LookupComponent("pdcPasswordsDataGrid")
@DialogMode(width = "64em")
public class PdcPasswordListView extends StandardListView<PdcPassword> {
}