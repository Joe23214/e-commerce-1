import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import vaadinTimePickerCss from 'themes/ecomShop/components/vaadin-time-picker.css?inline';
import vaadinTextFieldCss from 'themes/ecomShop/components/vaadin-text-field.css?inline';
import vaadinTextAreaCss from 'themes/ecomShop/components/vaadin-text-area.css?inline';
import vaadinTabsCss from 'themes/ecomShop/components/vaadin-tabs.css?inline';
import vaadinTabCss from 'themes/ecomShop/components/vaadin-tab.css?inline';
import vaadinSideNavCss from 'themes/ecomShop/components/vaadin-side-nav.css?inline';
import vaadinSideNavItemCss from 'themes/ecomShop/components/vaadin-side-nav-item.css?inline';
import vaadinSelectCss from 'themes/ecomShop/components/vaadin-select.css?inline';
import vaadinRadioGroupCss from 'themes/ecomShop/components/vaadin-radio-group.css?inline';
import vaadinRadioButtonCss from 'themes/ecomShop/components/vaadin-radio-button.css?inline';
import vaadinPasswordFieldCss from 'themes/ecomShop/components/vaadin-password-field.css?inline';
import vaadinNumberFieldCss from 'themes/ecomShop/components/vaadin-number-field.css?inline';
import vaadinNotificationCardCss from 'themes/ecomShop/components/vaadin-notification-card.css?inline';
import vaadinMultiSelectComboBoxCss from 'themes/ecomShop/components/vaadin-multi-select-combo-box.css?inline';
import vaadinMenuBarCss from 'themes/ecomShop/components/vaadin-menu-bar.css?inline';
import vaadinMenuBarButtonCss from 'themes/ecomShop/components/vaadin-menu-bar-button.css?inline';
import vaadinInputContainerCss from 'themes/ecomShop/components/vaadin-input-container.css?inline';
import vaadinEmailFieldCss from 'themes/ecomShop/components/vaadin-email-field.css?inline';
import vaadinDialogOverlayCss from 'themes/ecomShop/components/vaadin-dialog-overlay.css?inline';
import vaadinDateTimePickerCss from 'themes/ecomShop/components/vaadin-date-time-picker.css?inline';
import vaadinDatePickerCss from 'themes/ecomShop/components/vaadin-date-picker.css?inline';
import vaadinComboBoxCss from 'themes/ecomShop/components/vaadin-combo-box.css?inline';
import vaadinCheckboxCss from 'themes/ecomShop/components/vaadin-checkbox.css?inline';
import vaadinCheckboxGroupCss from 'themes/ecomShop/components/vaadin-checkbox-group.css?inline';
import vaadinButtonCss from 'themes/ecomShop/components/vaadin-button.css?inline';
import vaadinAppLayoutCss from 'themes/ecomShop/components/vaadin-app-layout.css?inline';
import uploadCss from 'themes/ecomShop/components/upload.css?inline';
import stepperCss from 'themes/ecomShop/components/stepper.css?inline';
import listCss from 'themes/ecomShop/components/list.css?inline';
import keyValuePairsCss from 'themes/ecomShop/components/key-value-pairs.css?inline';
import inputGroupCss from 'themes/ecomShop/components/input-group.css?inline';
import inputFieldCss from 'themes/ecomShop/components/input-field.css?inline';
import dialogCss from 'themes/ecomShop/components/dialog.css?inline';
import breadcrumbCss from 'themes/ecomShop/components/breadcrumb.css?inline';


if (!document['_vaadintheme_ecomShop_componentCss']) {
  registerStyles(
        'vaadin-time-picker',
        unsafeCSS(vaadinTimePickerCss.toString())
      );
      registerStyles(
        'vaadin-text-field',
        unsafeCSS(vaadinTextFieldCss.toString())
      );
      registerStyles(
        'vaadin-text-area',
        unsafeCSS(vaadinTextAreaCss.toString())
      );
      registerStyles(
        'vaadin-tabs',
        unsafeCSS(vaadinTabsCss.toString())
      );
      registerStyles(
        'vaadin-tab',
        unsafeCSS(vaadinTabCss.toString())
      );
      registerStyles(
        'vaadin-side-nav',
        unsafeCSS(vaadinSideNavCss.toString())
      );
      registerStyles(
        'vaadin-side-nav-item',
        unsafeCSS(vaadinSideNavItemCss.toString())
      );
      registerStyles(
        'vaadin-select',
        unsafeCSS(vaadinSelectCss.toString())
      );
      registerStyles(
        'vaadin-radio-group',
        unsafeCSS(vaadinRadioGroupCss.toString())
      );
      registerStyles(
        'vaadin-radio-button',
        unsafeCSS(vaadinRadioButtonCss.toString())
      );
      registerStyles(
        'vaadin-password-field',
        unsafeCSS(vaadinPasswordFieldCss.toString())
      );
      registerStyles(
        'vaadin-number-field',
        unsafeCSS(vaadinNumberFieldCss.toString())
      );
      registerStyles(
        'vaadin-notification-card',
        unsafeCSS(vaadinNotificationCardCss.toString())
      );
      registerStyles(
        'vaadin-multi-select-combo-box',
        unsafeCSS(vaadinMultiSelectComboBoxCss.toString())
      );
      registerStyles(
        'vaadin-menu-bar',
        unsafeCSS(vaadinMenuBarCss.toString())
      );
      registerStyles(
        'vaadin-menu-bar-button',
        unsafeCSS(vaadinMenuBarButtonCss.toString())
      );
      registerStyles(
        'vaadin-input-container',
        unsafeCSS(vaadinInputContainerCss.toString())
      );
      registerStyles(
        'vaadin-email-field',
        unsafeCSS(vaadinEmailFieldCss.toString())
      );
      registerStyles(
        'vaadin-dialog-overlay',
        unsafeCSS(vaadinDialogOverlayCss.toString())
      );
      registerStyles(
        'vaadin-date-time-picker',
        unsafeCSS(vaadinDateTimePickerCss.toString())
      );
      registerStyles(
        'vaadin-date-picker',
        unsafeCSS(vaadinDatePickerCss.toString())
      );
      registerStyles(
        'vaadin-combo-box',
        unsafeCSS(vaadinComboBoxCss.toString())
      );
      registerStyles(
        'vaadin-checkbox',
        unsafeCSS(vaadinCheckboxCss.toString())
      );
      registerStyles(
        'vaadin-checkbox-group',
        unsafeCSS(vaadinCheckboxGroupCss.toString())
      );
      registerStyles(
        'vaadin-button',
        unsafeCSS(vaadinButtonCss.toString())
      );
      registerStyles(
        'vaadin-app-layout',
        unsafeCSS(vaadinAppLayoutCss.toString())
      );
      registerStyles(
        'upload',
        unsafeCSS(uploadCss.toString())
      );
      registerStyles(
        'stepper',
        unsafeCSS(stepperCss.toString())
      );
      registerStyles(
        'list',
        unsafeCSS(listCss.toString())
      );
      registerStyles(
        'key-value-pairs',
        unsafeCSS(keyValuePairsCss.toString())
      );
      registerStyles(
        'input-group',
        unsafeCSS(inputGroupCss.toString())
      );
      registerStyles(
        'input-field',
        unsafeCSS(inputFieldCss.toString())
      );
      registerStyles(
        'dialog',
        unsafeCSS(dialogCss.toString())
      );
      registerStyles(
        'breadcrumb',
        unsafeCSS(breadcrumbCss.toString())
      );
      
  document['_vaadintheme_ecomShop_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

