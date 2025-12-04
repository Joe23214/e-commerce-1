package com.company.ecomShop.view.carrello;

import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.view.cupon.CuponView;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.company.ecomShop.view.volantino.Volantino;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.email.EmailException;
import io.jmix.email.Emailer;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.virtuallist.JmixVirtualList;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.sys.LogoutSupport;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Route(value = "carrello")
@ViewController(id = "Carrello")
@ViewDescriptor(path = "Carrello.xml")
public class Carrellov extends StandardView {
    @ViewComponent
    private CollectionLoader<CarrelloRiga> carrelloRigaDl;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private Div banner;
    @ViewComponent
    private Div authButton;
    @ViewComponent
    private Div piede;
    @ViewComponent
    private JmixButton accedi;
    @ViewComponent
    private JmixButton registrati;
    @Autowired
    private LogoutSupport logoutSupport;
    @ViewComponent
    private JmixButton logOut;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;
    String listino = "";
    @ViewComponent
    private JmixButton CarrelloIcona;
    @ViewComponent
    private HorizontalLayout socialicons1;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @ViewComponent
    private CollectionContainer<CarrelloRiga> carrelloRigaDc;
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private TypedTextField<Object> viaField;
    @ViewComponent
    private JmixComboBox<String> tipoPagamentoField;
    @ViewComponent
    private TypedTextField<Object> scalaField;
    @ViewComponent
    private TypedTextField<Object> regioneField;
    @ViewComponent
    private JmixComboBox<String> puntoVenditaField;
    @ViewComponent
    private TypedTextField<Object> provinciaField;
    @ViewComponent
    private TypedTextField<Object> pianoField;
    @ViewComponent
    private HorizontalLayout totaleBox;
    @ViewComponent
    private JmixTextArea noteField;
    @ViewComponent
    private TypedTextField<Object> nazioneField;
    @ViewComponent
    private JmixComboBox<TabellaFasce> fasciaOrariaField;
    @ViewComponent
    private TypedTextField<Object> totaleLabel;
    @ViewComponent
    private JmixFormLayout datiConsegnaLayout;
    @ViewComponent
    private TypedTextField<Object> civicoField;
    @ViewComponent
    private TypedTextField<Object> citofonoField;
    @ViewComponent
    private TypedTextField<Object> capField;
    @ViewComponent
    private JmixComboBox<String> tipoConsegnaField;
    @ViewComponent
    private TypedTextField<Object> cittaField;
    @ViewComponent
    private TypedTextField<Object> mailfield;
    private Span badge;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Emailer emailer;
    @Autowired
    private GetDataService getDataService;
    private Queue<String> notificationQueue = new LinkedList<>();
    private boolean notificationActive = false;
    @ViewComponent
    private JmixImage<Object> logo;
    @ViewComponent
    private JmixVirtualList<CarrelloRiga> CartItemList;
    @ViewComponent
    private Div desc;
    @ViewComponent
    private Div prez;
    @ViewComponent
    private Div quant;
    @ViewComponent
    private Div tot;
    @ViewComponent
    private Div um;
    @ViewComponent
    private Div intestazione;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private JmixButton promoBottone;
    @ViewComponent
    private JmixButton menuToggle1;
    @ViewComponent
    private JmixButton UtentiRegistrati;
    @ViewComponent
    private JmixButton invioNotifiche;
    @ViewComponent
    private JmixButton configEcom;
    boolean isMobile = UI.getCurrent().getInternals()
            .getExtendedClientDetails().getWindowInnerWidth() <= 480;
    @ViewComponent
    private Button adminHamburger;
    @ViewComponent
    private VerticalLayout adminMenu;

