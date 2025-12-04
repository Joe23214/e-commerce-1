package com.company.ecomShop.view.devicetoken;

import com.company.ecomShop.entity.DeviceToken;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "device-tokens", layout = MainView.class)
@ViewController(id = "DeviceToken.list")
@ViewDescriptor(path = "device-token-list-view.xml")
@LookupComponent("deviceTokensDataGrid")
@DialogMode(width = "64em")
public class DeviceTokenListView extends StandardListView<DeviceToken> {
}