package com.company.ecomShop.view.cupon;


import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.invionotifiche.InvioNotifiche;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.pdc.PdcListView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.company.ecomShop.view.volantino.Volantino;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "cupon-view")
@ViewController(id = "CuponView")
@ViewDescriptor(path = "cupon-view.xml")
public class CuponView extends StandardView {
    String listino = "";
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;
    @ViewComponent
    private Div piede;
    @ViewComponent
    private JmixButton accedi;
    @ViewComponent
    private JmixButton CarrelloIcona;
    @ViewComponent
    private JmixButton Home;
    @ViewComponent
    private JmixButton logOut;
    @ViewComponent
    private JmixButton registrati;
    @Autowired
    private GetDataService getDataService;
    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private JmixImage<Object> logo;
    private Span badge;
    @Autowired
    private LogoutSupport logoutSupport;
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private HorizontalLayout socialicons1;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @ViewComponent
    private CollectionContainer<CouponDto> couponDc;
    @ViewComponent
    private Tabs tabs;
    @ViewComponent
    private Div promoContent;
    @ViewComponent
    private Div couponContent;
    @ViewComponent
    private CollectionContainer<PromoDto> promoDc;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private Div authButton;
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

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick1(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, com.company.ecomShop.view.home.Home.class).navigate();
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

        Image twitterIcon1 = new Image("#", "TikTok");
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
        bottoneRosso.setWidth("100%");
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

    @Subscribe(id = "CarrelloIcona", subject = "clickListener")
    public void onCarrelloIconaClick(final ClickEvent<JmixButton> event) {
        CarrelloIcona.removeClassName("cart-grow");
        carrelloService.vaiAlCarrello();
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
        loadCouponData();
        loadPromoData();
        loadLogo();
        Carrello carrello = carrelloService.getOrCreateCarrelloAttivo();
        carrelloService.aggiornaPrezziCarrelloAttivo();

        boolean isMobile = UI.getCurrent().getInternals()
                .getExtendedClientDetails().getWindowInnerWidth() <= 480;


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
        } else {
            listino = "1";
            accedi.setVisible(true);
            registrati.setVisible(true);
            logOut.setVisible(false);
            CarrelloIcona.setVisible(false);
            badge.setVisible(false);
        }
        // Imposta il comportamento quando si seleziona un tab
        tabs.addSelectedChangeListener(event1 -> {
            Tab selectedTab = event1.getSelectedTab();
            if ("couponTab".equals(selectedTab.getId().orElse(""))) {
                showCouponContent();
                loadCouponData();  // ricarica i coupon
            } else if ("promoTab".equals(selectedTab.getId().orElse(""))) {
                showPromoContent();
                loadPromoData();   // ricarica le promo solo ora
            }
        });

    }
    private void showCouponContent() {
        couponContent.setVisible(true);
        promoContent.setVisible(false);
    }

    private void showPromoContent() {
        promoContent.setVisible(true);
        couponContent.setVisible(false);
    }
private void loadCouponData(){
    String username = currentAuthentication.getUser().getUsername();
    Pdc pdc = carrelloService.getPdcByUsername(username);
    String codiceConto = pdc.getCodiceConto();
//   String tesseraConto = carrelloService.getCodiceTesseraByCodiceCliente(codiceConto).toString();
    if (pdc == null) return;

    String dataOggi = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    List<CouponDto> coupons = getDataService.getCouponInfo(codiceConto, dataOggi);

    couponDc.setItems(coupons);
}
    private void loadPromoData(){
        String username = currentAuthentication.getUser().getUsername();
        Pdc pdc = carrelloService.getPdcByUsername(username);
        if (pdc == null) return;

        String codiceTessera = carrelloService.getTesseraByCodiceCliente(pdc.getCodiceConto()).getCodiceTessera();
        List<PromoDto> promos = getDataService.getActivePromotionsForTessera(codiceTessera);

        promoDc.setItems(promos);
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
}