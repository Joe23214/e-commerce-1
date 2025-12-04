package com.company.ecomShop.view.secondapagina;


import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.app.MyClickListener;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.listener.CarrelloChangedEvent;
import com.company.ecomShop.view.cupon.CuponView;
import com.company.ecomShop.view.dettaglioprodotto.Dettaglioprodotto;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.invionotifiche.InvioNotifiche;
import com.company.ecomShop.view.login.LoginView;
import com.company.ecomShop.view.pdc.PdcListView;
import com.company.ecomShop.view.registrazione.Registrazione;
import com.company.ecomShop.view.volantino.Volantino;
import com.example.application.components.Badge;
import com.example.application.components.Layout;
import com.example.application.components.PriceRange;
import com.example.application.components.dialogs.NativeDialog;
import com.example.application.components.list.ProductListItem;
import com.example.application.themes.RadioButtonTheme;
import com.example.application.utilities.BadgeVariant;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.kaesdingeling.hybridmenu.components.HMButton;
import de.kaesdingeling.hybridmenu.components.HMSubMenu;
import de.kaesdingeling.hybridmenu.components.LeftMenu;
import de.kaesdingeling.hybridmenu.data.MenuConfig;
import de.kaesdingeling.hybridmenu.data.enums.ETheme;
import io.jmix.core.DataManager;
import io.jmix.core.FileStorageLocator;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.sys.LogoutSupport;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.sun.el.lang.ELArithmetic.add;
import static com.vaadin.flow.theme.lumo.LumoUtility.Display.BLOCK;
import static com.vaadin.flow.theme.lumo.LumoUtility.Display.Breakpoint.XXLarge.FLEX;
import static com.vaadin.flow.theme.lumo.LumoUtility.Display.HIDDEN;


@Route(value = "seconda-pagina")
@ViewController(id = "SecondaPagina")
@ViewDescriptor(path = "seconda-pagina.xml")

@AnonymousAllowed
public class SecondaPagina extends StandardView {
    @ViewComponent
    private JmixButton CarrelloIcona;
    @Autowired
    private FileStorageLocator fileStorageLocator;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private Div authButton;
    @ViewComponent
    private HorizontalLayout socialicons1;
    @ViewComponent
    private Div paginator1;
    private final Random random = new Random();
    private Section sidebar;
    @ViewComponent
    private JmixFormLayout idDiv;
    @ViewComponent
    private JmixImage<Object> logo;
    @ViewComponent
    private JmixButton promoBottone;
    @Autowired
    private GetDataService getDataService;
    @ViewComponent
    private Div menuSide;
    @ViewComponent
    private JmixButton menuToggle;
    @ViewComponent
    private Button Home;
    @ViewComponent
    private Div paginator;
    @Autowired
    private ViewNavigators viewNavigators;
    private Integer numeroPagina = 1;
    private Integer elementiPagina = 12;
    private Integer totalePagine = 0;
    private String classeAn = "";
    private String cerca = "";
    @ViewComponent
    private Div piede;
    private String oldSearchText = "";
    String searchTextStatic = "";
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;

    String listino = "";

    private static final Logger log = LoggerFactory.getLogger(StandardView.class);
    @Autowired
    private MyClickListener myClickListener;
    @ViewComponent
    private JmixButton accedi;
    @ViewComponent
    private JmixButton registrati;
    @Autowired
    private LogoutSupport logoutSupport;
    @ViewComponent
    private JmixButton logOut;
    private Span badge;
    String codicePagineProdotti ="";
    String classAnSelezionata = "";
    @Autowired
    private CarrelloService carrelloService;
    @ViewComponent
    private JmixButton menuToggle1;
    @Autowired
    private CurrentAuthentication currentAuthentication;
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

