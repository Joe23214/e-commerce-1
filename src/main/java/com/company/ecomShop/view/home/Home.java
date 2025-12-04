package com.company.ecomShop.view.home;



import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.app.MyClickListener;

import com.company.ecomShop.app.NotificationScheduler;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.listener.CarrelloChangedEvent;
import com.company.ecomShop.view.configecom.ConfigEcomDetailView;
import com.company.ecomShop.view.cupon.CuponView;
import com.company.ecomShop.view.dettaglioprodotto.Dettaglioprodotto;
import com.company.ecomShop.view.invionotifiche.InvioNotifiche;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.pdc.PdcListView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.company.ecomShop.view.secondapagina.SecondaPagina;
import com.company.ecomShop.view.volantino.Volantino;
import com.example.application.components.Notification;
import com.flowingcode.vaadin.addons.carousel.Carousel;
import com.flowingcode.vaadin.addons.carousel.Slide;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.open.App;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.email.EmailException;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.download.DownloadDataProvider;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.sys.AppCookies;
import io.jmix.flowui.sys.LogoutSupport;
import io.jmix.flowui.view.*;


import org.apache.catalina.core.ApplicationSessionCookieConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.View;

import com.vaadin.flow.component.button.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.CookiePolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


@Route(value = "home")
@ViewController(id = "Home")
@ViewDescriptor(path = "provaui.xml")
@AnonymousAllowed
public class Home extends StandardView {
    @ViewComponent
    private JmixButton CarrelloIcona;
    @Autowired
    private UiComponents uiComponents;
    @ViewComponent
    private Div carousel;
    @ViewComponent
    private CollectionLoader<ArticoliClassificazioni> articoliClassificazionisDl;
    @ViewComponent
    private TypedTextField<Object> searchField;
    @ViewComponent
    private CollectionContainer<ArticoliClassificazioni> articoliClassificazionisDc;
    @ViewComponent
    private VerticalLayout listaClassificazioni;
    @ViewComponent
    private VerticalLayout listaArticoli;
    private String oldSearchText = "";
    @Autowired
    private GetDataService getDataService;
    String searchTextStatic = "";

    private static final Logger log = LoggerFactory.getLogger(StandardView.class);
    @ViewComponent
    private Div listeArtClassificazioni;

    @ViewComponent
    private H1 h1;
    @ViewComponent
    private Div piede;
    @ViewComponent
    private JmixImage<Object> logo;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private Div prodotti;
    @ViewComponent
    private Div promozioni;
    @Autowired
    private Notifications notifications;
    private Span badge;
    @ViewComponent
    private Div searchBox;
    @ViewComponent
    private Div navigazione;
    @ViewComponent
    private HorizontalLayout nav;
    @Autowired
    private MyClickListener myClickListener;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;

    String listino = "";
    @ViewComponent
    private JmixButton accedi;
    @ViewComponent
    private JmixButton registrati;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private LogoutSupport logoutSupport;
    @ViewComponent
    private JmixButton logOut;
    @ViewComponent
    private HorizontalLayout datiUtente;
    @ViewComponent
    private Div suggestionsBox;
    @ViewComponent
    private JmixButton tuttiProdotti;
    @ViewComponent
    private Div testoDescrittivo;
    @ViewComponent
    private VerticalLayout primaPagina;
    @Autowired
    private View view;
    @ViewComponent
    private Div authButton;
    @ViewComponent
    private HorizontalLayout socialicons1;
    @Autowired
    private Dialogs dialogs;
    @ViewComponent
    private Div cookies;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SessionData sessionData;
    @Autowired
    private Downloader downloader;
    @ViewComponent
    private H2 tuttiIprezzi;
    @Autowired
    private Emailer emailer;
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private JmixButton promoBottone;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private JmixButton configEcom;
    @ViewComponent
    private JmixButton invioNotifiche;
    @ViewComponent
    private JmixButton UtentiRegistrati;
    boolean isMobile = UI.getCurrent().getInternals()
            .getExtendedClientDetails().getWindowInnerWidth() <= 480;
    @ViewComponent
    private Button adminHamburger;
    @ViewComponent
    private VerticalLayout adminMenu;
    @Autowired
    private NotificationScheduler notificationScheduler;

    @Subscribe(id = "adminHamburger", subject = "clickListener")
    public void onAdminHamburgerClick(final ClickEvent<JmixButton> event) {
        adminMenu.setVisible(!adminMenu.isVisible());
    }

