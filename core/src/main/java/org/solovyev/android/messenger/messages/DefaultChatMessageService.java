package org.solovyev.android.messenger.messages;

import android.app.Application;
import android.widget.ImageView;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.time.DateTime;
import org.solovyev.android.http.ImageLoader;
import org.solovyev.android.messenger.accounts.Account;
import org.solovyev.android.messenger.accounts.AccountException;
import org.solovyev.android.messenger.accounts.AccountService;
import org.solovyev.android.messenger.accounts.UnsupportedAccountException;
import org.solovyev.android.messenger.chats.*;
import org.solovyev.android.messenger.entities.Entity;
import org.solovyev.android.messenger.users.PersistenceLock;
import org.solovyev.android.messenger.users.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import java.util.Arrays;
import java.util.List;

import static org.solovyev.android.messenger.accounts.AccountService.NO_ACCOUNT_ID;

/**
 * User: serso
 * Date: 6/11/12
 * Time: 7:50 PM
 */
@Singleton
public class DefaultChatMessageService implements ChatMessageService {

    /*
	**********************************************************************
    *
    *                           AUTO INJECTED FIELDS
    *
    **********************************************************************
    */

	@Inject
	@Nonnull
	private ImageLoader imageLoader;

	@Inject
	@Nonnull
	private AccountService accountService;

	@Inject
	@Nonnull
	private UserService userService;

	@Inject
	@Nonnull
	private ChatService chatService;

	@GuardedBy("lock")
	@Inject
	@Nonnull
	private ChatMessageDao chatMessageDao;

	@Inject
	@Nonnull
	private Application context;

	@Nonnull
	private final PersistenceLock lock;

	@Inject
	public DefaultChatMessageService(@Nonnull PersistenceLock lock) {
		this.lock = lock;
	}

	@Override
	public void init() {
	}

	@Nonnull
	@Override
	public List<ChatMessage> getChatMessages(@Nonnull Entity accountChat) {
		// todo serso: think about lock
		/*synchronized (lock) {*/
			return chatMessageDao.readMessages(accountChat.getEntityId());
		/*}*/
	}

	@Override
	public void setMessageIcon(@Nonnull ChatMessage message, @Nonnull ImageView imageView) {
		final Entity author = message.getAuthor();
		userService.setUserIcon(userService.getUserById(author), imageView);
	}


	@Nullable
	@Override
	public ChatMessage sendChatMessage(@Nonnull Entity user, @Nonnull Chat chat, @Nonnull ChatMessage chatMessage) throws AccountException {
		final Account account = getRealmByUser(user);
		final AccountChatService accountChatService = account.getAccountChatService();

		final String accountMessageId = accountChatService.sendChatMessage(chat, chatMessage);

		final MessageImpl message = MessageImpl.newInstance(account.newMessageEntity(accountMessageId == null ? NO_ACCOUNT_ID : accountMessageId, chatMessage.getEntity().getEntityId()));

		message.setAuthor(user);
		if (chat.isPrivate()) {
			final Entity secondUser = chat.getSecondUser();
			message.setRecipient(secondUser);
		}
		message.setBody(chatMessage.getBody());
		message.setTitle(chatMessage.getTitle());
		message.setSendDate(DateTime.now());

		// user's message is read (he is an author)
		final ChatMessageImpl result = Messages.newMessage(message, true);
		for (Message fwtMessage : chatMessage.getFwdMessages()) {
			result.addFwdMessage(fwtMessage);
		}

		result.setDirection(MessageDirection.out);

		if (account.getRealm().notifySentMessagesImmediately()) {
			chatService.saveChatMessages(chat.getEntity(), Arrays.asList(result), false);
		}

		return result;
	}

	@Override
	public int getUnreadMessagesCount() {
		synchronized (lock) {
			return this.chatMessageDao.getUnreadMessagesCount();
		}
	}

	@Nonnull
	private Account getRealmByUser(@Nonnull Entity userEntity) throws UnsupportedAccountException {
		return accountService.getAccountById(userEntity.getAccountId());
	}
}
