package com.company.ecomShop.view.articoliclassificazioni;

import com.company.ecomShop.entity.ArticoliClassificazioni;
import com.company.ecomShop.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.flowui.view.*;

import java.util.Collection;
import java.util.List;

@Route(value = "articoliClassificazionis", layout = MainView.class)
@ViewController(id = "ArticoliClassificazioni.list")
@ViewDescriptor(path = "articoli-classificazioni-list-view.xml")
@LookupComponent("articoliClassificazionisDataGrid")
@DialogMode(width = "50em")
public class ArticoliClassificazioniListView extends StandardListView<ArticoliClassificazioni> {

    @Install(to = "articoliClassificazionisDl", target = Target.DATA_LOADER)
    protected List<ArticoliClassificazioni> articoliClassificazionisDlLoadDelegate(LoadContext<ArticoliClassificazioni> loadContext) {
        // Here you can load entities from an external storage.
        // Set the loaded entities to the not-new state using EntityStates.setNew(entity, false).
        return List.of();
    }

    @Install(to = "articoliClassificazionisDataGrid.remove", subject = "delegate")
    private void articoliClassificazionisDataGridRemoveDelegate(final Collection<ArticoliClassificazioni> collection) {
        for (ArticoliClassificazioni entity : collection) {
            // Here you can remove entities from an external storage
        }
    }
}