    @Subscribe(id = "configEcom", subject = "clickListener")
    public void onConfigEcomClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, ConfigEcomDetailView.class).navigate();
    }

    @Subscribe(id = "menuToggle", subject = "clickListener")
    public void onMenuToggleClick(final ClickEvent<JmixButton> event) {
        if (isMobile) {
            authButton.setVisible(!authButton.isVisible());
        }
    }

    @Subscribe(id = "invioNotifiche", subject = "clickListener")
    public void onInvioNotificheClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, InvioNotifiche.class).navigate();
    }

    @Subscribe(id = "UtentiRegistrati", subject = "clickListener")
    public void onUtentiRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, PdcListView.class).navigate();
    }

    @Subscribe(id = "promoBottone", subject = "clickListener")
    public void onPromoBottoneClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, CuponView.class).navigate();
    }

    @Subscribe(id = "logOut", subject = "clickListener")
    public void onLogOutClick(final ClickEvent<JmixButton> event) {
        sessionDataProvider.getObject().setAttribute("listino", null);
        testoDescrittivo.removeAll();
        UI.getCurrent().getPage().executeJs("if(window.FlutterChannel){FlutterChannel.postMessage('logout');}");
        logoutSupport.logout();

    }

    @Subscribe(id = "tuttiProdotti", subject = "clickListener")
    public void onTuttiProdottiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, SecondaPagina.class)
                .withQueryParameters(QueryParameters.of("codice", "_________"))
                .navigate();
    }
    public void aggiornaCarrelloBadge(long numeroProdotti) {
        badge.setText(String.valueOf(numeroProdotti));
        CarrelloIcona.removeClassName("cart-grow");
        CarrelloIcona.addClassName("cart-grow");
    }

    @Subscribe(id = "CarrelloIcona", subject = "clickListener")
    public void onCarrelloIconaClick(final ClickEvent<JmixButton> event) {
        CarrelloIcona.removeClassName("cart-grow");
        carrelloService.vaiAlCarrello();
        String username = currentAuthentication.getUser().getUsername();
        Pdc pdc = carrelloService.getPdcByUsername(username);
        if (pdc == null) return;

        String dataOggi = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<CouponDto> coupons = getDataService.getCouponInfo(pdc.getCodiceConto(), dataOggi);
    }

    @EventListener
    public void onCarrelloChangedEvent(CarrelloChangedEvent event) {
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> aggiornaCarrelloBadge(event.getNumeroProdotti()));
        }
    }


    private void Carosello() {
        String[] rollingNames = {"rolling1", "rolling2", "rolling3"};
        String[] rollingPhoneNames = {"rolling1phone", "rolling2phone", "rolling3phone"};

        boolean isMobile = UI.getCurrent().getInternals()
                .getExtendedClientDetails().getWindowInnerWidth() <= 480;

        String[] baseNames = isMobile ? rollingPhoneNames : rollingNames;
        Path rollingDir = Paths.get("src/main/resources/META-INF/resources/rolling");

        Image[] images = new Image[3];
        for (int i = 0; i < 3; i++) {
            Path found = findImage(rollingDir, baseNames[i]);
            String defaultPath = "/META-INF/resources/rolling/" + baseNames[i] + ".jpg"; // fallback

            images[i] = uiComponents.create(Image.class);
            StreamResource sr = (found != null)
                    ? new StreamResource(found.getFileName().toString(), () -> {
                try { return new FileInputStream(found.toFile()); }
                catch (FileNotFoundException e) { return getClass().getResourceAsStream(defaultPath); }
            })
                    : new StreamResource(baseNames[i] + ".jpg", () -> getClass().getResourceAsStream(defaultPath));

            images[i].setSrc(sr);
            images[i].setClassName("caroselloImg12");
        }

        Slide s1 = new Slide(createSlideContent(images[0]));
        Slide s2 = new Slide(createSlideContent(images[1]));
        Slide s3 = new Slide(createSlideContent(images[2]));

        Carousel c = new Carousel(s1, s2, s3)
                .withAutoProgress()
                .withSlideDuration(2)
                .withStartPosition(0)
                .withoutSwipe();

        c.setHeight(isMobile ? "360px" : "440px");

        carousel.removeAll();
        carousel.add(c);
    }



    private Path findImage(Path dir, String baseName) {
        Path newJpg = dir.resolve(baseName + "-new.jpg");
        Path oldJpg = dir.resolve(baseName + "-old.jpg");
        Path originalJpg = dir.resolve(baseName + ".jpg");

        if (Files.exists(newJpg)) return newJpg;
        if (Files.exists(oldJpg)) return oldJpg;
        if (Files.exists(originalJpg)) return originalJpg;

        return null; // fallback gestito dopo
    }

    private StreamResource loadImageResource(Path baseDir, String fileName, String defaultPath) {
        Path candidate = baseDir.resolve(fileName);

        if (Files.exists(candidate)) {
            return new StreamResource(fileName, () -> {
                try {
                    return new FileInputStream(candidate.toFile());
                } catch (FileNotFoundException e) {
                    return getClass().getResourceAsStream(defaultPath);
                }
            });
        }

        return new StreamResource(fileName, () -> getClass().getResourceAsStream(defaultPath));
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

    public Component createSlideContent(Image image) {


        image.addClickListener(event -> {
            String SRC_PATH2 = "/META-INF/resources/rolling/rolling2.jpg";

            StreamResource streamResource2 = new StreamResource("rolling2",
                    () -> getClass().getResourceAsStream(SRC_PATH2));
            if (event.getSource().getSrc().toString().contains("rolling2")) {
                viewNavigators.view(this, Volantino.class).navigate();
            }
        });


        return image;
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
        if(sessionDataProvider.getObject().getAttribute("accesso_app") != null ){
            String valore = sessionDataProvider.getObject().getAttribute("accesso_app").toString();
            if(valore != null && valore == "1"){
                User user = (User)currentAuthentication.getUser();
                String contoPdc = carrelloService.getPdcByUsername(user.getUsername()).getCodiceConto();
                Long tessera = Long.parseLong(carrelloService.getTesseraByCodiceCliente(contoPdc).getCodiceTessera());
                if(tessera != null)
                notificationScheduler.sendWelcomeNotification(tessera);
            }
        }
        Element textFieldElement = searchField.getElement();
        Carosello();
        loadLogo();
        if (sessionDataProvider.getObject().getAttribute("listino") != null) {
            listino = sessionDataProvider.getObject().getAttribute("listino").toString();
            accedi.setVisible(false);
            registrati.setVisible(false);
            logOut.setVisible(true);
            datiUtente.setVisible(true);
            CarrelloIcona.setVisible(true);
            promoBottone.setVisible(true);
            inizializzaBadgePerUtenteLoggato();

            Paragraph paragraph = new Paragraph();
            paragraph.setText("Listino :" + listino);
            Paragraph paragraph1 = new Paragraph();
            paragraph1.setText("Utente :" + sessionDataProvider.getObject().getAttribute("username").toString());
            Paragraph paragraph2 = new Paragraph();
//           paragraph2.setText("listino 3 : sono i prezzi compreso iva a cartone . Listino 2 : sono i prezzi compreso iva per ordini di minimo 10 cartoni . ( questo quando si fa accesso per partita iva)");
            datiUtente.add(paragraph);
            datiUtente.add(paragraph1);

            Paragraph p1 = new Paragraph();
            Paragraph p2 = new Paragraph();

            if (listino.equalsIgnoreCase("2")) {
                p1.setText("LISTINO 2 : Prezzo ivato franco deposito");
                p1.getStyle().set("font-size", "20px").set("font-weight", "bold").set("color", "red");
                p1.addClassName("paragrafoIniziale");
            }

            if ( listino.equalsIgnoreCase("3")) {
                p2.setText("LISTINO 3 : Prezzo ivato consegnato");
                p2.getStyle().set("font-size", "20px").set("font-weight", "bold").set("color", "red");
                p2.addClassName("paragrafoIniziale");
            }


            testoDescrittivo.add(p1, p2);
//           datiUtente.add(paragraph2);
        } else {
            listino = "1";
            CarrelloIcona.setVisible(false);
            promoBottone.setVisible(false);
            accedi.setVisible(true);
            registrati.setVisible(true);
            logOut.setVisible(false);
            datiUtente.removeAll();
            tuttiIprezzi.setVisible(false);
            datiUtente.setVisible(false);
            Paragraph p = new Paragraph();
            p.setText("ACCEDI/REGISTRATI PER PREZZI INGROSSO");
            p.addClassName("paragrafoIniziale");
            testoDescrittivo.add(p);
            CarrelloIcona.setVisible(false);

        }
//        getDataService.geArticoliClassificazioni("1","DASH");//PROVA
        articoliInEvidenza(articoliEvidenza());
        String tipo_Tes = "";

        if (accedi.isVisible() || listino.equalsIgnoreCase("2")) {
            tipo_Tes = "";
        } else {
            tipo_Tes = "RS999";
        }
        List<Object[]> objects = getDataService.getArtOfferta(tipo_Tes);
        articoliInPromozione(objects);

        //MENU BAR CON MY CLICK LISTNER

        List<An> list = getDataService.getAn("12"); //PRENDO AN

        MenuBar menuBar = menuBar(list);

        nav.add(menuBar);//Menu Bar
        if (isMobile) {
            menuToggle.setVisible(true);
            // Nasconde i bottoni interni come se il toggle fosse cliccato
            authButton.setVisible(false);
            CarrelloIcona.setWidth("250%");
        }
        }

    private void addDownloadAppBanner() {
        // 🔹 Creiamo il layout principale del banner
        HorizontalLayout banner = new HorizontalLayout();
        banner.setWidthFull();
        banner.setPadding(true);
        banner.setSpacing(true);
        banner.setAlignItems(FlexComponent.Alignment.CENTER);
        banner.getStyle()
                .set("background", "linear-gradient(90deg, #0D47A1, #1976D2)")
                .set("color", "white")
                .set("border-radius", "12px")
                .set("margin-top", "20px")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.2)");

        // 🔹 Icona Android
        Image androidIcon = new Image("https://i.postimg.cc/fTxMXdVn/icons8-android-os-96.png", "Android");
        androidIcon.setWidth("48px");
        androidIcon.setHeight("48px");

        // 🔹 Testo
        VerticalLayout textLayout = new VerticalLayout();
        textLayout.setPadding(false);
        textLayout.setSpacing(false);
        textLayout.setWidthFull();

        Span title = new Span("📱 Scarica la nostra App Android");
        title.getStyle().set("font-size", "20px").set("font-weight", "bold");

        Span subtitle = new Span("Approfitta delle offerte esclusive e acquista in modo più veloce ovunque tu sia!");
        subtitle.getStyle().set("font-size", "14px").set("opacity", "0.9");

        textLayout.add(title, subtitle);

        // 🔹 Pulsante
        JmixButton downloadButton = uiComponents.create(JmixButton.class);
        downloadButton.setText("📲 Scarica ora");
        downloadButton.addThemeNames("primary", "success", "large");
        downloadButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "white")
                .set("border-radius", "10px")
                .set("font-weight", "bold");

        // 👉 Al click parte il download del file APK
        downloadButton.addClickListener(e -> {
            UI.getCurrent().getPage().open("/app-debug_shop.apk");
            // sostituisci con il path reale del tuo APK
        });

        banner.add(androidIcon, textLayout, downloadButton);
        banner.expand(textLayout);

        // 🔹 Aggiungi il banner al layout principale della view
        getContent().getElement().appendChild(banner.getElement());

        if(sessionDataProvider.getObject().getAttribute("accesso_app") != null ){
            String valore = sessionDataProvider.getObject().getAttribute("accesso_app").toString();
            if(valore != null && valore == "1"){
                banner.setVisible(false);
            }
        }
    }

    private void inizializzaBadgePerUtenteLoggato() {
        Object userObj = currentAuthentication.getUser();
        if (!(userObj instanceof User)) {
            return; // sicurezza: utente non loggato
        }

        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        long numeroProdotti = carrelloService.carrelloRigheCount(carrello);

        if (badge != null) {
            // Badge già creato → aggiorno solo il numero
            badge.setText(String.valueOf(numeroProdotti));
        } else {
            // Wrappa CarrelloIcona + Badge nello stesso contenitore
            Optional<Component> pOpt = CarrelloIcona.getParent();
            if (pOpt.isPresent() && pOpt.get() instanceof HasComponents parent) {
                parent.remove(CarrelloIcona);

                Div cartWrapper = new Div();
                cartWrapper.addClassName("cart-wrapper"); // wrapper esterno

                Div iconWrapper = new Div();
                iconWrapper.addClassName("icon-wrapper"); // wrapper relativo
                iconWrapper.add(CarrelloIcona);

                badge = new Span(String.valueOf(numeroProdotti));
                badge.addClassName("cart-badge");

                iconWrapper.add(badge);        // badge sopra l’icona
                cartWrapper.add(iconWrapper);  // aggiungi tutto nel wrapper
                parent.add(cartWrapper);
            }
        }
        promoBottone.setVisible(true);
        CarrelloIcona.setVisible(true);
        if (badge != null) {
            badge.setVisible(true);
        }
    }




    @Subscribe(id = "pagina", subject = "doubleClickListener")
    public void onPaginaClick(final ClickEvent<Div> event) {

    }


    // Metodo per popolare dinamicamente la lista dei prodotti
    @Subscribe(id = "articoliClassificazionisDc", target = Target.DATA_CONTAINER)
    public void onArtsDcCollectionChange(CollectionContainer.CollectionChangeEvent<ArticoliClassificazioni> event) {
        listaArticoli.removeAll(); // Rimuove gli elementi precedenti
        listaClassificazioni.removeAll();
        List<ArticoliClassificazioni> classi = new ArrayList<>();
        List<ArticoliClassificazioni> articoli = new ArrayList<>();
        if (event.getSource().getItems().size() > 0) {
            // Titolo Categorie
            Span titoloCategorie = uiComponents.create(Span.class);
            titoloCategorie.setText("Categorie");
            titoloCategorie.setClassName("sectionTitle");
            listaClassificazioni.add(titoloCategorie); // titolo classificazioni

            // Titolo Articoli
            Span titoloArticoli = uiComponents.create(Span.class);
            titoloArticoli.setText("Articoli");
            titoloArticoli.setClassName("sectionTitle");
            listaArticoli.add(titoloArticoli); // Aggiungi il titolo sopra la lista degli articoli
        }


        for (ArticoliClassificazioni item : event.getSource().getItems()) {
            if (item.getClassi().equalsIgnoreCase("Classi")) {
                // Crea e aggiungi una classificazione
                HorizontalLayout classificazioneItem = uiComponents.create(HorizontalLayout.class);
                classificazioneItem.setClassName("classificationItem");
                classificazioneItem.setWidthFull();

                Span classificazioneDescrizione = uiComponents.create(Span.class);
                classificazioneDescrizione.setText(item.getDescrizione());
                classificazioneDescrizione.setClassName("classificationDescription hoverable"); // Classe hover aggiunta

                classificazioneItem.add(classificazioneDescrizione);
                listaClassificazioni.add(classificazioneItem);
                classi.add(item);
            } else {
                // Crea e aggiungi un articolo
                HorizontalLayout articoloItem = uiComponents.create(HorizontalLayout.class);
                articoloItem.setClassName("articleItem");
                articoloItem.setWidthFull();

                // Immagine
                Image articoloImage = uiComponents.create(Image.class);
                articoloImage.setSrc("https://i.postimg.cc/66Z1fzrZ/dimension-capelli-grassi-shampoo-ml-250.jpg"); // Path immagine
                articoloImage.setClassName("articleImage");
                articoloImage.setWidth("100px");
                articoloImage.setHeight("75px");

                // Dettagli articolo
                VerticalLayout articoloDetails = uiComponents.create(VerticalLayout.class);
                articoloDetails.setClassName("articleDetails");

                Span articoloNome = uiComponents.create(Span.class);
                articoloNome.setText(item.getDescrizione());
                articoloNome.setClassName("articleName hoverable"); // Classe hover aggiunta

                Span articoloPrezzo = uiComponents.create(Span.class);
                articoloPrezzo.setText("€ " + item.getPrezzo());
                articoloPrezzo.setClassName("articlePrice");

                articoloDetails.add(articoloNome);
                articoloDetails.add(articoloPrezzo);

                // Aggiungi immagine e dettagli all'articolo
                articoloItem.add(articoloImage);
                articoloItem.add(articoloDetails);

                listaArticoli.add(articoloItem);
                articoli.add(item);
            }

        }

        // Link "Vedi tutte le categorie"
        Span linkCategorie = uiComponents.create(Span.class);
        linkCategorie.setText("Vedi tutte le categorie");
        linkCategorie.setClassName("viewAllLink hoverable"); // Classe hover aggiunta
        listaClassificazioni.add(linkCategorie);

        // Link "Vedi tutti gli articoli"
        Span linkArticoli = uiComponents.create(Span.class);
        linkArticoli.setText("Vedi tutti gli articoli");
        linkArticoli.setClassName("viewAllLink hoverable"); // Classe hover aggiunta
        listaArticoli.add(linkArticoli);

        if (classi.isEmpty()) {
            listaClassificazioni.removeAll();
        }
        if (articoli.isEmpty()) {
            listaArticoli.removeAll();
        }
    }


