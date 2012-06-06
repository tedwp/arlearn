package org.celstec.arlearn2.upload;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.generalitems.CreateGeneralItems;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;

import com.google.gdata.util.AuthenticationException;

public class GameUnpacker {

	private GamePackage gamePackage;
	private String auth;
	private Game createdGame;
	private List<GeneralItem> generalItems;
	private HashMap<Long, Long> identifierMapping = new HashMap<Long, Long>();
	private List<GeneralItem> manualItems; 

	public GameUnpacker(GamePackage arlPackage, String auth) {
		this.gamePackage = arlPackage;
		this.auth = auth;
	}

	public void unpack() {
		try {
			GameCreationStatus status = new GameCreationStatus();
			UsersDelegator qu = new UsersDelegator(auth);
			String myAccount = qu.getCurrentUserAccount();
			
			createGame(true);
			status.setGameId(createdGame.getGameId());
			status.setStatus(GameCreationStatus.GAME_CREATED);
			ChannelNotificator.getInstance().notify(myAccount, status);
			if (createdGame == null)
				return;
			updateGameId();
			insertGeneralItems(true);
			updateIdentifiers();
			status.setStatus(GameCreationStatus.IDENTIFIERS_UPDATED);
			ChannelNotificator.getInstance().notify(myAccount, status);
			insertGeneralItems(false);
			updateManualItems();
			status.setStatus(GameCreationStatus.PROCESSED_MANUAL_ITEMS);
			ChannelNotificator.getInstance().notify(myAccount, status);
			createGame(false);
			status.setStatus(100);
			ChannelNotificator.getInstance().notify(myAccount, status);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}

	private void updateManualItems() {
		Game game = gamePackage.getGame();
		Config config = game.getConfig();
		if (manualItems == null) return;
		for (GeneralItem generalItem : manualItems) {
			generalItem.setId(identifierMapping.get(generalItem.getId()));
		}
		game.getConfig().setManualItems(manualItems);
	}

	private void createGame(boolean resetId) throws AuthenticationException {
		Game game = gamePackage.getGame();
		if (game != null) {
			GameDelegator gd = new GameDelegator(auth);
			if (resetId) {
				game.setGameId(null);
				if (game.getConfig() != null && game.getConfig().getManualItems() != null) {
					manualItems = game.getConfig().getManualItems();
					game.getConfig().setManualItems(null);
				}
			}
			createdGame = gd.createGame(game);
		}
	}

	private void updateGameId() {
		generalItems = gamePackage.getGeneralItems();
		for (GeneralItem gi : generalItems) {
			gi.setGameId(createdGame.getGameId());
		}
	}

	private void insertGeneralItems(boolean resetId)
			throws AuthenticationException {
		CreateGeneralItems cr = new CreateGeneralItems(auth);
		for (GeneralItem generalItem : generalItems) {
			generalItem.getScope();
			Long oldId = generalItem.getId();
			if (resetId)
				generalItem.setId(null);
			Long newId = cr.createGeneralItem(generalItem).getId();
			if (resetId)
				identifierMapping.put(oldId, newId);

		}
	}

	private void updateIdentifiers() {
		for (GeneralItem generalItem : generalItems) {
			updateIdentifiers(generalItem.getDependsOn());
		}
	}

	private void updateIdentifiers(Dependency dep) {
		if (dep instanceof ActionDependency) {
			ActionDependency ad = (ActionDependency) dep;
			if (identifierMapping.containsKey(ad.getGeneralItemId()))
				ad.setGeneralItemId(identifierMapping.get(ad.getGeneralItemId()));
		}
		if (dep instanceof TimeDependency) {
			TimeDependency td = (TimeDependency) dep;
			if (td.getOffset() != null)
				updateIdentifiers(td.getOffset());
		}

		if (dep instanceof BooleanDependency) {
			BooleanDependency ad = (BooleanDependency) dep;
			for (Dependency d : ad.getDependencies()) {
				updateIdentifiers(d);
			}
		}
	}

}