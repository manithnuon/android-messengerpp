package org.solovyev.android.messenger.realms;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.solovyev.android.messenger.RealmConnection;
import org.solovyev.android.messenger.accounts.AbstractAccount;
import org.solovyev.android.messenger.accounts.AccountState;
import org.solovyev.android.messenger.chats.AccountChatService;
import org.solovyev.android.messenger.entities.EntityImpl;
import org.solovyev.android.messenger.users.AccountUserService;
import org.solovyev.android.messenger.users.User;
import org.solovyev.android.messenger.users.Users;

import javax.annotation.Nonnull;

@Singleton
public class TestAccount extends AbstractAccount<TestAccountConfiguration> {

	@Inject
	public TestAccount(@Nonnull TestRealmDef realmDef) {
		this(realmDef, 1);
	}

	public TestAccount(@Nonnull TestRealmDef realmDef, int index) {
		super(realmDef.getId() + "~" + index, realmDef, Users.newEmptyUser(EntityImpl.newInstance(realmDef.getId() + "~" + index, "user" + index)), new TestAccountConfiguration("test_field", 42), AccountState.enabled);
	}


	public TestAccount(@Nonnull String id, @Nonnull RealmDef realmDef, @Nonnull User user, @Nonnull TestAccountConfiguration configuration) {
		super(id, realmDef, user, configuration, AccountState.enabled);
	}

	@Nonnull
	@Override
	protected RealmConnection newRealmConnection0(@Nonnull Context context) {
		return new TestRealmConnection(this, context);
	}

	@Nonnull
	@Override
	public String getDisplayName(@Nonnull Context context) {
		return context.getString(getRealmDef().getNameResId());
	}

	@Nonnull
	@Override
	public AccountUserService getAccountUserService() {
		return new TestAccountService();
	}

	@Nonnull
	@Override
	public AccountChatService getAccountChatService() {
		return new TestAccountService();
	}
}