package com.company.ecomShop.view.dettaglioprodotto;


import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.Carrello;
import com.company.ecomShop.entity.ConfigEcom;
import com.company.ecomShop.listener.CarrelloChangedEvent;
import com.company.ecomShop.view.cupon.CuponView;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.invionotifiche.InvioNotifiche;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.pdc.PdcListView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.company.ecomShop.view.secondapagina.SecondaPagina;
import com.company.ecomShop.view.volantino.Volantino;
import com.example.application.components.Breadcrumb;
import com.example.application.components.BreadcrumbItem;
import com.example.application.components.Layout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.sys.LogoutSupport;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Route(value = "dettaglioprodotto")
@ViewController(id = "Dettaglioprodotto")
@ViewDescriptor(path = "dettaglioprodotto.xml")


@AnonymousAllowed
public class Dettaglioprodotto extends StandardView {
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private JmixButton CarrelloIcona;
    @ViewComponent
    private FlexLayout idDivProdotti;
    @ViewComponent
    private Div piede;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;
    String listino = "";
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
    private Span badge;
    String  descrizione = "";
    String codice = "";
    String codBarre = "";
    String prezzo = "";
    String nomeImg = "";
    String SRC_PATH="";
    String codicePagineProdotti="";
    String pezziXcartone = "";
    String giacenze="";
    String promo = "";
    String pagina = "";
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @ViewComponent
    private Div authButton;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private GetDataService getDataService;
    @ViewComponent
    private HorizontalLayout socialicons1;
    String flag = "";
    @ViewComponent
    private JmixImage<Object> logo;
    @ViewComponent
    private JmixButton promoBottone;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private JmixButton invioNotifiche;
    @ViewComponent
    private JmixButton UtentiRegistrati;
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
    @Subscribe(id = "accedi", subject = "clickListener")
    public void onAccediClick(final ClickEvent<JmixButton> event) {

        viewNavigators.view(this, LoginView.class)
                .navigate();

    }


    @Subscribe(id = "logOut", subject = "clickListener")
    public void onLogOutClick(final ClickEvent<JmixButton> event) {
        sessionDataProvider.getObject().setAttribute("listino",null);
        UI.getCurrent().getPage().executeJs("if(window.FlutterChannel){FlutterChannel.postMessage('logout');}");
        logoutSupport.logout();

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
    }

