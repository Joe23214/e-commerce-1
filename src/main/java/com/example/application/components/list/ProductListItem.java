package com.example.application.components.list;

import com.company.ecomShop.view.dettaglioprodotto.Dettaglioprodotto;
import com.company.ecomShop.view.secondapagina.SecondaPagina;
import com.example.application.components.Layout;
import com.sun.jna.platform.unix.X11;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.view.View;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileDescriptor;

public class ProductListItem extends com.vaadin.flow.component.html.ListItem {

    // Components
    private Layout image;
    private Layout row;
    private Layout column;
    private Layout primary;
    private Layout secondary;
    private Layout actions;
    private Layout primaryCode;
    private Layout primaryCodEan;
    private Layout pezzixCartone;
    private Layout giacenze;
    @Autowired
    private ViewNavigators viewNavigators;
    String SRC_PATH = "";


    public ProductListItem(String nomeFile, boolean promo, String primary,String primaryCode,String primaryCodEan,  String secondary, String pezzixCartone, String giacenze, Component... actions ) {
        this(nomeFile, promo, new Text(primary),new Text(primaryCode),new Text(primaryCodEan), new Text(secondary),new Text(pezzixCartone),new Text(giacenze), actions);
    }

    public ProductListItem(String nomeFile, boolean promo, Component primary, Component primaryCode,Component primaryCodEan, Component secondary,Component pezzixCartone,Component giacenze, Component... actions) {
        addClassNames(Background.BASE, Border.BOTTOM, Border.RIGHT, Display.FLEX, FlexDirection.COLUMN, Gap.MEDIUM,
                Padding.Bottom.MEDIUM, Padding.Horizontal.LARGE, Padding.Top.LARGE);

        Image imageProd = getFileRefFromSysFile(nomeFile);


        this.image = new Layout(imageProd);
        this.image.addClassNames("aspect-video", BorderRadius.MEDIUM);
        this.image.setAlignItems(Layout.AlignItems.CENTER);
        this.image.setJustifyContent(Layout.JustifyContent.CENTER);
        this.image.setOverflow(Layout.Overflow.HIDDEN);
        this.image.setWidth("225px");
        this.image.setHeight("225px");

        setImage(imageProd);



        this.primary = new Layout();
        this.primary.addClassNames("descrizioneClasseProdotti");
        setPrimary(primary);

        this.primaryCode = new Layout();
        this.primaryCode.addClassNames(FontSize.XSMALL);
        setPrimaryCode(primaryCode);


        this.primaryCodEan = new Layout();
        this.primaryCodEan.addClassNames(FontSize.XSMALL);
        setPrimaryCodEan(primaryCodEan);

        if(promo){
            this.secondary = new Layout();
            this.secondary.addClassNames("PreozzoListaProdottiPromo");
            setSecondary(secondary);
        }else {
            this.secondary = new Layout();
            this.secondary.addClassNames("PreozzoListaProdotti");
            setSecondary(secondary);
        }

        this.pezzixCartone = new Layout();
        this.pezzixCartone.addClassNames(FontSize.XSMALL);
        setPezziXCartone(pezzixCartone);


        this.giacenze = new Layout();
        this.giacenze.addClassNames(FontSize.XSMALL);
        setGiacenze(giacenze);

       // this.column = new Layout(this.primary,this.primaryCode,this.primaryCodEan,this.pezzixCartone,this.giacenze, this.secondary);

        Component favouriteIcon = actions.length > 0 ? actions[0] : null;
        Component cartIcon = actions.length > 1 ? actions[1] : null;
        // Layout orizzontale per prezzo + cuore + carrello
        // Layout bottom row
        Layout bottomRow = new Layout();
        bottomRow.setWidthFull();
        bottomRow.setJustifyContent(Layout.JustifyContent.BETWEEN);
        bottomRow.setAlignItems(Layout.AlignItems.CENTER);

// Sezioni
        Layout left = new Layout(this.secondary); // prezzo
        Layout center = new Layout();
        Layout right = new Layout();

        if (favouriteIcon != null) {
            center.add(favouriteIcon);
        }
        if (cartIcon != null) {
            right.add(cartIcon);
        }

        left.setWidth("33%");
        center.setWidth("33%");
        center.setJustifyContent(Layout.JustifyContent.CENTER);
        right.setWidth("33%");
        right.setJustifyContent(Layout.JustifyContent.END);

        bottomRow.add(left, center, right);

// Ora compila la colonna
        this.column = new Layout(
                this.primary,
                this.primaryCode,
                this.primaryCodEan,
                this.pezzixCartone,
                this.giacenze,
                bottomRow
        );


        this.column.setFlexDirection(Layout.FlexDirection.COLUMN);
        this.column.setFlexGrow();




        this.row = new Layout(this.column /*, this.actions*/);
        this.row.setAlignItems(Layout.AlignItems.CENTER);

        add(this.image, this.row);
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

        avatarImage.setClassName("immaggineProdotto");
        return avatarImage;

    }


//    private Image getPromoRefFromSysFile(String sysFile) {
//
//        Image avatarImage = new Image();
//
//
//
//        if(sysFile!=null) {
//            String path = sysFile.substring(0, 11);
//            String nomeFile = sysFile.substring(11);
//
//            String nomeFileDir = path + nomeFile;
//            SRC_PATH = "META-INF/resources/sales/promo/img/promo.jpg";
//
//
//
//
//            StreamResource streamResource1 = new StreamResource(nomeFile.substring(0, nomeFile.length() - 4),
//                    () -> getClass().getResourceAsStream(SRC_PATH));
//
//
//
//
//
//            avatarImage.setSrc(streamResource1);
//        }
//
//        avatarImage.setClassName("immaggineProdotto");
//        return avatarImage;
//
//    }




    public void setImage(Image imageProd) {
        this.image.removeAll();
        if (image != null) {
            image.addClassNames(MaxWidth.FULL);
            this.image.add(imageProd);
        }
    }


    public void setPrimary(Component... components) {
        this.primary.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.primary.add(component);
                }
            }
        }
        this.primary.setVisible(this.primary.getComponentCount() > 0);
    }


    public void setPrimaryCode(Component... components) {
        this.primaryCode.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.primaryCode.add(component);
                }
            }
        }
        this.primaryCode.setVisible(this.primaryCode.getComponentCount() > 0);
    }



    public void setPrimaryCodEan(Component... components) {
        this.primaryCodEan.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.primaryCodEan.add(component);
                }
            }
        }
        this.primaryCodEan.setVisible(this.primaryCodEan.getComponentCount() > 0);
    }




    public void setSecondary(Component... components) {
        this.secondary.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.secondary.add(component);
                }
            }
        }
        this.secondary.setVisible(this.secondary.getComponentCount() > 0);
    }

    private void setPezziXCartone(Component... components) {
        this.pezzixCartone.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.pezzixCartone.add(component);
                }
            }
        }
        this.pezzixCartone.setVisible(this.pezzixCartone.getComponentCount() > 0);
    }




    private void setGiacenze(Component... components) {
        this.giacenze.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.giacenze.add(component);
                }
            }
        }
        this.giacenze.setVisible(this.giacenze.getComponentCount() > 0);
    }



    public void setActions(Component... components) {
        this.actions.removeAll();
        if (components != null) {
            for (Component component : components) {
                if (component != null) {
                    this.actions.add(component);
                }
            }
        }
        this.actions.setVisible(this.actions.getComponentCount() > 0);
    }

}
