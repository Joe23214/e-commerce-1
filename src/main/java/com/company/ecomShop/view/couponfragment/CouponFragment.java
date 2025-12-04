package com.company.ecomShop.view.couponfragment;

import com.company.ecomShop.entity.CouponDto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
@RendererItemContainer("item")
@FragmentDescriptor("coupon-fragment.xml")
public class CouponFragment extends FragmentRenderer<VerticalLayout, CouponDto> {

    @ViewComponent
    private VerticalLayout root;

    @Override
    public void setItem(CouponDto item) {
        super.setItem(item);
        root.removeAll();

        if (item == null || item.getMessaggio() == null || item.getMessaggio().isBlank()) {
            showError("Nessun coupon disponibile");
            return;
        }

        buildRootContent(item);
    }

    private void showError(String msg) {
        Span label = new Span(msg);
        label.getStyle().set("color", "red");
        root.add(label);
    }

    private void buildRootContent(CouponDto item) {
        root.addClassName("coupon-box");
        root.setSpacing(true);

        // Validità
        Span validita = new Span("Periodo di validità:\nDa " +
                formatDate(item.getDataInizioPromo()) +
                " fino a " + formatDate(item.getDataFinePromo()));
        validita.addClassName("coupon-validity");
        root.add(validita);

        renderCouponMessage(item.getMessaggio(), item.getCodiceCupon(), root);
    }

    private void renderCouponMessage(String messaggio, String codice, VerticalLayout container) {
        List<String> righePulite = new ArrayList<>();

        // Normalizza gli a capo
        String testo = messaggio.replaceAll("[\\r\\n]+", "\n").trim();

        // Divide in righe
        String[] righe = testo.split("\\n");
        Pattern soloNumeri = Pattern.compile("^\\d+$");

        for (String r : righe) {
            String trimmed = r.trim();
            if (trimmed.isEmpty()) continue;

            // Elimina i primi due caratteri se la lunghezza >= 2
            if (trimmed.length() >= 2) {
                trimmed = trimmed.substring(2).trim();
            }

            // Ignora linee solo numeriche (00, 02, 30, 22, ecc.)
            if (soloNumeri.matcher(trimmed).matches()) {

                righePulite.add(""); // forza un a capo
                continue;
            }

            righePulite.add(trimmed);
        }

        // Aggiunge le righe alla UI
        for (String riga : righePulite) {
            if (riga.toUpperCase().contains("%CB%") || riga.toUpperCase().contains("%CN%")) {
                Image barcode = createBarcodeImage(codice);
                container.add(barcode);
            } else if (!riga.isBlank()) {
                Span span = new Span(riga);
                span.addClassName("coupon-text");
                container.add(span);
            } else {
                container.add(new Span(" "));
            }
        }
    }


    private Image createBarcodeImage(String codiceCupon) {
        try {
            // Crea il barcode
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(codiceCupon, BarcodeFormat.CODE_128, 200, 70);

            // Crea l'immagine dal BitMatrix
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Calcoliamo il 20% da ritagliare a sinistra e a destra
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // Determina il 20% della larghezza
            int margin = (int) (width * 0.15); // 20% della larghezza

            // Ritaglia l'immagine mantenendo solo il 60% centrale
            BufferedImage croppedImage = bufferedImage.getSubimage(margin, 0, width - 2 * margin, height);

            // Converte l'immagine ritagliata in byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Crea il resource per l'immagine
            StreamResource resource = new StreamResource("barcode.png", () -> new ByteArrayInputStream(imageBytes));

            // Crea l'immagine da visualizzare
            Image barcode = new Image(resource, "Codice a barre");
            barcode.addClassName("coupon-barcode");

            return barcode;

        } catch (IOException e) {
            e.printStackTrace();
            return new Image(); // In caso di errore, restituisci un'immagine vuota
        }
    }


    private String formatDate(String yyyymmdd) {
        try {
            LocalDate date = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return yyyymmdd;
        }
    }
}
