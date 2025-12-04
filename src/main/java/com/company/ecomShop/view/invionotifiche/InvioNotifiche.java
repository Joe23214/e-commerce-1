package com.company.ecomShop.view.invionotifiche;

import com.company.ecomShop.app.FcmService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.Art;
import com.company.ecomShop.entity.Art2;
import com.company.ecomShop.entity.DeviceToken;
import com.company.ecomShop.view.art.ArtListView;
import com.company.ecomShop.view.art2.Art2ListView;
import com.company.ecomShop.view.home.Home;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.radiobuttongroup.JmixRadioButtonGroup;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.valuepicker.EntityPicker;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "invio-notifiche")
@ViewController("ecomShop_InvioNotifiche")
@ViewDescriptor("invio-notifiche.xml")
public class InvioNotifiche extends StandardView {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private FcmService fcmService;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @Autowired
    private Notifications notifications;
    @Value("${path-andorid-link}")
    private String androidLink;
    @ViewComponent
    private TextField titleField;
    @ViewComponent
    private TextArea messageField;
    @ViewComponent
    private ComboBox<String> redirectViewSelect;
    @ViewComponent
    private Button sendButton;
    @ViewComponent private HorizontalLayout ricercaInternoArea;
    @ViewComponent
    private TypedTextField<Object> ricercaInterno;
    @ViewComponent private HorizontalLayout ricercaCodArea;
    @ViewComponent
    private TypedTextField<Object> ricercaCod;
    @ViewComponent private Button CercaInterno;
    @ViewComponent private Button CercaCod;
    @ViewComponent
    private TypedTextField<Object> descrInterno;
    @ViewComponent
    private TypedTextField<Object> descrCod;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private GetDataService getDataService;
    @ViewComponent
    private JmixRadioButtonGroup<String> tipoRicercaGroup;
    @ViewComponent
    private HorizontalLayout tipoRicercaArea;
    @Autowired
    private DialogWindows dialogWindows;
    // Campo di supporto per memorizzare il codice articolo selezionato
    private String selectedCodArt = null;

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }
    public String formattaPrezzo(String s) {
        String prova = s + "0000000";
        String result = prova.substring(0,prova.indexOf(".")) + prova.substring(prova.indexOf("."),prova.indexOf(".")+3);
        result = result.replace('.',',');
        return result;
    }

    @Subscribe
    public void onInit(InitEvent event) {
        // ✅ Popoliamo le opzioni della ComboBox
        Map<String, String> options = new LinkedHashMap<>();
        options.put("home", "🏠 Home");
        options.put("carrello", "🛒 Carrello");
        options.put("cupon-view", "🎟️ Coupon");
        options.put("volantino", "📰 Volantino");
        options.put("prodotto", "📦 Prodotto");

        redirectViewSelect.setItems(options.keySet());
        redirectViewSelect.setItemLabelGenerator(options::get);

        // Popoliamo RadioButtonGroup
        Map<String, String> tipoRicercaOptions = new LinkedHashMap<>();
        tipoRicercaOptions.put("interno", "🔹 Codice interno");
        tipoRicercaOptions.put("barre", "🔹 Codice a barre");

        tipoRicercaGroup.setItems(tipoRicercaOptions.keySet());
        tipoRicercaGroup.setItemLabelGenerator(tipoRicercaOptions::get);

        // Mostra/Nascondi tipoRicercaArea solo se "prodotto" è selezionato
        redirectViewSelect.addValueChangeListener(e -> {
            boolean isProdotto = "prodotto".equalsIgnoreCase(e.getValue());
            tipoRicercaArea.setVisible(isProdotto);

            // Reset campi
            tipoRicercaGroup.clear();
            ricercaInterno.clear();
            ricercaCod.clear();
            descrInterno.setValue("");
            descrCod.setValue("");

            // Nascondiamo anche le TextField finché non viene selezionato il radio button
            ricercaInternoArea.setVisible(false);
            ricercaCodArea.setVisible(false);
        });

        // Mostra/Nascondi le TextField in base al radio button selezionato
        tipoRicercaGroup.addValueChangeListener(e -> {
            String tipo = e.getValue();
            if ("interno".equalsIgnoreCase(tipo)) {
                ricercaInternoArea.setVisible(true);
                ricercaCodArea.setVisible(false);
            } else if ("barre".equalsIgnoreCase(tipo)) {
                ricercaInternoArea.setVisible(false);
                ricercaCodArea.setVisible(true);
            } else {
                ricercaInternoArea.setVisible(false);
                ricercaCodArea.setVisible(false);
            }

            // Reset dei valori ogni volta che cambio tipo
            ricercaInterno.clear();
            ricercaCod.clear();
            descrInterno.setValue("");
            descrCod.setValue("");
        });

        // 🔹 Collegamento bottoni Cerca
        CercaInterno.addClickListener(e -> openArtDialogAndSelect());
        CercaCod.addClickListener(e -> openArt2DialogAndSelect());
    }

  /* private void openArtDialogAndSelect() {
       dialogWindows.lookup(this, Art.class)
               .withSelectHandler(selectedItems -> {
                   if (selectedItems != null && !selectedItems.isEmpty()) {
                       Art art = selectedItems.iterator().next();
                       ricercaInterno.setValue(Optional.ofNullable(art.getCodiceArticolo()).orElse(""));
                       descrInterno.setValue("Descrizione: " + Optional.ofNullable(art.getDescrizione()).orElse(""));
                   }
               })
               .build()
               .open();
   }


    private void openArt2DialogAndSelect() {
        dialogWindows.lookup(this, Art2.class)
                .withSelectHandler(selectedItems -> {
                    if (selectedItems != null && !selectedItems.isEmpty()) {
                        Art2 art2 = selectedItems.iterator().next();
                        if (art2.getId() != null) {
                            // Recupero codice articolo da Art2
                            String codArt = art2.getId().getCodiceArticolo();
                            String codBar = art2.getId().getCodBarFor();

                            // Carico la descrizione da Art usando DataManager
                            Art art = dataManager.load(Art.class)
                                    .id(codArt)
                                    .optional()
                                    .orElse(null);

                            ricercaCod.setValue(Optional.ofNullable(codBar).orElse(""));
                            descrCod.setValue("Descrizione: " +
                                    (art != null ? Optional.ofNullable(art.getDescrizione()).orElse("") : ""));
                        }
                    }
                })
                .build()
                .open();
    }

    // ✅ Invio della notifica
    @Subscribe("sendButton")
    public void onSendButtonClick(ClickEvent<Button> event) {
        String title = titleField.getValue();
        String message = messageField.getValue();
        String redirectView = redirectViewSelect.getValue();

        if (title == null || title.isBlank() ||
                message == null || message.isBlank() ||
                redirectView == null) {
            notifications.create("⚠️ Compila tutti i campi prima di inviare la notifica.")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        // 🟩 Validazione extra per “prodotto”
        if ("prodotto".equalsIgnoreCase(redirectView)) {
            boolean internoOk = ricercaInterno.getValue() != null && !ricercaInterno.getValue().isBlank();
            boolean codOk = ricercaCod.getValue() != null && !ricercaCod.getValue().isBlank();

            if (!internoOk && !codOk) {
                notifications.create("⚠️ Inserisci almeno un codice prodotto.").show();
                return;
            }
            if (internoOk && codOk) {
                notifications.create("⚠️ Solo uno dei due campi deve essere valorizzato.").show();
                return;
            }
        }

        systemAuthenticator.withSystem(() -> {
            List<DeviceToken> tokens = dataManager.load(DeviceToken.class)
                    .query("select d from DeviceToken d where d.enabled = true")
                    .list();
            for (DeviceToken dt : tokens) {
                Map<String, String> data = new LinkedHashMap<>();
                data.put("icon", "ic_notification");
                data.put("sound", "default");

                if ("prodotto".equalsIgnoreCase(redirectView)) {
                    String codArt = null;

                    if (ricercaInterno.getValue() != null && !ricercaInterno.getValue().isBlank()) {
                        codArt = ricercaInterno.getValue();
                    } else if (ricercaCod.getValue() != null && !ricercaCod.getValue().isBlank()) {
                        List<Art2> art2List = dataManager.load(Art2.class)
                                .query("select a from Art2 a where a.id.codBarFor = :codBar and a.id.flagBF = 'B'")
                                .parameter("codBar", ricercaCod.getValue())
                                .list();
                        if (!art2List.isEmpty()) {
                            codArt = art2List.get(0).getId().getCodiceArticolo();
                        }
                    }

                    if (codArt != null) {
                        // 👈 usa sempre "1" come codicePagineProdotti
                        String linkProdotto = generaLinkProdotto(codArt, "1");
                        data.put("redirect", linkProdotto);
                    } else {
                        data.put("redirect", "home"); // fallback
                    }
                } else {
                    data.put("redirect", redirectView);
                }

                fcmService.sendNotificationToUser(dt.getUser(), title, message, data);
            }
            return null;
        });

        notifications.create("✅ Notifica inviata con successo!")
                .withType(Notifications.Type.SUCCESS)
                .show();

        // Reset campi
        titleField.clear();
        messageField.clear();
        redirectViewSelect.clear();
        ricercaInterno.clear();
        ricercaCod.clear();
        descrInterno.setValue("");
        descrCod.setValue("");
    }*/
  // ================================================