    @Subscribe(id = "menuToggle1", subject = "clickListener")
    public void onMenuToggle1Click(final ClickEvent<JmixButton> event) {
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

    @Subscribe(id = "accedi", subject = "clickListener")
    public void onAccediClick(final ClickEvent<JmixButton> event) {

        viewNavigators.view(this, LoginView.class)
                .navigate();

    }

    @Subscribe(id = "promoBottone", subject = "clickListener")
    public void onPromoBottoneClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, CuponView.class).navigate();
    }
    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, com.company.ecomShop.view.home.Home.class).navigate();
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
            menuToggle1.setVisible(true);
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

                // Mostro carrello e badge
                CarrelloIcona.setVisible(true);
                promoBottone.setVisible(true);
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

        if(UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()<=480){
            menuToggle.setVisible(true);
        }else{
            menuToggle.setVisible(false);
        }


//        logo.setSrc("https://i.postimg.cc/63ZpMjJ7/PHOTO-2025-01-21-08-40-55-1-removebg-preview.png");
        List<An> list = getDataService.getAn("15");
//         nav.add(getDataService.menuBar(list));


        MenuConfig menuConfig = new MenuConfig();
        menuConfig.setTheme(ETheme.Lumo);
        VaadinSession.getCurrent().setAttribute(MenuConfig.class, menuConfig);

        LeftMenu leftMenu = new LeftMenu();
        leftMenu.getStyle().set("max-width", "320px");
        leftMenu.getStyle().set("width", "320px");
        leftMenu.add(HMButton.get()
                .withCaption( UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()<=480 ? "X" : "")
                .withClickListener(event1 -> {
                    if( UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()<=480) {
                        if (menuSide.getClassNames().contains("open")) {
                            menuSide.removeClassName("open"); // Nasconde il menu
                        } else {
                            menuSide.addClassName("open"); // Mostra il menu
                        }
                    }
                }));

