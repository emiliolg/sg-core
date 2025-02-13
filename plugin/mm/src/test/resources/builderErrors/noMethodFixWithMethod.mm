package monumental;

entity ElBurri "Ortega"
  primary_key casaca
  described_by nombre
{
   casaca "casaca" : Int;
   nombre "Nombre" : String(30);
}

form ElBurriForm "El manso el form del Burri"
     entity ElBurri
{
  defensor "Paleta" : text_field ,on_change tiraCanio;
}
