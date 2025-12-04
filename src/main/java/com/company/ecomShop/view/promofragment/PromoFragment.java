package com.company.ecomShop.view.promofragment;

import com.company.ecomShop.entity.PromoDto;
import com.company.ecomShop.utility.CryptoUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
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

@RendererItemContainer("item")
@FragmentDescriptor("promo-fragment.xml")
public class PromoFragment extends FragmentRenderer<VerticalLayout, PromoDto> {

    @ViewComponent
    private VerticalLayout root;

    @Override
    public void setItem(PromoDto promo) {
        super.setItem(promo);
        root.removeAll();

        if (promo == null) {
            root.add(new Span("Nessuna promozione disponibile"));
            return;
        }

        try {
            buildPromoLayout(promo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buildPromoLayout(PromoDto promo) throws Exception {
        root.addClassName("coupon-box");
        root.addClassName("promo-expanded"); // nuova classe per maggiore altezza
        root.setWidthFull();
        root.setPadding(true);
        root.setSpacing(true);
        root.getStyle().set("gap", "1.5rem");

        // --- Titolo ---
        Span titolo = new Span(promo.getDescrizione());
        titolo.addClassName("promo-title");

        // --- Numero offerta ---
        Span offerta = new Span("Offerta n° " + promo.getNumeroOfferta());
        offerta.addClassName("promo-number");

        // --- QR code ---
        String json = promo.getJsonData();
        Image qrImage = generateQRCodeImage(CryptoUtils.encrypt(json));
        qrImage.addClassName("promo-qrcode-large");

        // --- Corpo verticale ---
        VerticalLayout body = new VerticalLayout();
        body.setAlignItems(FlexComponent.Alignment.CENTER);
        body.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        body.setSpacing(true);
        body.setPadding(true);
        body.add(titolo, offerta, qrImage);

        // --- Periodo validità sotto tutto ---
        Span periodo = new Span("Valido dal " + formatDate(promo.getDataInizioPromo()) +
                " al " + formatDate(promo.getDataFinePromo()));
        periodo.addClassName("coupon-validity");
        periodo.addClassName("promo-period-bottom");

        root.add(body, periodo);
    }

    private Image generateQRCodeImage(String text) {
        if (text == null || text.isBlank()) {
            return new Image();
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            StreamResource resource = new StreamResource("qrcode-" + System.currentTimeMillis() + ".png",
                    () -> new ByteArrayInputStream(imageBytes));

            Image qr = new Image(resource, "QR Code");
            qr.setWidth("220px");
            qr.setHeight("220px");
            qr.addClassName("promo-qrcode-large");

            return qr;
        } catch (Exception e) {
            e.printStackTrace();
            return new Image();
        }
    }

    private String formatDate(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.isBlank()) return "";
        try {
            return yyyymmdd.substring(6, 8) + "/" + yyyymmdd.substring(4, 6) + "/" + yyyymmdd.substring(0, 4);
        } catch (Exception e) {
            return yyyymmdd;
        }
    }
}