    @Subscribe(id = "adminHamburger", subject = "clickListener")
    public void onAdminHamburgerClick(final ClickEvent<JmixButton> event) {
        adminMenu.setVisible(!adminMenu.isVisible());
    }
    @Subscribe(id = "menuToggle1", subject = "clickListener")
    public void onMenuToggle1Click(final ClickEvent<JmixButton> event) {
        if (isMobile) {
            authButton.setVisible(!authButton.isVisible());
        }
    }
    @Subscribe(id = "promoBottone", subject = "clickListener")
    public void onPromoBottoneClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, CuponView.class).navigate();
    }

    private void loadLogo() {
        Path iconsDir = Paths.get("src/main/resources/META-INF/resources/icons");

        Path newLogoJpg = iconsDir.resolve("icon-new.jpg");
        Path oldLogoJpg = iconsDir.resolve("icon-old.jpg");
        Path originalLogo = iconsDir.resolve("icon.jpg");

        String defaultLogoPath = "/META-INF/resources/icons/icon.jpg";

        StreamResource logoResource;
        if (Files.exists(newLogoJpg)) {
            logoResource = new StreamResource("icon-new.jpg", () -> {
                try { return new FileInputStream(newLogoJpg.toFile()); }
                catch (FileNotFoundException e) { return getClass().getResourceAsStream(defaultLogoPath); }
            });
        } else if (Files.exists(oldLogoJpg)) {
            logoResource = new StreamResource("icon-old.jpg", () -> {
                try { return new FileInputStream(oldLogoJpg.toFile()); }
                catch (FileNotFoundException e) { return getClass().getResourceAsStream(defaultLogoPath); }
            });
        } else if (Files.exists(originalLogo)) {
            logoResource = new StreamResource("icon.jpg", () -> {
                try { return new FileInputStream(originalLogo.toFile()); }
                catch (FileNotFoundException e) { return getClass().getResourceAsStream(defaultLogoPath); }
            });
        } else {
            logoResource = new StreamResource("icon.jpg", () -> getClass().getResourceAsStream(defaultLogoPath));
        }

        logo.setSrc(logoResource);
        logo.setClassName("logo");
    }


    @Subscribe(id = "accedi", subject = "clickListener")
    public void onAccediClick(final ClickEvent<JmixButton> event) {

        viewNavigators.view(this, LoginView.class)
                .navigate();

    }


    @Subscribe(id = "logOut", subject = "clickListener")
    public void onLogOutClick(final ClickEvent<JmixButton> event) {
        sessionDataProvider.getObject().setAttribute("listino", null);
        UI.getCurrent().getPage().executeJs("if(window.FlutterChannel){FlutterChannel.postMessage('logout');}");
        logoutSupport.logout();

    }

    public void aggiornaCarrelloBadge(long numeroProdotti) {
        badge.setText(String.valueOf(numeroProdotti));
        CarrelloIcona.removeClassName("cart-grow");
        CarrelloIcona.addClassName("cart-grow");
    }
    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        User user = (User) currentAuthentication.getUser();
        carrelloRigaDl.setParameter("utente", user.getId().toString());
        carrelloRigaDl.load();


    }

   private void initPdcData() {
       User user = (User) currentAuthentication.getUser();
       if (user == null) return;

       // Recupera il PDC dell'utente
       Pdc pdc = carrelloService.getPdcByUsername(user.getUsername());

       if (pdc == null) {
           // Nessun PDC trovato: lascia campi vuoti
           nazioneField.setValue(null);
           regioneField.setValue(null);
           provinciaField.setValue(null);
           capField.setValue(null);
           viaField.setValue(null);
           civicoField.setValue(null);
           cittaField.setValue(null);
           citofonoField.setValue(null);
           scalaField.setValue(null);
           pianoField.setValue(null);
           mailfield.setValue(null);
           puntoVenditaField.setValue(null);
           noteField.setValue(null);
           return;
       }

       // Recupera dati aggiuntivi in modo sicuro
       Optional<PdcDatiAggiuntivi> pdcy = carrelloService.findAggiuntivi(pdc.getCodiceConto());

       // Popola campi dal PDC
       nazioneField.setValue(pdc.getNazione());
       provinciaField.setValue(pdc.getProvincia());
       cittaField.setValue(pdc.getLocalita());
       capField.setValue(pdc.getCap());
       viaField.setValue(pdc.getIndirizzo());
       mailfield.setValue(pdc.getEmail());
       puntoVenditaField.setValue(pdc.getPdvCrea());
       noteField.setValue(pdc.getNote());

       // Popola campi dai dati aggiuntivi se presenti
       civicoField.setValue(pdcy.map(PdcDatiAggiuntivi::getCivico).orElse(""));
       citofonoField.setValue(pdcy.map(PdcDatiAggiuntivi::getCitofono).orElse(""));
       scalaField.setValue(pdcy.map(PdcDatiAggiuntivi::getScala).orElse(""));
       pianoField.setValue(pdcy.map(PdcDatiAggiuntivi::getPiano).orElse(""));
   }

    public void footer() {
        // Crea il layout principale del footer
        ConfigEcom config = getDataService.getConfigEcom();
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidthFull();
        footerLayout.getStyle()
                .set("background-color", "#5abd84")
                .set("color", "#ecf0f1")
                //.set("padding", "20px 10px")
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
        Paragraph link1 = new Paragraph(config.getTestoFooter());
        link1.addClassName("footerLink");
        HorizontalLayout horizontalLayout = new HorizontalLayout(wAppIcon, link1);
        horizontalLayout.setClassName("Chiamaci");


        verticalLayout.add(new Paragraph(config.getRagSoc()),
                new Paragraph(config.getIndirizzo()),
                new Paragraph("CAP"+ config.getCap() ),
                new Paragraph(config.getCitta()),
                new Paragraph("P.IVA: "+ config.getPIva()),
                horizontalLayout);

        Div aboutContent = new Div(verticalLayout);
        aboutContent.getStyle().set("color", "#ecf0f1");
        aboutSection.add(aboutTitle, aboutContent);

        // Sezione 2: "Link utili"
        VerticalLayout linksSection = new VerticalLayout();
        linksSection.setSpacing(false);
        linksSection.setPadding(false);
        linksSection.addClassName("footer-section");

        H3 linksTitle = new H3("Contatti");
        linksTitle.getStyle().set("color", "#ecf0f1");

//            Image wAppIcon = new Image("https://i.postimg.cc/hjBRV391/icons8-whatsapp-50.png", "Wapp");
//            wAppIcon.setWidth("32px");
//            wAppIcon.setHeight("32px");
//            wAppIcon.getStyle().set("filter", "invert(1)");
//            Paragraph link1 = new Paragraph( "Chiama : +393479456912");
//            link1.addClassName("footerLink");
//            HorizontalLayout horizontalLayout = new HorizontalLayout();
//            horizontalLayout.add(wAppIcon,link1);


//            linksSection.add(linksTitle, horizontalLayout);

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
        String linkFacebook = config.getLinkFacebook() != null ? config.getLinkFacebook() : "";
        facebookIcon.add(new Anchor(linkFacebook));

        Image instagramIcon = new Image("#", "Instagram");
        instagramIcon.setWidth("32px");
        instagramIcon.setHeight("32px");
        instagramIcon.getStyle().set("filter", "invert(1)");

        Image twitterIcon = new Image("#", "TicToc");
        twitterIcon.setWidth("32px");
        twitterIcon.setHeight("32px");
        twitterIcon.getStyle().set("filter", "invert(1)");

        facebookIcon.addClickListener(imageClickEvent -> {
            UI.getCurrent().getPage().open(linkFacebook);
        });

        instagramIcon.addClickListener(imageClickEvent -> {
            String linkInsta = config.getLinkInstagram() != null ? config.getLinkInstagram() : "";
            UI.getCurrent().getPage().open(linkInsta);
        });

        twitterIcon.addClickListener(imageClickEvent -> {
            String linktic = config.getLinkTiktok() != null ? config.getLinkTiktok() : "";
            UI.getCurrent().getPage().open(linktic);
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

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick1(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }


    @Subscribe
    public void onReady(final ReadyEvent event) {
        totaleLabel.getStyle().set("color", "#ecf0f1");
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

    @Subscribe(id = "registrati", subject = "clickListener")
    public void onRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Registrazione.class).navigate();
    }

    @Subscribe(id = "carrelloRigaDc", target = Target.DATA_CONTAINER)
    public void onCarrelloRigaDcItemChange(InstanceContainer.ItemChangeEvent<CarrelloRiga> event) {
        aggiornaTotale();

        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
        aggiornaCarrelloBadge(numeroProdotti);
    }

    @Subscribe(id = "carrelloRigaDc", target = Target.DATA_CONTAINER)
    public void onCarrelloRigaDcCollectionChange(CollectionContainer.CollectionChangeEvent<CarrelloRiga> event) {
        aggiornaTotale();

        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
        aggiornaCarrelloBadge(numeroProdotti);
    }

    private void aggiornaTotale() {
        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        Double totale = carrelloService.getTotaleCarrello(carrello);
        totaleLabel.setValue(String.format("€ %.2f", totale));
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        boolean isFullAccess = currentAuthentication.getUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> "ROLE_system-full-access".equals(auth));
        adminHamburger.setVisible(isFullAccess);
        invioNotifiche.setVisible(isFullAccess);
        UtentiRegistrati.setVisible(isFullAccess);
        configEcom.setVisible(isFullAccess);
        loadLogo();
        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        carrelloService.aggiornaPrezziCarrelloAttivo();

        boolean isMobile = UI.getCurrent().getInternals()
                .getExtendedClientDetails().getWindowInnerWidth() <= 480;
        if (isMobile) {
            menuToggle1.setVisible(true);
            // Nasconde i bottoni interni come se il toggle fosse cliccato
            authButton.setVisible(false);
        }
        // Banner
        Image banneri = new Image("/images/Banner.png", "Banner");
        banneri.addClassName("site-banner");   // classe CSS per gestire desktop/mobile
        banneri.setWidth("100%");
        banneri.setHeight("auto");
        banneri.getStyle().set("object-fit", "cover");

        if (isMobile) {
            banneri.setHeight("160px");
            banneri.getStyle().set("object-position", "center 10%");

            // Spostiamo il div del banner, non solo l'immagine
            banner.getStyle().set("margin-top", "150px");
            desc.setVisible(false);
            tot.setVisible(false);
            um.setVisible(false);
            prez.setVisible(false);
            quant.setVisible(false);

        }
        banner.removeAll();   // svuota eventuali placeholder
        banner.add(banneri);
        // Wrappa CarrelloIcona + Badge
        Optional<Component> pOpt = CarrelloIcona.getParent();
        if (pOpt.isPresent() && pOpt.get() instanceof HasComponents parent) {
            parent.remove(CarrelloIcona);

            Div cartWrapper = new Div();
            cartWrapper.addClassName("cart-wrapper"); // wrapper esterno

            Div iconWrapper = new Div();
            iconWrapper.addClassName("icon-wrapper"); // wrapper relativo
            iconWrapper.add(CarrelloIcona);

            badge = new Span("0");   // inizializza il campo della classe
            badge.addClassName("cart-badge");

            iconWrapper.add(badge);       // badge sopra l’icona
            cartWrapper.add(iconWrapper); // aggiungi tutto nel wrapper
            parent.add(cartWrapper);
        }

        // Aggiorna badge con numero prodotti
        long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
        aggiornaCarrelloBadge(numeroProdotti);

        // Gestione listino e visibilità bottoni
        if (sessionDataProvider.getObject().getAttribute("listino") != null) {
            listino = sessionDataProvider.getObject().getAttribute("listino").toString();
            accedi.setVisible(false);
            registrati.setVisible(false);
            logOut.setVisible(true);
            CarrelloIcona.setVisible(true);
            badge.setVisible(true);
            promoBottone.setVisible(true);
        } else {
            listino = "1";
            accedi.setVisible(true);
            registrati.setVisible(true);
            logOut.setVisible(false);
            CarrelloIcona.setVisible(false);
            badge.setVisible(false);
            promoBottone.setVisible(false);
        }

        // Popola combo tipo consegna e pagamento
        tipoConsegnaField.setItems("Ritiro in negozio", "Consegna a domicilio");
        tipoPagamentoField.setItems("POS", "Contanti");

        // Inizializza campi dal PDC (solo consegna a domicilio)
        initPdcData();

        // Listener tipo consegna
        tipoConsegnaField.addValueChangeListener(change -> {
            String selected = Optional.ofNullable(change.getValue()).orElse("");

            // Valorizza flag sul carrello
            Carrello carrelloAttivo = carrelloService.getOrCreateCarrelloAttivo();
            if ("Ritiro in negozio".equals(selected)) {
                carrelloAttivo.setFlagRitiroConsegna("R");
            } else if ("Consegna a domicilio".equals(selected)) {
                carrelloAttivo.setFlagRitiroConsegna("C");
            } else {
                carrelloAttivo.setFlagRitiroConsegna(null);
            }
            dataManager.save(carrelloAttivo);

            // Visibilità campi
            boolean isRitiro = "Ritiro in negozio".equals(selected);
            boolean isConsegna = "Consegna a domicilio".equals(selected);

            puntoVenditaField.setVisible(isRitiro);
            fasciaOrariaField.setVisible(isRitiro);
            nazioneField.setVisible(isConsegna);
            regioneField.setVisible(isConsegna);
            provinciaField.setVisible(isConsegna);
            capField.setVisible(isConsegna);
            viaField.setVisible(isConsegna);
            civicoField.setVisible(isConsegna);
            citofonoField.setVisible(isConsegna);
            scalaField.setVisible(isConsegna);
            pianoField.setVisible(isConsegna);
            cittaField.setVisible(isConsegna);

            // Obbligatorietà campi
            puntoVenditaField.setRequired(isRitiro);
            fasciaOrariaField.setRequired(isRitiro);
            nazioneField.setRequired(isConsegna);
            regioneField.setRequired(isConsegna);
            provinciaField.setRequired(isConsegna);
            capField.setRequired(isConsegna);
            viaField.setRequired(isConsegna);
            civicoField.setRequired(isConsegna);
            cittaField.setRequired(isConsegna);

            // Reset fasce e PV
            fasciaOrariaField.clear();
            puntoVenditaField.clear();
        });

        // Popola punti ritiro
        Map<String, String> puntiRitiro = carrelloService.getPuntiRitiro();
        puntoVenditaField.setItems(puntiRitiro.keySet());
        puntoVenditaField.setItemLabelGenerator(cod -> puntiRitiro.get(cod));
        puntoVenditaField.setValue(null);

        // Listener punto vendita: aggiorna fasce disponibili
        // Listener punto vendita: aggiorna fasce disponibili
        puntoVenditaField.addValueChangeListener(change -> {
            String codPV = change.getValue();
            System.out.println("PV selezionato: " + codPV);

            // pulisce le fasce precedenti
            fasciaOrariaField.clear();

            if (codPV != null) {
                // Popola le fasce relative al PV
                popolaFascePerPV(codPV);

                // Salva il PV scelto nel carrello
                Carrello carrelloAttivo = carrelloService.getOrCreateCarrelloAttivo();
                carrelloAttivo.setPdv(codPV);
                dataManager.save(carrelloAttivo);

                System.out.println("PDV salvato su carrello: " + carrelloAttivo.getPdv());
            } else {
                fasciaOrariaField.setVisible(false);
            }
        });


    }


    private void popolaFascePerPV(String codPV) {
        fasciaOrariaField.clear();
        if (codPV == null) return;

        int oreScarto = carrelloService.getOreScarto();

        // Recupera le fasce con codNegozio uguale al PV selezionato
        List<TabellaFasce> fasceDisponibili = dataManager.load(TabellaFasce.class)
                .query("select f from TabellaFasce f where f.id.codNegozio = :codNegozio")
                .parameter("codNegozio", codPV)
                .list();

        System.out.println("Fasce trovate per PV " + codPV + ": " + fasceDisponibili.size());
        if (fasceDisponibili.isEmpty()) {
            fasciaOrariaField.setVisible(false);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        LocalDateTime soglia = LocalDateTime.now().plusHours(oreScarto);

        // Popola la combo
        fasciaOrariaField.setItems(fasceDisponibili);
        fasciaOrariaField.setItemLabelGenerator(f -> {
            String inizioStr = normalizzaOrario(f.getOraInizio());
            String fineStr = normalizzaOrario(f.getOraFine());

            LocalTime inizio = LocalTime.parse(inizioStr, formatter);
            LocalTime fine = LocalTime.parse(fineStr, formatter);

            boolean giornoDopo = inizio.isBefore(soglia.toLocalTime());

            // Mostra in formato leggibile HH:mm - HH:mm
            return inizio.format(DateTimeFormatter.ofPattern("HH:mm")) +
                    " - " +
                    fine.format(DateTimeFormatter.ofPattern("HH:mm")) +
                    (giornoDopo ? " (giorno dopo)" : "");
        });

        fasciaOrariaField.setVisible(true);
    }

    private String normalizzaOrario(String orario) {
        if (orario == null) return "0000";
        orario = orario.trim();

        // Se è tipo "900" → "0900"
        if (orario.matches("\\d{3}")) {
            return "0" + orario;
        }
        // Se è tipo "930" → "0930"
        if (orario.matches("\\d{1,2}\\d{2}") && orario.length() < 4) {
            return String.format("%04d", Integer.parseInt(orario));
        }
        // Se è tipo "09:30" → "0930"
        if (orario.matches("\\d{1,2}:\\d{2}")) {
            return orario.replace(":", "");
        }

        // Altrimenti lo restituisco com’è (già "HHmm")
        return orario;
    }


    private void showNextNotification() {
        if (notificationActive || notificationQueue.isEmpty()) return;

        notificationActive = true;
        String message = notificationQueue.poll();
        Notification notification = new Notification(message, 5000, Notification.Position.MIDDLE);
        notification.addDetachListener(event -> {
            notificationActive = false;
            showNextNotification(); // mostra la prossima notifica in coda
        });
        notification.open();
    }

    @Subscribe(id = "confermaOrdine", subject = "clickListener")
    public void onConfermaOrdineClick(final ClickEvent<JmixButton> event) {
        try {
            // Validazione email
            validateEmail(mailfield.getValue());

            // Controllo carrello
            Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
            if (carrelloRigaDc.getItems().isEmpty()) {
                notificationQueue.add("Il carrello è vuoto. Aggiungi prodotti prima di confermare.");
                showNextNotification();
                return;
            }

            // Tipo consegna
            String tipoConsegna = Optional.ofNullable(tipoConsegnaField.getValue()).orElse("");
            if (tipoConsegna.isEmpty()) {
                notificationQueue.add("Seleziona il tipo di consegna.");
                showNextNotification();
                return;
            }

            // Tipo pagamento
            String tipoPagamento = Optional.ofNullable(tipoPagamentoField.getValue()).orElse("");
            if (tipoPagamento.isEmpty()) {
                notificationQueue.add("Seleziona il tipo di pagamento.");
                showNextNotification();
                return;
            }

            // Controlli campi obbligatori
            if ("Ritiro in negozio".equals(tipoConsegna)) {
                if (puntoVenditaField.getValue() == null || fasciaOrariaField.getValue() == null) {
                    notificationQueue.add("Seleziona il punto vendita e la fascia oraria per il ritiro.");
                    showNextNotification();
                    return;
                }
            } else if ("Consegna a domicilio".equals(tipoConsegna)) {
                if (nazioneField.getValue() == null || regioneField.getValue() == null || provinciaField.getValue() == null ||
                        capField.getValue() == null || viaField.getValue() == null || civicoField.getValue() == null || cittaField.getValue() == null) {
                    notificationQueue.add("Compila tutti i dati di consegna obbligatori.");
                    showNextNotification();
                    return;
                }
            } else {
                notificationQueue.add("Tipo di consegna non valido.");
                showNextNotification();
                return;
            }

            // Se siamo arrivati qui, tutti i controlli frontend sono ok → conferma carrello
            TabellaFasce fasciaSelezionata = fasciaOrariaField.getValue();
            String codiceFasciaOraria = fasciaSelezionata != null ? fasciaSelezionata.getId().getCodFascia() : null;

            carrelloService.confermaCarrello(
                    puntoVenditaField.getValue(),
                    carrello.getFlagRitiroConsegna(),
                    citofonoField.getValue() ,
                    truncate(pianoField.getValue(), 2),
                    truncate(noteField.getValue(), 250),
                    truncate(scalaField.getValue(), 2),
                    tipoPagamento,
                    truncate(nazioneField.getValue(), 50),
                    truncate(regioneField.getValue(), 50),
                    truncate(provinciaField.getValue(), 50),
                    truncate(cittaField.getValue(), 50),
                    truncate(capField.getValue(), 10),
                    truncate(viaField.getValue(), 50),
                    truncate(civicoField.getValue(), 10),
                    codiceFasciaOraria
            );

            // Invia mail
            carrelloService.inviaOrdineCompleto(carrello, mailfield.getValue());

            // Notifica di successo
            notificationQueue.add("Ordine confermato con successo! Riceverai una mail di conferma.");
            showNextNotification();

            // Redirect alla home
            viewNavigators.view(this, Home.class).navigate();

        } catch (ValidationException ve) {
            notificationQueue.add("Errore: " + ve.getMessage());
            showNextNotification();
        } catch (EmailException ee) {
            notificationQueue.add("Errore nell'invio dell'email. Controlla l'indirizzo e riprova.");
            showNextNotification();
        } catch (Exception e) {
            // Errori tecnici lato backend → mostrare notifica **dopo redirect**
            viewNavigators.view(this, Home.class).navigate();
            UI.getCurrent().access(() -> {
                Notification backendError = new Notification("Si è verificato un errore tecnico durante la conferma dell'ordine. controllare.", 5000, Notification.Position.MIDDLE);
                backendError.open();
            });
        }
    }


    // Metodo di utilità per troncare valori null-safe
    private String truncate(Object value, int maxLength) {
        if (value == null) return null;
        String str = value.toString().trim();
        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }




    @Subscribe(id = "svuotaCarrello", subject = "clickListener")
    public void onSvuotaCarrelloClick(final ClickEvent<JmixButton> event) {
        carrelloService.svuotaCarrello(carrelloService.getOrCreateCarrelloAttivo().getIdOrdine());
        Notification.show("Il carrello è stato svuotato", 3000, Notification.Position.MIDDLE);
        getContent().getUI().ifPresent(ui -> ui.getPage().executeJs("location.reload();"));
        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
        aggiornaCarrelloBadge(numeroProdotti);
    }

    private void validateEmail(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Il campo email è obbligatorio");
        }

        // Regex robusta e compatibile con la maggior parte delle email valide
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!value.matches(emailRegex)) {
            throw new ValidationException("Inserisci un indirizzo email valido");
        }
    }


}