//        leftMenu.add(HMLabel.get()
//                .withCaption("<b>Categorie</b>")
//                .withIcon(new Image("./frontend/HybridMenu/Images/Logo.png", "Logo")));
//

        // Crea il menu a 3 livelli
        for (An an : list) {

            HMSubMenu memberList = leftMenu.add(HMSubMenu.get().withCaption(an.getDescrizione()));
            memberList.getButton().setId(an.getCodice());
            memberList.addClassNames("bottoniMenuSide");

            memberList.getButton().addClickListener(event1 -> {
                if ((UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth() > 480)
                        || !memberList.isOpen() // && !classAnSelezionata.equalsIgnoreCase(event1.getSource().getId().get()) //il controllo viene fatto al contrario perchè hsubmenu mette open appena clicchi sul menu
                    //||  (memberList1.isOpen() && classAnSelezionata.equalsIgnoreCase(event1.getSource().getId().get()))
                ) {

                    searchTextStatic="";
                    idDiv.remove(idDiv.getComponent("sideBar"));
                    idDiv.remove(idDiv.getComponent("content"));
                    addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
                    Section sidebar1 = createSidebar();
                    classeAn = event1.getSource().getId().get();
                    Component component = createContent(classeAn + "______", searchTextStatic, listino, 1, elementiPagina);
                    sidebar1.setId("sideBar");
                    component.setId("content");
                    classeAn = event1.getSource().getId().get();
                    idDiv.add(sidebar1, component);
                    numeroPagina = 1;
                    closeSidebar();
                    menuSide.removeClassName("open");
                    codicePagineProdotti = classeAn;
                }
            });


            for (An specifica1 : getDataService.getAnSpecifiche(an.getCodice())) {
                HMSubMenu memberList1 = memberList.add(HMSubMenu.get().withCaption(specifica1.getDescrizione()));
                memberList1.getButton().setId(specifica1.getCodice());
                memberList1.addClassNames("bottoniMenuSide");

                memberList1.getButton().addClickListener(event1 -> {
                    if(( UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()>480)
                            ||  !memberList1.isOpen() // && !classAnSelezionata.equalsIgnoreCase(event1.getSource().getId().get()) //il controllo viene fatto al contrario perchè hsubmenu mette open appena clicchi sul menu
                        //||  (memberList1.isOpen() && classAnSelezionata.equalsIgnoreCase(event1.getSource().getId().get()))
                    ) {
                        searchTextStatic="";
                        idDiv.remove(idDiv.getComponent("sideBar"));
                        idDiv.remove(idDiv.getComponent("content"));
                        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
                        Section sidebar1 = createSidebar();
                        classeAn = event1.getSource().getId().get();
                        Component component = createContent(classeAn + "___", searchTextStatic, listino, 1, elementiPagina);
                        classeAn = event1.getSource().getId().get();
                        sidebar1.setId("sideBar");
                        component.setId("content");
                        idDiv.add(sidebar1, component);
                        numeroPagina = 1;
                        closeSidebar();
                        menuSide.removeClassName("open");
                        classAnSelezionata = "";
                    } else{
                        if(( UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth()<=480)){
                            classAnSelezionata = event1.getSource().getId().get();
                        }
                    }
                    codicePagineProdotti = classeAn;
                });

//


                for (An specifica2 : getDataService.getAnSpecifiche(specifica1.getCodice())) {
                    HMSubMenu memberList2 = memberList1.add(HMSubMenu.get().withCaption(specifica2.getDescrizione()));
                    memberList2.getButton().setId(specifica2.getCodice());
                    memberList2.addClassNames("bottoniMenuSide");
                    memberList2.getButton().addClickListener(event1 -> {
                        searchTextStatic="";
                        idDiv.remove(idDiv.getComponent("sideBar"));
                        idDiv.remove(idDiv.getComponent("content"));
                        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
                        Section sidebar1 = createSidebar();
                        classeAn = event1.getSource().getId().get();
                        Component component = createContent(classeAn,searchTextStatic, listino, 1, elementiPagina);
                        classeAn = event1.getSource().getId().get();
                        sidebar1.setId("sideBar");
                        component.setId("content");
                        idDiv.add(sidebar1, component);
                        numeroPagina = 1;
                        closeSidebar();
                        menuSide.removeClassName("open");
                        classAnSelezionata = "";
                        codicePagineProdotti = classeAn;
                    });

                }
            }
        }


        menuSide.add(leftMenu);




    }

    @Subscribe(id = "menuToggle", subject = "clickListener")
    public void onMenuToggleClick(final ClickEvent<JmixButton> event) {
        if (menuSide.getClassNames().contains("open")) {
            menuSide.removeClassName("open"); // Nasconde il menu
        } else {
            menuSide.addClassName("open"); // Mostra il menu
        }
    }




    public void ProductListView() {
        addClassNames(FLEX, LumoUtility.Height.FULL, HIDDEN);
        add(createSidebar(), createContent("001", "1", searchTextStatic,1, elementiPagina));
        closeSidebar();
    }

    private Section createSidebar() {
        H2 title = new H2("Filters");
        title.addClassNames(LumoUtility.FontSize.MEDIUM);

        Button close = new Button(LineAwesomeIcon.TIMES_SOLID.create());
        close.addClickListener(e -> {
            this.sidebar.setEnabled(false);
            this.sidebar.removeClassName(LumoUtility.Border.RIGHT);
            // Desktop
            this.sidebar.getStyle().set("margin-inline-start", "-20rem");
            // Mobile
            this.sidebar.addClassNames("-start-full");
            this.sidebar.removeClassName("start-0");
        });
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.setAriaLabel("Close sidebar");
        close.setTooltipText("Close sidebar");

        Layout header = new Layout(title, close);
        header.addClassNames(LumoUtility.Padding.End.MEDIUM, LumoUtility.Padding.Start.LARGE, LumoUtility.Padding.Vertical.SMALL);
        header.setAlignItems(Layout.AlignItems.CENTER);
        header.setJustifyContent(Layout.JustifyContent.BETWEEN);

        CheckboxGroup<String> brands = new CheckboxGroup("Brands");
        brands.setItems("LuxeLiving", "DecoHaven", "CasaCharm", "HomelyCraft", "ArtisanHaus");
        brands.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        setRenderer(brands);

        PriceRange priceRange = new PriceRange("Price");

        CheckboxGroup<String> rating = new CheckboxGroup("Rating");
        rating.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        rating.setItems("1 star", "2 stars", "3 stars", "4 stars", "5 stars");
        rating.setRenderer(new ComponentRenderer<>(item -> {
            String count = Integer.toString(this.random.nextInt(100));

            Badge badge = new Badge(count);
            badge.addThemeVariants(BadgeVariant.CONTRAST, BadgeVariant.SMALL, BadgeVariant.PILL);

            int stars = Integer.parseInt(item.split(" ")[0]);

            Span span = new Span(getStars(stars), badge);
            span.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL);
            span.getElement().setAttribute("aria-hidden", "true");

            Span screenReader = new Span(item + ", " + count + " items");
            screenReader.addClassNames(LumoUtility.Accessibility.SCREEN_READER_ONLY);

            return new Span(span, screenReader);
        }));

        CheckboxGroup<String> availability = new CheckboxGroup("Availability");
        availability.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        availability.setItems("In stock", "Out of stock");
        setRenderer(availability);

        Layout form = new Layout(brands, priceRange, rating, availability);
        form.addClassNames(LumoUtility.Padding.Horizontal.LARGE);
        form.setFlexDirection(Layout.FlexDirection.COLUMN);

        this.sidebar = new Section(header, form);
        this.sidebar.addClassNames("backdrop-blur-sm", "bg-tint-90", LumoUtility.Border.RIGHT,
                LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Position.ABSOLUTE, "lg:static", "bottom-0", "top-0",
                "transition-all", "z-10");
        this.sidebar.setWidth(20, Unit.REM);
        return this.sidebar;
    }

    private void setRenderer(CheckboxGroup<String> checkboxGroup) {
        checkboxGroup.setRenderer(new ComponentRenderer<>(item -> {
            Badge badge = new Badge(Integer.toString(this.random.nextInt(100)));
            badge.addThemeVariants(BadgeVariant.CONTRAST, BadgeVariant.SMALL, BadgeVariant.PILL);

            Span span = new Span(new Text(item), badge);
            span.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL);
            return span;
        }));
    }

    private Text getStars(int stars) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < stars) {
                builder.append("★");
            } else {
                builder.append("☆");
            }
        }
        return new Text(builder.toString());
    }




    public Component createContent(String codiceAn,String text, String listino, int numeroPagina, int elementiPagina) {

        List<Art> list = getDataService.geArticoliByText(codiceAn,text, listino, numeroPagina-1 <0 ? 0 : numeroPagina-1, elementiPagina);
        Integer totaleArticoli;
        if(!text.equalsIgnoreCase("")){
            totaleArticoli = getDataService.getArtTotaliByText(text,codiceAn,listino);
        }else{
            totaleArticoli= getDataService.getArtTotali(codiceAn,listino);
        }

        Div div = new Div();
        Div div1 = new Div();
        div = paginator;
        div1 = paginator1;
        div.setVisible(true);
        div1.setVisible(true);
        TypedTextField textFieldNPagine = (TypedTextField) div.getComponentAt(2);
        TypedTextField textFieldNPagineTot = (TypedTextField) div.getComponentAt(3);
        textFieldNPagine.setValue(String.valueOf(numeroPagina));
        totalePagine = totaleArticoli / elementiPagina + 1;
        classeAn = codiceAn;
        textFieldNPagineTot.setValue(String.valueOf(totalePagine));

        TypedTextField textFieldNPagine1 = (TypedTextField) div1.getComponentAt(2);
        TypedTextField textFieldNPagineTot1 = (TypedTextField) div1.getComponentAt(3);
        textFieldNPagineTot1.setValue(String.valueOf(totalePagine));
        textFieldNPagine1.setValue(String.valueOf(numeroPagina));

        Layout content = new Layout(createToolbar(), div, createList(list),div1);
        content.addClassNames(LumoUtility.Flex.GROW);
        content.setFlexDirection(Layout.FlexDirection.COLUMN);
        content.setOverflow(Layout.Overflow.HIDDEN);


        return content;


    }

    public Component createToolbar() {
        TextField search = new TextField();

        search.addClassNames(LumoUtility.Flex.GROW, LumoUtility.MinWidth.NONE);
        search.setAriaLabel("Cerca");
        search.setPlaceholder("Cerca...");
        search.setPrefixComponent(LumoIcon.SEARCH.create());



        search.addKeyUpListener(event -> {
            if( event.getCode().toString().contains("Enter")  ) {
                menuToggle.focus();
            }
        });

        search.addValueChangeListener(event -> {
            if(idDiv != null && !idDiv.findOwnComponent("content").isEmpty()  && !idDiv.findOwnComponent("sideBar").isEmpty()  ) {
                String testo = search.getValue();
                String searchText = searchTextStatic; // Assicurati che searchTextStatic sia la variabile globale
                log.error("ENTRATO: " + searchText);
                searchTextStatic = search.getValue();


                idDiv.remove(idDiv.getComponent("content"));
                idDiv.remove(idDiv.getComponent("sideBar"));

                String classeAnRicerca = "_________";
                addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
                Component component = createContent(classeAnRicerca, search.getValue(), listino, 1, elementiPagina);
                Section sidebar1 = createSidebar();

                sidebar1.setId("sideBar");
                component.setId("content");
                //classeAn = "_________";


                idDiv.add(sidebar1, component);

                closeSidebar();

            }
        });



        MultiSelectComboBox brands = new MultiSelectComboBox<>();
        brands.addClassNames(HIDDEN, "lg:inline-flex", LumoUtility.MinWidth.NONE);
        brands.setAriaLabel("Brands");
        brands.setItems("LuxeLiving", "DecoHaven", "CasaCharm", "HomelyCraft", "ArtisanHaus");
        brands.setPlaceholder("Brands");

        Button price = new Button("Price");
        price.addClassNames(HIDDEN, "lg:inline-block");
        price.setIcon(new Icon("lumo", "angle-down"));
        price.setIconAfterText(true);

        PriceRange priceRange = new PriceRange("Price");
        priceRange.addClassNames(LumoUtility.Margin.SMALL, LumoUtility.Padding.Top.XSMALL);
        priceRange.setWidth(16, Unit.REM);

        NativeDialog priceDialog = new NativeDialog(priceRange);
        priceDialog.setRight(8, Unit.REM);
        priceDialog.setTop(7.5f, Unit.REM);
        price.addClickListener(e -> priceDialog.showModal());

        // TODO: a11y improvements, opened/closed states
        Button filters = new Button("Filters", LineAwesomeIcon.SLIDERS_H_SOLID.create());
        filters.addClickListener(e -> {
            if (this.sidebar.isEnabled()) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        //filters.addClickListener(e -> toggleSidebar());

        RadioButtonGroup<String> mode = new RadioButtonGroup();
        mode.setAriaLabel("View mode");
        mode.setItems("Grid", "List");
        mode.setRenderer(new ComponentRenderer<>(item -> renderIconWithAriaLabel(item)));
        mode.setValue("Grid");
        setRadioButtonGroupTheme(mode, RadioButtonTheme.TOGGLE);

        if (!searchTextStatic.equalsIgnoreCase("")) {
            search.setValue(searchTextStatic);
        } else {
            search.setValue("");
        }


        Layout toolbar = new Layout(search, brands, price);
        toolbar.addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.SMALL);
        toolbar.setAlignItems(Layout.AlignItems.CENTER);
        toolbar.setGap(Layout.Gap.MEDIUM);



        return toolbar;
    }




    private void toggleSidebar() {
        if (this.sidebar.isEnabled()) {
            closeSidebar();
        } else {
            openSidebar();
        }
    }

    private void openSidebar() {
        this.sidebar.setEnabled(true);
        this.sidebar.setVisible(true);
        this.sidebar.addClassNames(LumoUtility.Border.RIGHT);
        // Desktop
        this.sidebar.getStyle().remove("margin-inline-start");
        // Mobile
        this.sidebar.addClassNames("start-0");
        this.sidebar.removeClassName("-start-full");
    }

    private void closeSidebar() {
        this.sidebar.setEnabled(false);
        this.sidebar.removeClassName(LumoUtility.Border.RIGHT);
        // Desktop
        this.sidebar.getStyle().set("margin-inline-start", "-20rem");
        // Mobile
        this.sidebar.addClassNames("-start-full");
        this.sidebar.removeClassName("start-0");
        this.sidebar.setVisible(false);
    }

    private Component renderIconWithAriaLabel(String item) {
        Component icon = item.equals("Grid") ?
                LineAwesomeIcon.TH_SOLID.create() :
                LineAwesomeIcon.LIST_SOLID.create();
        icon.getElement().setAttribute("aria-label", item);
        return icon;
    }

    String SRC_PATH = "/META-INF/resources/sales/";

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

    protected InputStream getResource() {
        return getClass().getResourceAsStream(SRC_PATH);
    }
 public Component createList(List<Art> listaArticoli) {

     String tipo_Tes = "";
     if(accedi.isVisible()){
         tipo_Tes = "";
     } else {
         tipo_Tes = "RS999";
     }

     com.example.application.components.list.List list = new com.example.application.components.list.List();
     if(UI.getCurrent().getInternals().getExtendedClientDetails().getWindowInnerWidth() <= 480) {
         list.setAutoFill(150, Unit.PIXELS);
     } else {
         list.setAutoFill(320, Unit.PIXELS);
     }
     list.setOverflow(Layout.Overflow.AUTO);

     for (int i = 0; i < listaArticoli.size(); i++) {

         String codiceArticolo = listaArticoli.get(i).getCodiceArticolo();
         String descrizione = listaArticoli.get(i).getDescrizione();
         String codiceEan = getDataService.primoCodiceABarre(codiceArticolo);
         String prezzo = "€" + formattaPrezzo(listaArticoli.get(i).getPrezzoCons().toString());
         String promozione = "NO";

         String nomeimg = getDataService.getArtImgPrincipale(codiceArticolo);
         List<Object[]> verPromo = getDataService.verArtOfferta(tipo_Tes, codiceArticolo);
         boolean promo = false;
         if(!verPromo.isEmpty()){
             promo = true;
             Object m = verPromo.get(0);
             prezzo = ((Object[]) m)[1].toString();
             prezzo = formattaPrezzo(prezzo);
             prezzo = "€" + prezzo.replace(".",",") + " PROMO!";
             promozione = "SI";
         }

         String pezzixCartoni = getDataService.getArtxCartone(codiceArticolo);
         int giacenza = Integer.parseInt(getDataService.getGiacenze(codiceArticolo));
         // ora sono Integer, possono essere null
         Integer giacenzaGiallo = carrelloService.getGiacenzaGiallo();
         Integer giacenzaRosso = carrelloService.getGiacenzaRosso();

         ProductListItem item = new ProductListItem(
                 nomeimg,
                 promo,
                 descrizione,
                 "Codice Articolo: " + codiceArticolo,
                 "Codice EAN: " + codiceEan,
                 prezzo,
                 "Pezzi x Cartone : " + pezzixCartoni,
                 "Giacenze : " + giacenza,
                 createIconButton(LineAwesomeIcon.HEART, "Favourite SparkleClean")
         );

         // --- Pallino colorato solo se soglie configurate (>0) ---
         if (giacenzaGiallo != null && giacenzaRosso != null
                 && giacenzaGiallo > 0 && giacenzaRosso > 0) {

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
             } else if (giacenza > giacenzaGiallo) {
                 pallino.getStyle().set("background-color", "green");
             } else if (giacenza > giacenzaRosso) {
                 pallino.getStyle().set("background-color", "yellow");
             } else {
                 pallino.getStyle().set("background-color", "red");
             }

             item.getElement().getStyle().set("position", "relative");
             item.getElement().appendChild(pallino.getElement());
         }
         // --------------------------------------------------------

         String finalPrezzo = prezzo;
         String finalPromozione = promozione;
         item.addClickListener(event -> {
             Map<String,String> parametri = new HashMap<>();
             parametri.put("codice", codiceArticolo);
             parametri.put("descrizione", descrizione);
             parametri.put("prezzoCons", finalPrezzo);
             parametri.put("codiceBar", codiceEan);
             parametri.put("nomeImg", nomeimg != null ? nomeimg : "");
             parametri.put("codiceProdotti", codicePagineProdotti);
             parametri.put("promo", finalPromozione);
             parametri.put("pagina", numeroPagina.toString());

             viewNavigators.view(this, Dettaglioprodotto.class)
                     .withQueryParameters(QueryParameters.simple(parametri))
                     .navigate();
         });

         list.add(item);
     }

     return list;
 }

    private Button createIconButton(LineAwesomeIcon icon, String label) {
        Button button = new Button(icon.create());
        button.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        button.setAriaLabel(label);
        button.setTooltipText(label);
        button.setVisible(false);
        return button;
    }

    private void setRadioButtonGroupTheme(RadioButtonGroup group, String... themeNames) {
        group.addThemeNames(themeNames);
        group.getChildren().forEach(component -> {
            for (String themeName : themeNames) {
                component.getElement().getThemeList().add(themeName);
            }
        });
    }
    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {

        List<String> messageParams = event.getQueryParameters().getParameters().get("codice");

        List<String> pagina = event.getQueryParameters().getParameters().get("pagina");
        if (pagina != null && !pagina.isEmpty()) {
            numeroPagina = Integer.valueOf(pagina.get(0));
        }

        if (messageParams != null && !messageParams.isEmpty()) {
            codicePagineProdotti=messageParams.get(0);
            addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
            Section sidebar1 = createSidebar();
            Component component = createContent(messageParams.get(0), searchTextStatic,listino, numeroPagina, elementiPagina);
            sidebar1.setId("sideBar");
            component.setId("content");

//            Paginator paginator = new Paginator();
//            int numeroOggetti = 12;
//            int numeroElementi = component.getElement().getChildCount();
//            int numberPages = numeroElementi / numeroOggetti;
//
//            //paginator.setNumberOfPages(numberPages);
//
//            paginator.addChangeSelectedPageListener(e -> {
//
//                //loadItems(people,grid,e.getPage(),itemsPerPage);
//                idDiv.remove(idDiv.getComponent("sideBar"));
//                idDiv.remove(idDiv.getComponent("content"));
//                addClassNames( FLEX, LumoUtility.Height.FULL, BLOCK);
//                Section sidebar2 = createSidebar();
//                Component component2 = createContent(classeAn,"1" ,e.getPage(), elementiPagina);
//                sidebar1.setId("sideBar");
//                component.setId("content");
//                idDiv.add(sidebar1,component);
//
//            });

            idDiv.add(sidebar1, component);


            closeSidebar();
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


    @Subscribe("Home")
    public void onHomeAttach(final AttachEvent event) {
        viewNavigators.view(this, com.company.ecomShop.view.home.Home.class).navigate();
    }

    @Subscribe(id = "icon1", subject = "clickListener")
    public void onIcon1Click(final ClickEvent<Icon> event) {
        idDiv.remove(idDiv.getComponent("sideBar"));
        idDiv.remove(idDiv.getComponent("content"));
        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
        Section sidebar1 = createSidebar();

        Component component = createContent(classeAn,searchTextStatic, listino, 1, elementiPagina);
        sidebar1.setId("sideBar");
        component.setId("content");
        idDiv.add(sidebar1, component);

        closeSidebar();

    }

    @Subscribe(id = "icon1_1", subject = "clickListener")
    public void onIcon1_1Click(final ClickEvent<Icon> event) {
        if (numeroPagina > 1) {
            idDiv.remove(idDiv.getComponent("sideBar"));
            idDiv.remove(idDiv.getComponent("content"));
            addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
            Section sidebar1 = createSidebar();

            Component component = createContent(classeAn,searchTextStatic, listino, numeroPagina - 1, elementiPagina);
            sidebar1.setId("sideBar");
            component.setId("content");
            idDiv.add(sidebar1, component);
            numeroPagina = numeroPagina - 1;

            closeSidebar();

        }
    }

    @Subscribe(id = "icon1_2", subject = "clickListener")
    public void onIcon1_2Click(final ClickEvent<Icon> event) {

        if (numeroPagina < totalePagine) {
            idDiv.remove(idDiv.getComponent("sideBar"));
            idDiv.remove(idDiv.getComponent("content"));
            addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
            Section sidebar1 = createSidebar();

            Component component = createContent(classeAn,searchTextStatic, listino, numeroPagina + 1, elementiPagina);
            sidebar1.setId("sideBar");
            component.setId("content");
            idDiv.add(sidebar1, component);

            numeroPagina = numeroPagina + 1;

            closeSidebar();

        }
    }

    @Subscribe(id = "icon1_3", subject = "clickListener")
    public void onIcon1_3Click ( final ClickEvent<Icon> event) {
        idDiv.remove(idDiv.getComponent("sideBar"));
        idDiv.remove(idDiv.getComponent("content"));
        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
        Section sidebar1 = createSidebar();


        Component component = createContent(classeAn, searchTextStatic, listino, totalePagine, elementiPagina);
        sidebar1.setId("sideBar");
        component.setId("content");
        idDiv.add(sidebar1, component);
        numeroPagina = totalePagine;
        closeSidebar();


    }


    @Subscribe(id = "icon", subject = "clickListener")
    public void onIconClick(final ClickEvent<Icon> event) {
        idDiv.remove(idDiv.getComponent("sideBar"));
        idDiv.remove(idDiv.getComponent("content"));
        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
        Section sidebar1 = createSidebar();

        Component component = createContent(classeAn,searchTextStatic, listino, 1, elementiPagina);
        sidebar1.setId("sideBar");
        component.setId("content");
        idDiv.add(sidebar1, component);

        closeSidebar();

    }

    @Subscribe(id = "icon_1", subject = "clickListener")
    public void onIcon_1Click(final ClickEvent<Icon> event) {
        if (numeroPagina > 1) {
            idDiv.remove(idDiv.getComponent("sideBar"));
            idDiv.remove(idDiv.getComponent("content"));
            addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
            Section sidebar1 = createSidebar();

            Component component = createContent(classeAn,searchTextStatic, listino, numeroPagina - 1, elementiPagina);
            sidebar1.setId("sideBar");
            component.setId("content");
            idDiv.add(sidebar1, component);
            numeroPagina = numeroPagina - 1;

            closeSidebar();

        }
    }

    @Subscribe(id = "icon_2", subject = "clickListener")
    public void onIcon_2Click(final ClickEvent<Icon> event) {

        if (numeroPagina < totalePagine) {
            idDiv.remove(idDiv.getComponent("sideBar"));
            idDiv.remove(idDiv.getComponent("content"));
            addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
            Section sidebar1 = createSidebar();

            Component component = createContent(classeAn,searchTextStatic, listino, numeroPagina + 1, elementiPagina);
            sidebar1.setId("sideBar");
            component.setId("content");
            idDiv.add(sidebar1, component);

            numeroPagina = numeroPagina + 1;

            closeSidebar();

        }
    }

    @Subscribe(id = "icon_3", subject = "clickListener")
    public void onIcon_3Click ( final ClickEvent<Icon> event) {
        idDiv.remove(idDiv.getComponent("sideBar"));
        idDiv.remove(idDiv.getComponent("content"));
        addClassNames(FLEX, LumoUtility.Height.FULL, BLOCK);
        Section sidebar1 = createSidebar();


        Component component = createContent(classeAn, searchTextStatic, listino, totalePagine, elementiPagina);
        sidebar1.setId("sideBar");
        component.setId("content");
        idDiv.add(sidebar1, component);
        numeroPagina = totalePagine;
        closeSidebar();


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

    public String formattaPrezzo(String s){




        String prova = s + "0000000";
        String result = prova.substring(0,prova.indexOf(".")) + prova.substring(prova.indexOf("."),prova.indexOf(".")+3);
        result = result.replace('.',',');


        return result;


    }
    @Subscribe(id = "registrati", subject = "clickListener")
    public void onRegistratiClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Registrazione.class).navigate();
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

}
