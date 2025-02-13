/**
 * ##### JQuery Plugin #####
 *
 * Usage:
 *
 * var form = $('id').form();
 * form.load('domain.Some');
 * form.load('domain.Some', pk);
 *
 * The above but chained:
 *
 * $('id').form().load('domain.Some');
 * $('id').form().load('domain.Some', pk);
 */
(function ( $ ) {

    $.suigeneris = {

        boot: $.Deferred(),

        exec: function( fn ) {
            if(this.loaded()) {
                fn();
            } else {
                this.boot.then(fn);
            }
        },

        loaded: function() {
            return this.boot.state() == 'resolved';
        },

        resolve: function () {
            this.boot.resolve();
        },

        menu : function(id, configuration) { /** To be injected by gwt. */ },

        box : function(id, configuration) { /** To be injected by gwt. */ },

        load : function(id, fqn, pk) { /** To be injected by gwt. */ },

        cancel : function(id) { /** To be injected by gwt. */ },

        detach : function(id) { /** To be injected by gwt. */ },

        focus : function(id) { /** To be injected by gwt. */ },

        addListener : function(id, listener) { /** To be injected by gwt. */ },

        removeListener : function(id, listener) { /** To be injected by gwt. */ },

        switchUser : function(id) { /** To be injected by gwt. */ },

        userName : function(id, configuration) { /** To be injected by gwt. */ },

        changeOu : function(id) { /** To be injected by gwt. */ },

        shortcuts : function(id) { /** To be injected by gwt. */ },

        feedback : function(id) { /** To be injected by gwt. */ },

        fullscreen : function(id) { /** To be injected by gwt. */ },

        userProfile : function(id) { /** To be injected by gwt. */ },

        logout : function(id) { /** To be injected by gwt. */ },

        toggleMenu : function(id) { /** To be injected by gwt. */ }

    };

    function findOrCreateId(elem, defaultId) {
        var id = elem.attr('id');
        // Assign auto id if undefined
        if (!id) {
            id = defaultId;
            elem.attr('id', id);
        }
        return id;
    }

    $.fn.switchUser = function() {
        var id = findOrCreateId($(this), 'switchuser');
        $.suigeneris.exec(function() { $.suigeneris.switchUser(id); });
    };

    $.fn.changeOu = function() {
        var id = findOrCreateId($(this), 'changeou');
        $.suigeneris.exec(function() { $.suigeneris.changeOu(id); });
    };

    $.fn.shortcuts = function() {
        var id = findOrCreateId($(this), 'shortcuts');
        $.suigeneris.exec(function() { $.suigeneris.shortcuts(id); });
    };

    $.fn.feedback = function() {
        var id = findOrCreateId($(this), 'feedback');
        $.suigeneris.exec(function() { $.suigeneris.feedback(id); });
    };

    $.fn.logout = function() {
        var id = findOrCreateId($(this), 'logout');
        $.suigeneris.exec(function() { $.suigeneris.logout(id); });
    };

    $.fn.fullscreen = function() {
        var id = findOrCreateId($(this), 'fullscreen');
        $.suigeneris.exec(function() { $.suigeneris.fullscreen(id); });
    };

    $.fn.toggleMenu = function() {
        var id = findOrCreateId($(this), 'togglemenu');
        $.suigeneris.exec(function() { $.suigeneris.toggleMenu(id); });
    };

    $.fn.userProfile = function() {
        var id = findOrCreateId($(this), 'userprofile');
        $.suigeneris.exec(function() { $.suigeneris.userProfile(id); });
    };

    $.fn.userName = function(options) {

        var settings = $.extend ( {
            picture : true,
            username : false
        }, options);

        var id = findOrCreateId($(this), 'username');
        $.suigeneris.exec(function() { $.suigeneris.userName(id, settings); });
    };

    $.fn.menu = function( options ) {

        var settings = $.extend ( {
            position : 'left',
            fqn      : undefined,
            hideRoot : false,
            maxMenus : undefined
        }, options);

        var id = $(this).attr('id');

        $.suigeneris.exec(function() { $.suigeneris.menu(id, settings); });
    };

    $.fn.form = function( options ) {

        var settings = $.extend ( {
            focusOnLoad : true,
            history     : false,
            home        : true,
            hideHeader  : false,
            hideFooter  : false,
            actions     : []
        }, options);

        var id = $(this).attr('id');

        $.suigeneris.exec(function() { $.suigeneris.box(id, settings); });

        return {
            load : function(fqn, pk) {
                $.suigeneris.exec(function() { $.suigeneris.load(id, fqn, pk); });
            },

            cancel : function() {
                $.suigeneris.exec(function() { $.suigeneris.cancel(id); });
            },

            detach : function() {
                $.suigeneris.exec(function() { $.suigeneris.detach(id); });
            },

            focus : function() {
                $.suigeneris.exec(function() { $.suigeneris.focus(id); });
            },

            addListener : function(listener) {
                $.suigeneris.exec(function() { $.suigeneris.addListener(id, listener); });
            },

            removeListener : function(listener) {
                $.suigeneris.exec(function() { $.suigeneris.removeListener(id, listener); });
            }
        }
    };
})(jQuery);