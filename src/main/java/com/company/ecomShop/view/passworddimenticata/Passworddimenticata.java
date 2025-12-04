package com.company.ecomShop.view.passworddimenticata;


import com.company.ecomShop.entity.Pdc;
import com.company.ecomShop.entity.PdcPassword;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.Resources;
import io.jmix.email.EmailException;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.textfield.JmixEmailField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@Route(value = "passworddimenticata")
@ViewController(id = "Passworddimenticata")
@ViewDescriptor(path = "passworddimenticata.xml")
public class Passworddimenticata extends StandardView {

    @Autowired
    private Emailer emailer;

    @Autowired
    protected Resources resources;
    @ViewComponent
    private JmixEmailField emailUtente;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private JmixButton conferma;

    @Value("${myapp.session.attributeName}")
    private String attributeName;

    private void sendByEmail() throws IOException, EmailException {

        List<Pdc> pdc = dataManager.load(Pdc.class).
                query("select e from Pdc  e where e.email =:email").
                parameter("email", emailUtente.getValue()).
                list();

        if(pdc.size()>0) {



            EmailInfo emailInfo = EmailInfoBuilder.create()
                    .setAddresses(emailUtente.getValue())
                    .setSubject("Recupero Password")
                    .setFrom("recuperopassword@mail.com")
                    .setBody("Clicca Il Seguente Link per reimpostare la Password" + " "
                            +"https://"+attributeName+"/recupera-password?codice="+pdc.get(0).getCodiceConto()+"")
                    .build();
            emailer.sendEmail(emailInfo);
        }else{
            notifications.create("Indirizzo Email non Trovato").show();
        }
    }

    @Subscribe(id = "conferma", subject = "clickListener")
    public void onConfermaClick(final ClickEvent<JmixButton> event) {
        try {
            sendByEmail();
        }catch (Exception ex){
            ex.printStackTrace();

        }
    }
}