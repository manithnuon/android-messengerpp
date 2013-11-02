package org.solovyev.android.messenger;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.TestRealm;
import org.solovyev.android.messenger.realms.vk.VkRealm;
import org.solovyev.android.messenger.realms.xmpp.XmppRealm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * User: serso
 * Date: 2/27/13
 * Time: 9:42 PM
 */
@Singleton
public class TestConfiguration implements Configuration {

	@Inject
	@Nonnull
	private TestRealm testRealm;

	@Inject
	@Nonnull
	private VkRealm vkRealm;

	@Inject
	@Nonnull
	private XmppRealm xmppRealm;

	@Nonnull
	@Override
	public Collection<Realm> getRealms() {
		return Arrays.<Realm>asList(testRealm, vkRealm, xmppRealm);
	}
}