// 🔹 Selettore per Art (ricerca per codice interno)
// ================================================
  private void openArtDialogAndSelect() {
      dialogWindows.lookup(this, Art.class)
              .withSelectHandler(selectedItems -> {
                  if (selectedItems != null && !selectedItems.isEmpty()) {
                      Art art = selectedItems.iterator().next();

                      // Salva il codice articolo scelto
                      selectedCodArt = Optional.ofNullable(art.getCodiceArticolo()).orElse(null);

                      // Aggiorna i campi UI
                      ricercaInterno.setValue(selectedCodArt != null ? selectedCodArt : "");
                      descrInterno.setValue("Descrizione: " +
                              Optional.ofNullable(art.getDescrizione()).orElse(""));

                      // Pulisci eventuale campo alternativo
                      ricercaCod.clear();
                      descrCod.clear();
                  }
              })
              .build()
              .open();
  }

    // ================================================
// 🔹 Selettore per Art2 (ricerca per codice a barre)
// ================================================
    private void openArt2DialogAndSelect() {
        dialogWindows.lookup(this, Art2.class)
                .withSelectHandler(selectedItems -> {
                    if (selectedItems != null && !selectedItems.isEmpty()) {
                        Art2 art2 = selectedItems.iterator().next();
                        if (art2.getId() != null) {
                            // Recupera codice articolo da Art2
                            String codArt = art2.getId().getCodiceArticolo();
                            String codBar = art2.getId().getCodBarFor();

                            // Carica l'Art per la descrizione
                            Art art = dataManager.load(Art.class)
                                    .id(codArt)
                                    .optional()
                                    .orElse(null);

                            // Salva internamente il codice articolo selezionato
                            selectedCodArt = codArt;

                            // Aggiorna campi UI
                            ricercaCod.setValue(Optional.ofNullable(codBar).orElse(""));
                            descrCod.setValue("Descrizione: " +
                                    (art != null ? Optional.ofNullable(art.getDescrizione()).orElse("") : ""));

                            // Pulisci eventuale campo alternativo
                            ricercaInterno.clear();
                            descrInterno.setValue("");
                        }
                    }
                })
                .build()
                .open();
    }

    // ================================================
