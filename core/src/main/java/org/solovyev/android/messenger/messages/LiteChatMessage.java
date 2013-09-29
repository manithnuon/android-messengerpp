package org.solovyev.android.messenger.messages;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.solovyev.android.messenger.MessengerEntity;
import org.solovyev.android.messenger.entities.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:58 PM
 */
public interface LiteChatMessage extends MessengerEntity {

	@Nonnull
	Entity getEntity();

	@Nonnull
	Entity getAuthor();

	@Nullable
	Entity getRecipient();

	boolean isPrivate();

	@Nullable
	Entity getSecondUser(@Nonnull Entity user);

	@Nonnull
	DateTime getSendDate();

	@Nonnull
	DateTime getLocalSendDateTime();

	@Nonnull
	LocalDate getLocalSendDate();

	@Nonnull
	String getTitle();

	@Nonnull
	String getBody();

	@Nonnull
	LiteChatMessage clone();
}
