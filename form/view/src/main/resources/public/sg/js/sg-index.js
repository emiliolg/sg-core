var callback = function() {

    // change locale option
    var userOptions = document.getElementById("userOptions");

    var locales = userOptions.querySelectorAll('a[data-locale]');
    for (var i = 0; i < locales.length; i++) {
        var locale = locales[i];

        locale.addEventListener("click", function(e) {
            var request = new XMLHttpRequest();
            request.open('GET', '/sg/success?locale=' + e.target.getAttribute('data-locale'), true);
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
            request.send();

            request.onload = function() {
                if (this.status >= 200 && this.status < 400) {
                    location.reload();
                }
            };
        });
    }

    // change theme css and update menu selection
    function loadTheme(theme) {
        if (theme) {
            document.getElementById('theme').setAttribute('href', theme);
            localStorage.setItem('suigeneris/theme', theme);

            userOptions.querySelector('.active').setAttribute('class', '');
            userOptions.querySelector('[data-theme="' + theme + '"]').parentNode.setAttribute('class', 'active');
        }
    }

    // load the last stored theme
    loadTheme(localStorage.getItem('suigeneris/theme'));

    // change theme option
    var themeLinks = userOptions.querySelectorAll('a[data-theme]');

    for (var j = 0; j < themeLinks.length; j++) {
        // select theme option listener
        themeLinks[j].addEventListener("click", function(e) {
            loadTheme(e.target.getAttribute('data-theme'));
        });
    }
};

if (
    document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)
) {
    callback();
} else {
    document.addEventListener("DOMContentLoaded", callback);
}