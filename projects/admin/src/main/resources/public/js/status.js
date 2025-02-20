function openService(evt, serviceName) {
    // Declare all variables
    var i, tablinks;
    $("#current").load("status/content/"+encodeURIComponent(serviceName))

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the link that opened the tab
    evt.currentTarget.className += " active";
}
