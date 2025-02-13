package tekgenesis.showcase schema showcase;

enum Bands "Bands"
{
      THE_NATIONAL;
      HOMERO;
 }

entity ImageResource
    primary_key idKey
{
    idKey   : Int;
    img  : Resource, optional;
    imgs : entity ImageResources* {
        name : String;
        img : Resource, optional;
    };
}

form ImageForm "Image Form" on_load load entity ImageResource
{

    header {
        message(title);
    };
    idKey;
    imgBound "Entity field (Image)" : img, upload;
    imgEnum "Enum field" : Bands, image, default THE_NATIONAL,on_click imageClick;
    imgString "Image field with tooltip": image, tooltip "This is a tooltip shown over the image";
    imgMultiple "Multiple images" : image, multiple;
    imgResource "Resource field": Resource, image, optional;
    gallery "Images" : String, gallery;
    upy "Video only Upload" : upload, multiple;
    video "Video" : video, default "http://vimeo.com/27359051";
    video2 "Video2" : video;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };

}

form SimpleImageForm "Image Form" on_load load
    entity ImageResource
{

    header {
        message(title);
    };
    id : idKey, internal, optional;
    imgBound "Entity field" : img;
    imgEnum "Enum field" : Bands, image, default THE_NATIONAL;
    imgString "Image field with tooltip": image;
    imgMultiple "Multiple images" : image, multiple;
    imgResource "Resource field": Resource, image, optional;
    gallery "Images" : String, gallery;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };

}

form TableImageForm "Table Image Form"
    entity ImageResource
{
    header {
        message(title);
    };

    id "Id"   : idKey, optional;
    "Img"  : img, optional ;
    clear "Clear" : button, on_click clear;
    imgs "Imgs" : imgs, table(3) {
        imgsName "String" : name;
        imgsImg "Img" : img, optional;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, imgs), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, imgs);
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}
