package com.company.ecomShop.view.marketnewstoreartimg;

import com.company.ecomShop.entity.MarketnewstoreArtImg;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "marketnewstoreArtImgs", layout = MainView.class)
@ViewController(id = "MarketnewstoreArtImg.list")
@ViewDescriptor(path = "marketnewstore-art-img-list-view.xml")
@LookupComponent("marketnewstoreArtImgsDataGrid")
@DialogMode(width = "64em")
public class MarketnewstoreArtImgListView extends StandardListView<MarketnewstoreArtImg> {
}