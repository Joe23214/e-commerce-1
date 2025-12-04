package com.company.ecomShop.view.recuperapassword;


import com.company.ecomShop.entity.PdcPassword;
import com.company.ecomShop.entity.User;
import com.company.ecomShop.view.home.Home;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.JmixPasswordField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Route(value = "recupera-password")
@ViewController(id = "RecuperaPassword")
@ViewDescriptor(path = "recupera-password.xml")
public class RecuperaPassword extends StandardView {


    private String codiceConto="";
    @ViewComponent
    private JmixPasswordField ConfermaPassword;
    @ViewComponent
    private JmixPasswordField nuovaPassword;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {

        List<String> messageParams = event.getQueryParameters().getParameters().get("codice");
        codiceConto=messageParams.get(0);
    }

    @Subscribe(id = "conferma", subject = "clickListener")
    public void onConfermaClick(final ClickEvent<JmixButton> event) {
       if(nuovaPassword.getValue().equalsIgnoreCase(ConfermaPassword.getValue())){
           List<PdcPassword> pdc = dataManager.load(PdcPassword.class).
                   query("select e from PdcPassword  e where e.codice =:codice").
                   parameter("codice", codiceConto).
                   list();

           List<User> user = dataManager.load(User.class).
                   query("select e from User  e where  e.username =:username").
                   parameter("username", pdc.get(0).getUsername()).
                   list();

           pdc.get(0).setPassword(nuovaPassword.getValue());
           user.get(0).setPassword(passwordEncoder.encode(nuovaPassword.getValue()));
           dataManager.save(pdc.get(0));
           dataManager.save(user.get(0));
           viewNavigators.view(this, Home.class).navigate();
       }else{
           notifications.create("Le password non sono uguali").show();
       }

    }





}