//    @Subscribe("searchField")
//    public void onSearchFieldKeyUp(final KeyUpEvent event) {
//
//
//        if (!searchTextStatic.trim().isEmpty()) {
//            List<ArticoliClassificazioni> suggestions = getDataService.geArticoliClassificazioni(listino, searchTextStatic);
//            updateSuggestionsBox(suggestions);
//        } else {
//            suggestionsBox.addClassName("hidden");
//        }
//    }


    private void updateSuggestionsBox(List<ArticoliClassificazioni> suggestions) {
        suggestionsBox.removeAll();
        if (suggestions.isEmpty()) {
            suggestionsBox.addClassName("hidden");
            return;
        }

        suggestionsBox.removeClassName("hidden");


        // Creazione del contenitore principale con categorie e suggerimenti
        Div suggestionsContainer = new Div();
        suggestionsContainer.addClassName("suggestionsContainer");


        primaPagina.addClickListener(verticalLayoutClickEvent -> {
            suggestionsBox.addClassName("hidden");

        });
        // Sidebar per le categorie
        Div categoryContainer = new Div();
        categoryContainer.addClassName("suggestionsCategories");

        List<ArticoliClassificazioni> categories = new ArrayList<>();
        for (int i = 0; i < suggestions.size(); i++) {
            if (suggestions.get(i).getClassi().equalsIgnoreCase("Classi")) {
                ArticoliClassificazioni articoliClassificazioniSeggestionCategories = new ArticoliClassificazioni();
                articoliClassificazioniSeggestionCategories.setPrezzo(suggestions.get(i).getPrezzo());
                articoliClassificazioniSeggestionCategories.setDescrizione(suggestions.get(i).getDescrizione());
                articoliClassificazioniSeggestionCategories.setCodiceArticolo(suggestions.get(i).getCodiceArticolo());
                articoliClassificazioniSeggestionCategories.setClassi(suggestions.get(i).getClassi());
                categories.add(articoliClassificazioniSeggestionCategories);
            }
        }
        Span categoryTitle = new Span("CATEGORIE");
        categoryContainer.add(categoryTitle);
        for (ArticoliClassificazioni category : categories) {
            Span categoryItem = new Span(category.getDescrizione());
            categoryItem.addClassName("category-item");
            categoryItem.addClickListener(spanClickEvent -> {


                viewNavigators.view(this, SecondaPagina.class)
                        .withQueryParameters(QueryParameters.of("codice", category.getCodiceArticolo()))
                        .navigate();

            });
            categoryContainer.add(categoryItem);


        }

        // Griglia dei suggerimenti
        Div suggestionsGrid = new Div();
        suggestionsGrid.addClassName("suggestionBox");

        List<ArticoliClassificazioni> articoli = new ArrayList<>();
        for (int i = 0; i < suggestions.size(); i++) {
            if (suggestions.get(i).getClassi().equalsIgnoreCase("Articoli")) {
                ArticoliClassificazioni articoliClassificazioniSeggestionCategories = new ArticoliClassificazioni();
                articoliClassificazioniSeggestionCategories.setPrezzo(suggestions.get(i).getPrezzo());
                articoliClassificazioniSeggestionCategories.setDescrizione(suggestions.get(i).getDescrizione());
                articoliClassificazioniSeggestionCategories.setCodiceArticolo(suggestions.get(i).getCodiceArticolo());
                articoliClassificazioniSeggestionCategories.setClassi(suggestions.get(i).getClassi());
                articoli.add(articoliClassificazioniSeggestionCategories);
            }
        }

        for (ArticoliClassificazioni item : articoli) {
            Div suggestionItem = new Div();
            suggestionItem.addClassName("suggestion-item");

            String nomeImg = getDataService.getArtImgPrincipale(item.getCodiceArticolo());


            Image img = getFileRefFromSysFile(nomeImg);
            img.addClassName("suggestion-image");

            item.setImmagine(img.getSrc());

            Div textContainer = new Div();
            textContainer.addClassName("suggestion-text");

            Span title = new Span(item.getDescrizione());
            title.addClassName("title");

            Span details = new Span("Codice Articolo: " + item.getCodiceArticolo());
            Span detail1 = new Span("  Prezzo: " + formattaPrezzo(item.getPrezzo()).replace(".",","));
            details.addClassName("details");

            textContainer.add(title, details, detail1);
            suggestionItem.add(img, textContainer);

            suggestionItem.addClickListener(e -> {
                searchField.setValue(item.getDescrizione());
                if (item.getClassi().equalsIgnoreCase("Articoli")) {
                    Map<String, String> parametri = new HashMap<>();
                    parametri.put("codice", item.getCodiceArticolo());
                    parametri.put("descrizione", item.getDescrizione());

                    parametri.put("codiceBar", getDataService.primoCodiceABarre(item.getCodiceArticolo()));
                    if (item.getImmagine() != null && !item.getImmagine().equalsIgnoreCase("")) {
                        parametri.put("nomeImg", getDataService.getArtImgPrincipale(item.getCodiceArticolo()));
                    } else {
                        parametri.put("nomeImg", "");
                    }
                    parametri.put("codiceProdotti", "_________");



                    String tipo_Tes = "";
                    if (accedi.isVisible()) {
                        tipo_Tes = "";

                        ;
                    } else {
                        tipo_Tes = "RS999";

                    }
                    if (!getDataService.verArtOfferta(tipo_Tes, item.getCodiceArticolo()).isEmpty()) {
                        parametri.put("promo", "SI");

                        parametri.put("prezzoCons", "€" + item.getPrezzo() + " PROMO!");
                    } else {
                        parametri.put("promo", "NO");
                        parametri.put("prezzoCons", item.getPrezzo());
                    }


                    viewNavigators.view(this, Dettaglioprodotto.class)
                            .withQueryParameters(QueryParameters.simple(parametri))
                            .navigate();
                }


            });

            suggestionsGrid.add(suggestionItem);
        }

        // Unione di categorie e suggerimenti
        suggestionsContainer.add(categoryContainer, suggestionsGrid);

        suggestionsBox.add(suggestionsContainer);
        suggestionsBox.add(suggestionsGrid);
    }


