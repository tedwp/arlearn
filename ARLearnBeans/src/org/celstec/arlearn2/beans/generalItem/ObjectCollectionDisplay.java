package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.NarratorItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.celstec.arlearn2.beans.serializer.json.NarratorItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class ObjectCollectionDisplay extends GeneralItem {

    private List<DisplayZone> displayZones;
    private String richText;

    public ObjectCollectionDisplay(){

    }

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public List<DisplayZone> getDisplayZones() {
        return displayZones;
    }

    public void setDisplayZones(List<DisplayZone> displayZones) {
        this.displayZones = displayZones;
    }

    public static class DisplayZone extends Bean {
        private List<DisplayObject> objects;
        private String title;
        public DisplayZone(){

        }

        public List<DisplayObject> getObjects() {
            return objects;
        }

        public void setObjects(List<DisplayObject> objects) {
            this.objects = objects;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class DisplayObject extends Bean {

        public Integer scope;

        private String imgUrl;

        private ActionDependency dependsOn;

        public DisplayObject() {

        }

        public Integer getScope() {
            return scope;
        }

        public void setScope(Integer scope) {
            this.scope = scope;
        }

        public ActionDependency getDependsOn() {
            return dependsOn;
        }

        public void setDependsOn(ActionDependency dependsOn) {
            this.dependsOn = dependsOn;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static GeneralItemSerializer objectCollectionDisplaySerializer = new GeneralItemSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            ObjectCollectionDisplay ou = (ObjectCollectionDisplay) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getDisplayZones() != null) returnObject.put("displayZones", ListSerializer.toJSON(ou.getDisplayZones()));
                if (ou.getRichText() != null) returnObject.put("richText", ou.getRichText());



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanSerializer displayZoneSerializer = new BeanSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            DisplayZone ou = (DisplayZone) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getObjects() != null) returnObject.put("objects", ListSerializer.toJSON(ou.getObjects()));
                if (ou.getTitle() != null) returnObject.put("title", ou.getTitle());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanSerializer displayObjectSerializer = new BeanSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            DisplayObject ou = (DisplayObject) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (ou.getScope() != null) returnObject.put("scope", ou.getScope());
                if (ou.getImgUrl() != null) returnObject.put("imgUrl", ou.getImgUrl());
                if (ou.getDependsOn() != null) returnObject.put("dependsOn", JsonBeanSerialiser.serialiseToJson(ou.getDependsOn()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static GeneralItemDeserializer objectCollectionDisplayDeserializer = new GeneralItemDeserializer(){

        @Override
        public ObjectCollectionDisplay toBean(JSONObject object) {
            ObjectCollectionDisplay gi = new ObjectCollectionDisplay();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            ObjectCollectionDisplay gi = (ObjectCollectionDisplay) genericBean;

            if (object.has("displayZones")) gi.setDisplayZones(ListDeserializer.toBean(object.getJSONArray("displayZones"), DisplayZone.class));
            if (object.has("richText")) gi.setRichText(object.getString("richText"));


        }

    };


    public static BeanDeserializer displayZoneDeserializer = new BeanDeserializer(){

        @Override
        public DisplayZone toBean(JSONObject object) {
            DisplayZone gi = new DisplayZone();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            DisplayZone gi = (DisplayZone) genericBean;

            if (object.has("objects")) gi.setObjects(ListDeserializer.toBean(object.getJSONArray("objects"), DisplayObject.class));
            if (object.has("title")) gi.setTitle(object.getString("title"));


        }

    };

    public static BeanDeserializer displayObjectDeserializer = new BeanDeserializer(){

        @Override
        public DisplayObject toBean(JSONObject object) {
            DisplayObject gi = new DisplayObject();
            try {
                initBean(object, gi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gi;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            DisplayObject gi = (DisplayObject) genericBean;
            if (object.has("scope")) gi.setScope(object.getInt("scope"));
            if (object.has("imgUrl")) gi.setImgUrl(object.getString("imgUrl"));
            if (object.has("dependsOn")) gi.setDependsOn((ActionDependency) JsonBeanDeserializer.deserialize(ActionDependency.class, object.getJSONObject("dependsOn")));


        }

    };

    public static void main(String[] args) throws  Exception{
        ObjectCollectionDisplay display = new ObjectCollectionDisplay();
        display.setDisplayZones(new ArrayList<DisplayZone>());
        DisplayZone zone1 = new DisplayZone();
        zone1.setTitle("Blauwe dingen");
        zone1.setObjects(new ArrayList<DisplayObject>());

        DisplayZone zone2 = new DisplayZone();
        zone2.setTitle("Rode dingen");
        zone2.setObjects(new ArrayList<DisplayObject>());


        DisplayObject dob = new DisplayObject();
        dob.setScope(Dependency.USER_SCOPE);
        dob.setDependsOn(new ActionDependency());
        dob.getDependsOn().setGeneralItemId(123l);


        zone1.getObjects().add(dob);
        zone1.getObjects().add((DisplayObject)JsonBeanDeserializer.deserialize(dob.toString()));

        display.getDisplayZones().add(zone1);
        display.getDisplayZones().add(zone2);
        System.out.println("display object "+display.toString());
        System.out.println("display object "+JsonBeanDeserializer.deserialize(display.toString()));

    }
}
