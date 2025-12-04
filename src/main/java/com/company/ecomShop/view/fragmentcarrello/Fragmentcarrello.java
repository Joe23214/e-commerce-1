package com.company.ecomShop.view.fragmentcarrello;

import com.company.ecomShop.app.CarrelloService;
import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.Art;
import com.company.ecomShop.entity.CarrelloRiga;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.core.DataManager;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.textfield.JmixIntegerField;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

@FragmentDescriptor("fragmentCarrello.xml")
@RendererItemContainer("item")
public class Fragmentcarrello extends FragmentRenderer<HorizontalLayout, CarrelloRiga> {

    @ViewComponent
    private InstanceContainer<CarrelloRiga> item;

    @ViewComponent
    private InstanceContainer<Art> item2;

    @ViewComponent
    private InstanceLoader<Art> artLoader;

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private GetDataService getDataService;

    @ViewComponent
    private JmixIntegerField quantitaField;

    @ViewComponent
    private Span prezzo;

    @ViewComponent
    private Span descrizione;

    @ViewComponent
    private Span valoreTotale;

    @ViewComponent
    private Span unitaMisura;

    @ViewComponent
    private JmixImage<Object> img;

    // -----------------------------
    // INCREMENTA
    // -----------------------------
    @Subscribe(id = "incrementaQta", subject = "clickListener")
    public void onIncrementaQtaClick(ClickEvent<Button> event) {
        CarrelloRiga riga = item.getItem();
        Art art = item2.getItem();
        if (art == null) return;

        boolean isBilancia = "1".equals(art.getFlagBilancia());
        int step = isBilancia ? 100 : 1;
        int max = calcolaMax(art, isBilancia);

        int nuovaQta = Math.min(quantitaField.getValue() + step, max);
        aggiornaQuantita(riga, nuovaQta, isBilancia);
    }

    // -----------------------------
    // DECREMENTA
    // -----------------------------
    @Subscribe(id = "decrementaQta", subject = "clickListener")
    public void onDecrementaQtaClick(ClickEvent<Button> event) {
        CarrelloRiga riga = item.getItem();
        Art art = item2.getItem();
        if (art == null) return;

        boolean isBilancia = "1".equals(art.getFlagBilancia());
        int step = isBilancia ? 100 : 1;

        int nuovaQta = quantitaField.getValue() - step;

        if (nuovaQta <= 0) {
            eliminaRiga();
        } else {
            aggiornaQuantita(riga, nuovaQta, isBilancia);
        }
    }

    // -----------------------------
    // AGGIORNA QUANTITÀ SENZA RICARICA
    // -----------------------------
    private void aggiornaQuantita(CarrelloRiga riga, int qtaGrammiOPz, boolean isBilancia) {
        // Passiamo al service la quantità così com'è
        carrelloService.aggiornaQuantita(riga.getCodiceArticolo(), (double) qtaGrammiOPz);

        // Aggiorniamo localmente l'item
        double qtaDb = isBilancia ? qtaGrammiOPz / 1000.0 : qtaGrammiOPz;
        riga.setQtaPeso(qtaDb);
        item.setItem(riga);

        quantitaField.setValue(qtaGrammiOPz);

        // Aggiorna totale riga
        Double valore = riga.getValore();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        valoreTotale.setText(valore != null ? currencyFormat.format(valore) : "-");

        // Refresh pagina
        UI.getCurrent().getPage().reload();
    }

    // -----------------------------
    // ELIMINA RIGA
    // -----------------------------
    private void eliminaRiga() {
        String codiceArticolo = item.getItem().getCodiceArticolo();
        carrelloService.eliminaRigaCarrello(codiceArticolo);

        getContent().setVisible(false);

        UI.getCurrent().getPage().reload();
    }

    @Subscribe(id = "eliminaRiga", subject = "clickListener")
    public void onEliminaRigaClick(ClickEvent<Icon> event) {
        eliminaRiga();
    }

    // -----------------------------
    // SET ITEM
    // -----------------------------
    @Override
    public void setItem(CarrelloRiga carrelloRiga) {
        super.setItem(carrelloRiga);

        String codiceArticolo = carrelloRiga.getCodiceArticolo();
        Art art = dataManager.load(Art.class)
                .query("select a from Art a where a.codiceArticolo = :codiceArticolo")
                .parameter("codiceArticolo", codiceArticolo)
                .optional()
                .orElse(null);

        if (art != null) {
            item2.setItem(art);
            descrizione.setText(art.getDescrizione());

            Double prz = carrelloService.recuperaPrezzoArticolo(codiceArticolo);
            prezzo.setText(prz != null ? String.format(Locale.ITALY, "%.2f €", prz) : "-");

            String nomeImg = getDataService.getArtImgPrincipale(codiceArticolo);

            if (nomeImg != null && !nomeImg.isEmpty()) {
                String path = nomeImg.substring(0, 11);
                String fileName = nomeImg.substring(11);
                String resourcePath = "/META-INF/resources/sales/" + path + fileName;

                try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                    if (is != null) {
                        // Devi servire l'immagine come URL web, non come stream Java
                        // In Vaadin/Jmix le risorse sotto META-INF/resources sono servite come statiche
                        String webPath = "/sales/" + path + fileName;
                        img.setSrc(webPath);
                    } else {
                        img.setSrc("/images/placeholder.png");
                    }
                } catch (IOException e) {
                    img.setSrc("/images/placeholder.png");
                }
            } else {
                img.setSrc("/images/placeholder.png");
            }
            img.setWidth("80px");
            img.setHeight("80px");
            img.getStyle().set("object-fit", "cover");

            boolean isBilancia = "1".equals(art.getFlagBilancia());

            if (isBilancia) {
                int qtaGrammi = (int) Math.round(carrelloRiga.getQtaPeso() * 1000);
                quantitaField.setValue(qtaGrammi);
                int maxGrammi = calcolaMax(art, true);
                quantitaField.setMin(100);
                quantitaField.setMax(maxGrammi);
                quantitaField.setStep(100);
                unitaMisura.setText("GR.");
            } else {
                int qtaPz = carrelloRiga.getQtaPeso().intValue();
                quantitaField.setValue(qtaPz);
                int maxPz = calcolaMax(art, false);
                quantitaField.setMin(1);
                quantitaField.setMax(maxPz);
                quantitaField.setStep(1);
                unitaMisura.setText("PZ.");
            }

            // Blocca input manuale
            quantitaField.setReadOnly(true);
            quantitaField.getElement().executeJs(
                    "this.inputElement.setAttribute('readonly', true);" +
                            "this.inputElement.setAttribute('onpaste', 'return false');" +
                            "this.inputElement.setAttribute('ondrop', 'return false');" +
                            "this.inputElement.setAttribute('onkeydown', 'return false');"
            );

            unitaMisura.getStyle().set("font-weight", "700");

            Double valore = carrelloRiga.getValore();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
            valoreTotale.setText(valore != null ? currencyFormat.format(valore) : "-");
        } else {
            descrizione.setText("-");
            prezzo.setText("-");
            valoreTotale.setText("-");
            unitaMisura.setText("-");
        }
    }

    // -----------------------------
    // CALCOLA MASSIMO QUANTITÀ
    // -----------------------------
    private int calcolaMax(Art art, boolean isBilancia) {
        double giacenza = Double.parseDouble(getDataService.getGiacenze(art.getCodiceArticolo()));
        return isBilancia ? (int) Math.round(giacenza * 1000) : (int) Math.round(giacenza);
    }
}
