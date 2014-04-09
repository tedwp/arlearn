package org.celstec.arlearn2.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.GameEvent;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.dao.gen.GameLocalObject;
import org.celstec.dao.gen.GameLocalObjectDao;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class GameFragment extends SherlockFragment {

    private Game game;
    private View gameView;

    public GameFragment(Game game) {
        this.game = game;
        ARL.games.syncGame(game.getGameId());
        ARL.eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ARL.eventBus.unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gameView = inflater.inflate(R.layout.store_game_overview, container, false);
        return gameView;
    }

    public void onEventMainThread(GameEvent event) {
        if (event.getGameId() == game.getGameId()) {
            GameLocalObject localObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(event.getGameId());
            ((TextView) gameView.findViewById(R.id.gameTitleId)).setText(localObject.getTitle());
        }
    }

    private void fillOutLayout() {
        GameLocalObject localObject = DaoConfiguration.getInstance().getGameLocalObjectDao().load(game.getGameId());
        ((TextView) gameView.findViewById(R.id.gameTitleId)).setText(game.getTitle());
        if (localObject != null) {
            ((TextView) gameView.findViewById(R.id.gameTitleId)).setText(localObject.getTitle());
//            localObject.get
            ((TextView) gameView.findViewById(R.id.gameDescriptionId)).setText(localObject.getDescription());
        }


    }
}
