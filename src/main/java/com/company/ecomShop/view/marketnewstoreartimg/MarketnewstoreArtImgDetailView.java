package com.company.ecomShop.view.marketnewstoreartimg;

import com.company.ecomShop.entity.MarketnewstoreArtImg;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "marketnewstoreArtImgs/:id", layout = MainView.class)
@ViewController(id = "MarketnewstoreArtImg.detail")
@ViewDescriptor(path = "marketnewstore-art-img-detail-view.xml")
@EditedEntityContainer("marketnewstoreArtImgDc")
public class MarketnewstoreArtImgDetailView extends StandardDetailView<MarketnewstoreArtImg> {
}