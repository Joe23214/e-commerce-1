package com.company.ecomShop.listener;

import com.company.ecomShop.app.GetDataService;
import com.company.ecomShop.entity.User;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
@ConditionalOnWebApplication
public class AuthenticationEventListener {
    @Autowired
    private GetDataService getDataService;

    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;

   /* @EventListener
    public void onAuthenticationSuccess(final AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();

        String username = user.getUsername();
        if (!username.equalsIgnoreCase("system")) {
            Integer listino = getDataService.getListinoCliente(username);
            if(listino==null){
                listino = 1;
            }
            sessionDataProvider.getObject().setAttribute("listino", listino);
            sessionDataProvider.getObject().setAttribute("username", username);
        }
    }*/
  @EventListener
  public void onAuthenticationSuccess(final AuthenticationSuccessEvent event) {
      Object principal = event.getAuthentication().getPrincipal();

      // 🔒 Evita ClassCastException per autenticazioni "client_credentials"
      if (!(principal instanceof User user)) {
          // È probabilmente un client OAuth2 (es. "my-client"), quindi esco
          return;
      }

      String username = user.getUsername();

      // Evita di impostare sessione per l'utente "system"
      if (!"system".equalsIgnoreCase(username)) {
          Integer listino = getDataService.getListinoCliente(username);
          if (listino == null) {
              listino = 1;
          }
          sessionDataProvider.getObject().setAttribute("listino", listino);
          sessionDataProvider.getObject().setAttribute("username", username);
      }
  }

}