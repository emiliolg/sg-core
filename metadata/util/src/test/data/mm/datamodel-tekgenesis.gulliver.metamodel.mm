package tekgenesis.gulliver.metamodel;

enum AmenityType {
    SERVICE;
    HOTEL;
    ROOM;
}

entity City "City"
    primary_key code
    described_by name
{
    code              "Code"              : String(3);
    name              "Name"              : String(150);
    telephoneCityCode "TelephoneCityCode" : String(5), optional;
    stateProvince     "StateProvince"     : StateProvince;
}

form CityForm entity City;

entity CityMapping "CityMapping"
    primary_key provider, city
{
    provider         "Provider"         : Provider;
    city             "City"             : City;
    providerCityCode "ProviderCityCode" : String(6);
}

form CityMappingForm entity CityMapping;

entity Country "Country"
    primary_key code
    described_by name
{
    code   "Code"                : String(2);
    name   "Name"                : String(100);
    telephoneAccessCode
           "TelephoneAccessCode" : String(5), optional;
    source "Source"              : String(20);
}

form CountryForm entity Country;

entity CountryMapping "CountryMapping"
    primary_key provider, country
{
    provider "Provider"            : Provider;
    country  "Country"             : Country;
    providerCountryCode
             "ProviderCountryCode" : String(5);
}

entity Currency "Currency"
    primary_key code
    described_by name
{
    code "Code" : String(3);
    name "Name" : String(150);
}

entity CurrencyMapping "CurrencyMapping"
    primary_key provider, currency
{
    provider "Provider"             : Provider;
    currency "Currency"             : Currency;
    providerCurrencyCode
             "ProviderCurrencyCode" : String(5);
}

entity Hotel "Hotel"
    primary_key provider, code
    described_by description
{
    provider         "Provider"                : Provider;
    code             "Code"                    : String(5);
    name             "Name"                    : String(150);
    description      "Description"             : String(2000);
    shortLocation    "ShortLocation"           : String(50);
    longLocation     "LongLocation"            : String(250);
    longitude        "Longitude"               : String(150);
    latitude         "Latitude"                : String(150);
    addressLine      "AddressLine"             : String(150);
    streetNumber     "StreetNumber"            : Int;
    postalCode       "PostalCode"              : String(10);
    city             "City"                    : City;
    rating           "Rating"                  : Rating;
    creditCards      "CreditCards"             : String(30), optional;
    buildingConstructedYear
                     "BuildingConstructedYear" : String(4), optional;
    buildingRenovatedYear
                     "BuildingRenovatedYear"   : String(4), optional;
    totalRooms       "TotalRooms"              : Int, optional;
    totalFloors      "TotalFloors"             : Int, optional;
    checkInFromTime  "CheckInFromTime"         : String(5);
    checkInToTime    "CheckInToTime"           : String(5);
    checkOutFromTime "CheckOutFromTime"        : String(5);
    checkOutToTime   "CheckOutToTime"          : String(5);
    childrenFromAge  "ChildrenFromAge"         : String(2);
    childrenToAge    "ChildrenToAge"           : String(2);
}

entity HotelAmenity "HotelAmenity"
    primary_key hotel
{
    hotel              "Hotel"              : Hotel;
    amenityType        "AmenityType"        : AmenityType;
    amenityDescription "AmenityDescription" : String(250);
}

entity HotelContactNumber "HotelContactNumber"
    primary_key hotel
{
    hotel             "Hotel"             : Hotel;
    areaCityCode      "AreaCityCode"      : String(5);
    countryAccessCode "CountryAccessCode" : String(2);
    phoneNumber       "PhoneNumber"       : String(10);
    phoneExtension    "PhoneExtension"    : String(5);
    phoneType         "PhoneType"         : PhoneType;
}

entity HotelMultimediaDescription "HotelMultimediaDescription"
    primary_key hotel
    described_by description
{
    hotel              "Hotel"              : Hotel;
    multimediaType     "MultimediaType"     : MultimediaType;
    isMain             "IsMain"             : Int;
    multimediaCategory "MultimediaCategory" : MultimediaCategory;
    url                "Url"                : String(500);
    title              "Title"              : String(250);
    description        "Description"        : String(2000);
}

entity HotelRelativePosition "HotelRelativePosition"
    primary_key hotel
    described_by description
{
    hotel       "Hotel"       : Hotel;
    distance    "Distance"    : Int;
    providerUOM "ProviderUOM" : String(3);
    description "Description" : String(250);
}

entity HotelRoomType "HotelRoomType"
    primary_key hotel
{
    hotel        "Hotel"        : Hotel;
    roomTypeCode "RoomTypeCode" : String(10);
}

entity HotelRoomTypesAmenities "HotelRoomTypesAmenities"
    primary_key roomType
{
    roomType           "RoomType"           : HotelRoomType;
    amenityDescription "AmenityDescription" : String(250);
}

entity HotelType "HotelType"
    primary_key code
    described_by name
{
    code "Code" : String(3);
    name "Name" : String(50);
}

entity HotelTypeMapping "HotelTypeMapping"
    primary_key provider, hotelType
{
    provider  "Provider"              : Provider;
    hotelType "HotelType"             : HotelType;
    providerHotelTypeCode
              "ProviderHotelTypeCode" : String(5);
}

entity MultimediaCategory "MultimediaCategory"
    primary_key code
    described_by description
{
    code        "Code"        : String(5);
    description "Description" : String(2000);
}

enum MultimediaType {
    IMAGEITEM : "ImageItem";
    VIDEOITEM : "VideoItem";
}

enum PhoneType {
    FAX;
    MOBILE;
    BUSINESS;
    EMERGENCY;
    HOME;
}

entity Product "Product"
    primary_key code
    described_by name
{
    code "Code" : String(3);
    name "Name" : String(150);
}

entity ProductProvider "ProductProvider"
    primary_key product, provider
{
    product  "Product"  : Product;
    provider "Provider" : Provider;
}

entity Provider "Provider"
    primary_key code
    described_by name
{
    code "Code" : String(3);
    name "Name" : String(150);
}

entity ProviderProperty
    primary_key provider, property
{
    provider "Provider" : Provider;
    property "Property" : String(50);
    value    "Value"    : String(400);
}

entity Rating "Rating"
    primary_key code
    described_by description
{
    code        "Code"        : String(3);
    description "Description" : String(150);
}

entity RatingMapping "RatingMapping"
    primary_key provider, rating, providerRatingCode
    described_by provider, rating
{
    provider           "Provider"           : Provider;
    rating             "Rating"             : Rating;
    providerRatingCode "ProviderRatingCode" : String(5);
}

entity StateProvince "StateProvince"
    primary_key country, code
    described_by name
{
    code              "Code"              : String(3);
    country           "Country"           : Country;
    name              "Name"              : String(100);
    stateProvinceType "StateProvinceType" : StateProvinceType, optional;
}

entity StateProvinceType "StateProvinceType"
    primary_key code
    described_by name
{
    code "Code" : String(3);
    name "Name" : String(40);
}

entity UnitOfMeasure "UnitOfMeasure"
    primary_key code
    described_by description
{
    code        "Code"        : String(3);
    description "Description" : String(150);
}

entity UnitOfMeasureMapping "UnitOfMeasureMapping"
    primary_key provider, unitOfMeasure
{
    provider      "Provider"                  : Provider;
    unitOfMeasure "UnitOfMeasure"             : UnitOfMeasure;
    providerUnitOfMeasureCode
                  "ProviderUnitOfMeasureCode" : String(5);
}
