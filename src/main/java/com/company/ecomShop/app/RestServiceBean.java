package com.company.ecomShop.app;

import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class RestServiceBean {
    private final CarrelloService carrelloService;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;

    public RestServiceBean(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Servizio REST attivo ✅";
    }

    // Login con tessera → restituisce username/password PDC
    @GetMapping("/tokenByTessera")
    public ResponseEntity<?> loginByTessera(@RequestParam(name = "codiceTessera") String codiceTessera) {
        Map<String, String> cred = carrelloService.getCredenzialiByCodiceTessera(codiceTessera);
        if (cred == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Tessera non valida"));
        }
        return ResponseEntity.ok(cred);
    }

    @GetMapping("/verifyUser")
    public ResponseEntity<?> verifyUser(
            @RequestParam(name = "usernameInput") String usernameInput,
            @RequestParam(name = "passwordInput") String passwordInput
    ) {
        boolean valido = carrelloService.verificaCredenziali(usernameInput, passwordInput);

        if (valido) {
            return ResponseEntity.ok(Map.of("result", "OK"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenziali non valide"));
        }
    }

    @GetMapping("/autologin")
    public ResponseEntity<String> autologinWeb(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        {
            sessionDataProvider.getObject().setAttribute("accesso_app", "1");
            // Genera l’HTML del form con submit automatico
            String html = """
                        <html><body>
                        <form id='loginForm' action='/home' method='post'>
                            <input type='hidden' name='username' value='%s'/>
                            <input type='hidden' name='password' value='%s'/>
                        </form>
                        <script>
                            document.getElementById('loginForm').submit();
                        </script>
                        </body></html>
                    """.formatted(username, password);
            // settare variabile di sessione accesso da app
            return ResponseEntity.ok(html);
        }


    }
}