//
//    @Subscribe("searchField")
//    public void onSearchFieldKeyDown(final KeyDownEvent event) {
//        String key = event.getKey().getKeys().toString().replace("[", "").replace("]", "");
//        if (!key.equalsIgnoreCase("Backspace")) {
//            searchTextStatic = searchTextStatic + key;
//        } else {
//
//            if (searchTextStatic.length() > 0) {
//                searchTextStatic = searchTextStatic.substring(0, searchTextStatic.length() - 1);
//            } else {
//                searchTextStatic = "";
//            }
//
//        }
//        if(searchTextStatic.equalsIgnoreCase("")){
//            suggestionsBox.addClassName("hidden");
//        }
//    }


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


    public List<Art> articoliEvidenza() {
        Date dateIniziale;
        Date dateFinale = new Date(); // Data di oggi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Data di oggi come stringa
        String dateFin = sdf.format(dateFinale);


        // Calcola la data di 30 giorni fa
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        dateIniziale = calendar.getTime();
        String dateIni = sdf.format(dateIniziale);
        return dataManager.load(Art.class)

                //in (select f.codiceArticolo from marketnewstore_Art f where f.descrizione like '%" + descrizione +"%')
                .query("select c from Art c where c.codClasse7 ='1' and c.dataCreazione >= :dataIn and c.dataCreazione <= :dataFin and c.codiceArticolo in (select l.id.codiceArticolo from Li l where l.id.listino = 1 and l.prezzoVend is not null ) order by c.dataCreazione Desc")
                .parameter("dataIn", dateIni)
                .parameter("dataFin", dateFin)
                .list();
    }


    //creazione delle card degli articoli in modo dinamico
   /* public void articoliInEvidenza(List<Art> art) {

        List<Art> articoliEvidenza = articoliEvidenza();
        if (articoliEvidenza.size() > 0) {
            for (int i = 0; i < articoliEvidenza.size(); i++) {

                Div carteProdotti = new Div();
                carteProdotti.setClassName("product-card");
                String nomeImg = getDataService.getArtImgPrincipale(articoliEvidenza.get(i).getCodiceArticolo());
                Image image = getFileRefFromSysFile(nomeImg);

                carteProdotti.add(image);
                Div descrizioneProdotto = new Div();
                descrizioneProdotto.setClassName("product-description");
                descrizioneProdotto.setText(articoliEvidenza.get(i).getDescrizione());
                carteProdotti.add(descrizioneProdotto);
                Div prezzoProdotto = new Div();
                prezzoProdotto.setClassName("product-price");
                prezzoProdotto.setText("€ " + formattaPrezzo(getDataService.gePrezzoArt(listino, articoliEvidenza.get(i).getCodiceArticolo())).replace(".",","));
                carteProdotti.add(prezzoProdotto);
                prodotti.add(carteProdotti);

                int finalI = i;
                carteProdotti.addClickListener(event -> {
                    Map<String, String> parametri = new HashMap<>();
                    parametri.put("codice", articoliEvidenza.get(finalI).getCodiceArticolo());
                    parametri.put("descrizione", articoliEvidenza.get(finalI).getDescrizione());
                    parametri.put("prezzoCons", "€ " + formattaPrezzo((getDataService.gePrezzoArt(listino, articoliEvidenza.get(finalI).getCodiceArticolo()))).replace(".",","));
                    parametri.put("codiceBar", getDataService.primoCodiceABarre(articoliEvidenza.get(finalI).getCodiceArticolo()));
                    if (image != null) {
                        parametri.put("nomeImg", getDataService.getArtImgPrincipale(articoliEvidenza.get(finalI).getCodiceArticolo()) != null ?
                                getDataService.getArtImgPrincipale(articoliEvidenza.get(finalI).getCodiceArticolo()) : "");
                    } else {
                        parametri.put("nomeImg", "");
                    }
                    parametri.put("codiceProdotti", "_________");
                    parametri.put("promo", "NO");
                    parametri.put("pagina","");
                    viewNavigators.view(this, Dettaglioprodotto.class)
                            .withQueryParameters(QueryParameters.simple(parametri))
                            .navigate();

                });
            }

        }


    }*/
    public void articoliInEvidenza(List<Art> art) {

        List<Art> articoliEvidenza = articoliEvidenza();
        if (articoliEvidenza.size() > 0) {
            for (int i = 0; i < articoliEvidenza.size(); i++) {

                Div carteProdotti = new Div();
                carteProdotti.setClassName("product-card");
                String nomeImg = getDataService.getArtImgPrincipale(articoliEvidenza.get(i).getCodiceArticolo());
                Image image = getFileRefFromSysFile(nomeImg);
                carteProdotti.add(image);

                Div descrizioneProdotto = new Div();
                descrizioneProdotto.setClassName("product-description");
                descrizioneProdotto.setText(articoliEvidenza.get(i).getDescrizione());
                carteProdotti.add(descrizioneProdotto);

                Div prezzoProdotto = new Div();
                prezzoProdotto.setClassName("product-price");
                String prezzoFormattato = "€ " + formattaPrezzo(getDataService.gePrezzoArt(listino, articoliEvidenza.get(i).getCodiceArticolo())).replace(".", ",");
                prezzoProdotto.setText(prezzoFormattato);
                carteProdotti.add(prezzoProdotto);

                // ...
                carteProdotti.getElement().getStyle().set("position", "relative");
                buildPallino(articoliEvidenza.get(i).getCodiceArticolo())
                        .ifPresent(p -> carteProdotti.getElement().appendChild(p.getElement()));
                prodotti.add(carteProdotti);


                int finalI = i;
                carteProdotti.addClickListener(event -> {
                    Map<String, String> parametri = new HashMap<>();
                    parametri.put("codice", articoliEvidenza.get(finalI).getCodiceArticolo());
                    parametri.put("descrizione", articoliEvidenza.get(finalI).getDescrizione());
                    parametri.put("prezzoCons", prezzoFormattato);
                    parametri.put("codiceBar", getDataService.primoCodiceABarre(articoliEvidenza.get(finalI).getCodiceArticolo()));
                    parametri.put("nomeImg", nomeImg != null ? nomeImg : "");
                    parametri.put("codiceProdotti", "_________");
                    parametri.put("promo", "NO");
                    parametri.put("pagina", "");
                    viewNavigators.view(this, Dettaglioprodotto.class)
                            .withQueryParameters(QueryParameters.simple(parametri))
                            .navigate();
                });
            }
        }
    }


    public String formattaPrezzo(String s) {

        String prova = s + "0000000";
        String result = prova.substring(0,prova.indexOf(".")) + prova.substring(prova.indexOf("."),prova.indexOf(".")+3);



        return result;
    }



    private Optional<Span> buildPallino(String codiceArt) {
        // Recupero giacenza come stringa
        String giacStr = getDataService.getGiacenze(codiceArt);

        int giacenza;
        try {
            giacenza = giacStr != null && !giacStr.isBlank()
                    ? Integer.parseInt(giacStr)
                    : 0;
        } catch (NumberFormatException e) {
            giacenza = 0; // fallback
        }

        // Recupero soglie
        Integer sogliaGiallo = carrelloService.getGiacenzaGiallo();
        Integer sogliaRosso = carrelloService.getGiacenzaRosso();

        // 🔴 Se le soglie non sono valorizzate oppure sono <= 0 → non costruisco il pallino
        if (sogliaGiallo == null || sogliaRosso == null || sogliaGiallo <= 0 || sogliaRosso <= 0) {
            return Optional.empty();
        }

        Span pallino = new Span();
        pallino.getStyle()
                .set("position", "absolute")
                .set("width", "14px")
                .set("height", "14px")
                .set("border-radius", "50%")
                .set("bottom", "12px")
                .set("right", "12px")
                .set("box-shadow", "0 0 4px rgba(0,0,0,0.3)");

        if (giacenza <= 0) {
            pallino.getStyle().set("background-color", "black");
        } else if (giacenza > sogliaGiallo) {
            pallino.getStyle().set("background-color", "green");
        } else if (giacenza > sogliaRosso) {
            pallino.getStyle().set("background-color", "yellow");
        } else {
            pallino.getStyle().set("background-color", "red");
        }

        return Optional.of(pallino);
    }

    public void articoliInPromozione(List<Object[]> artInPromo) {

        if (artInPromo.size() > 0) {
            for (Object m : artInPromo) {

                Div carteProdotti = new Div();
                carteProdotti.setClassName("product-card");
                String codiceArt = ((Object[]) m)[0].toString();
                String nomeImg = getDataService.getArtImgPrincipale(codiceArt);
                Image image = getFileRefFromSysFile(nomeImg);
                carteProdotti.add(image);

                Div descrizioneProdotto = new Div();
                descrizioneProdotto.setClassName("product-description");
                descrizioneProdotto.setText(((Object[]) m)[2].toString());
                carteProdotti.add(descrizioneProdotto);

                Div prezzoProdotto = new Div();
                prezzoProdotto.setClassName("product-price-offerta");
                String prezzoFormattato = "€" + formattaPrezzo(((Object[]) m)[1].toString()).replace(".", ",");
                prezzoProdotto.setText(prezzoFormattato);
                carteProdotti.add(prezzoProdotto);

                // --- Pallino colorato in basso a destra ---
                // ...
                carteProdotti.getElement().getStyle().set("position", "relative");
                buildPallino(codiceArt)
                        .ifPresent(p -> carteProdotti.getElement().appendChild(p.getElement()));
                promozioni.add(carteProdotti);




                String descrizione = ((Object[]) m)[2].toString();
                String finalPrezzo = prezzoFormattato;
                carteProdotti.addClickListener(event -> {
                    Map<String, String> parametri = new HashMap<>();
                    parametri.put("codice", codiceArt);
                    parametri.put("descrizione", descrizione);
                    parametri.put("prezzoCons", finalPrezzo);
                    parametri.put("codiceBar", getDataService.primoCodiceABarre(codiceArt));
                    parametri.put("nomeImg", nomeImg != null ? nomeImg : "");
                    parametri.put("codiceProdotti", "_________");
                    parametri.put("promo", "SI");
                    parametri.put("pagina", "");
                    viewNavigators.view(this, Dettaglioprodotto.class)
                            .withQueryParameters(QueryParameters.simple(parametri))
                            .navigate();
                });
            }
        }
    }


    @Subscribe(id = "scrollLeftBtn", subject = "clickListener")
    public void onScrollLeftBtnClick(final ClickEvent<JmixButton> event) {

        // Logica per lo scroll a sinistra
        //callJsFunction consente di eseguire una funzione JavaScript sul lato client direttamente da un componente sul lato server.
        //scrollBy è una funzione nativa del browser
        prodotti.getElement().callJsFunction("scrollBy", -250, 0);
    }

    @Subscribe(id = "scrollRightBtn", subject = "clickListener")
    public void onScrollRightBtnClick(final ClickEvent<JmixButton> event) {
        prodotti.getElement().callJsFunction("scrollBy", 250, 0);
    }

    @Subscribe(id = "scrollRightBtn1", subject = "clickListener")
    public void onScrollRightBtn1Click(final ClickEvent<JmixButton> event) {
        promozioni.getElement().callJsFunction("scrollBy", 250, 0);
    }

    @Subscribe(id = "scrollLeftBtn1", subject = "clickListener")
    public void onScrollLeftBtn1Click(final ClickEvent<JmixButton> event) {
        promozioni.getElement().callJsFunction("scrollBy", -250, 0);
    }


    public MenuBar menuBar(List<An> list) {
        MenuBar menuBar = new MenuBar();
        menuBar.setWidthFull(); // Set the width to 100%
        menuBar.addClassName("unorderedList");

        // Enable submenus to open on hover
        menuBar.setOpenOnHover(true);

        // Listener for window resize
        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeEvent -> {
            menuBar.getElement().executeJs("this.style.display='none'; this.offsetHeight; this.style.display='flex';");
        });

        // Add menu items
        Map<String, String> stringStringMap = new HashMap<>();

        for (An item : list) {
            MenuItem menuItem = menuBar.addItem(item.getDescrizione(), myClickListener);
            stringStringMap.put(item.getDescrizione(), item.getCodice());

            SubMenu subMenu = menuItem.getSubMenu();
            List<An> anSpecifiche = getDataService.getAnSpecifiche(item.getCodice());
            for (An subItem : anSpecifiche) {
                subMenu.addItem(subItem.getDescrizione(), myClickListener);
                stringStringMap.put(subItem.getDescrizione(), subItem.getCodice());
            }
            menuItem.setClassName("navigazioneButton");

            myClickListener.setStringStringMap(stringStringMap);

//                menuItem.addClickListener(event -> {
//                    viewNavigators.view(this, SecondaPagina.class)
//                            .withQueryParameters(QueryParameters.of("codice", myClickListener.getCodice()))
//                            .navigate();
//                });

            subMenu.getItems().forEach(menuItem1 -> {
                menuItem1.addClickListener(event -> {
                    viewNavigators.view(this, SecondaPagina.class)
                            .withQueryParameters(QueryParameters.of("codice", myClickListener.getCodice()))
                            .navigate();
                });

            });

//                tuttiProdotti.addClickListener(event -> {
//                   viewNavigators.view(this,SecondaPagina.class)
//                           .withQueryParameters(QueryParameters.of("codice", list.get(0).getCodice()))
//                           .navigate();
//                });

        }


        return menuBar;

    }

    @Subscribe(id = "accedi", subject = "clickListener")
    public void onAccediClick(final ClickEvent<JmixButton> event) {

        viewNavigators.view(this, LoginView.class)
                .navigate();

    }

    @Subscribe(id = "registrati", subject = "clickListener")
    public void onRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Registrazione.class).navigate();
    }

  /*  @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        UI.getCurrent().navigate("home");

    }*/


    private Image getFileRefFromSysFile(String sysFile) {

        Image avatarImage = new Image();
        if (sysFile != null) {
            String path = sysFile.substring(0, 11);
            String nomeFile = sysFile.substring(11);

            String nomeFileDir = path + nomeFile;
            String SRC_PATH = "/META-INF/resources/sales/" + path + nomeFile;

            StreamResource streamResource1 = new StreamResource(nomeFile.substring(0, nomeFile.length() - 4),
                    () -> getClass().getResourceAsStream(SRC_PATH));


            avatarImage.setSrc(streamResource1);
        }

        return avatarImage;

    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        footer();
        addDownloadAppBanner();
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
            viewNavigators.view(this,Volantino.class).navigate();
        });
        //socialicons1.add(facebookIcon1, instagramIcon1, twitterIcon1);
        socialicons1.add(bottoneRosso); //Ho aggiunto il bottone rosse e rimosso i social (il div contenitore è rimasto social icon)



        //RICERCA TESTUALE TELEFONO


        String textAreaFinal = "searchField";
        searchField.setId(textAreaFinal);
        String script = String.format("document.getElementById('searchField').addEventListener(\"keyup\", function(event) {" +
                        // "  if (event.keyCode == 13) {" +
                        // "    alert(event.keyCode);" +
                        // " document.getElementById('textAreaTo').value = document.getElementById('charAreaTo').value + String.fromCharCode(event.keyCode);" +
                        "    document.getElementById('searchField').dispatchEvent(new Event('change'));" +  // Dispara l'evento 'change'

                        // " document.getElementById('charAreaTo').focus();" +
                        // "  }" +
                        "});",
                textAreaFinal);
        searchField.getUI().ifPresent(ui -> {
            ui.getPage().executeJs(script);
        });




        AppCookies appCookies = new AppCookies();


        if (appCookies.getCookieValue("statoCookie") == null) {
            dialogs.createOptionDialog()
                    .withHeader("Apprezziamo la tua privacy")
                    .withText("Utilizziamo cookie e altre tecnologie per personalizzare la tua esperienza.\n" +   //eseguire attività di marketing e raccogliere analisi  da inserire in caso di ecommerce
                            "\n" +
                            "Puoi selezionare \"Accetta\" per concedere il consenso a questi usi, " +
                            "\"Rifiuta\" per rifiutare questi usi.  " +
                            "Consulta informativa sulla privacy")
                    .withActions(
                            new DialogAction(DialogAction.Type.OK).withText("Privacy").withIcon(VaadinIcon.FILE.create()).withHandler(
                                    e2 -> {
                                        consultaPrivacy();
                                        viewNavigators.view(this,Home.class).navigate();
                                    }),
                            new DialogAction(DialogAction.Type.CLOSE).withText("Accetta").withIcon(VaadinIcon.CHECK.create())
                                    .withHandler(e -> accettaCookie()),
                            new DialogAction(DialogAction.Type.CLOSE).withText("Rifiuta").withIcon(VaadinIcon.CLOSE.create()).withHandler(
                                    e1 -> rifiutaCookie())
                    ).withWidth("450px").withHeight("450px").open();


        }
