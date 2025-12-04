package com.company.ecomShop.view.sysfile;

import com.company.ecomShop.entity.SysFile;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "sysFiles", layout = MainView.class)
@ViewController(id = "SysFile.list")
@ViewDescriptor(path = "sys-file-list-view.xml")
@LookupComponent("sysFilesDataGrid")
@DialogMode(width = "64em")
public class SysFileListView extends StandardListView<SysFile> {
}