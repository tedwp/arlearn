package org.celstec.arlearn2.mobileclient.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

    public static ImageResources INSTANCE = GWT.create(ImageResources.class);

   
    @Source("icon_maps.png")
    ImageResource listMap();
    
    @Source("list_icon.png")
    ImageResource list();
    
    @Source("multiple_choice.png")
    ImageResource multipleChoice();
    
    @Source("scanbarcodeicon.png")
    ImageResource scanBarcodeIcon();
}


