package tekgenesis.authorization.social;

handler SocialHandler
    unrestricted
    on_route "/sg/social"
{
    "/login" : Html, login;
}