package com.company.ecomShop.view.configecom;

import com.company.ecomShop.entity.ConfigEcom;
import com.company.ecomShop.view.home.Home;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Route(value = "config-ecom")
@ViewController("ConfigEcom.detail")
@ViewDescriptor("config-ecom-detail-view.xml")
public class ConfigEcomDetailView extends StandardView {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private Notifications notifications;

    @ViewComponent
    private TypedTextField<String> cfgField;

    @ViewComponent
    private TypedTextField<Object> qtaGiacenzaRossoField;

    @ViewComponent
    private InstanceContainer<ConfigEcom> configEcomDc;

    @ViewComponent
    private TypedTextField<Object> qtaGiacenzaGialloField;

    private final String[] carouselDesktop = {"rolling1", "rolling2", "rolling3"};
    private final String[] carouselMobile = {"rolling1phone", "rolling2phone", "rolling3phone"};

    // ======================================================
    // Cartelle relative, compatibili con deploy
    // ======================================================
    private final Path baseResourceDir = Paths.get(System.getProperty("user.dir"), "target", "app-resources");
    private final Path rollingDir = baseResourceDir.resolve("rolling");
    private final Path iconsDir = baseResourceDir.resolve("icons");
    private final Path pdfDir = baseResourceDir.resolve("pdf");
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe(id = "Home", subject = "clickListener")
    public void onHomeClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(this, Home.class).navigate();
    }

    // ===================== UPLOAD VOLANTINO =====================
    private void createVolantinoUploadManager() {
        Div groupBox = uiComponents.create(Div.class);
        groupBox.getStyle().set("border", "1px solid #ddd")
                .set("padding", "15px")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px");

        Span title = uiComponents.create(Span.class);
        title.setText("Volantino PDF");
        title.getStyle().set("font-weight", "700").set("font-size", "16px").set("margin-bottom", "10px");
        groupBox.add(title);

        Span subtitle = uiComponents.create(Span.class);
        subtitle.setText("Carica il volantino in formato PDF (verrà sovrascritto quello attuale)");
        subtitle.getStyle().set("font-size", "12px").set("color", "gray").set("margin-bottom", "10px");
        groupBox.add(subtitle);

        VerticalLayout layout = uiComponents.create(VerticalLayout.class);

        Upload upload = new Upload();
        upload.setAcceptedFileTypes("application/pdf"); // solo PDF
        upload.setMaxFileSize(50 * 1024 * 1024); // 50 MB
        upload.setReceiver((fileName, mimeType) -> receiveAndReplaceVolantino());
        upload.addSucceededListener(e -> {
            notifications.create("Volantino caricato con successo").show();

            getUI().ifPresent(ui -> ui.getPage().executeJs("""
                const iframe = document.querySelector("iframe[src*='volantino.pdf']");
                if (iframe) {
                    iframe.src = 'pdf/volantino.pdf?v=' + Date.now();
                }
            """));
        });

        layout.add(upload);
        groupBox.add(layout);
        getContent().add(groupBox);
    }

    private OutputStream receiveAndReplaceVolantino() {
        try {
            Files.createDirectories(pdfDir);
            Path pdfPath = pdfDir.resolve("volantino.pdf");
            Path tempPath = pdfDir.resolve("volantino-temp.pdf");

            return new FilterOutputStream(Files.newOutputStream(tempPath)) {
                @Override
                public void close() throws IOException {
                    super.close();
                    Files.move(tempPath, pdfPath, StandardCopyOption.REPLACE_EXISTING);
                }
            };
        } catch (IOException e) {
            notifications.create("Errore nel caricamento del volantino: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

    // ===================== INIT & BEFORE SHOW =====================
    @Subscribe
    public void onInit(InitEvent event) {
        createFaviconUploadManager();
        cfgField.setReadOnly(false);

        createCarouselUploads("Desktop", carouselDesktop, rollingDir, 1200, 440);
        createCarouselUploads("Mobile", carouselMobile, rollingDir, 735, 450);
        createLogoUploadManager();

        qtaGiacenzaRossoField.addValueChangeListener(e -> updateRequiredFields());
        qtaGiacenzaGialloField.addValueChangeListener(e -> updateRequiredFields());

        qtaGiacenzaRossoField.addValidator(v -> validateQuantities());
        qtaGiacenzaGialloField.addValidator(v -> validateQuantities());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        createVolantinoUploadManager();

        ConfigEcom cfg = dataManager.load(ConfigEcom.class)
                .query("select e from ConfigEcom e where e.cfg = :cfg")
                .parameter("cfg", "1")
                .optional()
                .orElseGet(() -> {
                    ConfigEcom newCfg = dataManager.create(ConfigEcom.class);
                    newCfg.setCfg("1");
                    newCfg.setMail("example@email.com");
                    newCfg.setVersion(1);
                    return dataManager.save(newCfg);
                });

        configEcomDc.setItem(cfg);
        cfgField.setReadOnly(true);
    }

    @Subscribe("saveAction")
    public void onSave(ActionPerformedEvent event) {
        try {
            ConfigEcom cfg = configEcomDc.getItem();
            dataManager.save(cfg);

            notifications.create("Configurazione salvata con successo").show();

            getUI().ifPresent(ui -> ui.navigate(Home.class));
        } catch (Exception e) {
            notifications.create("Errore durante il salvataggio: " + e.getMessage()).show();
        }
    }

    private void updateRequiredFields() {
        boolean rossoHasValue = qtaGiacenzaRossoField.getValue() != null && !qtaGiacenzaRossoField.getValue().toString().isBlank();
        boolean gialloHasValue = qtaGiacenzaGialloField.getValue() != null && !qtaGiacenzaGialloField.getValue().toString().isBlank();

        qtaGiacenzaRossoField.setRequired(gialloHasValue);
        qtaGiacenzaGialloField.setRequired(rossoHasValue);
    }

    private void validateQuantities() {
        boolean rossoHasValue = qtaGiacenzaRossoField.getValue() != null && !qtaGiacenzaRossoField.getValue().toString().isBlank();
        boolean gialloHasValue = qtaGiacenzaGialloField.getValue() != null && !qtaGiacenzaGialloField.getValue().toString().isBlank();

        if ((rossoHasValue && !gialloHasValue) || (!rossoHasValue && gialloHasValue)) {
            throw new ValidationException("Se inserisci la quantità rossa devi valorizzare anche la quantità gialla e viceversa.");
        }
    }

    // ===================== UPLOAD CAROUSEL =====================
    private void createCarouselUploads(String label, String[] fileBases, Path baseDir, int targetWidth, int targetHeight) {
        Div groupBox = uiComponents.create(Div.class);
        groupBox.getStyle().set("border", "1px solid #ddd")
                .set("padding", "15px")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px");

        Span title = uiComponents.create(Span.class);
        title.setText(label + " Immagini Carosello");
        title.getStyle().set("font-weight", "700").set("font-size", "16px").set("margin-bottom", "10px");
        groupBox.add(title);

        Span subtitle = uiComponents.create(Span.class);
        subtitle.setText("Le immagini devono essere " + targetWidth + " x " + targetHeight + " px (solo JPG)");
        subtitle.getStyle().set("font-size", "12px").set("color", "gray").set("margin-bottom", "10px");
        groupBox.add(subtitle);

        HorizontalLayout uploadsRow = uiComponents.create(HorizontalLayout.class);
        uploadsRow.setSpacing(true);

        for (String fileBase : fileBases) {
            VerticalLayout uploadContainer = uiComponents.create(VerticalLayout.class);
            uploadContainer.getStyle().set("border", "1px solid #ccc")
                    .set("padding", "10px")
                    .set("border-radius", "8px")
                    .set("align-items", "center");

            Span lbl = uiComponents.create(Span.class);
            lbl.setText(fileBase + " (.jpg)");
            lbl.getStyle().set("font-size", "12px").set("margin-bottom", "5px");
            uploadContainer.add(lbl);

            Upload upload = new Upload();
            upload.setAcceptedFileTypes("image/jpeg");
            upload.setMaxFileSize(5 * 1024 * 1024);
            upload.setReceiver((fileName, mimeType) -> receiveAndResizeImage(fileBase, baseDir, targetWidth, targetHeight));
            upload.addSucceededListener(e -> notifications.create("Immagine caricata con successo: " + e.getFileName()).show());
            uploadContainer.add(upload);

            createImagePreview(baseDir, fileBase, uploadContainer);

            uploadsRow.add(uploadContainer);
        }

        groupBox.add(uploadsRow);
        getContent().add(groupBox);
    }

    private OutputStream receiveAndResizeImage(String baseName, Path baseDir,
                                               int targetWidth, int targetHeight) {
        try {
            Files.createDirectories(baseDir);

            Path newImage = baseDir.resolve(baseName + "-new.jpg");
            Path oldImage = baseDir.resolve(baseName + "-old.jpg");
            Path originalImage = baseDir.resolve(baseName + ".jpg");

            if (Files.exists(newImage)) {
                Files.move(newImage, oldImage, StandardCopyOption.REPLACE_EXISTING);
            } else if (Files.exists(originalImage) && !Files.exists(oldImage)) {
                Files.copy(originalImage, oldImage, StandardCopyOption.REPLACE_EXISTING);
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            return new FilterOutputStream(buffer) {
                @Override
                public void close() throws IOException {
                    super.close();
                    try (InputStream in = new ByteArrayInputStream(buffer.toByteArray())) {
                        BufferedImage inputImage = ImageIO.read(in);
                        if (inputImage == null) throw new IOException("Formato immagine non valido");

                        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = resized.createGraphics();
                        g.drawImage(inputImage, 0, 0, targetWidth, targetHeight, null);
                        g.dispose();

                        ImageIO.write(resized, "jpg", newImage.toFile());
                    }
                }
            };
        } catch (IOException e) {
            notifications.create("Errore nel salvataggio dell'immagine: " + baseName).show();
            throw new RuntimeException(e);
        }
    }

    private void createImagePreview(Path baseDir, String baseName, VerticalLayout container) {
        HorizontalLayout previewLayout = uiComponents.create(HorizontalLayout.class);

        Path oldImage = baseDir.resolve(baseName + "-old.jpg");
        if (Files.exists(oldImage)) {
            Image preview = new Image(new StreamResource(oldImage.getFileName().toString(), () -> {
                try { return new FileInputStream(oldImage.toFile()); }
                catch (FileNotFoundException e) { return null; }
            }), oldImage.getFileName().toString());

            preview.setHeight("120px");
            preview.getStyle().set("border-radius", "8px").set("border", "1px solid #aaa");

            Button restoreBtn = new Button("Ripristina", click -> {
                try {
                    Path newPath = baseDir.resolve(baseName + "-new.jpg");
                    Path oldPath = baseDir.resolve(baseName + "-old.jpg");
                    if (Files.exists(oldPath)) {
                        if (Files.exists(newPath)) {
                            Path tempPath = baseDir.resolve(baseName + "-temp.jpg");
                            Files.move(newPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
                            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                            Files.move(tempPath, oldPath, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                        notifications.create("Immagine ripristinata con successo").show();
                        getContent().getUI().ifPresent(ui -> ui.getPage().executeJs("location.reload();"));
                    }
                } catch (IOException e) {
                    notifications.create("Errore nel ripristino").show();
                }
            });

            VerticalLayout previewContainer = uiComponents.create(VerticalLayout.class);
            previewContainer.setAlignItems(VerticalLayout.Alignment.CENTER);
            previewContainer.add(preview, restoreBtn);
            previewLayout.add(previewContainer);
        }

        container.add(previewLayout);
    }

    // ===================== UPLOAD LOGO =====================
    private void createLogoUploadManager() {
        Div groupBox = uiComponents.create(Div.class);
        groupBox.getStyle().set("border", "1px solid #ddd")
                .set("padding", "15px")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px");

        Span title = uiComponents.create(Span.class);
        title.setText("Logo");
        title.getStyle().set("font-weight", "700").set("font-size", "16px").set("margin-bottom", "10px");
        groupBox.add(title);

        Span subtitle = uiComponents.create(Span.class);
        subtitle.setText("Il logo deve essere 509 x 490 px (solo JPG)");
        subtitle.getStyle().set("font-size", "12px").set("color", "gray").set("margin-bottom", "10px");
        groupBox.add(subtitle);

        VerticalLayout layout = uiComponents.create(VerticalLayout.class);

        Upload upload = new Upload();
        upload.setAcceptedFileTypes("image/jpeg");
        upload.setMaxFileSize(2 * 1024 * 1024);
        upload.setReceiver((fileName, mimeType) -> receiveAndResizeImage("icon", iconsDir, 509, 490));
        upload.addSucceededListener(e -> {
            notifications.create("Logo caricato con successo: " + e.getFileName()).show();
            getContent().getUI().ifPresent(ui -> ui.getPage().executeJs("location.reload();"));
        });
        layout.add(upload);

        createLogoPreview(layout);

        groupBox.add(layout);
        getContent().add(groupBox);
    }

    private void createLogoPreview(VerticalLayout container) {
        HorizontalLayout previewLayout = uiComponents.create(HorizontalLayout.class);

        Path oldLogo = iconsDir.resolve("icon-old.jpg");
        if (Files.exists(oldLogo)) {
            Image preview = new Image(new StreamResource(oldLogo.getFileName().toString(), () -> {
                try { return new FileInputStream(oldLogo.toFile()); }
                catch (FileNotFoundException e) { return null; }
            }), oldLogo.getFileName().toString());

            preview.setHeight("120px");
            preview.getStyle().set("border-radius", "8px").set("border", "1px solid #aaa");

            Button restoreBtn = new Button("Ripristina", click -> {
                try {
                    Path newPath = iconsDir.resolve("icon-new.jpg");
                    Path oldPath = iconsDir.resolve("icon-old.jpg");
                    if (Files.exists(oldPath)) {
                        if (Files.exists(newPath)) {
                            Path tempPath = iconsDir.resolve("icon-temp.jpg");
                            Files.move(newPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
                            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                            Files.move(tempPath, oldPath, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                        notifications.create("Logo ripristinato con successo").show();
                        getContent().getUI().ifPresent(ui -> ui.getPage().executeJs("location.reload();"));
                    }
                } catch (IOException e) {
                    notifications.create("Errore nel ripristino del logo").show();
                }
            });

            VerticalLayout previewContainer = uiComponents.create(VerticalLayout.class);
            previewContainer.setAlignItems(VerticalLayout.Alignment.CENTER);
            previewContainer.add(preview, restoreBtn);
            previewLayout.add(previewContainer);
        }

        container.add(previewLayout);
    }

    // ===================== UPLOAD FAVICON =====================
    private void createFaviconUploadManager() {
        Div groupBox = uiComponents.create(Div.class);
        groupBox.getStyle().set("border", "1px solid #ddd")
                .set("padding", "15px")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px");

        Span title = uiComponents.create(Span.class);
        title.setText("Favicon del sito");
        title.getStyle().set("font-weight", "700").set("font-size", "16px").set("margin-bottom", "10px");
        groupBox.add(title);

        Span subtitle = uiComponents.create(Span.class);
        subtitle.setText("Formato richiesto: 32 x 32 px (solo ICO)");
        subtitle.getStyle().set("font-size", "12px").set("color", "gray").set("margin-bottom", "10px");
        groupBox.add(subtitle);

        VerticalLayout layout = uiComponents.create(VerticalLayout.class);

        Upload upload = new Upload();
        upload.setAcceptedFileTypes("image/png", "image/jpeg", ".png");
        upload.setMaxFileSize(512 * 1024);
        upload.setReceiver((fileName, mimeType) -> receiveAndReplaceFavicon());
        upload.addSucceededListener(e -> {
            notifications.create("Favicon caricata con successo: " + e.getFileName()).show();
            getContent().getUI().ifPresent(ui -> ui.getPage().executeJs("location.reload();"));
        });
        layout.add(upload);

        groupBox.add(layout);
        getContent().add(groupBox);
    }

    private OutputStream receiveAndReplaceFavicon() {
        try {
            Files.createDirectories(iconsDir);

            Path iconFile = iconsDir.resolve("icon.png");
            Path tempFile = iconsDir.resolve("icon-temp.png");
            Path oldFile = iconsDir.resolve("icon-old.png");

            if (Files.exists(iconFile)) {
                Files.move(iconFile, oldFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return new FilterOutputStream(Files.newOutputStream(tempFile)) {
                @Override
                public void close() throws IOException {
                    super.close();
                    BufferedImage input = ImageIO.read(tempFile.toFile());
                    if (input == null) throw new IOException("Immagine non valida");

                    BufferedImage resized = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resized.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(input, 0, 0, 32, 32, null);
                    g.dispose();

                    ImageIO.write(resized, "png", iconFile.toFile());
                    Files.deleteIfExists(tempFile);
                }
            };
        } catch (IOException e) {
            notifications.create("Errore nel caricamento dell'icona: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

}