// 🔹 Invio della notifica semplificato
// ================================================
    @Subscribe("sendButton")
    public void onSendButtonClick(ClickEvent<Button> event) {
        String title = titleField.getValue();
        String message = messageField.getValue();
        String redirectView = redirectViewSelect.getValue();

        if (title == null || title.isBlank() ||
                message == null || message.isBlank() ||
                redirectView == null) {
            notifications.create("⚠️ Compila tutti i campi prima di inviare la notifica.")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        // 🟩 Validazione specifica per "prodotto"
        if ("prodotto".equalsIgnoreCase(redirectView)) {
            if (selectedCodArt == null || selectedCodArt.isBlank()) {
                notifications.create("⚠️ Seleziona un prodotto prima di inviare la notifica.").show();
                return;
            }
        }

        systemAuthenticator.withSystem(() -> {
            List<DeviceToken> tokens = dataManager.load(DeviceToken.class)
                    .query("select d from DeviceToken d where d.enabled = true")
                    .list();

            for (DeviceToken dt : tokens) {
                Map<String, String> data = new LinkedHashMap<>();
                data.put("icon", "ic_notification");
                data.put("sound", "default");

                if ("prodotto".equalsIgnoreCase(redirectView)) {
                    // Usa direttamente il codice articolo selezionato
                    String linkProdotto = generaLinkProdotto(selectedCodArt, "1");
                    data.put("redirect", linkProdotto);
                } else {
                    data.put("redirect", redirectView);
                }

                fcmService.sendNotificationToUser(dt.getUser(), title, message, data);
            }
            return null;
        });

        notifications.create("✅ Notifica inviata con successo!")
                .withType(Notifications.Type.SUCCESS)
                .show();

        // Reset campi UI e variabile
        titleField.clear();
        messageField.clear();
        redirectViewSelect.clear();
        ricercaInterno.clear();
        ricercaCod.clear();
        descrInterno.setValue("");
        descrCod.setValue("");
        selectedCodArt = null;
    }

    private String generaLinkProdotto(String codArt, String codicePagineProdotti) {
        Art prodotto = dataManager.load(Art.class)
                .id(codArt)
                .optional()
                .orElse(null);

        if (prodotto == null) return "#"; // fallback se il prodotto non esiste

        // Recupero valori
        String descrizione = prodotto.getDescrizione();
        Double prezzoCons = prodotto.getPrezzoCons();
        String prezzo = "€" + formattaPrezzo(prezzoCons != null ? prezzoCons.toString() : "0.00");

        // Recupero codice a barre e immagine tramite i tuoi servizi
        String codiceBar = getDataService.primoCodiceABarre(prodotto.getCodiceArticolo());
        String nomeImg = getDataService.getArtImgPrincipale(prodotto.getCodiceArticolo());

        // Costruisco i parametri della query string
        Map<String, String> parametri = new HashMap<>();
        parametri.put("codice", prodotto.getCodiceArticolo());
        parametri.put("descrizione", descrizione != null ? descrizione : "");
        parametri.put("prezzoCons", prezzo);
        parametri.put("codiceBar", codiceBar != null ? codiceBar : "");
        parametri.put("nomeImg", nomeImg != null ? nomeImg : "");
        parametri.put("codiceProdotti", codicePagineProdotti != null ? codicePagineProdotti : "1"); // 👈 sempre "1"
        parametri.put("promo", "NO");
        parametri.put("pagina", "1");

        String queryString = parametri.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        String baseUrl = androidLink;

        return baseUrl + "/dettaglioprodotto?" + queryString;
    }

}
