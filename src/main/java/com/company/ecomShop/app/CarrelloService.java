package com.company.ecomShop.app;
import com.company.ecomShop.entity.*;
import com.company.ecomShop.entity.key.CarrelloRigaCompKey;
import com.helger.commons.io.resource.FileSystemResource;
import com.vaadin.flow.component.UI;
import io.jmix.core.DataManager;
import io.jmix.core.NoResultException;
import io.jmix.core.SaveContext;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.core.session.SessionData;
import io.jmix.email.*;
import io.jmix.flowui.Notifications;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.Reflections.log;

@Service
public class CarrelloService {
    private final GetDataService getDataService;
    private final DataManager dataManager;
    private final CurrentAuthentication currentAuthentication;
    private final Notifications notifications;
    private final Emailer emailer;
    private final JavaMailSender javaMailSender;
    @Autowired
    private ObjectProvider<SessionData> sessionDataProvider;
    @Autowired
    private SystemAuthenticator systemAuthenticator;
    @PersistenceContext(unitName = "marketnewstoredb") // store name
    private EntityManager entityManager;
    @Value("${app.base-url}")
    private String baseUrl;
    @Value("${myapp.session.attributeName}")
    private String dominio;
    private final UnconstrainedDataManager unconstrainedDataManager;

    public CarrelloService(DataManager dataManager,
                           CurrentAuthentication currentAuthentication, Notifications notifications, Emailer emailer, GetDataService getDataService, JavaMailSender javaMailSender, UnconstrainedDataManager unconstrainedDataManager) {
        this.dataManager = dataManager;
        this.currentAuthentication = currentAuthentication;
        this.notifications = notifications;
        this.emailer = emailer;
        this.getDataService = getDataService;
        this.javaMailSender = javaMailSender;
        this.unconstrainedDataManager = unconstrainedDataManager;
    }

    private User getUtenteCorrente() {
        return (User) currentAuthentication.getUser();
    }

    private int nextProgressivo(Carrello carrello) {
        String maxProg = dataManager.loadValue(
                        "select max(r.id.progressivo) from CarrelloRiga r where r.id.idOrdine = :idOrdine",
                        String.class)
                .parameter("idOrdine", carrello.getIdOrdine())
                .store("marketnewstoredb")
                .optional()
                .orElse(null);

        int max = 0;
        if (maxProg != null) {
            try {
                max = Integer.parseInt(maxProg);
            } catch (NumberFormatException ignored) {}
        }
        return max + 1;
    }

    // Recupera carrello attivo (dataOra == null)
    public Optional<Carrello> getCarrelloAttivoUtente() {
        String utente = getUtenteCorrente().getId().toString();
        return dataManager.load(Carrello.class)
                .query("select c from Carrello c where c.utente = :utente and c.dataOra is null")
                .parameter("utente", utente)
                .optional();
    }

    // Recupera o crea carrello attivo (vuoto, senza pdv e flagRitiro)
    public Carrello getOrCreateCarrelloAttivo() {
        return getCarrelloAttivoUtente()
                .orElseGet(() -> creaNuovoCarrello());
    }

