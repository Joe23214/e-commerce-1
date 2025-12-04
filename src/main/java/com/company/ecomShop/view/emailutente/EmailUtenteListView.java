package com.company.ecomShop.view.emailutente;

import com.company.ecomShop.entity.EmailUtente;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "emailUtentes", layout = MainView.class)
@ViewController(id = "EmailUtente.list")
@ViewDescriptor(path = "email-utente-list-view.xml")
@LookupComponent("emailUtentesDataGrid")
@DialogMode(width = "64em")
public class EmailUtenteListView extends StandardListView<EmailUtente> {
}