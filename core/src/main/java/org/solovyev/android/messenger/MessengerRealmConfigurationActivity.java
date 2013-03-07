package org.solovyev.android.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.inject.Inject;
import org.solovyev.android.messenger.core.R;
import org.solovyev.android.messenger.realms.*;
import roboguice.event.EventListener;

import javax.annotation.Nonnull;

/**
 * Current activity can be used in two cases:
 * 1. Initial configuration of new realm
 * 2. Old realm editing
 *
 * List of parameters for different moded is different:
 * 1. Realm DEF id should be passed as an extra with id {@link MessengerRealmConfigurationActivity#EXTRA_REALM_DEF_ID} in intent
 * 2. Realm id should be passed as an extra with id {@link MessengerRealmConfigurationActivity#EXTRA_REALM_DEF_ID} in intent
 *
 * If no parameters are passed activity will be closed
 *
 *
 */
public class MessengerRealmConfigurationActivity extends MessengerFragmentActivity implements EventListener<RealmGuiEvent> {

    @Nonnull
    private static final String EXTRA_REALM_DEF_ID = "realm_def_id";

    @Nonnull
    private static final String EXTRA_REALM_ID = "realm_id";

    @Inject
    @Nonnull
    private RealmService realmService;

    public MessengerRealmConfigurationActivity() {
        super(R.layout.msg_main, false, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if (intent != null) {
            final String realmDefId = intent.getStringExtra(EXTRA_REALM_DEF_ID);
            final String realmId = intent.getStringExtra(EXTRA_REALM_ID);
            if (realmId != null) {
                final Realm realm = realmService.getRealmById(realmId);
                prepareUiForEdit(realm);
            } else if (realmDefId != null) {
                final RealmDef realmDef = realmService.getRealmDefById(realmDefId);
                prepareUiForCreate(realmDef);
            } else {
                finish();
            }
        }

         getEventManager().registerObserver(RealmGuiEvent.class, this);
    }

    private void prepareUiForCreate(@Nonnull RealmDef realmDef) {
        getFragmentService().setFirstFragment(realmDef.getConfigurationFragmentClass(), null, new RealmDefFragmentReuseCondition(realmDef), BaseRealmConfigurationFragment.FRAGMENT_TAG);

    }

    private void prepareUiForEdit(@Nonnull Realm realm) {
        final Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString(BaseRealmConfigurationFragment.EXTRA_REALM_ID, realm.getId());
        getFragmentService().setFirstFragment(realm.getRealmDef().getConfigurationFragmentClass(), null, null, BaseRealmConfigurationFragment.FRAGMENT_TAG);
    }

    @Override
    protected void onDestroy() {
        getEventManager().unregisterObserver(RealmGuiEvent.class, this);

        super.onDestroy();
    }


    @Override
    public void onEvent(@Nonnull RealmGuiEvent event) {
        if (event.getType() == RealmGuiEventType.realm_edit_finished) {
            finish();
        }
    }

    /*
    **********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */

    public static void startForNewRealm(@Nonnull Context context, @Nonnull RealmDef realmDef) {
        final Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MessengerRealmConfigurationActivity.class);
        intent.putExtra(EXTRA_REALM_DEF_ID, realmDef.getId());
        context.startActivity(intent);
    }

    public static void startForEditRealm(@Nonnull Context context, @Nonnull Realm realm) {
        final Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MessengerRealmConfigurationActivity.class);
        intent.putExtra(EXTRA_REALM_ID, realm.getId());
        context.startActivity(intent);
    }
}
