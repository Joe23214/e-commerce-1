package com.company.ecomShop.view.emailutente;

import com.company.ecomShop.entity.EmailUtente;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "emailUtentes/:id", layout = MainView.class)
@ViewController(id = "EmailUtente.detail")
@ViewDescriptor(path = "email-utente-detail-view.xml")
@EditedEntityContainer("emailUtenteDc")
public class EmailUtenteDetailView extends StandardDetailView<EmailUtente> {
}