package com.company.ecomShop.view.devicetoken;

import com.company.ecomShop.entity.DeviceToken;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "device-tokens/:id", layout = MainView.class)
@ViewController(id = "DeviceToken.detail")
@ViewDescriptor(path = "device-token-detail-view.xml")
@EditedEntityContainer("deviceTokenDc")
public class DeviceTokenDetailView extends StandardDetailView<DeviceToken> {
}