    @EventListener
    public void onCarrelloChangedEvent(CarrelloChangedEvent event) {
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> aggiornaCarrelloBadge(event.getNumeroProdotti()));
        }
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
        if (isMobile) {
            menuToggle.setVisible(true);
            // Nasconde i bottoni interni come se il toggle fosse cliccato
            authButton.setVisible(false);
        }
        loadLogo();

        // Controlla se l'utente è loggato
        Object listinoAttr = sessionDataProvider.getObject().getAttribute("listino");
        boolean isLoggedIn = listinoAttr != null;

        if (isLoggedIn) {
            // Utente loggato → posso lavorare con DB
            listino = listinoAttr.toString();
            promoBottone.setVisible(true);
            accedi.setVisible(false);
            registrati.setVisible(false);
            logOut.setVisible(true);
            CarrelloIcona.setVisible(true);

            // Carrello attivo
            Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();

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
            badge.setVisible(true);

        } else {
            // Utente NON loggato → nascondo tutto
            listino = "1";
            accedi.setVisible(true);
            registrati.setVisible(true);
            promoBottone.setVisible(false);
            logOut.setVisible(false);
            CarrelloIcona.setVisible(false);
            if (badge != null) badge.setVisible(false);
        }
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


    public void specificheProdotto() {
        idDivProdotti.add(createLayout());
        idDivProdotti.setFlexWrap(FlexLayout.FlexWrap.WRAP_REVERSE);
        idDivProdotti.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
    }

    private Component createLayout() {
        Layout layout = new Layout(createImages(), createInformation());
        layout.addClassNames("responsive-layout"); // Classe per il layout adattivo
        layout.setWidthFull();
        return layout;
    }

    public Component createImages() {
        Layout images = new Layout(createPreview(), createThumbnails());
        images.addClassNames("image-container", LumoUtility.Padding.MEDIUM);
        images.setAlignSelf(Layout.AlignSelf.START);
        images.setBoxSizing(Layout.BoxSizing.BORDER);
        images.setFlexDirection(Layout.FlexDirection.COLUMN);
        images.setGap(Layout.Gap.SMALL);
        images.setMaxWidth("600px"); // Grande su desktop
        images.setWidthFull();
        return images;
    }

    public Component createPreview() {
        Image img = new Image();
        if (nomeImg != null && !nomeImg.equalsIgnoreCase("")) {
            img = getFileRefFromSysFile(nomeImg);
        }
        img.setWidth("100%");
        img.getStyle()
                .set("max-height", "450px") // Grande su desktop
                .set("object-fit", "contain");

        Layout preview = new Layout(img);
        preview.addClassNames("image-preview", LumoUtility.BorderRadius.MEDIUM, LumoUtility.BoxShadow.SMALL);
        preview.setAlignItems(Layout.AlignItems.CENTER);
        preview.setJustifyContent(Layout.JustifyContent.CENTER);
        preview.setOverflow(Layout.Overflow.HIDDEN);
        preview.setWidthFull();
        return preview;
    }
    public Component createInformation() {
        Layout contentLayout = new Layout();
        contentLayout.addClassNames("info-container", LumoUtility.BoxSizing.BORDER,
                LumoUtility.MaxWidth.SCREEN_SMALL, LumoUtility.Padding.MEDIUM);
        contentLayout.setFlexDirection(Layout.FlexDirection.COLUMN);
        contentLayout.setAlignItems(Layout.AlignItems.START);
        contentLayout.setGap(Layout.Gap.SMALL);
        contentLayout.setWidthFull();

        // Breadcrumb
        BreadcrumbItem breadcrumbItem1 = new BreadcrumbItem("Prodotti", SecondaPagina.class);
        Breadcrumb breadcrumb = new Breadcrumb(
                new BreadcrumbItem("Home", Home.class),
                breadcrumbItem1
        );
        breadcrumb.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        breadcrumbItem1.addClickListener(event -> {
            Map<String, String> parametri = new HashMap<>();
            parametri.put("pagina", pagina);
            parametri.put("codice", codicePagineProdotti);
            viewNavigators.view(this, SecondaPagina.class)
                    .withQueryParameters(QueryParameters.simple(parametri))
                    .navigate();
        });

        // Torna indietro
        JmixButton jmixButton = new JmixButton();
        jmixButton.setText("Torna Indietro");
        jmixButton.addClassName("bottoneRegistrati");
        jmixButton.addClickListener(buttonClickEvent -> {
            Map<String, String> parametri = new HashMap<>();
            parametri.put("pagina", pagina);
            parametri.put("codice", codicePagineProdotti);
            viewNavigators.view(this, SecondaPagina.class)
                    .withQueryParameters(QueryParameters.simple(parametri))
                    .navigate();
        });

        // Titoli
        H2 title1 = new H2("Codice Interno : " + codice);
        title1.addClassNames(LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.MEDIUM);

        H2 title = new H2(descrizione);
        title.addClassNames(LumoUtility.FontSize.MEDIUM);

        // Prezzo
        Span price = new Span("Prezzo: " + prezzo);
        if (promo.equalsIgnoreCase("NO")) {
            price.addClassNames("PrezzoDettaglio");
        } else {
            price.addClassNames("PrezzoDettaglioPromo");
        }

        // Codice a barre
        Span codiceBarre = new Span("Codice a Barre : " + codBarre);
        codiceBarre.addClassNames(LumoUtility.FontWeight.BOLD,
                LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.NONE);

        // Descrizione estesa
        Paragraph description = new Paragraph(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        );
        description.addClassNames(LumoUtility.Margin.Bottom.LARGE, LumoUtility.Margin.Top.MEDIUM);
        description.setVisible(false);

        // Giacenza con KG se prodotto a peso
        boolean isQuantitaDecimale = "1".equals(flag);
        H5 giacenza = new H5("GIACENZE: " + giacenze + (isQuantitaDecimale ? " KG" : ""));
        giacenza.addClassNames(LumoUtility.FontSize.MEDIUM);

        // Pezzi x cartone
        H5 pezzixcartone = new H5("PEZZI X CARTONE : " + pezziXcartone);
        pezzixcartone.addClassNames(LumoUtility.FontSize.MEDIUM);

        // Controllo login
        boolean isLoggedIn = sessionDataProvider.getObject().getAttribute("listino") != null;

        // Layout quantità + bottone
        Layout quantityLayout;
        int giacenzaInt = Integer.parseInt(giacenze);

        if (giacenzaInt > 0) {
            IntegerField quantity = new IntegerField("Quantità");
            if (isQuantitaDecimale) {
                quantity.setLabel("La quantità è espressa in grammi.");
                quantity.setWidth("300px");
                quantity.setStep(100);
                quantity.setValue(100);
                quantity.setMin(100);
                quantity.setMax(giacenzaInt * 1000); // max in grammi
            } else {
                quantity.setStep(1);
                quantity.setValue(1);
                quantity.setMin(1);
                quantity.setMax(giacenzaInt);
            }
            quantity.setStepButtonsVisible(true);

            // JS per blocco input manuale
            quantity.getElement().executeJs(
                    "this.inputElement.setAttribute('readonly', true);" +
                            "this.inputElement.style.color='black';" +
                            "this.inputElement.style.backgroundColor='white';" +
                            "const buttons = this.shadowRoot.querySelectorAll('vaadin-integer-field-button');" +
                            "buttons.forEach(b => { b.style.backgroundColor='white'; b.style.color='black'; });" +
                            "this.inputElement.addEventListener('focus', e => { e.target.style.backgroundColor='white'; });" +
                            "this.inputElement.setAttribute('onkeydown', 'return false');" +
                            "this.inputElement.setAttribute('onpaste', 'return false');" +
                            "this.inputElement.setAttribute('ondrop', 'return false');"
            );

            Button add = new Button("Aggiungi al carrello", new Icon(VaadinIcon.CART));
            add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

            if (isMobile) {
                // Disposizione verticale
                quantityLayout = new Layout();
                quantityLayout.setFlexDirection(Layout.FlexDirection.COLUMN);
                quantityLayout.setAlignItems(Layout.AlignItems.STRETCH);
                quantityLayout.setGap(Layout.Gap.SMALL);

                // Su mobile, il campo quantità e il bottone uno sotto l'altro
                quantityLayout.add(quantity, add);

                // Il bottone occupa tutta la larghezza del contenitore
                add.setWidthFull();
                quantity.setWidthFull();
            } else {
                // Disposizione normale (orizzontale)
                quantityLayout = new Layout(quantity, add);
                quantityLayout.setAlignItems(Layout.AlignItems.BASELINE);
                quantityLayout.setGap(Layout.Gap.SMALL);
            }
            add.addClickListener(click -> {
                Number value = quantity.getValue();
                if (value == null || value.doubleValue() <= 0) {
                    Notification.show("Inserire una quantità valida");
                    return;
                }
                carrelloService.aggiungiArticoloAlCarrello(codice, value.doubleValue());
                Notification.show("Articolo aggiunto al carrello!");

                Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
                long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
                aggiornaCarrelloBadge(numeroProdotti);
            });

            // Visibilità solo se loggato
            quantity.setVisible(isLoggedIn);
            add.setVisible(isLoggedIn);
            quantityLayout.setVisible(isLoggedIn);

        } else {
            Button notify = new Button("Avvisami appena torna disponibile");
            notify.getStyle().set("background-color", "#007BFF");
            notify.getStyle().set("color", "white");
            notify.getStyle().set("border", "none");
            notify.getStyle().set("border-radius", "4px");
            notify.getStyle().set("padding", "8px 16px");
            notify.addClickListener(click -> carrelloService.salvaAvvisoProdotto(codice));

            quantityLayout = new Layout(notify);
            quantityLayout.setAlignItems(Layout.AlignItems.START);

            // Visibilità solo se loggato
            notify.setVisible(isLoggedIn);
            quantityLayout.setVisible(isLoggedIn);
        }

        // Aggiunta di tutti i componenti al layout
        contentLayout.add(
                breadcrumb,
                title,
                title1,
                price,
                codiceBarre,
                description,
                quantityLayout,
                pezzixcartone,
                giacenza,
                jmixButton
        );

        return contentLayout;
    }

   /* public Component createInformation() {
        Layout contentLayout = new Layout();
        contentLayout.addClassNames("info-container", LumoUtility.BoxSizing.BORDER,
                LumoUtility.MaxWidth.SCREEN_SMALL, LumoUtility.Padding.MEDIUM);
        contentLayout.setFlexDirection(Layout.FlexDirection.COLUMN);
        contentLayout.setAlignItems(Layout.AlignItems.START);
        contentLayout.setGap(Layout.Gap.SMALL);
        contentLayout.setWidthFull();

        // Breadcrumb
        BreadcrumbItem breadcrumbItem1 = new BreadcrumbItem("Prodotti", SecondaPagina.class);
        Breadcrumb breadcrumb = new Breadcrumb(
                new BreadcrumbItem("Home", Home.class),
                breadcrumbItem1
        );
        breadcrumb.addClassNames(LumoUtility.Margin.Bottom.XSMALL);

        breadcrumbItem1.addClickListener(event -> {
            Map<String, String> parametri = new HashMap<>();
            parametri.put("pagina", pagina);
            parametri.put("codice", codicePagineProdotti);
            viewNavigators.view(this, SecondaPagina.class)
                    .withQueryParameters(QueryParameters.simple(parametri))
                    .navigate();
        });

        // Torna indietro
        JmixButton jmixButton = new JmixButton();
        jmixButton.setText("Torna Indietro");
        jmixButton.addClassName("bottoneRegistrati");
        jmixButton.addClickListener(buttonClickEvent -> {
            Map<String, String> parametri = new HashMap<>();
            parametri.put("pagina", pagina);
            parametri.put("codice", codicePagineProdotti);
            viewNavigators.view(this, SecondaPagina.class)
                    .withQueryParameters(QueryParameters.simple(parametri))
                    .navigate();
        });

        // Titoli
        H2 title1 = new H2("Codice Interno : " + codice);
        title1.addClassNames(LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.MEDIUM);

        H2 title = new H2(descrizione);
        title.addClassNames(LumoUtility.FontSize.MEDIUM);

        // Prezzo
        Span price = new Span("Prezzo: " + prezzo);
        if (promo.equalsIgnoreCase("NO")) {
            price.addClassNames("PrezzoDettaglio");
        } else {
            price.addClassNames("PrezzoDettaglioPromo");
        }

        // Codice a barre
        Span codiceBarre = new Span("Codice a Barre : " + codBarre);
        codiceBarre.addClassNames(LumoUtility.FontWeight.BOLD,
                LumoUtility.Margin.Bottom.XSMALL, LumoUtility.Margin.Top.NONE);

        // Descrizione estesa
        Paragraph description = new Paragraph(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        );
        description.addClassNames(LumoUtility.Margin.Bottom.LARGE, LumoUtility.Margin.Top.MEDIUM);
        description.setVisible(false);

        // Giacenza con KG se prodotto a peso
        boolean isQuantitaDecimale = "1".equals(flag);
        H5 giacenza = new H5("GIACENZE: " + giacenze + (isQuantitaDecimale ? " KG" : ""));
        giacenza.addClassNames(LumoUtility.FontSize.MEDIUM);

        // Pezzi x cartone
        H5 pezzixcartone = new H5("PEZZI X CARTONE : " + pezziXcartone);
        pezzixcartone.addClassNames(LumoUtility.FontSize.MEDIUM);

        boolean isLoggedIn = sessionDataProvider.getObject().getAttribute("listino") != null;

        // Layout quantità + bottone
        Layout quantityLayout;

        int giacenzaInt = Integer.parseInt(giacenze);

        if (giacenzaInt > 0) {
            // Campo quantità
            IntegerField quantity = new IntegerField("Quantità");
            if (isQuantitaDecimale) {
                quantity.setLabel("La quantità è espressa in grammi.");
                quantity.setWidth("300px");
                quantity.setStep(100);
                quantity.setValue(100);
                quantity.setMin(100);
                quantity.setMax(giacenzaInt * 1000); // max in grammi
            } else {
                quantity.setStep(1);
                quantity.setValue(1);
                quantity.setMin(1);
                quantity.setMax(giacenzaInt);
            }
            quantity.setStepButtonsVisible(true);

            // JS per blocco input manuale
            quantity.getElement().executeJs(
                    "this.inputElement.setAttribute('readonly', true);" +
                            "this.inputElement.style.color='black';" +
                            "this.inputElement.style.backgroundColor='white';" +
                            "const buttons = this.shadowRoot.querySelectorAll('vaadin-integer-field-button');" +
                            "buttons.forEach(b => { b.style.backgroundColor='white'; b.style.color='black'; });" +
                            "this.inputElement.addEventListener('focus', e => { e.target.style.backgroundColor='white'; });" +
                            "this.inputElement.setAttribute('onkeydown', 'return false');" +
                            "this.inputElement.setAttribute('onpaste', 'return false');" +
                            "this.inputElement.setAttribute('ondrop', 'return false');"
            );

            // Bottone aggiungi al carrello
            Button add = new Button("Aggiungi al carrello", new Icon(VaadinIcon.CART));
            add.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            add.addClickListener(click -> {
                Number value = quantity.getValue();
                if (value == null || value.doubleValue() <= 0) {
                    Notification.show("Inserire una quantità valida");
                    return;
                }
                carrelloService.aggiungiArticoloAlCarrello(codice, value.doubleValue());
                Notification.show("Articolo aggiunto al carrello!");

                Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
                long numeroProdotti = carrelloService.carrelloRigheCount(carrello);
                aggiornaCarrelloBadge(numeroProdotti);
            });

            quantityLayout = new Layout(quantity, add);
            quantityLayout.setAlignItems(Layout.AlignItems.BASELINE);
            quantityLayout.setGap(Layout.Gap.SMALL);

        } else {
            // Giacenza 0 → bottone blu "Avvisami"
            Button notify = new Button("Avvisami appena torna disponibile");
            notify.getStyle().set("background-color", "#007BFF"); // blu
            notify.getStyle().set("color", "white");
            notify.getStyle().set("border", "none");
            notify.getStyle().set("border-radius", "4px");
            notify.getStyle().set("padding", "8px 16px");
            notify.addClickListener(click ->  carrelloService.salvaAvvisoProdotto(codice));

            quantityLayout = new Layout(notify);
            quantityLayout.setAlignItems(Layout.AlignItems.START);
        }

        // Mostra layout solo se utente loggato
        quantityLayout.setVisible(currentAuthentication.getUser() != null);

        // Aggiunta di tutti i componenti al layout
        contentLayout.add(
                breadcrumb,
                title,
                title1,
                price,
                codiceBarre,
                description,
                quantityLayout,
                pezzixcartone,
                giacenza,
                jmixButton
        );

        return contentLayout;
    }*/

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


    public Component createThumbnails() {
        Layout thumbnails = new Layout(
//                createThumbnail(this.img1, this.alt1),
//                createThumbnail(this.img2, this.alt2),
//                createThumbnail(this.img3, this.alt3),
//                createThumbnail(this.img4, this.alt4)
        );
        thumbnails.setColumns(Layout.GridColumns.COLUMNS_4);
        thumbnails.setDisplay(Layout.Display.GRID);
        thumbnails.setGap(Layout.Gap.MEDIUM);
        thumbnails.setVisible(false);
        return thumbnails;
    }

    public Component createThumbnail(String src, String alt) {
        Image img = new Image(src, alt);
        img.setWidthFull();

        Layout thumbnail = new Layout(img);
        thumbnail.addClassNames("aspect-square", LumoUtility.BorderRadius.LARGE, LumoUtility.BoxShadow.SMALL, LumoUtility.Overflow.HIDDEN);
        thumbnail.setAlignItems(Layout.AlignItems.CENTER);
        thumbnail.setJustifyContent(Layout.JustifyContent.CENTER);
        return thumbnail;
    }



    private Component createStar() {
        SvgIcon star = LineAwesomeIcon.STAR_SOLID.create();
        star.addClassNames(LumoUtility.IconSize.SMALL);
        return star;
    }

    private Component createHalfStar() {
        SvgIcon star = LineAwesomeIcon.STAR_HALF_SOLID.create();
        star.addClassNames(LumoUtility.IconSize.SMALL);
        return star;
    }

    private Component renderLabelWithDescription(String title, String desc) {
        Span primary = new Span(title);

        Span secondary = new Span(desc);
        secondary.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Layout layout = new Layout(primary, secondary);
        layout.addClassNames(LumoUtility.Padding.SMALL);
        layout.setFlexDirection(Layout.FlexDirection.COLUMN);
        layout.setGap(Layout.Gap.XSMALL);
        return layout;
    }


    public void add(Component... components) {
        Objects.requireNonNull(components, "Components should not be null");
        add(Arrays.asList(components));
    }

    /**
     * Adds the given components as children of this component.
     * <p>
     * In case any of the specified components has already been added to another
     * parent, it will be removed from there and added to this one.
     *
     * @param components
     *            the components to add
     */
    public void add(Collection<Component> components) {
        Objects.requireNonNull(components, "Components should not be null");
        components.stream()
                .map(component -> Objects.requireNonNull(component,
                        "Component to add cannot be null"))
                .map(Component::getElement).forEach(getElement()::appendChild);
    }

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {

        String codiceMag="1";
        String proprietaMag="0";

        List<String> messageParams = event.getQueryParameters().getParameters().get("codice");
        List<String> messageParams1 = event.getQueryParameters().getParameters().get("descrizione");
        List<String> messageParams2 = event.getQueryParameters().getParameters().get("prezzoCons");
        List<String> messageParams3 = event.getQueryParameters().getParameters().get("codiceBar");
        List<String> messafeParam4  = event.getQueryParameters().getParameters("nomeImg");
        List<String> messageParam5  = event.getQueryParameters().getParameters("codiceProdotti");
        List<String> messageParam6  = event.getQueryParameters().getParameters("promo");
        List<String> messageParam7 = event.getQueryParameters().getParameters("pagina");

        codice= messageParams.get(0).toString();
        descrizione=messageParams1.get(0).toString();
        prezzo=messageParams2.get(0).toString();
        codBarre=messageParams3.get(0).toString();
        nomeImg=messafeParam4.get(0) != null ? messafeParam4.get(0).toString() : "" ;
        codicePagineProdotti = messageParam5.get(0).toString();
        promo = messageParam6.get(0).toString();
        if(messageParam7.isEmpty()) {
            pagina = "1";
        }else{
            pagina = messageParam7.get(0).toString();
        }


        pezziXcartone=getDataService.getArtxCartone(codice);
        giacenze=getDataService.getGiacenze(codice);

        String codiceArticolo = carrelloService.findCodiceArticoloByCodBarFor(codBarre);
        if (codiceArticolo != null) {
            flag = carrelloService.getFlagPeso(codiceArticolo);
        } else {
            flag = null;
        }
        specificheProdotto();
    }

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }



    private Image getFileRefFromSysFile(String sysFile) {

        Image avatarImage = new Image();



        if(sysFile!=null) {
            String path = sysFile.substring(0, 11);
            String nomeFile = sysFile.substring(11);

            String nomeFileDir = path + nomeFile;
            SRC_PATH = "/META-INF/resources/sales/" + path + nomeFile;




            StreamResource streamResource1 = new StreamResource(nomeFile.substring(0, nomeFile.length() - 4),
                    () -> getClass().getResourceAsStream(SRC_PATH));






            avatarImage.setSrc(streamResource1);
        }

        return avatarImage;

    }

    @Subscribe(id = "registrati", subject = "clickListener")
    public void onRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Registrazione.class).navigate();
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

}