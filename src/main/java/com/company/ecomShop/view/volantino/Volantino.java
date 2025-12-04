package com.company.ecomShop.view.volantino;


import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.entity.Carrello;
import com.company.ecomShop.view.cupon.CuponView;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.invionotifiche.InvioNotifiche;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.pdc.PdcListView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.core.Resources;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.kit.component.button.JmixButton;
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
import java.util.Optional;

@Route(value = "volantino")
@ViewController(id = "Volantino")
@ViewDescriptor(path = "volantino.xml")
@AnonymousAllowed
public class Volantino extends StandardView {
    @ViewComponent
    private JmixButton CarrelloIcona;
    @Autowired
    private Resources resources;
    @ViewComponent
    private FlexLayout idDivPdf;
    @ViewComponent
    private Div piede;  @Autowired
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
    @Autowired
    private CarrelloService carrelloService;
    private Span badge;
    @ViewComponent
    private JmixImage<Object> logo;
    @ViewComponent
    private JmixButton promoBottone;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private Div authButton;
    @Autowired
    private CurrentAuthentication currentAuthentication;
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

    @Subscribe(id = "CarrelloIcona", subject = "clickListener")
    public void onCarrelloIconaClick(final ClickEvent<JmixButton> event) {
        carrelloService.vaiAlCarrello();
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

        // Controllo se l'utente è loggato
        boolean isLoggedIn = sessionDataProvider.getObject().getAttribute("listino") != null;

        if (isLoggedIn) {
            listino = sessionDataProvider.getObject().getAttribute("listino").toString();

            // Disabilito pulsanti di accesso/registrazione
            accedi.setVisible(false);
            registrati.setVisible(false);
            logOut.setVisible(true);
            promoBottone.setVisible(true);
            // Mostro carrello e badge
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
            // Utente non loggato: nascondo badge, carrello e pulsanti logout
            listino = "1";
            accedi.setVisible(true);
            registrati.setVisible(true);
            logOut.setVisible(false);
            CarrelloIcona.setVisible(false);
            promoBottone.setVisible(false);
            if (badge != null) badge.setVisible(false);
        }

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();

        if(accedi.isVisible() || (listino.equalsIgnoreCase("1") || listino.equalsIgnoreCase("2"))) {
            StreamResource resource = new StreamResource("volantino.pdf", () ->
                    resources.getResourceAsStream("META-INF/resources/pdf/volantino.pdf"));
            pdfViewer.setSrc(resource);
            pdfViewer.setZoom("0.5");
            pdfViewer.getStyle().setWidth("100%");
            pdfViewer.getStyle().setHeight("700px");
            pdfViewer.addClassName("pdf-viewer-height");
            pdfViewer.openThumbnailsView();
            idDivPdf.setSizeFull();
            idDivPdf.add(pdfViewer);
        }else{
            StreamResource resource = new StreamResource("offerteingrosso.pdf", () ->
                    resources.getResourceAsStream("META-INF/resources/pdf/offerteingrosso.pdf"));
            pdfViewer.setSrc(resource);
            pdfViewer.setZoom("0.5");
            pdfViewer.getStyle().setWidth("100%");
            pdfViewer.getStyle().setHeight("700px");
            pdfViewer.addClassName("pdf-viewer-height");
            pdfViewer.openThumbnailsView();
            idDivPdf.setSizeFull();
            idDivPdf.add(pdfViewer);
        }
        if( UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()<=480){
            pdfViewer.closeThumbnailsView();
            pdfViewer.setZoom("0.3");
        }
            footer();
    }





    public void footer() {
        // Crea il layout principale del footer
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
        Paragraph link1 = new Paragraph("PER INFO E ORDINI SCRIVI SU WHATSAPP AL: +393479456912");
        link1.addClassName("footerLink");
        HorizontalLayout horizontalLayout = new HorizontalLayout(wAppIcon, link1);
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
//
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

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        PdfViewer pdfViewer  = (PdfViewer) idDivPdf.getComponentAt(0);
        String z=pdfViewer.getZoom();
        viewNavigators.view(this, Home.class).navigate();
    }

    @Subscribe(id = "registrati", subject = "clickListener")
    public void onRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Registrazione.class).navigate();
    }


    public void aggiornaCarrelloBadge(long numeroProdotti) {
        badge.setText(String.valueOf(numeroProdotti));
        CarrelloIcona.removeClassName("cart-grow");
        CarrelloIcona.addClassName("cart-grow");
    }




}