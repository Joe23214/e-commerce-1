package com.company.ecomShop.view.sysfile;

import com.company.ecomShop.entity.SysFile;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "sysFiles/:id", layout = MainView.class)
@ViewController(id = "SysFile.detail")
@ViewDescriptor(path = "sys-file-detail-view.xml")
@EditedEntityContainer("sysFileDc")
public class SysFileDetailView extends StandardDetailView<SysFile> {
}