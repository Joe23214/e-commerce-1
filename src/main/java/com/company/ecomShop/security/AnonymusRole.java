package com.company.ecomShop.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.role.annotation.*;

import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "AnonymusRole", code = AnonymusRole.CODE)
public interface AnonymusRole {
    String CODE = "anonymus-role";
    @EntityPolicy(entityName = "PdcDatiAggiuntivi", actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityName = "Art", actions = {EntityPolicyAction.ALL})
    @EntityPolicy(entityName = "EmailUtente", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "Art", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "sec_RoleAssignmentEntity", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "sec_RoleAssignmentEntity", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "Comuni", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "Comuni", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "Pdc", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "Pdc", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "PdcPassword", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "PdcPassword", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "User", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "User", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "FatturapaPdc", actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(entityName = "FatturapaPdc", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityName = "ArtScheda", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityName = "Salmg", attributes = "*", action = EntityAttributePolicyAction.MODIFY)



    @ViewPolicy(viewIds = "LoginView")
    @SpecificPolicy(resources = "ui.loginToUi")
    void login();



@ViewPolicy(viewIds = {"Home"})
    @ViewPolicy(viewIds = {"SecondaPagina"})
    @ViewPolicy(viewIds = {"Dettaglioprodotto"})
    @ViewPolicy(viewIds = {"Registrazione"})
    @ViewPolicy(viewIds = {"LoginView"})
    @ViewPolicy(viewIds = {"Volantino"})
    @ViewPolicy(viewIds = {"Passworddimenticata"})
@ViewPolicy(viewIds = {"RecuperaPassword"})
    @SpecificPolicy(resources = "*")

    void views();


    }