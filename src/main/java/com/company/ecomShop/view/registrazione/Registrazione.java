package com.company.ecomShop.view.registrazione;


import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.volantino.Volantino;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.DataManager;
import io.jmix.core.Resources;
import io.jmix.core.session.SessionData;
import io.jmix.email.Emailer;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.checkboxgroup.JmixCheckboxGroup;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.textfield.JmixEmailField;
import io.jmix.flowui.component.textfield.JmixNumberField;
import io.jmix.flowui.component.textfield.JmixPasswordField;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.download.DownloadDataProvider;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import  com. vaadin. flow. component.html.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Route(value = "registrazione")
@ViewController(id = "Registrazione")
@ViewDescriptor(path = "registrazione.xml")

@AnonymousAllowed
public class Registrazione extends StandardView {


    @ViewComponent
    private Div piede;
    @ViewComponent
    private JmixPasswordField password;
    @ViewComponent
    private JmixEmailField email;
    @ViewComponent
    private JmixNumberField nTel;
    @ViewComponent
    private TypedTextField<Object> nome;
    @ViewComponent
    private TypedTextField<Object> cognome;
    @ViewComponent
    private TypedTextField<Object> rSociale;
    @ViewComponent
    private TypedTextField<Object> indirizzo1;
    @ViewComponent
    private TypedTextField<Object> indirizzo2;
    @ViewComponent
    private JmixComboBox<String> stato;
    @ViewComponent
    private JmixComboBox<String> provincia;
    @ViewComponent
    private JmixComboBox<String> citta;
    @ViewComponent
    private TypedTextField<Object> cap;
    @ViewComponent
    private TypedTextField<Object> pIva;
    @ViewComponent
    private TypedTextField<Object> pec;
    @ViewComponent
    private TypedTextField<Object> sdi;
    @ViewComponent
    private JmixCheckbox checkNormativa;
    @ViewComponent
    private JmixButton registrati;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private CollectionContainer<Comuni> comunisDc;
    @ViewComponent
    private CollectionLoader<Comuni> comunisDl;
    @ViewComponent
    private JmixPasswordField password1;
    @Autowired
    private GetDataService getDataService;
    @ViewComponent
    private TypedTextField<Object> pecCliente;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private TypedTextField<Object> usernameField;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;
    @ViewComponent
    private JmixButton infoPrivacy;
    @Autowired
    private Downloader downloader;
    @Autowired
    private Resources resources;
    @ViewComponent
    private Div authButton;
    @ViewComponent
    private HorizontalLayout socialicons1;
    @ViewComponent
    private JmixCheckboxGroup<Integer> listinoUtente;
    private Integer lastSelectedItem = null;
    @ViewComponent
    private JmixCheckbox riceviEmail;
    @ViewComponent
    private Paragraph spunta1;
    @ViewComponent
    private Paragraph spunta2;
    @Autowired
    private Emailer emailer;
    @Autowired
    private Dialogs dialogs;
    @ViewComponent
    private TypedTextField<Object> citofono;
    @ViewComponent
    private TypedTextField<Object> civico;
    @ViewComponent
    private TypedTextField<Object> scala;
    @ViewComponent
    private TypedTextField<Object> piano;
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private JmixButton Home;

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }
    @Subscribe
    public void onInit(final InitEvent event) {

        var session = sessionDataProvider.getObject();
        Object attr = session.getAttribute("accesso_app");
        if (attr != null && "1".equals(attr.toString())) {
            Home.setVisible(false);
        }

        String statoValore="italia";
        stato.setItems(statoValore);
        comunisDl.load();
        AtomicReference<String> provinciaControllo = new AtomicReference<>("");


        Map<String, String> map = new LinkedHashMap<>();
        Map<String, String> map1 = new LinkedHashMap<>();

        comunisDc.getItems().forEach(comuni -> {
            if(!provinciaControllo.get().equalsIgnoreCase(comuni.getProvincia())) {
                map.put(comuni.getProvincia(),comuni.getProvincia());
                provinciaControllo.set(comuni.getProvincia());
            }
        });
        comunisDc.getItems().forEach((comuni -> {
            map1.put(comuni.getComune(),comuni.getComune());

        }));

        ComponentUtils.setItemsMap(provincia, map);
        ComponentUtils.setItemsMap(citta, map1);
        Map<Integer, String> mapListino = new LinkedHashMap<>();
        mapListino.put(2, "Cliente Cash (Listino 2)");
        mapListino.put(3, "Cliente Cedi (Listino 3");
        ComponentUtils.setItemsMap(listinoUtente, mapListino);
        listinoUtente.addValueChangeListener(this::onCheckboxGroupValueChange);

        spunta1.setText( "Il sottoscritto dopo la registrazione effettuata,  autorizza il trattamento dei dati personali nel rispetto della vigente normativa sulla protezione dei dati personali ed, in particolare, il Regolamento Europeo per la protezione dei dati personali 2016/679, il d.lgs. 30/06/2003 n. 196 e successive modifiche e integrazioni, come modificato da ultimo dal d.lgs. 10/08/2018 n. 101. ( OBBLIGATORIO) \n" +
                " ");
        spunta2.setText(" Autorizzo il trattamento dei dati personali registrati al fine di ricevere aggiornamenti tramite mail e whatzupp in riferimento a nuovi prodotti ,  nuova giacenza prodotti ed informazioni di tipo commerciali dalla società venditrice.  in base al D. Lgs. 196/2003 e al Regolamento UE 2016/679 ( NON OBBLIGATORIO )\n" +
                " ");
        spunta1.getStyle().set("font-weight","bold");

        checkNormativa.setLabel(spunta1.getText());
        riceviEmail.setLabel(spunta2.getText());


    }

    private void onCheckboxGroupValueChange(AbstractField.ComponentValueChangeEvent<CheckboxGroup<Integer>, Set<Integer>> checkboxGroupSetComponentValueChangeEvent) {
        Set<Integer> selectedItems = checkboxGroupSetComponentValueChangeEvent.getValue();
        if (selectedItems != null && selectedItems.size() > 1) {
            // Remove all selections except the last one
            Integer newSelection = selectedItems.stream().filter(item -> !item.equals(lastSelectedItem)).findFirst().orElse(null);
            if (newSelection != null) {
                listinoUtente.setValue(Set.of(newSelection));
                lastSelectedItem = newSelection;
            }
        } else if (selectedItems != null && selectedItems.size() == 1) {
            lastSelectedItem = selectedItems.iterator().next();
        }
    }





  @Subscribe(id = "registrati", subject = "clickListener")
  public void onRegistratiClick(final ClickEvent<JmixButton> event) {
      if (verificaCampi()) {

          if (password.getValue().equals(password1.getValue())) {
              try {

                  // Creazione entità Pdc, PdcPassword, FatturapaPdc (come prima)
                  Pdc pdc = new Pdc();
                  pdc.setCodiceConto(getDataService.getProg("CW"));
                  pdc.setNomeCli(nome.getValue());
                  pdc.setCognomeCli(cognome.getValue());
                  pdc.setDescrizione(rSociale.getValue());
                  pdc.setIndirizzo(indirizzo1.getValue());
                  pdc.setNazione(stato.getValue());
                  pdc.setProvincia(provincia.getValue());
                  pdc.setCap(cap.getValue().toString());
                  pdc.setPartitaIva(pIva.getValue());
                  pdc.setPec(pecCliente.getValue());
                  pdc.setVersion(1);
                  pdc.setEmail(email.getValue());
                  pdc.setListinoCl(listinoUtente.getValue().iterator().next());

                  PdcPassword pdcPassword = new PdcPassword();
                  pdcPassword.setCodice(pdc.getCodiceConto());
                  pdcPassword.setPassword(password.getValue());
                  pdcPassword.setUsername(usernameField.getValue());

                  FatturapaPdc fatturapaPdc = new FatturapaPdc();
                  fatturapaPdc.setCodiceConto(pdc.getCodiceConto());
                  fatturapaPdc.setEmailPec(pec.getValue());
                  fatturapaPdc.setCoddestinazione(sdi.getValue());
                  fatturapaPdc.setVersion(1);

                  // Salvataggio dati
                  getDataService.creaUser(email.getValue(), password.getValue(),
                          pdc.getCognomeCli(), pdc.getNomeCli(), usernameField.getValue());
                  getDataService.createRole(usernameField.getValue());
                  dataManager.save(fatturapaPdc);
                  dataManager.save(pdcPassword);
                  dataManager.save(pdc);

                  // Salvataggio dati aggiuntivi
                  PdcDatiAggiuntivi datiAggiuntivi = dataManager.create(PdcDatiAggiuntivi.class);
                  datiAggiuntivi.setCodiceConto(pdc.getCodiceConto());
                  datiAggiuntivi.setPiano(piano.getValue());
                  datiAggiuntivi.setScala(scala.getValue());
                  datiAggiuntivi.setCitofono(citofono.getValue());
                  datiAggiuntivi.setCivico(civico.getValue());
                  datiAggiuntivi.setCreateTs(new Date());
                  datiAggiuntivi.setCreatedBy(usernameField.getValue());
                  datiAggiuntivi.setVersion(1);
                  dataManager.save(datiAggiuntivi);

                  // Invio email se selezionato
                  if (riceviEmail.getValue()) {
                      EmailUtente emailUtente = dataManager.create(EmailUtente.class);
                      emailUtente.setCodice(pdc.getCodiceConto());
                      emailUtente.setDataOra(new Date());
                      dataManager.save(emailUtente);

                      try {
                          MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
                          MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                          helper.setTo(email.getValue());
                          helper.setSubject("Registrazione Confermata");
                          helper.setFrom("noreply@shopEcom.it");

                          StringBuilder body = new StringBuilder();

                          body.append("<html><head>");
                          body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
                          body.append("<style>");
                          body.append("  @media only screen and (max-width:600px) {");
                          body.append("    .desktop-content {display:none !important;}");
                          body.append("    .mobile-card {display:block !important; width:100% !important; padding:15px; box-sizing:border-box; margin-bottom:15px; border:1px solid #e0e0e0; border-radius:8px;}");
                          body.append("  }");
                          body.append("  @media only screen and (min-width:601px) {");
                          body.append("    .mobile-card {display:none !important;}");
                          body.append("  }");
                          body.append("</style>");
                          body.append("</head><body style='font-family:Arial,sans-serif; color:#333; margin:0; padding:0; background-color:#f8f8f8;'>");

                          // Container principale
                          body.append("<div style='max-width:700px; margin:30px auto; background-color:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1); padding:30px;'>");

                          // Header con logo
                          body.append("<div style='text-align:center; margin-bottom:20px;'>")
                                  .append("<img src='cid:logo' alt='Logo Azienda' style='max-height:80px;'>")
                                  .append("</div>");

                          // Saluto personalizzato
                          body.append("<h2 style='color:#222; font-weight:600; margin-bottom:10px;'>Ciao ").append(nome.getValue()).append("!</h2>");
                          body.append("<p style='font-size:16px; line-height:1.5; color:#555;'>La tua registrazione è stata completata con successo.</p>");

                          // Desktop content
                          body.append("<div class='desktop-content' style='padding:15px; border:1px solid #e0e0e0; border-radius:8px; margin-bottom:15px;'>");
                          body.append("<p style='font-size:14px; color:#333;'>Le tue credenziali di accesso:</p>");
                          body.append("<p>Username: <strong>").append(usernameField.getValue()).append("</strong><br>");
                          body.append("Password: <strong>").append(password.getValue()).append("</strong></p>");
                          body.append("</div>");

                          // Mobile card
                          body.append("<div class='mobile-card'>")
                                  .append("<span style='font-weight:600;'>Username:</span> <span>").append(usernameField.getValue()).append("</span><br>")
                                  .append("<span style='font-weight:600;'>Password:</span> <span>").append(password.getValue()).append("</span>")
                                  .append("</div>");

                          // Footer elegante
                          body.append("<div style='border-top:1px solid #e0e0e0; padding:20px; text-align:center; font-size:14px; color:#999; margin-top:20px;'>")
                                  .append("<p>Grazie per aver scelto il nostro negozio.</p>")
                                  .append("<p><strong>Il team 2Emme Distribuzione</strong></p>")
                                  .append("</div>");

                          body.append("</div></body></html>");

                          helper.setText(body.toString(), true);

                          // Logo inline
                          helper.addInline("logo", carrelloService.getLogoResource(), "image/png");

                          javaMailSenderImpl.send(mimeMessage);

                          dialogs.createMessageDialog()
                                  .withHeader("Success")
                                  .withText("Cliente registrato. Effettuare il login")
                                  .open();

                      } catch (Exception e) {
                          notifications.create("Errore invio email").show();
                          e.printStackTrace();
                      }
                  } else {
                      notifications.create("Indirizzo Email non Trovato").show();
                  }

                  viewNavigators.view(this, Home.class).navigate();

              } catch (Exception ex) {
                  if (ex instanceof PersistenceException) {
                      notifications.create("Questo Username è già in uso").show();
                  } else {
                      ex.printStackTrace();
                  }
              }
          } else {
              notifications.create("Le Password non corrispondono").show();
          }

      } else {
          notifications.create("Inserisci tutti i campi obbligatori").show();
      }


}

    public boolean verificaCampi(){




        if((usernameField.getValue()!=null && !usernameField.getValue().toString().equalsIgnoreCase(""))&&
                (email.getValue()!=null && !email.getValue().toString().equalsIgnoreCase(""))&&
                (password.getValue()!=null && !password.getValue().toString().equalsIgnoreCase(""))&&
                        (password1.getValue()!=null && !password1.getValue().toString().equalsIgnoreCase(""))&&
                                (nTel.getValue()!=null && !nTel.getValue().toString().equalsIgnoreCase(""))&&
                                        (nome.getValue()!=null && !nome.getValue().toString().equalsIgnoreCase(""))&
                                                (cognome.getValue()!=null && !cognome.getValue().toString().equalsIgnoreCase(""))&&
                                                        (rSociale.getValue()!=null && !rSociale.getValue().toString().equalsIgnoreCase(""))&&
                                                                (indirizzo1.getValue()!=null && !indirizzo1.getValue().toString().equalsIgnoreCase(""))&&
                                                                        (stato.getValue()!=null && !stato.getValue().toString().equalsIgnoreCase(""))&&
                                                                                (provincia.getValue()!=null && !provincia.getValue().toString().equalsIgnoreCase(""))&&
                                                                                        (citta.getValue()!=null && !citta.getValue().toString().equalsIgnoreCase(""))&&
                                                                                                (cap.getValue()!=null && !cap.getValue().toString().equalsIgnoreCase(""))&&
                                                                                                        (pIva.getValue()!=null && !pIva.getValue().toString().equalsIgnoreCase(""))&&
// elimino il controllo su per o sdi in come da richiesta di Marco                                           ((!pec.isEmpty())||
//                                                                                                                        !sdi.isEmpty())&&
                                                                                                                            (pecCliente.getValue()!=null && !pecCliente.getValue().toString().equalsIgnoreCase(""))&& checkNormativa.getValue() && !listinoUtente.getValue().isEmpty()){


                                                                                                                    return true;
        }else{
           return false;

        }

    }

    @Subscribe(id = "infoPrivacy", subject = "clickListener")
    public void onInfoPrivacyClick(final ClickEvent<JmixButton> event) {
        String tomcatPath = System.getProperty("catalina.base");
        File file = new File(tomcatPath+"/shopEcomstore/INFORMATIVAARTT.pdf");



        try {
            FileInputStream inputStream = new FileInputStream(file);

            DownloadDataProvider dataProvider = (() -> inputStream);

            downloader.setShowNewWindow(false);
            downloader.download(dataProvider,file.getName());

        }catch (Exception exception){
            exception.printStackTrace();
        }




    }


    public void footer () {
        // Crea il layout principale del footer
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidthFull();
        footerLayout.getStyle()
                .set("background-color", "#5abd84")
                .set("color", "#ecf0f1")
               // .set("padding", "20px 10px")
                .set("text-align", "center");

        // Sezione 1: "Chi siamo"
        VerticalLayout aboutSection = new VerticalLayout();
        aboutSection.setSpacing(false);
        aboutSection.setPadding(false);
        aboutSection.addClassName("footer-section");

        H3 aboutTitle = new H3("Riferimenti");
        aboutTitle.getStyle().set("color", "#ecf0f1");
        VerticalLayout verticalLayout = new VerticalLayout();

        Image wAppIcon = new Image("https://i.postimg.cc/hjBRV391/icons8-whatsapp-50.png", "Wapp");
        wAppIcon.setWidth("26px");
        wAppIcon.setHeight("26px");
        wAppIcon.setClassName("wapp");
        wAppIcon.getStyle().set("filter", "invert(1)");
        Paragraph link1 = new Paragraph( "PER INFO E ORDINI SCRIVI SU WHATSAPP AL: +393479456912");
        link1.addClassName("footerLink");
        HorizontalLayout horizontalLayout = new HorizontalLayout(wAppIcon,link1);
        horizontalLayout.setClassName("Chiamaci");


        verticalLayout.add(new Paragraph("2EMME DISTRIBUZIONE SRL"),
                new Paragraph("VIALE DELL'ARTIGIANATO SECONDA TRAV DX"),
                new Paragraph("CAP 70026"),
                new Paragraph("MODUGNO (BA)"),
                new Paragraph("P.IVA 0823763072"),
                horizontalLayout);


        Div aboutContent = new Div(verticalLayout);
        aboutContent.getStyle().set("color", "#ecf0f1");
        aboutSection.add(aboutTitle, aboutContent);

        // Sezione 2: "Link utili"
        // Sezione 2: "Link utili"
        VerticalLayout linksSection = new VerticalLayout();
        linksSection.setSpacing(false);
        linksSection.setPadding(false);
        linksSection.addClassName("footer-section");

        H3 linksTitle = new H3("Contatti");
        linksTitle.getStyle().set("color", "#ecf0f1");

//        Image wAppIcon = new Image("https://i.postimg.cc/hjBRV391/icons8-whatsapp-50.png", "Wapp");
//        wAppIcon.setWidth("32px");
//        wAppIcon.setHeight("32px");
//        wAppIcon.getStyle().set("filter", "invert(1)");
//        Paragraph link1 = new Paragraph( "Chiama : +393479456912");
//        link1.addClassName("footerLink");
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.add(wAppIcon,link1);
//
//
//
//        linksSection.add(linksTitle, horizontalLayout);

        // Sezione 3: "Seguici"
        VerticalLayout socialSection = new VerticalLayout();
        socialSection.setSpacing(false);
        socialSection.setPadding(false);
        socialSection.addClassName("footer-section");

        H3 socialTitle = new H3("Seguici");
        socialTitle.getStyle().set("color", "#ecf0f1");

        HorizontalLayout socialIcons = new HorizontalLayout();
        socialIcons.setSpacing(true);
        socialIcons.addClassName("footer-social-icons");




        Image facebookIcon = new Image("#", "Facebook");
        facebookIcon.setWidth("32px");
        facebookIcon.setHeight("32px");
        facebookIcon.getStyle().set("filter", "invert(1)");
        facebookIcon.add(new Anchor("#"));

        Image instagramIcon = new Image("#", "Instagram");
        instagramIcon.setWidth("32px");
        instagramIcon.setHeight("32px");
        instagramIcon.getStyle().set("filter", "invert(1)");

        Image twitterIcon = new Image("#", "TicToc");
        twitterIcon.setWidth("32px");
        twitterIcon.setHeight("32px");
        twitterIcon.getStyle().set("filter", "invert(1)");

        facebookIcon.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });

        instagramIcon.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });

        twitterIcon.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });



        socialIcons.add(facebookIcon, instagramIcon, twitterIcon);

        socialSection.add(socialTitle, socialIcons);

        // Aggiungi tutte le sezioni al layout principale del footer
        footerLayout.add(aboutSection, linksSection, socialSection);

        // Aggiungi il footer al layout principale della pagina
        piede.removeAll();
        piede.add(footerLayout);
        if(sessionDataProvider.getObject().getAttribute("accesso_app") != null ){
            String valore = sessionDataProvider.getObject().getAttribute("accesso_app").toString();
            if(valore != null && valore == "1"){
                piede.setVisible(false);
            }
        }
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        footer();
        Image facebookIcon1 = new Image("#", "Facebook");
        facebookIcon1.setWidth("32px");
        facebookIcon1.setHeight("32px");
        facebookIcon1.setClassName("icon");

        Image instagramIcon1 = new Image("#", "Instagram");
        instagramIcon1.setWidth("32px");
        instagramIcon1.setHeight("32px");
        instagramIcon1.setClassName("icon");

        Image twitterIcon1 = new Image("#", "TicToc");
        twitterIcon1.setWidth("32px");
        twitterIcon1.setHeight("32px");
        twitterIcon1.setClassName("icon");

        facebookIcon1.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });

        instagramIcon1.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });

        twitterIcon1.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open("#");
        });

        socialicons1.removeAll();
        socialicons1.setSpacing(true);
        socialicons1.addClassName("socialIcon1");

        JmixButton bottoneRosso = new JmixButton();
        bottoneRosso.setText("Volantino");
        bottoneRosso.setClassName("Volantino");
        bottoneRosso.addClickListener(event1 -> {
            viewNavigators.view(this, Volantino.class).navigate();
        });
        //socialicons1.add(facebookIcon1, instagramIcon1, twitterIcon1);
        socialicons1.add(bottoneRosso); //Ho aggiunto


    }

    @Subscribe("provincia")
    public void onProvinciaComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixComboBox<?>, ?> event) {
        Map<String, String> map1 = new LinkedHashMap<>();



        List<Comuni> list = dataManager.load(Comuni.class).
                query("select e from Comuni e where e.provincia =:provincia order by e.comune").
                parameter("provincia", event.getValue()).
                list();


        for(int i = 0 ;i< list.size();i++){
            map1.put(list.get(i).getComune(),list.get(i).getComune());
        }
        ComponentUtils.setItemsMap(citta, map1);

    }






}