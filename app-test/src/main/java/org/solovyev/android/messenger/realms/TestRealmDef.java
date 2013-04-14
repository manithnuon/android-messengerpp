package org.solovyev.android.messenger.realms;

import android.content.Context;
import android.widget.ImageView;
import com.google.inject.Singleton;
import org.solovyev.android.messenger.RealmConnection;
import org.solovyev.android.messenger.entities.Entity;
import org.solovyev.android.messenger.entities.EntityImpl;
import org.solovyev.android.messenger.icons.RealmIconService;
import org.solovyev.android.messenger.test.R;
import org.solovyev.android.messenger.users.User;
import org.solovyev.android.properties.AProperty;
import org.solovyev.common.security.Cipherer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@Singleton
public class TestRealmDef extends AbstractRealmDef {

    @Nonnull
    public static final String REALM_ID = "test";

    public TestRealmDef() {
        super(REALM_ID, R.string.mpp_test_realm_name, R.drawable.mpp_test_icon, TestRealmConfigurationFragment.class, TestRealmConfiguration.class, false);
    }

    @Nonnull
    public RealmConnection createRealmConnection(@Nonnull Context context, @Nonnull Realm realm) {
        return null;
    }

    @Nonnull
    public static Entity newEntity(@Nonnull String realmEntityId) {
       return EntityImpl.newInstance(REALM_ID, realmEntityId);
    }

    @Nonnull
    @Override
    public Realm newRealm(@Nonnull String realmId, @Nonnull User user, @Nonnull RealmConfiguration configuration, RealmState state) {
        return new TestRealm(realmId, this, user, (TestRealmConfiguration) configuration);
    }

    @Nonnull
    @Override
    public RealmBuilder newRealmBuilder(@Nonnull RealmConfiguration configuration, @Nullable Realm editedRealm) {
        return null;
    }

    @Nonnull
    @Override
    public List<AProperty> getUserProperties(@Nonnull User user, @Nonnull Context context) {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public RealmIconService getRealmIconService() {
        return new RealmIconService() {
            @Override
            public void setUserIcon(@Nonnull User user, @Nonnull ImageView imageView) {

            }

            @Override
            public void setUserPhoto(@Nonnull User user, @Nonnull ImageView imageView) {
            }

            @Override
            public void fetchUsersIcons(@Nonnull List<User> users) {
            }

            @Override
            public void setUsersIcon(@Nonnull List<User> users, @Nonnull ImageView imageView) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    @Nullable
    @Override
    public Cipherer<RealmConfiguration, RealmConfiguration> getCipherer() {
        return null;
    }

}
