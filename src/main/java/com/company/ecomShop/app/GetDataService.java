package com.company.ecomShop.app;

import com.company.ecomShop.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.sun.jna.platform.win32.Sspi;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import jakarta.persistence.*;
import org.apache.catalina.Group;
import org.apache.catalina.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class GetDataService {

    private final MyClickListener myClickListener;
    private final PasswordEncoder passwordEncoder;
    private final DataManager dataManager;
    private final FileStorageLocator fileStorageLocator;
    @PersistenceContext(unitName = "marketnewstoredb") // store name
    private EntityManager entityManager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @PersistenceContext(unitName = "marketnewstorecb") // store name
    private EntityManager entityManager1;
    private List<String> lastPromoJsonList;

    public List<String> getLastPromoJsonList() {
        return lastPromoJsonList;
    }

    public GetDataService(MyClickListener myClickListener, PasswordEncoder passwordEncoder, DataManager dataManager, FileStorageLocator fileStorageLocator) {
        this.myClickListener = myClickListener;
        this.passwordEncoder = passwordEncoder;
        this.dataManager = dataManager;
        this.fileStorageLocator = fileStorageLocator;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public List geArticoliClassificazioni(String listino, String testo) {

        List results = new ArrayList();

        String q = "";

        if (testo.contains("'")) {
            testo = testo.replace("'", " ");
        }

        if (listino.equalsIgnoreCase("1")) {

            q =
                    "    select top 8 'Classi' as Classe,codice,descrizione, '' as prezzo from marketnewstoreDb..an where descrizione like '%" + testo + "%'\n" +
                            " or codice in (select CLASSE_ESTESA from marketnewstoredb..art where descrizione like '%" + testo + "%' and cod_classe_7 ='1' )\n" +
                            "    union\n" +
                            "    select top 6 'Articoli' as Classe,codice_articolo,descrizione,\n" +
                            "            prezzo = (select prezzo_vend from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo=marketnewstoredb..art.codice_articolo and marketnewstoredb..lis.listino='" + "1" + "')\n" +
                            "    from marketnewstoredb..art where descrizione like '%" + testo + "%'\n";
        } else if (listino.equalsIgnoreCase("2")) {
            q =
                    "    select top 8 'Classi' as Classe,codice,descrizione, '' as prezzo from marketnewstoreDb..an where descrizione like '%" + testo + "%'\n" +
                            " or codice in (select CLASSE_ESTESA from marketnewstoredb..art where descrizione like '%" + testo + "%' and cod_classe_7 ='1' )\n" +
                            "    union\n" +
                            "    select top 3 'Articoli' as Classe,codice_articolo,descrizione,\n" +
                            "            prezzo = (select sec_prezzo_ve from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo=marketnewstoredb..art.codice_articolo and marketnewstoredb..lis.listino='" + "1" + "')\n" +
                            "    from marketnewstoredb..art where descrizione like '%" + testo + "%'\n";
        } else if (listino.equalsIgnoreCase("3")) {
            q =
                    "    select top 8 'Classi' as Classe,codice,descrizione, '' as prezzo from marketnewstoreDb..an where descrizione like '%" + testo + "%'\n" +
                            " or codice in (select CLASSE_ESTESA from marketnewstoredb..art where descrizione like '%" + testo + "%' and cod_classe_7 ='1' )\n" +
                            "    union\n" +
                            "    select top 3 'Articoli' as Classe,codice_articolo,descrizione,\n" +
                            "            prezzo = (select terzo_prezzo_ve from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo=marketnewstoredb..art.codice_articolo and marketnewstoredb..lis.listino='" + "1" + "')\n" +
                            "    from marketnewstoredb..art where descrizione like '%" + testo + "%'\n";
        }

        q = q + " and cod_classe_7 ='1' ";
        List<ArticoliClassificazioni> risultato = new ArrayList<>();

        try {
            results = entityManager.createNativeQuery(q).getResultList();
            entityManager.close();
        } catch (Exception ex) {
            System.out.println("il carattere" + testo + "non è supportato");


        }


        for (Object m : results) {
            ArticoliClassificazioni articoliClassificazioni = new ArticoliClassificazioni();
            articoliClassificazioni.setClassi(((Object[]) m)[0] != null ? ((Object[]) m)[0].toString() : "");
            articoliClassificazioni.setCodiceArticolo(((Object[]) m)[1] != null ? ((Object[]) m)[1].toString() : "");
            articoliClassificazioni.setDescrizione(((Object[]) m)[2] != null ? ((Object[]) m)[2].toString() : "");
            articoliClassificazioni.setPrezzo(((Object[]) m)[3] != null ? ((Object[]) m)[3].toString() : "0.0");

            risultato.add(articoliClassificazioni);
        }


        return risultato;
    }


    @Transactional("marketnewstoredbTransactionManager")
    public String gePrezzoArt(String listino, String codice) {

        List results = new ArrayList();

        String q = "";

        if (listino.equalsIgnoreCase("1")) {

            q = "(select prezzo_vend from marketnewstoreDb..lis where  marketnewstoreDb..lis.codice_articolo='" + codice + "' and marketnewstoredb..lis.listino='" + "1" + "')";
        } else if (listino.equalsIgnoreCase("2")) {
            q = "(select case when sec_prezzo_ve is not null then sec_prezzo_ve else prezzo_vend end from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo='" + codice + "' and marketnewstoredb..lis.listino='1')";
        } else if (listino.equalsIgnoreCase("3")) {

            q = "(select case when terzo_prezzo_ve is not null then terzo_prezzo_ve else prezzo_vend end from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo='" + codice + "' and marketnewstoredb..lis.listino='1')";
        }


        Object result = entityManager.createNativeQuery(q).getSingleResult();

        entityManager.close();


        return result.toString();
    }


    @Transactional("marketnewstoredbTransactionManager")
    public List geArticoliByText(String codice, String testo, String listino, int numeroPagina, int elementiPagina) {

        List results = new ArrayList();
        codice = (codice + "_________").substring(0, 9);
        String q = " with aa as (";


        if (listino.equalsIgnoreCase("1")) {


            q = q + "   select codice_articolo,descrizione,(select prezzo_vend from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by descrizione) as riga from art where DESCRIZIONE like '%" + testo + "%' and  cod_classe_7 ='1' AND CLASSE_ESTESA like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("2")) {
            q = q + "   select codice_articolo,descrizione,(select sec_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by descrizione) as riga  from art where DESCRIZIONE  like '%" + testo + "%'  and  cod_classe_7 ='1' AND CLASSE_ESTESA like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("3")) {
            q = q + "   select codice_articolo,descrizione,(select terzo_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by descrizione) as riga  from art where DESCRIZIONE like  '%" + testo + "%'    and cod_classe_7 ='1' AND CLASSE_ESTESA like '" + codice + "'";
        }

        q = q + ") select * from aa where riga > (" + numeroPagina + "*" + elementiPagina + ") and riga <= (" + numeroPagina + "*" + elementiPagina + "+" + elementiPagina + ")";


        List<Art> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();

        for (Object m : results) {
            Art articoliClassificazioni = new Art();
            articoliClassificazioni.setCodiceArticolo(((Object[]) m)[0] != null ? ((Object[]) m)[0].toString() : "");
            articoliClassificazioni.setDescrizione(((Object[]) m)[1] != null ? ((Object[]) m)[1].toString() : "");
            articoliClassificazioni.setPrezzoCons(Double.valueOf(((Object[]) m)[2] != null ? ((Object[]) m)[2].toString() : "0.0"));

            risultato.add(articoliClassificazioni);
        }


        return risultato;
    }


//        select prezzo_vend,sec_prezzo_ve, terzo_prezzo_ve from marketnewstoreDb..lis
//
//
//    select 'Classi' as Classe,codice,descrizione, '' as prezzo from marketnewstoreDb..an where descrizione like '%DETER%'
//    union
//    select top 3 'Articoli' as Classe,codice_articolo,descrizione,
//            prezzo = (select prezzo_vend from marketnewstoreDb..lis where marketnewstoreDb..lis.codice_articolo=marketnewstoredb..art.codice_articolo and marketnewstoredb..lis.listino='1')
//    from marketnewstoredb..art where descrizione like 'DASH%'


    public List<Object[]> getArtOfferta(String tipoTes) {

        String jpql = "SELECT DISTINCT p.codPanel, p.qta1 AS prezzo, a.descrizione AS descrizione " +
                "FROM Prom p " +
                "JOIN Art a ON a.codiceArticolo = p.codPanel " +
                "WHERE p.tipoAppl = '2' " +
                "AND p.tipoOff = '7' " +
                "AND p.dataIniz <= :dataCorrente " +
                "AND p.dataFin >= :dataCorrente1 " +
                "AND p.codPanel <> ''" +
                "AND p.tipoTes = :tipoTes" +
                " AND a.codClasse7 ='1' ";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Data di oggi come stringa
        String dateFin = sdf.format(new Date());

        // Calcola la data di 30 giorni fa come stringa
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -30);
//        String dateIni = sdf.format(calendar.getTime());

        Query query = entityManager.createQuery(jpql);
        query.setParameter("dataCorrente", dateFin);
        query.setParameter("dataCorrente1", dateFin);
        query.setParameter("tipoTes", tipoTes);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> verArtOfferta(String tipoTes, String codiceArticolo) {

        String jpql = "SELECT DISTINCT p.codPanel, p.qta1 AS prezzo, a.descrizione AS descrizione " +
                "FROM Prom p " +
                "JOIN Art a ON a.codiceArticolo = p.codPanel " +
                "WHERE p.tipoAppl = '2' " +
                "AND p.tipoOff = '7' " +
                "AND p.dataIniz <= :dataCorrente " +
                "AND p.dataFin >= :dataCorrente1 " +
                "AND p.codPanel <> ''" +
                "AND p.tipoTes = :tipoTes " +
                "AND p.codPanel =:codiceArticolo " +
                " AND a.codClasse7 ='1' ";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // Data di oggi come stringa
        String dateFin = sdf.format(new Date());

//        // Calcola la data di 30 giorni fa come stringa
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -30);
//        String dateIni = sdf.format(calendar.getTime());

        Query query = entityManager.createQuery(jpql);
        query.setParameter("dataCorrente", dateFin);
        query.setParameter("dataCorrente1", dateFin);
        query.setParameter("tipoTes", tipoTes);
        query.setParameter("codiceArticolo", codiceArticolo);
        List<Object[]> results = query.getResultList();
        return results;
    }


    public List<An> getAn(String nCategorie) {
        List results = new ArrayList();

        String q = "";

        if (!nCategorie.equalsIgnoreCase("tutti")) {

            q =
                    "   select top " + nCategorie + "  CODICE ,DESCRIZIONE from  marketnewstoreDb..an where CODICE like '___' order by codice";
        } else {

            q =
                    "   select  CODICE ,DESCRIZIONE from  marketnewstoreDb..an where CODICE like '___' order by codice";
        }

        List<An> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();

        for (Object m : results) {
            An an = new An();
            an.setCodice(((Object[]) m)[0] != null ? ((Object[]) m)[0].toString() : "");
            an.setDescrizione(((Object[]) m)[1] != null ? ((Object[]) m)[1].toString() : "");

            risultato.add(an);
        }


        return risultato;
    }


    public List<An> getAnSpecifiche(String codice) {
        List results = new ArrayList();

        String q = "";


        q =
                "   select   CODICE ,DESCRIZIONE from an where CODICE like '" + codice + "___' order by codice";


        List<An> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();

        for (Object m : results) {
            An an = new An();
            an.setCodice(((Object[]) m)[0] != null ? ((Object[]) m)[0].toString() : "");
            an.setDescrizione(((Object[]) m)[1] != null ? ((Object[]) m)[1].toString() : "");

            risultato.add(an);
        }


        return risultato;
    }


    public List<Art> getAnSpecificheArticoli(String codice, String listino, int numeroPagina, int elementiPagina) {
        List results = new ArrayList();
        numeroPagina = numeroPagina - 1;
        codice = (codice + "_________").substring(0, 9);
        String q = " with aa as (";

        if (listino.equalsIgnoreCase("1")) {


            q = q + "   select codice_articolo,descrizione,(select prezzo_vend from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga from art where " +
                    "   AND COD_CLASSE_7 ='1' AND  CLASSE_ESTESA like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("2")) {
            q = q + "   select codice_articolo,descrizione,(select sec_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where  COD_CLASSE_7 ='1' AND   CLASSE_ESTESA  like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("3")) {
            q = q + "   select codice_articolo,descrizione,(select terzo_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where COD_CLASSE_7 ='1' AND CLASSE_ESTESA like  '" + codice + "'";
        }

        q = q + ") select * from aa where riga >= (" + numeroPagina + "*" + elementiPagina + ") and riga <= (" + numeroPagina + "*" + elementiPagina + "+" + elementiPagina + ")";

        List<Art> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();

        for (Object m : results) {
            Art art = new Art();
            art.setCodiceArticolo(((Object[]) m)[0] != null ? ((Object[]) m)[0].toString() : "");
            art.setDescrizione(((Object[]) m)[1] != null ? ((Object[]) m)[1].toString() : "");
            art.setPrezzoCons(Double.valueOf(((Object[]) m)[2] != null ? ((Object[]) m)[2].toString() : "0.0"));

            risultato.add(art);
        }

        return risultato;
    }


    public Integer getArtTotali(String codice, String listino) {

        List results = new ArrayList();

        codice = (codice + "_________").substring(0, 9);

        String q = " with aa as (";
        if (listino.equalsIgnoreCase("1")) {


            q = q + "   select codice_articolo,descrizione,(select prezzo_vend from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga from art where " +
                    "    COD_CLASSE_7 ='1' AND  CLASSE_ESTESA like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("2")) {
            q = q + "   select codice_articolo,descrizione,(select sec_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where  COD_CLASSE_7 ='1' AND   CLASSE_ESTESA  like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("3")) {
            q = q + "   select codice_articolo,descrizione,(select terzo_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where COD_CLASSE_7 ='1' AND CLASSE_ESTESA like  '" + codice + "'";
        }

        q = q + ") select * from aa ";

        List<Art> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();


//        q = q + "   select codice_articolo,descrizione from art where   COD_CLASSE_7 ='1' AND CLASSE_ESTESA like '" + codice + "'  ";
//
//
//        List<Art> risultato = new ArrayList<>();
//
//        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();


        return results.size();
    }


    public Integer getArtTotaliByText(String text, String codice, String listino) {
        List results = new ArrayList();

        codice = (codice + "_________").substring(0, 9);
        String q = " with aa as (";

        if (listino.equalsIgnoreCase("1")) {


            q = q + "   select codice_articolo,descrizione,(select prezzo_vend from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga from art where " +
                    "   COD_CLASSE_7 ='1' AND  DESCRIZIONE like '%" + text + "%'  AND CLASSE_ESTESA like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("2")) {
            q = q + "   select codice_articolo,descrizione,(select sec_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where  COD_CLASSE_7 ='1'  AND  DESCRIZIONE like '%" + text + "%'  AND   CLASSE_ESTESA  like '" + codice + "'";

        } else if (listino.equalsIgnoreCase("3")) {
            q = q + "   select codice_articolo,descrizione,(select terzo_prezzo_ve from LIS where LIS.codice_articolo=ART.codice_articolo and LIS.listino='" + "1" + "') as prezzo ,ROW_NUMBER() over (order by codice_articolo,descrizione) as riga  from art where COD_CLASSE_7 ='1'  AND  DESCRIZIONE like '%" + text + "%' AND CLASSE_ESTESA like  '" + codice + "'";
        }

        q = q + ") select * from aa ";

        List<Art> risultato = new ArrayList<>();

        results = entityManager.createNativeQuery(q).getResultList();


//        q = q + "   select codice_articolo,descrizione from art where COD_CLASSE_7 ='1' AND  DESCRIZIONE like '%" + text + "%' and CLASSE_ESTESA like '" + codice + "' ";
//
//
//        List<Art> risultato = new ArrayList<>();
//
//        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();


        return results.size();
    }

    public String primoCodiceABarre(String codiceArticolo) {

        String numero = "";


        String ImpostaQuery = "select top 1  COD_BAR_FOR  from  art2 where CODICE_ARTICOLO='" + codiceArticolo + "' AND FLAG_B_F='B'";


        Object result = null;


        result = entityManager.createNativeQuery(ImpostaQuery).getResultList();

        if (result != null) {
            return numero = result.toString();

        } else
            return numero = "";

    }

    public Integer getListinoCliente(String username) {

        try {
            List<PdcPassword> mtc = dataManager.load(PdcPassword.class).
                    query("select e from PdcPassword  e where e.username =:username").
                    parameter("username", username).
                    list();

            List<Pdc> mtc1 = dataManager.load(Pdc.class).
                    query("select e from Pdc  e where e.codiceConto =:codiceConto").
                    parameter("codiceConto", mtc.get(0).getCodice()).
                    list();


            return Integer.valueOf((mtc1 != null && mtc1.size() > 0) ? mtc1.get(0).getListinoCl() : null);
        } catch (Exception e) {
            return null;
        }

    }


    @Transactional("marketnewstoredbTransactionManager")
    public String getProg(String tipo) {
        String progressivo = "";

        // identifica il database della DITTA dal codice del negozio
        //StoredProcedureQuery mxquery = entityManager.createStoredProcedureQuery("GET_NP");

        //mxquery.registerStoredProcedureParameter("TIPO", String.class, ParameterMode.IN);

        //mxquery.setParameter("TIPO", tipo);

        Object result = null;
        //mxquery.execute();
        result = entityManager.createNativeQuery("exec GET_NP '" + tipo + "'").getSingleResult();
        //result =  mxquery.getSingleResult();


        if (result != null) {

            progressivo = result.toString();


        }

        return progressivo;


    }


    public void creaUser(String email, String password, String cognomeCli, String nomeCli, String userName) {
        User usr = new User();
        usr.setId(UUID.randomUUID());
        usr.setPassword(passwordEncoder.encode(password));
        usr.setFirstName(nomeCli);
        usr.setLastName(cognomeCli);
        usr.setUsername(userName);
        usr.setEmail(email);
        usr.setActive(true);
        usr.setTimeZoneId("Europe/Rome");
        dataManager.save(usr);
    }


    public void createRole(String userName) {
        RoleAssignmentEntity roleAssignmentEntity = new RoleAssignmentEntity();
        roleAssignmentEntity.setId(UUID.randomUUID());
        roleAssignmentEntity.setUsername(userName);
        roleAssignmentEntity.setRoleType("resource");
        roleAssignmentEntity.setVersion(1);
        roleAssignmentEntity.setRoleCode("flowui-filter");


        RoleAssignmentEntity roleAssignmentEntity1 = new RoleAssignmentEntity();
        roleAssignmentEntity1.setId(UUID.randomUUID());
        roleAssignmentEntity1.setUsername(userName);
        roleAssignmentEntity1.setRoleType("resource");
        roleAssignmentEntity1.setVersion(1);
        roleAssignmentEntity1.setRoleCode("ui-minimal");


        RoleAssignmentEntity roleAssignmentEntity2 = new RoleAssignmentEntity();
        roleAssignmentEntity2.setId(UUID.randomUUID());
        roleAssignmentEntity2.setUsername(userName);
        roleAssignmentEntity2.setRoleType("resource");
        roleAssignmentEntity2.setVersion(1);
        roleAssignmentEntity2.setRoleCode("anonymus-role");

        RoleAssignmentEntity roleAssignmentEntity3 = new RoleAssignmentEntity();
        roleAssignmentEntity3.setId(UUID.randomUUID());
        roleAssignmentEntity3.setUsername(userName);
        roleAssignmentEntity3.setRoleType("resource");
        roleAssignmentEntity3.setVersion(1);
        roleAssignmentEntity3.setRoleCode("registered-user");

        dataManager.save(roleAssignmentEntity);
        dataManager.save(roleAssignmentEntity1);
        dataManager.save(roleAssignmentEntity2);
        dataManager.save(roleAssignmentEntity3);

    }

    public String getArtImgPrincipale(String codiceArticolo) {
        try {


            //String ImpostaQuery = "select *   from  MARKETNEWSTORE_ART_IMG where CODICE_ARTICOLO='" + numero + "'";


            String ImpostaQuery = "select concat(concat(ID ,'.'), EXT),create_ts from SYS_FILE sf" +
                    " where ID in (select art_img_principale_id  from marketnewstore_art_img asnfdl where codice_articolo='" + codiceArticolo + "')";


            Object result = null;
            Object result1 = null;
            String file = "";


            result = entityManager1.createNativeQuery(ImpostaQuery).getResultList();

            if (result != null && ((Vector<Object[]>) result).get(0).length > 0) {
                MarketnewstoreArtImg marketnewstoreArtImg = new MarketnewstoreArtImg();
                String codiceImmagine = ((Vector<Object[]>) result).get(0)[0] != null ? ((Vector<Object[]>) result).get(0)[0].toString() : "";
                Timestamp tempo = ((Vector<Object[]>) result).get(0)[1] != null ? (Timestamp) ((Vector<Object[]>) result).get(0)[1] : null;

                String folder = tempo.toString().substring(0, 10).replace('-', '/');
                file = folder + "/" + codiceImmagine;
            }


            return file;
        } catch (Exception ne) {

            return null;
        }
    }

    @Transactional("marketnewstoredbTransactionManager")
    public String getArtxCartone(String codice) {

        List results = new ArrayList();


        String q = "   select  * From Art where codice_articolo =  '" + codice + "'";


        String risultato = "";

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();
        if (results.size() > 0) {
            for (Object m : results) {

                risultato = ((Object[]) m)[21] != null ? ((Object[]) m)[21].toString() : "0";


            }
        } else {
            risultato = "0";
        }


        return String.valueOf(Double.valueOf(risultato).intValue());
    }


    @Transactional("marketnewstoredbTransactionManager")
    public String getGiacenze(String codice) {

        List results = new ArrayList();


        String q = "   select  * From Salmg where codice_articolo =  '" + codice + "' and CODICE_MAGAZZINO = 1  and PROPRIETA_MAGAZ = 0 ";


        String risultato = "";

        results = entityManager.createNativeQuery(q).getResultList();

        entityManager.close();

        if (results.size() > 0) {
            for (Object m : results) {

                risultato = ((Object[]) m)[22] != null ? ((Object[]) m)[22].toString() : "0.0";
//                risultato = risultato.substring(0,risultato.length()-2).replace(".",",");

            }
        } else {
            risultato = "0";
        }


        return String.valueOf(Double.valueOf(risultato.replace(",", ".")).intValue());
    }

    @Transactional("marketnewstoredbTransactionManager")
    public ConfigEcom getConfigEcom() {
        String jpql = "SELECT ce FROM ConfigEcom ce";
        TypedQuery<ConfigEcom> query = entityManager.createQuery(jpql, ConfigEcom.class);
        return query.getSingleResult();
    }


    @Transactional("marketnewstoredbTransactionManager")
    public List<CouponDto> getCouponInfo(String codiceCliente, String data) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("GetListaCouponCliente");

        query.registerStoredProcedureParameter("cliente", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("data", String.class, ParameterMode.IN);

        query.setParameter("cliente", codiceCliente);
        query.setParameter("data", data);

        query.execute();

        List<Object[]> results = query.getResultList();
        System.out.println("Coupon trovati: " + results.size());
        List<CouponDto> coupons = new ArrayList<>();

        for (Object[] row : results) {
            coupons.add(new CouponDto(
                    (String) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3]
            ));
        }
        return coupons;
    }

   /* @Transactional("marketnewstoredbTransactionManager")
    public List<PromoDto> getActivePromotionsForTessera(String tessera) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("GETPROM_ATTIVE_CLIENTE");

        query.registerStoredProcedureParameter("cod_tessera", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("cod_articolo", String.class, ParameterMode.IN);

        query.setParameter("cod_tessera", tessera);
        query.setParameter("cod_articolo", "");

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<PromoDto> promos = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> rowMap = new LinkedHashMap<>();

            for (int i = 0; i < row.length; i++) {
                // Crea chiavi generiche COL_1, COL_2, ecc. in MAIUSCOLO
                rowMap.put("COL_" + (i + 1), row[i]);
            }

            // Converte la riga in JSON per QR Code
            String jsonRow;
            try {
                jsonRow = objectMapper.writeValueAsString(rowMap);
            } catch (Exception e) {
                jsonRow = "{}";
            }

            // Estrai i campi noti, se esistono (usa posizione o nomi se li conosci)
            String dataIniz = Objects.toString(row[0], "");
            String dataFin = Objects.toString(row[1], "");
            String descr = Objects.toString(row[2], "");
            String numeroOff = Objects.toString(row[3], "");
            String codMess = Objects.toString(row[4], "");

            PromoDto dto = new PromoDto(numeroOff, codMess, descr, dataIniz, dataFin);
            dto.setJsonData(jsonRow); // aggiungi il JSON completo

            promos.add(dto);
        }

        return promos;
    }*/
  /* @Transactional("marketnewstoredbTransactionManager")
   public List<PromoDto> getActivePromotionsForTessera(String tessera) {
       StoredProcedureQuery query = entityManager
               .createStoredProcedureQuery("GETPROM_ATTIVE_CLIENTE");

       query.registerStoredProcedureParameter("cod_tessera", String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter("cod_articolo", String.class, ParameterMode.IN);

       query.setParameter("cod_tessera", tessera);
       query.setParameter("cod_articolo", "");

       query.execute();

       @SuppressWarnings("unchecked")
       List<Object[]> results = query.getResultList();

       List<PromoDto> promos = new ArrayList<>();
       List<String> promoJsonList = new ArrayList<>(); // JSON pronti per QR code

       ObjectMapper mapper = new ObjectMapper();

       for (Object[] row : results) {
           // --- Popola DTO (per UI) ---
           String dataIniz = Objects.toString(row[0], "");
           String dataFin = Objects.toString(row[1], "");
           String descr = Objects.toString(row[2], "");
           String numeroOff = Objects.toString(row[3], "");
           String codMess = Objects.toString(row[4], "");
           promos.add(new PromoDto(numeroOff, codMess, descr, dataIniz, dataFin));

           // --- Costruisci JSON "PROM" ---
           Map<String, Object> promSection = new LinkedHashMap<>();
           for (int i = 0; i < row.length; i++) {
               promSection.put(String.valueOf(i + 1), Objects.toString(row[i], ""));
           }

           // --- Dati di contesto ---
           String tipoAppl = Objects.toString(row[5], ""); // verifica indice corretto
           String codPanel = Objects.toString(row[6], ""); // idem
           String descrizione = descr; // già preso sopra

           // --- Se tipo_appl = 4, aggiungi blocchi PANELNEWx ---
           if ("4".equals(tipoAppl) && !codPanel.isEmpty()) {
               List<Object[]> panelRows = entityManager
                       .createNativeQuery("SELECT * FROM PANELNEW WHERE CODICE_PAN = :cod_panel")
                       .setParameter("cod_panel", codPanel)
                       .getResultList();

               int panelIndex = 0;
               for (Object[] prow : panelRows) {
                   Map<String, String> panelMap = new LinkedHashMap<>();
                   for (int j = 0; j < prow.length; j++) {
                       panelMap.put(String.valueOf(j + 1), Objects.toString(prow[j], ""));
                   }
                   promSection.put("PANELNEW" + panelIndex, panelMap);
                   panelIndex++;
               }
           }

           // --- Costruisci struttura finale come nel tuo formato ---
           Map<String, Object> outerJson = new LinkedHashMap<>();
           outerJson.put("PROM", promSection);
           outerJson.put("TESSERA", tessera);
           outerJson.put("INIZIATIVA", descrizione);

           try {
               String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outerJson);
               promoJsonList.add(json);
               System.out.println(json);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       // --- Opzionale: salva i JSON in cache, file, o restituiscili tramite un getter ---
       this.lastPromoJsonList = promoJsonList; // campo del service, se vuoi accedervi altrove

       return promos; // <-- mantiene la compatibilità con la tua view
   }*/
   @Transactional("marketnewstoredbTransactionManager")
   public List<PromoDto> getActivePromotionsForTessera(String tessera) {
       StoredProcedureQuery query = entityManager
               .createStoredProcedureQuery("GETPROM_ATTIVE_CLIENTE");

       query.registerStoredProcedureParameter("cod_tessera", String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter("cod_articolo", String.class, ParameterMode.IN);

       query.setParameter("cod_tessera", tessera);
       query.setParameter("cod_articolo", "");

       query.execute();

       @SuppressWarnings("unchecked")
       List<Object[]> results = query.getResultList();

       List<PromoDto> promos = new ArrayList<>();
       List<String> promoJsonList = new ArrayList<>(); // JSON pronti per QR code

       ObjectMapper mapper = new ObjectMapper();

       for (Object[] row : results) {
           // --- Popola DTO (per UI) ---
           String dataIniz = Objects.toString(row[14], "");
           String dataFin = Objects.toString(row[13], "");
           String descr = Objects.toString(row[17], "");
           String numeroOff = Objects.toString(row[5], "");

           // --- Costruisci JSON "PROM" ---
           Map<String, Object> promSection = new LinkedHashMap<>();
           for (int i = 0; i < row.length; i++) {
               promSection.put(String.valueOf(i + 1), Objects.toString(row[i], ""));
           }

           // --- Dati di contesto ---
           String tipoAppl = Objects.toString(row[64], "");
           String codPanel = Objects.toString(row[9], "");
           String descrizione = descr;

           // --- Se tipo_appl = 4, aggiungi blocchi PANELNEWx ---
           if ("4".equals(tipoAppl) && !codPanel.isEmpty()) {
               List<Object[]> panelRows = entityManager
                       .createNativeQuery("SELECT * FROM PANELNEW WHERE CODICE_PAN = :cod_panel")
                       .setParameter("cod_panel", codPanel)
                       .getResultList();

               int panelIndex = 0;
               for (Object[] prow : panelRows) {
                   Map<String, String> panelMap = new LinkedHashMap<>();
                   for (int j = 0; j < prow.length; j++) {
                       panelMap.put(String.valueOf(j + 1), Objects.toString(prow[j], ""));
                   }
                   promSection.put("PANELNEW" + panelIndex, panelMap);
                   panelIndex++;
               }
           }

           // --- Costruisci struttura JSON finale ---
           Map<String, Object> outerJson = new LinkedHashMap<>();
           outerJson.put("PROM", promSection);
           outerJson.put("TESSERA", tessera);
           outerJson.put("INIZIATIVA", descrizione);

           String jsonData = "";
           try {
               jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outerJson);
               promoJsonList.add(jsonData);
               System.out.println(jsonData);
           } catch (Exception e) {
               e.printStackTrace();
           }

           // --- Crea DTO con jsonData ---
           PromoDto dto = new PromoDto(numeroOff, descr, dataIniz, dataFin);
           dto.setJsonData(jsonData);
           promos.add(dto);
       }

       // --- Opzionale: salva i JSON in cache o file ---
       this.lastPromoJsonList = promoJsonList;

       return promos;
   }


}
