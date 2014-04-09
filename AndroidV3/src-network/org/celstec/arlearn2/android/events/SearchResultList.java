package org.celstec.arlearn2.android.events;

import org.celstec.arlearn2.beans.game.GamesList;

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
public class SearchResultList {

    private GamesList gamesList;

    public SearchResultList(GamesList gamesList) {
        this.gamesList = gamesList;
    }

    public GamesList getGamesList() {
        return gamesList;
    }

    public void setGamesList(GamesList gamesList) {
        this.gamesList = gamesList;
    }
}