//);
        //( cookieConsent.getElement()).add(htmlDeclina);


    }


    private void accettaCookie() {


        AppCookies appCookies = new AppCookies();
        appCookies.addCookie("statoCookie", "si");

    }


    private void rifiutaCookie() {

        AppCookies appCookies = new AppCookies();
        appCookies.addCookie("statoCookie", "no");

    }


    private void consultaPrivacy() {

        String tomcatPath = System.getProperty("catalina.base");
        File file = new File(tomcatPath+"/shopEcomstore/INFORMATIVAARTT.pdf");
        try
        {
            FileInputStream inputStream = new FileInputStream(file);

            DownloadDataProvider dataProvider = (() -> inputStream);

            downloader.setShowNewWindow(true);
            downloader.download(dataProvider, file.getName());
        }catch(
                Exception exception)

        {
            exception.printStackTrace();
        }
    }

    @Subscribe("searchField")
    public void onSearchFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<?>, ?> event) {
//        notifications.create("."+ searchField.getValue() + ".").show();
//        //KEYDOWN
//        String key = event.getValue().toString().replace("[", "").replace("]", "");
//        if (!key.equalsIgnoreCase("Backspace")) {
//            searchTextStatic = searchTextStatic + key;
//        } else {
//
//            if (searchTextStatic.length() > 0) {
//                searchTextStatic = searchTextStatic.substring(0, searchTextStatic.length() - 1);
//            } else {
//                searchTextStatic = "";
//            }
//
//        }
//        if(searchTextStatic.equalsIgnoreCase("")){
//            suggestionsBox.addClassName("hidden");
//        }

        //KEYUP
        if (!searchField.getValue().isEmpty()) {
            List<ArticoliClassificazioni> suggestions = getDataService.geArticoliClassificazioni(listino, searchField.getValue());
            updateSuggestionsBox(suggestions);
        } else {
            suggestionsBox.addClassName("hidden");
        }

    }
}