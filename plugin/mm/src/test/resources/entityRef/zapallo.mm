package cocina;

entity Zapallo "Zapallo"
    primary_key color
    described_by color
{
    color : String(50);
    tamanio: Int;
}
entity ZapallitosConHuevo "ZapallitosConHuevo"
{
    zapallo : Zapallo;
}

form ZapalloForm "ZapalloForm"
    entity Zapallo
{
    id    "Id"    : id, internal;
    color "Color" : color, text_area;
    horizontal, style "form-actions" {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

