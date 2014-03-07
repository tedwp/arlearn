package org.celstec.arlearn2.android.db;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
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
public class ConfigAdapter {
    private Context context;
    private Properties properties;

    public ConfigAdapter(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties() {
        try {
            AssetManager assetManager = context.getAssets();

            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);

        } catch (IOException e) {
            Log.e("ConfigAdapter",e.toString());
        }
        return properties;

    }

}