package com.company.ecomShop.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.*;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(
        name = "RegisteredUser",
        code = RegisteredUserRole.CODE,
        scope = SecurityScope.UI
)
public interface RegisteredUserRole {
    String CODE = "registered-user";

    // ================================================================
    // 🔹 ACCESSO COMPLETO AL MODELLO DATI
    // ================================================================
    @EntityPolicy(entityName = "*", actions = EntityPolicyAction.ALL)
    @EntityAttributePolicy(entityName = "*", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void entities();

    // ================================================================
    // 🔹 ACCESSO A TUTTE LE VISTE (NAVIGAZIONE UI)
    // ================================================================
    @ViewPolicy(viewIds = {
            "Home",
            "SecondaPagina",
            "Dettaglioprodotto",
            "Carrello",
            "CuponView",
            "Volantino",
            "PromoFragment",
            "CouponFragment",
            "Registrazione",
            "RecuperaPassword",
            "Passworddimenticata",
            "LoginView",
            "MainView"
    })
    @SpecificPolicy(resources = "*")
    void views();

    // ================================================================
    // 🔹 POLICY UI (operazioni e funzionalità standard Jmix)
    // ================================================================
    @SpecificPolicy(resources = {
            "ui.loginToUi",
            "ui.logoutFromUi",
            "ui.showNotifications",
            "ui.navigate",
            "ui.viewSettings",
            "ui.showDetails"
    })
    void uiPolicies();
}