    // Crea nuovo carrello vuoto (dataOra = null, senza pdv e flagRitiro)
    private Carrello creaNuovoCarrello() {
        Carrello carrello = new Carrello();
        carrello.setIdOrdine(UUID.randomUUID());
        carrello.setUtente(getUtenteCorrente().getId().toString());
        carrello.setPdv(null);
        carrello.setFlagRitiroConsegna(null);
        carrello.setDataOra(null); // carrello non confermato

        dataManager.save(carrello);  // salva subito il carrello vuoto

        return carrello;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Carrello aggiungiArticoliAlCarrello(List<String> codiciArticolo, Double quantita) {
        Carrello carrello = getOrCreateCarrelloAttivo();

        List<CarrelloRiga> righeDaSalvare = new ArrayList<>();

        for (String codiceArticolo : codiciArticolo) {
            CarrelloRiga esistente = dataManager.load(CarrelloRiga.class)
                    .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine and r.codiceArticolo = :codice")
                    .parameter("idOrdine", carrello.getIdOrdine())
                    .parameter("codice", codiceArticolo)
                    .optional()
                    .orElse(null);

            Double prezzo = recuperaPrezzoArticolo(codiceArticolo);
            String flagPeso = getFlagPeso(codiceArticolo);

            Double qtaFinale = quantita;
            if ("1".equals(flagPeso)) {
                // prodotto a peso, converti grammi in kg
                qtaFinale = quantita / 1000.0;
            }

            if (esistente != null) {
                double nuovaQta = esistente.getQtaPeso() + qtaFinale;
                esistente.setQtaPeso(nuovaQta);
                esistente.setValore(prezzo * nuovaQta);
                righeDaSalvare.add(esistente);
            } else {
                CarrelloRiga riga = new CarrelloRiga();
                CarrelloRigaCompKey key = new CarrelloRigaCompKey();
                key.setIdOrdine(carrello.getIdOrdine());
                key.setProgressivo(String.format("%03d", nextProgressivo(carrello)));

                riga.setId(key);
                riga.setCodiceArticolo(codiceArticolo);
                riga.setFlagPeso(flagPeso);
                riga.setQtaPeso(qtaFinale);  // salva in kg
                riga.setValore(prezzo * qtaFinale);

                righeDaSalvare.add(riga);
            }
        }

        SaveContext ctx = new SaveContext()
                .saving(carrello)
                .saving(righeDaSalvare.toArray(new CarrelloRiga[0]));

        dataManager.save(ctx);
        return carrello;
    }

    public Carrello aggiungiArticoloAlCarrello(String codiceArticolo, Double quantita) {
        return aggiungiArticoliAlCarrello(List.of(codiceArticolo), quantita);
    }

    public int carrelloRigheCount(Carrello carrello) {
        Long count = dataManager.loadValue(
                        "select count(r) from CarrelloRiga r where r.id.idOrdine = :idOrdine",
                        Long.class)
                .parameter("idOrdine", carrello.getIdOrdine())
                .store("marketnewstoredb")
                .optional()
                .orElse(0L);
        return count.intValue();
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Carrello confermaCarrello(
            String pdv,
            String flagRitiroConsegna,
            String citofono,
            String piano,
            String note,
            String scala,
            String tipoPagamento,
            String nazione,
            String regione,
            String provincia,
            String citta,
            String cap,
            String via,
            String civico,
            String codiceFasciaOraria
    ) {
        Carrello carrello = getOrCreateCarrelloAttivo();

        // Troncamenti sicuri in base alle lunghezze DB
        if (pdv != null && pdv.length() > 2) pdv = pdv.substring(0, 2);
        if (flagRitiroConsegna != null && flagRitiroConsegna.length() > 1)
            flagRitiroConsegna = flagRitiroConsegna.substring(0, 1);

        carrello.setPdv(pdv);
        carrello.setFlagRitiroConsegna(flagRitiroConsegna);
        carrello.setDataOra(new Date());
        carrello.setUtente(getUtenteCorrente().getId().toString());

        dataManager.save(carrello);
        // Componi indirizzo completo
        StringBuilder indirizzoCompleto = new StringBuilder();
        if (nazione != null && !nazione.isEmpty()) indirizzoCompleto.append(nazione).append(", ");
        if (regione != null && !regione.isEmpty()) indirizzoCompleto.append(regione).append(", ");
        if (provincia != null && !provincia.isEmpty()) indirizzoCompleto.append(provincia).append(", ");
        if (citta != null && !citta.isEmpty()) indirizzoCompleto.append(citta).append(", ");
        if (cap != null && !cap.isEmpty()) indirizzoCompleto.append(cap).append(", ");
        if (via != null && !via.isEmpty()) indirizzoCompleto.append(via).append(", ");
        if (civico != null && !civico.isEmpty()) indirizzoCompleto.append(civico);

        String indirizzoFinale = indirizzoCompleto.toString().trim();
        if (indirizzoFinale.endsWith(",")) {
            indirizzoFinale = indirizzoFinale.substring(0, indirizzoFinale.length() - 1).trim();
        }
        // Troncamento sicuro indirizzo
        if (indirizzoFinale.length() > 150) indirizzoFinale = indirizzoFinale.substring(0, 150);

        CarelloChiusura chiusura = new CarelloChiusura();
        chiusura.setIdOrdine(carrello.getIdOrdine());
        chiusura.setFlagStato("C");
        chiusura.setDataOra(new Date());

        // Troncamenti sicuri per CarelloChiusura
        if (citofono != null && citofono.length() > 2) citofono = citofono.substring(0, 2);
        if (piano != null && piano.length() > 2) piano = piano.substring(0, 2);
        if (scala != null && scala.length() > 2) scala = scala.substring(0, 2);
        if (tipoPagamento != null && tipoPagamento.length() > 3) tipoPagamento = tipoPagamento.substring(0, 3);
        if (note != null && note.length() > 250) note = note.substring(0, 250);
        if (codiceFasciaOraria != null && codiceFasciaOraria.length() > 2)
            codiceFasciaOraria = codiceFasciaOraria.substring(0, 2);

        chiusura.setCitofono(citofono);
        chiusura.setPiano(piano);
        chiusura.setScala(scala);
        chiusura.setTipoPagamento(tipoPagamento);
        chiusura.setNote(note);
        chiusura.setIndirizzo(indirizzoFinale);
        chiusura.setCodiceFasciaOraria(codiceFasciaOraria);

        dataManager.save(chiusura);

        return getOrCreateCarrelloAttivo();
    }

    public void svuotaCarrello(UUID idOrdine) {
        List<CarrelloRiga> righe = dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine")
                .parameter("idOrdine", idOrdine)
                .list();
        righe.forEach(dataManager::remove);
    }

    public void eliminaRigaCarrello(String codiceArticolo) {
        Carrello carrello = getOrCreateCarrelloAttivo();

        Optional<CarrelloRiga> riga = dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine and r.codiceArticolo = :codice")
                .parameter("idOrdine", carrello.getIdOrdine())
                .parameter("codice", codiceArticolo)
                .optional();

        riga.ifPresent(dataManager::remove);
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Double recuperaPrezzoArticolo(String codiceArticolo) {
        Object listinoAttr = sessionDataProvider.getObject().getAttribute("listino");
        if (listinoAttr == null) {
            throw new IllegalStateException("Listino non disponibile nella sessione");
        }

        int listinoInt;
        try {
            listinoInt = Integer.parseInt(listinoAttr.toString().trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Listino non valido: " + listinoAttr);
        }

        String campoPrezzo = switch (listinoInt) {
            case 1 -> "PREZZO_VEND";
            case 2 -> "SEC_PREZZO_VE";
            case 3 -> "TERZO_PREZZO_VE";
            default -> throw new IllegalStateException("Listino non valido: " + listinoInt);
        };

        String codiceClean = codiceArticolo.trim();

        // Query SQL nativa
        String test2 = "SELECT " + campoPrezzo + " FROM marketnewstoreDb..LIS WHERE CODICE_ARTICOLO = '" + codiceClean + "' AND LISTINO = " + listinoInt + " ";
        Object result = entityManager.createNativeQuery(test2).getSingleResult();

        if (result == null) {
            return 0.0;  // Nessun prezzo trovato0
        }

        return result != null ? ((Number) result).doubleValue() : 0.0;
    }


    public Double getTotaleCarrello(Carrello carrello) {

        List<CarrelloRiga> righe = dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine")
                .parameter("idOrdine", carrello.getIdOrdine())
                .list();

        return righe.stream()
                .mapToDouble(r -> r.getValore() != null ? r.getValore() : 0.0)
                .sum();
    }

    public void vaiAlCarrello() {
        UI.getCurrent().navigate("carrello");
    }

    public String getFlagPeso(String codiceArticolo) {
        String flag = dataManager.loadValue("select a.flagBilancia from Art a where a.codiceArticolo = :codiceArticolo", String.class)
                .parameter("codiceArticolo", codiceArticolo)
                .store("marketnewstoredb")
                .one();
        return flag;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public void aggiornaQuantita(String codiceArticolo, Double nuovaQuantita) {
        Carrello carrello = getOrCreateCarrelloAttivo();

        Optional<CarrelloRiga> rigaOpt = dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine and r.codiceArticolo = :codice")
                .parameter("idOrdine", carrello.getIdOrdine())
                .parameter("codice", codiceArticolo)
                .optional();

        rigaOpt.ifPresent(riga -> {
            if (nuovaQuantita <= 0) {
                dataManager.remove(riga);
            } else {
                String flagPeso = riga.getFlagPeso();
                Double qtaFinale = nuovaQuantita;
                if ("1".equals(flagPeso)) {
                    qtaFinale = nuovaQuantita / 1000.0;
                }

                riga.setQtaPeso(qtaFinale);
                Double prezzo = recuperaPrezzoArticolo(codiceArticolo);
                riga.setValore(prezzo * qtaFinale);
                dataManager.save(riga);
            }
        });

    }

    @Transactional("marketnewstoredbTransactionManager")
    public String findCodiceArticoloByCodBarFor(String codBarFor) {
        String sql = "SELECT CODICE_ARTICOLO FROM marketnewstoreDb..ART2 WHERE COD_BAR_FOR = '" + codBarFor.trim().replace("[", "").replace("]", "") + "' AND FLAG_B_F = 'B'";

        List<Object> results = entityManager.createNativeQuery(sql).getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0).toString();

    }
    @Transactional("marketnewstoredbTransactionManager")
    public int getOreScarto() {
        String sql = "SELECT ORE_SCARTO FROM CONFIG_ECOM";

        List<?> results = entityManager
                .createNativeQuery(sql)
                .setMaxResults(1)
                .getResultList();

        if (results.isEmpty() || results.get(0) == null) {
            return 0; // default se non configurato
        }

        Object result = results.get(0);

        if (result instanceof Number) {
            return ((Number) result).intValue();
        } else {
            try {
                return Integer.parseInt(result.toString());
            } catch (NumberFormatException e) {
                return 0; // fallback in caso di valore non valido
            }
        }
    }


    @Transactional("marketnewstoredbTransactionManager")
    public boolean aggiornaPrezziCarrelloAttivo() {
        Optional<Carrello> carrelloOpt = getCarrelloAttivoUtente();

        if (carrelloOpt.isEmpty()) {
            return false; // Nessun carrello attivo
        }

        Carrello carrello = carrelloOpt.get();

        // Carico tutte le righe del carrello attivo
        List<CarrelloRiga> righe = dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine")
                .parameter("idOrdine", carrello.getIdOrdine())
                .list();

        List<CarrelloRiga> righeDaAggiornare = new ArrayList<>();
        StringBuilder messaggioNotifica = new StringBuilder();
        boolean prezziAggiornati = false;

        for (CarrelloRiga riga : righe) {
            Double oldValore = riga.getValore();
            Double prezzoDb = recuperaPrezzoArticolo(riga.getCodiceArticolo());
            Double qta = riga.getQtaPeso() != null ? riga.getQtaPeso() : 0.0;

            Double nuovoValore = prezzoDb * qta;

            if (!Objects.equals(oldValore, nuovoValore)) {
                riga.setValore(nuovoValore);
                righeDaAggiornare.add(riga);
                prezziAggiornati = true;

                messaggioNotifica.append(String.format("• %s: da €%.2f a €%.2f\n",
                        riga.getCodiceArticolo(), oldValore, nuovoValore));
            }
        }

        if (!righeDaAggiornare.isEmpty()) {
            dataManager.save(new SaveContext().saving(righeDaAggiornare));

            notifications.create("Prezzi aggiornati:\n" + messaggioNotifica.toString())
                    .withType(Notifications.Type.WARNING)
                    .show();
        }

        return prezziAggiornati;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Map<String, String> getPuntiRitiro() {
        String sql = "SELECT PROGRESSIVO, LOCALITA, INDIRIZZO FROM PUNTI_RITIRO";

        List<Object[]> results = entityManager
                .createNativeQuery(sql)
                .getResultList();

        Map<String, String> puntiRitiroMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Integer progressivo = (Integer) row[0];
            String localita = (String) row[1];
            String indirizzo = (String) row[2];

            // Etichetta leggibile: "Località - Indirizzo"
            String label = (localita != null ? localita : "") +
                    (indirizzo != null ? " - " + indirizzo : "");

            puntiRitiroMap.put(progressivo.toString(), label);
        }

        return puntiRitiroMap;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Optional<PuntiRitiro> getPuntoRitiroByPDV(String pdv) {
        List<PuntiRitiro> results = entityManager.createQuery(
                        "SELECT p FROM PuntiRitiro p WHERE p.pdv = :pdv", PuntiRitiro.class)
                .setParameter("pdv", pdv)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }


    public Pdc getPdcByUsername(String username) {
        List<PdcPassword> mtc = dataManager.load(PdcPassword.class)
                .query("select e from PdcPassword e where e.username = :username")
                .parameter("username", username)
                .list();

        if (mtc.isEmpty()) {
            // Nessun PdcPassword trovato, ritorna null
            return null;
        }

    // Recupera il codice conto dal primo elemento
    String codiceConto = mtc.get(0).getCodice();

    // Recupera Pdc corrispondente
    List<Pdc> pdcList = dataManager.load(Pdc.class)
            .query("select e from Pdc e where e.codiceConto = :codiceConto")
            .parameter("codiceConto", codiceConto)
            .list();

    if (pdcList.isEmpty()) {
        // Nessun Pdc trovato per il codiceConto
        return null;
    }

    return pdcList.get(0);
    }


    // Recupera tutte le righe di un carrello specifico
    @Transactional("marketnewstoredbTransactionManager")
    public List<CarrelloRiga> getCarrelloRigaByCarrello(Carrello carrello) {
        if (carrello == null) {
            return List.of();
        }

        return dataManager.load(CarrelloRiga.class)
                .query("select r from CarrelloRiga r where r.id.idOrdine = :idOrdine")
                .parameter("idOrdine", carrello.getIdOrdine())
                .list();
    }

    // Recupera la chiusura del carrello (CarelloChiusura) se esiste
    @Transactional("marketnewstoredbTransactionManager")
    public Optional<CarelloChiusura> getCarrelloChiusuraByCarrello(Carrello carrello) {
        if (carrello == null) {
            return Optional.empty();
        }

        try {
            CarelloChiusura chiusura = dataManager.load(CarelloChiusura.class)
                    .query("select c from CarelloChiusura c where c.idOrdine = :idOrdine")
                    .parameter("idOrdine", carrello.getIdOrdine())
                    .one();
            return Optional.ofNullable(chiusura);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    @Transactional("marketnewstoredbTransactionManager")
    public void inviaOrdineCompleto(Carrello carrello, String destinatarioCliente) throws EmailException, MessagingException, IOException {
        // invio mail cliente
       inviaMailOrdineCliente(carrello, destinatarioCliente);

        // invio mail backend con XML
       inviaMailOrdineXMLInMemory(carrello);
        svuotaCarrello(carrello.getIdOrdine());
    }

    @Transactional("marketnewstoredbTransactionManager")
    public void inviaMailOrdineCliente(Carrello carrello, String destinatario) throws MessagingException, IOException {
        List<CarrelloRiga> righe = getCarrelloRigaByCarrello(carrello);
        Optional<CarelloChiusura> chiusuraOpt = getCarrelloChiusuraByCarrello(carrello);

        if (righe.isEmpty()) {
            throw new IllegalStateException("Carrello vuoto, impossibile inviare email.");
        }

        StringBuilder body = new StringBuilder();
        body.append("<html><head>");
        body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
        body.append("<style>");
        body.append("  @media only screen and (max-width:600px) {");
        body.append("    .desktop-table {display:none !important;}");
        body.append("    .mobile-card {display:block !important; width:100% !important; margin-bottom:15px !important; border:1px solid #e0e0e0 !important; border-radius:8px !important; padding:15px !important; box-sizing:border-box !important;}");
        body.append("    .mobile-card .label {font-weight:600; color:#555555; display:block; margin-top:5px;}");
        body.append("    .mobile-card .value {color:#333333; display:block; margin-bottom:5px;}");
        body.append("  }");
        body.append("  @media only screen and (min-width:601px) {");
        body.append("    .mobile-card {display:none !important;}");
        body.append("  }");
        body.append("</style>");
        body.append("</head><body style='font-family:Arial,sans-serif; color:#333333; margin:0; padding:0; background-color:#f8f8f8;'>");

        // Container principale
        body.append("<div style='max-width:700px; margin:30px auto; background-color:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1); padding:30px;'>");

        // Header con logo
        body.append("<div style='text-align:center; margin-bottom:20px;'>")
                .append("<img src='cid:logo' alt='Logo Azienda' style='max-height:80px;'>")
                .append("</div>");

        // Saluto
        body.append("<h2 style='color:#222222; font-weight:600; margin-bottom:10px;'>Grazie per il tuo ordine!</h2>");
        body.append("<p style='font-size:16px; line-height:1.5; color:#555555;'>Gentile cliente, il tuo ordine è stato ricevuto e stiamo preparando tutto con cura. Qui di seguito trovi i dettagli.</p>");

        // Dettagli ordine
        body.append("<h3 style='color:#222222; border-bottom:1px solid #e0e0e0; padding-bottom:10px;'>Dettagli ordine</h3>");
        body.append("<p style='font-size:14px; color:#555555;'>Data ordine: ")
                .append(carrello.getDataOra() != null ? carrello.getDataOra() : carrello.getCreateTs())
                .append("</p>");

        double totale = 0.0;

        // Tabella desktop
        body.append("<table class='desktop-table' style='width:100%; border-collapse:collapse; margin-top:20px;'>")
                .append("<thead>")
                .append("<tr style='background-color:#f5f5f5; font-weight:600;'>")
                .append("<th style='text-align:center; padding:10px;'>#</th>")
                .append("<th style='text-align:left; padding:10px;'>Prodotto</th>")
                .append("<th style='text-align:right; padding:10px;'>Quantità</th>")
                .append("<th style='text-align:right; padding:10px;'>Prezzo</th>")
                .append("<th style='text-align:right; padding:10px;'>Totale</th>")
                .append("</tr>")
                .append("</thead><tbody>");

        for (CarrelloRiga riga : righe) {
            double valore = riga.getValore() != null ? riga.getValore() : 0.0;
            double prezzoUnitario = (riga.getQtaPeso() != null && riga.getQtaPeso() != 0) ? valore / riga.getQtaPeso() : valore;
            totale += valore;
            String cellImgHtml = "🛒";

            // Riga tabella desktop
            body.append("<tr style='border-bottom:1px solid #e0e0e0;'>")
                    .append("<td style='text-align:center; padding:12px;'>").append(cellImgHtml).append("</td>")
                    .append("<td style='padding:12px; color:#333333;'>").append(riga.getCodiceArticolo()).append("</td>")
                    .append("<td style='text-align:right; padding:12px;'>").append(String.format("%.2f", riga.getQtaPeso())).append("</td>")
                    .append("<td style='text-align:right; padding:12px;'>€").append(String.format("%.2f", prezzoUnitario)).append("</td>")
                    .append("<td style='text-align:right; padding:12px; font-weight:600;'>€").append(String.format("%.2f", valore)).append("</td>")
                    .append("</tr>");

            // Blocco mobile
            body.append("<div class='mobile-card' style='display:none;'>")
                    .append("<span class='label'>Prodotto:</span><span class='value'>").append(riga.getCodiceArticolo()).append("</span>")
                    .append("<span class='label'>Quantità:</span><span class='value'>").append(String.format("%.2f", riga.getQtaPeso())).append("</span>")
                    .append("<span class='label'>Prezzo unitario:</span><span class='value'>€").append(String.format("%.2f", prezzoUnitario)).append("</span>")
                    .append("<span class='label'>Totale:</span><span class='value'>€").append(String.format("%.2f", valore)).append("</span>")
                    .append("</div>");
        }

        body.append("</tbody></table>");

        body.append("<p style='text-align:right; font-size:16px; font-weight:600; margin-top:15px;'>Totale ordine: €")
                .append(String.format("%.2f", totale))
                .append("</p>");

        // Ritiro / Consegna
        if (chiusuraOpt.isPresent()) {
            CarelloChiusura chiusura = chiusuraOpt.get();
            body.append("<h3 style='color:#222222; border-bottom:1px solid #e0e0e0; padding-bottom:10px; margin-top:30px;'>Dettagli ritiro/consegna</h3>");

            if ("R".equalsIgnoreCase(carrello.getFlagRitiroConsegna())) {
                Optional<String> indirizzoPdv = getIndirizzoPuntoVendita(Integer.parseInt(carrello.getPdv()));
                body.append("<p style='font-size:14px; color:#555555; margin:10px 0;'><strong>Punto vendita:</strong> ")
                        .append(indirizzoPdv.orElse("ID " + carrello.getPdv()));

                getFasciaOrariaCompleta(chiusura).ifPresent(fascia ->
                        body.append("<br><strong>Data e fascia oraria ritiro:</strong> ").append(fascia)
                );
                body.append("</p>");
            } else {
                body.append("<p style='font-size:14px; color:#555555; margin:10px 0;'><strong>Indirizzo consegna:</strong> ").append(chiusura.getIndirizzo()).append("<br>");
                if (chiusura.getCitofono() != null) body.append("Citofono: ").append(chiusura.getCitofono()).append("<br>");
                if (chiusura.getPiano() != null) body.append("Piano: ").append(chiusura.getPiano()).append("<br>");
                if (chiusura.getScala() != null) body.append("Scala: ").append(chiusura.getScala()).append("<br>");
                body.append("</p>");
            }

            if (chiusura.getNote() != null && !chiusura.getNote().isEmpty()) {
                body.append("<p style='font-size:14px; color:#555555; margin:10px 0;'><strong>Note ordine:</strong> ").append(chiusura.getNote()).append("</p>");
            }
        }

        // Footer elegante
        body.append("<div style='border-top:1px solid #e0e0e0; padding:20px; text-align:center; font-size:14px; color:#999999;'>")
                .append("<p>Grazie per aver scelto il nostro e-commerce di alta qualità.</p>")
                .append("<p><strong>Il team del tuo negozio</strong></p>")
                .append("</div>");

        body.append("</div></body></html>");

        // --- Preparazione MimeMessage ---
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("Conferma ordine");
        helper.setText(body.toString(), true);
        helper.setFrom("noreply@shop_ecom.com");

        try {
            ByteArrayResource logoResource = getLogoResource();
            helper.addInline("logo", logoResource, "image/png");
        } catch (IOException e) {
            System.out.println("Logo non trovato, email inviata senza logo. " + e.getMessage());
        }

        javaMailSender.send(mimeMessage);
    }




    /**
     * Recupera il logo dinamicamente (new/old/default)
     */
    public ByteArrayResource getLogoResource() throws IOException {
        Path iconsDir = Paths.get("src/main/resources/META-INF/resources/icons");
        Path newLogo = iconsDir.resolve("icon-new.png");
        Path oldLogo = iconsDir.resolve("icon-old.png");
        String defaultLogoPath = "/META-INF/resources/icons/icon.png";

        if (Files.exists(newLogo)) {
            return new ByteArrayResource(Files.readAllBytes(newLogo));
        } else if (Files.exists(oldLogo)) {
            return new ByteArrayResource(Files.readAllBytes(oldLogo));
        } else {
            try (InputStream is = getClass().getResourceAsStream(defaultLogoPath)) {
                if (is == null) {
                    throw new FileNotFoundException("Logo di default non trovato: " + defaultLogoPath);
                }
                return new ByteArrayResource(is.readAllBytes());
            }
        }
    }

    @Transactional("marketnewstoredbTransactionManager")
   private void inviaMailOrdineXMLInMemory(Carrello carrello) throws EmailException {
       List<CarrelloRiga> righe = getCarrelloRigaByCarrello(carrello);
       Optional<CarelloChiusura> ccOpt = getCarrelloChiusuraByCarrello(carrello);

       if (righe.isEmpty() || ccOpt.isEmpty()) {
           throw new IllegalStateException("Ordine incompleto: nessuna riga o chiusura non presente");
       }

       CarelloChiusura cc = ccOpt.get();

       try {
           // Creazione XML
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document doc = dBuilder.newDocument();

           Element root = doc.createElement("Ordine");
           doc.appendChild(root);

           Element idOrdine = doc.createElement("ID");
           idOrdine.appendChild(doc.createTextNode(carrello.getIdOrdine().toString()));
           root.appendChild(idOrdine);

           Element utente = doc.createElement("Utente");
           utente.appendChild(doc.createTextNode(Optional.ofNullable(carrello.getUtente()).orElse("")));
           root.appendChild(utente);

           Element dataOra = doc.createElement("DataOra");
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           dataOra.appendChild(doc.createTextNode(sdf.format(cc.getDataOra())));
           root.appendChild(dataOra);

           // Determinazione modalità
           String flag = carrello.getFlagRitiroConsegna();
           if (flag == null) {
               throw new IllegalStateException("Flag ritiro/consegna mancante per l'ordine");
           }

           String modalita;
           if ("C".equalsIgnoreCase(flag)) {
               modalita = "Consegna";
           } else if ("R".equalsIgnoreCase(flag)) {
               modalita = "Ritiro";
           } else {
               throw new IllegalStateException("Flag ritiro/consegna non valido: " + flag);
           }

           Element modalitaEl = doc.createElement("Modalita");
           modalitaEl.appendChild(doc.createTextNode(modalita));
           root.appendChild(modalitaEl);

           if ("Consegna".equals(modalita)) {
               // --- Consegna a domicilio ---
               Element consegnaEl = doc.createElement("DettagliSpedizione");

               Element indirizzo = doc.createElement("Indirizzo");
               indirizzo.appendChild(doc.createTextNode(Optional.ofNullable(cc.getIndirizzo()).orElse("")));
               consegnaEl.appendChild(indirizzo);

               if (cc.getCitofono() != null) {
                   Element citofono = doc.createElement("Citofono");
                   citofono.appendChild(doc.createTextNode(cc.getCitofono()));
                   consegnaEl.appendChild(citofono);
               }

               if (cc.getPiano() != null) {
                   Element piano = doc.createElement("Piano");
                   piano.appendChild(doc.createTextNode(cc.getPiano()));
                   consegnaEl.appendChild(piano);
               }

               if (cc.getScala() != null) {
                   Element scala = doc.createElement("Scala");
                   scala.appendChild(doc.createTextNode(cc.getScala()));
                   consegnaEl.appendChild(scala);
               }

               if (cc.getCodiceFasciaOraria() != null) {
                   Element fascia = doc.createElement("FasciaOraria");
                   fascia.appendChild(doc.createTextNode(cc.getCodiceFasciaOraria()));
                   consegnaEl.appendChild(fascia);
               }

               root.appendChild(consegnaEl);

           } else {
               // --- Ritiro in negozio ---
               Element ritiroEl = doc.createElement("DettagliRitiro");

               Optional<PuntiRitiro> prOpt = getPuntoRitiroByPDV(carrello.getPdv());
               if (prOpt.isEmpty()) {
                   throw new IllegalStateException("Nessun punto ritiro trovato per PDV: " + carrello.getPdv());
               }

               PuntiRitiro pr = prOpt.get();

               Element pdv = doc.createElement("PDV");
               pdv.appendChild(doc.createTextNode(Optional.ofNullable(pr.getPdv()).orElse("")));
               ritiroEl.appendChild(pdv);

               if (pr.getIndirizzo() != null) {
                   Element indirizzo = doc.createElement("Indirizzo");
                   indirizzo.appendChild(doc.createTextNode(pr.getIndirizzo()));
                   ritiroEl.appendChild(indirizzo);
               }

               if (cc.getCodiceFasciaOraria() != null) {
                   Element fascia = doc.createElement("FasciaOraria");
                   fascia.appendChild(doc.createTextNode(cc.getCodiceFasciaOraria()));
                   ritiroEl.appendChild(fascia);
               }

               root.appendChild(ritiroEl);
           }

           // --- Prodotti ---
           Element prodottiEl = doc.createElement("Prodotti");
           double totale = 0.0;
           for (CarrelloRiga r : righe) {
               Element prodEl = doc.createElement("Prodotto");

               Element codice = doc.createElement("Codice");
               codice.appendChild(doc.createTextNode(r.getCodiceArticolo()));
               prodEl.appendChild(codice);

               Element quantita = doc.createElement("Quantita");
               quantita.appendChild(doc.createTextNode(String.valueOf(r.getQtaPeso())));
               prodEl.appendChild(quantita);

               Element prezzo = doc.createElement("Prezzo");
               double prezzoUnitario = r.getValore() / (r.getQtaPeso() != null && r.getQtaPeso() != 0 ? r.getQtaPeso() : 1);
               prezzo.appendChild(doc.createTextNode(String.format("%.2f", prezzoUnitario)));
               prodEl.appendChild(prezzo);

               Element totaleRiga = doc.createElement("TotaleRiga");
               totaleRiga.appendChild(doc.createTextNode(String.format("%.2f", r.getValore())));
               prodEl.appendChild(totaleRiga);

               prodottiEl.appendChild(prodEl);
               totale += r.getValore() != null ? r.getValore() : 0.0;
           }
           root.appendChild(prodottiEl);

           Element totaleEl = doc.createElement("Totale");
           totaleEl.appendChild(doc.createTextNode(String.format("%.2f", totale)));
           root.appendChild(totaleEl);

           // Trasforma in byte array
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           transformer.transform(new DOMSource(doc), new StreamResult(baos));
           byte[] xmlBytes = baos.toByteArray();

           EmailAttachment attachment = new EmailAttachment(xmlBytes, "ordine_" + carrello.getIdOrdine() + ".xml");
ConfigEcom x = new ConfigEcom();
           EmailInfo emailInfo = EmailInfoBuilder.create()
                   .setAddresses(x.getMail())
                   .setSubject("Ordine #" + carrello.getIdOrdine() + " - XML")
                   .setFrom("ordini-noreply@shop_ecom.com")
                   .setBody("In allegato XML ordine completo.")
                   .addAttachment(attachment)
                   .build();

           emailer.sendEmail(emailInfo);

       } catch (Exception e) {
           throw new RuntimeException("Errore generazione/invio mail XML: " + e.getMessage(), e);
       }
   }

    public Optional<String> getIndirizzoPuntoVendita(Integer pdv) {
        if (pdv == null) {
            return Optional.empty();
        }

        try {
            // SQL Server: concatenazione con '+'
            String sql = "SELECT indirizzo + ', ' + localita FROM PUNTI_RITIRO WHERE pdv = ?";
            String indirizzoCompleto = (String) entityManager.createNativeQuery(sql)
                    .setParameter(1, pdv)
                    .getSingleResult();
            return Optional.ofNullable(indirizzoCompleto);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    @Transactional(readOnly = true)
    public Optional<String> getFasciaOrariaCompleta(CarelloChiusura chiusura) {
        if (chiusura == null || chiusura.getCodiceFasciaOraria() == null) {
            return Optional.empty();
        }

        String codFascia = chiusura.getCodiceFasciaOraria().trim();

        try {
            String sql = "SELECT ora_inizio, ora_fine FROM TABELLA_FASCE WHERE COD_FASCIA = ?";
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter(1, codFascia)
                    .getSingleResult();

            if (result == null || result.length != 2) {
                return Optional.empty();
            }

            // Formatter DB
            DateTimeFormatter formatterOraDb = DateTimeFormatter.ofPattern("HHmm");
            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterOra = DateTimeFormatter.ofPattern("HH:mm");

            // Normalizza gli orari prima di fare parse
            String inizioStr = normalizzaOrario(result[0] != null ? result[0].toString() : null);
            String fineStr   = normalizzaOrario(result[1] != null ? result[1].toString() : null);

            LocalTime oraInizio = LocalTime.parse(inizioStr, formatterOraDb);
            LocalTime oraFine   = LocalTime.parse(fineStr, formatterOraDb);

            LocalDate oggi = LocalDate.now();
            LocalTime oraCorrente = LocalTime.now();
            LocalDate dataRitiro = oraCorrente.isAfter(oraInizio) ? oggi.plusDays(1) : oggi;

            String fasciaLeggibile = dataRitiro.format(formatterData) +
                    " dalle " + oraInizio.format(formatterOra) +
                    " alle " + oraFine.format(formatterOra);

            return Optional.of(fasciaLeggibile);

        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            // Log utile in produzione
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Funzione di utilità per normalizzare l’orario
    private String normalizzaOrario(String orario) {
        if (orario == null || orario.trim().isEmpty()) return "0000";
        orario = orario.trim();

        // Se è tipo "900" → "0900"
        if (orario.matches("\\d{3}")) {
            return "0" + orario;
        }
        // Se è tipo "930" → "0930"
        if (orario.matches("\\d{1,2}\\d{2}") && orario.length() < 4) {
            return String.format("%04d", Integer.parseInt(orario));
        }
        // Se è tipo "09:30" → "0930"
        if (orario.matches("\\d{1,2}:\\d{2}")) {
            return orario.replace(":", "");
        }

        // Altrimenti restituisci così com’è (già "HHmm")
        return orario;
    }
    @Transactional("marketnewstoredbTransactionManager")
    public Integer getGiacenzaGiallo() {
        List<Integer> results = entityManager.createQuery(
                        "SELECT ce.qtaGiacenzaGiallo FROM ConfigEcom ce", Integer.class)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional("marketnewstoredbTransactionManager")
    public Integer getGiacenzaRosso() {
        List<Integer> results = entityManager.createQuery(
                        "SELECT ce.qtaGiacenzaRosso FROM ConfigEcom ce", Integer.class)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional("marketnewstoredbTransactionManager")
    public void salvaAvvisoProdotto(String codArt) {
        User utente = getUtenteCorrente();
        if (utente == null) {
            notifications.create("Utente non trovato").show();
            return;
        }

        // Recupero codice conto (funzione che hai già?)
        Pdc pdc = getPdcByUsername(utente.getUsername());
        if (pdc == null || pdc.getCodiceConto() == null) {
            notifications.create("Non è possibile recuperare il conto dell'utente").show();
            return;
        }

        Avvisi avviso = new Avvisi();
        avviso.setCodArt(codArt);
        avviso.setIdUtente(utente.getId().toString());
        avviso.setCodConto(pdc.getCodiceConto()); // ← necessario per non avere NULL

        dataManager.save(avviso);

        notifications.create("Ti avviseremo quando il prodotto sarà disponibile!").show();
    }


    @Transactional("marketnewstoredbTransactionManager")
    public void processAvvisi() {
        systemAuthenticator.withSystem(() -> {
            List<Avvisi> avvisi = dataManager.load(Avvisi.class).all().list();
            log.info("Avvisi trovati: {}", avvisi.size());

            for (Avvisi avviso : avvisi) {
                try {
                    String codArt = avviso.getCodArt();
                    String giacenzaStr = getDataService.getGiacenze(codArt);

                    int giacenza = 0;
                    try {
                        giacenza = Integer.parseInt(giacenzaStr);
                    } catch (NumberFormatException e) {
                        log.warn("Giacenza non valida per {}: {}", codArt, giacenzaStr);
                    }

                    if (giacenza > 0) {
                        boolean inviato = sendEmailAvviso(avviso);
                        if (inviato) {
                            dataManager.remove(avviso);
                            log.info("Avviso rimosso per utente {} e prodotto {}", avviso.getIdUtente(), codArt);
                        }
                    }

                } catch (Exception e) {
                    log.error("Errore processando avviso {}", avviso, e);
                }
            }

            return null; // richiesto dal Supplier<Void>
        });
    }


   private String generaLinkProdotto(String codArt, String codicePagineProdotti) {
        Art prodotto = dataManager.load(Art.class)
                .id(codArt)
                .optional()
                .orElse(null);

        if (prodotto == null) return "#"; // fallback se il prodotto non esiste

        // Recupero valori come nella createList
        String descrizione = prodotto.getDescrizione();

        // Protezione contro null per il prezzo
        Double prezzoCons = prodotto.getPrezzoCons();
        String prezzo = "€" + formattaPrezzo(prezzoCons != null ? prezzoCons.toString() : "0.00");

        String codiceBar = getDataService.primoCodiceABarre(prodotto.getCodiceArticolo());
        String nomeImg = getDataService.getArtImgPrincipale(prodotto.getCodiceArticolo());

        Map<String, String> parametri = new HashMap<>();
        parametri.put("codice", prodotto.getCodiceArticolo());
        parametri.put("descrizione", descrizione != null ? descrizione : "");
        parametri.put("prezzoCons", prezzo);
        parametri.put("codiceBar", codiceBar != null ? codiceBar : "");
        parametri.put("nomeImg", nomeImg != null ? nomeImg : "");
        parametri.put("codiceProdotti", codicePagineProdotti != null ? codicePagineProdotti : "");
        parametri.put("promo", "NO");
        parametri.put("pagina", "1");

        // Costruisco la query string
        String queryString = parametri.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        // L’URL finale che sarà identico a quello del click su card
        return baseUrl + "/dettaglioprodotto?" + queryString;
    }



    public String formattaPrezzo(String s) {
        String prova = s + "0000000";
        String result = prova.substring(0,prova.indexOf(".")) + prova.substring(prova.indexOf("."),prova.indexOf(".")+3);
        result = result.replace('.',',');
        return result;
    }

   private boolean sendEmailAvviso(Avvisi avviso) {
       try {
           Optional<User> utenteOpt = dataManager.load(User.class)
                   .id(UUID.fromString(avviso.getIdUtente()))
                   .optional();

           if (utenteOpt.isEmpty() || utenteOpt.get().getEmail() == null || utenteOpt.get().getEmail().isBlank()) {
               return false;
           }

           User utente = utenteOpt.get();
           String emailUtente = utente.getEmail();
           String nomeUtente = utente.getFirstName() != null ? utente.getFirstName() : "Cliente";

           // Se esiste una tabella prodotti, puoi caricare il nome reale del prodotto
           String nomeProdotto = dataManager.loadValue(
                           "select p.descrizione from Art p where p.codiceArticolo = :codArt", String.class)
                   .parameter("codArt", avviso.getCodArt())
                   .store("marketnewstoredb")
                   .optional()
                   .orElse(avviso.getCodArt()); // fallback al codice prodotto

           String linkProdotto = generaLinkProdotto(avviso.getCodArt(), "1");

           MimeMessage mimeMessage = javaMailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
           helper.setTo(emailUtente);
           helper.setSubject("Il prodotto che cercavi è disponibile!");
           helper.setFrom("noreply@shopEcom.it");

           StringBuilder body = new StringBuilder();

           body.append("<html><head>");
           body.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
           body.append("<style>");
           body.append("  @media only screen and (max-width:600px) {");
           body.append("    .desktop-content {display:none !important;}");
           body.append("    .mobile-card {display:block !important; width:100% !important; padding:15px; box-sizing:border-box; margin-bottom:15px; border:1px solid #e0e0e0; border-radius:8px;}");
           body.append("  }");
           body.append("  @media only screen and (min-width:601px) {");
           body.append("    .mobile-card {display:none !important;}");
           body.append("  }");
           body.append("</style>");
           body.append("</head><body style='font-family:Arial,sans-serif; color:#333; margin:0; padding:0; background-color:#f8f8f8;'>");

           // Container principale
           body.append("<div style='max-width:700px; margin:30px auto; background-color:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1); padding:30px;'>");

           // Header con logo
           body.append("<div style='text-align:center; margin-bottom:20px;'>")
                   .append("<img src='cid:logo' alt='Logo Azienda' style='max-height:80px;'>")
                   .append("</div>");

           // Saluto personalizzato
           body.append("<h2 style='color:#222; font-weight:600; margin-bottom:10px;'>Ciao ").append(nomeUtente).append("!</h2>");
           body.append("<p style='font-size:16px; line-height:1.5; color:#555;'>Il prodotto <strong>")
                   .append(nomeProdotto)
                   .append("</strong> che cercavi è ora disponibile!</p>");

           // Desktop content
           body.append("<div class='desktop-content' style='padding:15px; border:1px solid #e0e0e0; border-radius:8px; margin-bottom:15px;'>");
           body.append("<p style='font-size:14px; color:#333;'>Clicca sul link per acquistarlo subito:</p>");
           body.append("<p><a href='").append(linkProdotto)
                   .append("' style='display:inline-block; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>Vai al prodotto</a></p>");
           body.append("</div>");

           // Mobile card
           body.append("<div class='mobile-card'>")
                   .append("<span style='font-weight:600;'>Prodotto:</span> <span>").append(nomeProdotto).append("</span><br>")
                   .append("<a href='").append(linkProdotto)
                   .append("' style='display:inline-block; margin-top:10px; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>Vai al prodotto</a>")
                   .append("</div>");

           // Footer elegante
           body.append("<div style='border-top:1px solid #e0e0e0; padding:20px; text-align:center; font-size:14px; color:#999; margin-top:20px;'>")
                   .append("<p>Grazie per aver scelto il nostro negozio.</p>")
                   .append("<p><strong>Il team 2Emme Distribuzione</strong></p>")
                   .append("</div>");

           body.append("</div></body></html>");

           helper.setText(body.toString(), true);

           // Logo inline
           helper.addInline("logo", getLogoResource(), "image/png");

           javaMailSender.send(mimeMessage);
           return true;

       } catch (Exception e) {
           log.error("Errore invio email avviso {}", avviso, e);
           return false;
       }
   }
    //no link prodotto
  /* private boolean sendEmailAvviso(Avvisi avviso) {
       try {
           Optional<User> utenteOpt = dataManager.load(User.class)
                   .id(UUID.fromString(avviso.getIdUtente()))
                   .optional();

           if (utenteOpt.isEmpty() || utenteOpt.get().getEmail() == null || utenteOpt.get().getEmail().isBlank()) {
               return false;
           }

           User utente = utenteOpt.get();
           String emailUtente = utente.getEmail();
           String nomeUtente = utente.getFirstName() != null ? utente.getFirstName() : "Cliente";

           String nomeProdotto = dataManager.loadValue(
                           "select p.descrizione from Art p where p.codiceArticolo = :codArt", String.class)
                   .parameter("codArt", avviso.getCodArt())
                   .store("marketnewstoredb")
                   .optional()
                   .orElse(avviso.getCodArt());

           // 👉 qui non mando al prodotto ma alla pagina login
           String linkLogin = "https://" + dominio + "/login";


           MimeMessage mimeMessage = javaMailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
           helper.setTo(emailUtente);
           helper.setSubject("Accedi per acquistare il prodotto che cercavi!");
           helper.setFrom("noreply@shopEcom.it");

           StringBuilder body = new StringBuilder();
           body.append("<html><head>")
                   .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>")
                   .append("<style>")
                   .append("  @media only screen and (max-width:600px) {")
                   .append("    .desktop-content {display:none !important;}")
                   .append("    .mobile-card {display:block !important; width:100% !important; padding:15px; box-sizing:border-box; margin-bottom:15px; border:1px solid #e0e0e0; border-radius:8px;}")
                   .append("  }")
                   .append("  @media only screen and (min-width:601px) {")
                   .append("    .mobile-card {display:none !important;}")
                   .append("  }")
                   .append("</style>")
                   .append("</head><body style='font-family:Arial,sans-serif; color:#333; margin:0; padding:0; background-color:#f8f8f8;'>");

           body.append("<div style='max-width:700px; margin:30px auto; background-color:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1); padding:30px;'>");

           body.append("<div style='text-align:center; margin-bottom:20px;'>")
                   .append("<img src='cid:logo' alt='Logo Azienda' style='max-height:80px;'>")
                   .append("</div>");

           body.append("<h2 style='color:#222; font-weight:600; margin-bottom:10px;'>Ciao ").append(nomeUtente).append("!</h2>");
           body.append("<p style='font-size:16px; line-height:1.5; color:#555;'>Il prodotto <strong>")
                   .append(nomeProdotto)
                   .append("</strong> che cercavi è disponibile, ma per acquistarlo devi prima accedere.</p>");

           body.append("<div class='desktop-content' style='padding:15px; border:1px solid #e0e0e0; border-radius:8px; margin-bottom:15px;'>");
           body.append("<p style='font-size:14px; color:#333;'>Clicca sul link per accedere e completare l'acquisto:</p>");
           body.append("<p><a href='").append(linkLogin)
                   .append("' style='display:inline-block; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>Vai al login</a></p>");
           body.append("</div>");

           body.append("<div class='mobile-card'>")
                   .append("<span style='font-weight:600;'>Prodotto:</span> <span>").append(nomeProdotto).append("</span><br>")
                   .append("<a href='").append(linkLogin)
                   .append("' style='display:inline-block; margin-top:10px; padding:10px 20px; background-color:#007bff; color:#fff; text-decoration:none; border-radius:5px;'>Vai al login</a>")
                   .append("</div>");

           body.append("<div style='border-top:1px solid #e0e0e0; padding:20px; text-align:center; font-size:14px; color:#999; margin-top:20px;'>")
                   .append("<p>Grazie per aver scelto il nostro negozio.</p>")
                   .append("<p><strong>Il team 2Emme Distribuzione</strong></p>")
                   .append("</div>");

           body.append("</div></body></html>");

           helper.setText(body.toString(), true);
           helper.addInline("logo", getLogoResource(), "image/png");
           javaMailSender.send(mimeMessage);

           return true;
       } catch (Exception e) {
           log.error("Errore invio email avviso {}", avviso, e);
           return false;
       }
   }*/


    @Transactional("marketnewstoredbTransactionManager")
    public Optional<PdcDatiAggiuntivi> findAggiuntivi(String codiceConto) {
        return dataManager.load(PdcDatiAggiuntivi.class)
                .query("select p from PdcDatiAggiuntivi p where p.codiceConto = :codiceConto")
                .parameter("codiceConto", codiceConto)
                .optional();
    }

    @Transactional("marketnewstoredbTransactionManager")
    public TessereClienti getTesseraByCodiceCliente(String codiceCliente) {
        return dataManager.load(TessereClienti.class)
                .query("select tc from TessereClienti tc where tc.codiceCliente = :codiceCliente")
                .parameter("codiceCliente", codiceCliente)
                .optional()  // restituisce Optional<TessereClienti>
                .orElse(null);
    }

    public boolean verificaCredenziali(String usernameInput, String passwordInput) {
        List<PdcPassword> pdcList = unconstrainedDataManager.load(PdcPassword.class)
                .query("select e from PdcPassword e where e.username = :username and e.password = :password")
                .parameter("username", usernameInput)
                .parameter("password", passwordInput)
                .list();

        return !pdcList.isEmpty();
    }
   public Map<String, String> getCredenzialiByCodiceTessera(String codiceTessera) {
       TessereClienti tessera = unconstrainedDataManager.load(TessereClienti.class)
               .query("select tc from TessereClienti tc where tc.codiceTessera = :codiceTessera")
               .parameter("codiceTessera", codiceTessera)
               .optional()
               .orElse(null);

       if (tessera == null) return null;

       String codiceConto = tessera.getCodiceCliente();

       List<PdcPassword> pdcPassList = unconstrainedDataManager.load(PdcPassword.class)
               .query("select e from PdcPassword e where e.codice = :codiceConto")
               .parameter("codiceConto", codiceConto)
               .list();

       if (pdcPassList.isEmpty()) return null;

       PdcPassword pdcPass = pdcPassList.get(0);

       Map<String, String> credenziali = new HashMap<>();
       credenziali.put("username", pdcPass.getUsername());
       credenziali.put("password", pdcPass.getPassword());
       credenziali.put("codiceCliente", codiceConto);

       return credenziali;
   }

}