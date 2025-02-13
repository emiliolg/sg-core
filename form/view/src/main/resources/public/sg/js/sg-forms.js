window.sg = (function (sg) {

    sg.inject = {

        // execute first menus, then bootstrap (forms & other stuff in html tags) then standard ones.
        bootListeners: {
            menu: [],
            bootstrap: [],
            standard: []
        },
        bootState: 'loading',

        exec: function (fn, priority) {
            if (sg.inject.loaded()) {
                fn();
            } else {
                sg.inject.bootListeners[priority || 'standard'].push(fn);
            }
        },

        loaded: function () {
            return sg.inject.bootState === 'resolved';
        },

        resolve: function () {
            sg.inject.bootState = 'resolved';

            var listeners = this.bootListeners;
            Object.keys(listeners).forEach(function (priority) {
                listeners[priority].forEach(function(listener) { listener(); });
                listeners[priority] = [];
            });
        },

        menu: function (id, configuration) { /** To be injected by gwt. */ },

        box: function (id, configuration) { /** To be injected by gwt. */ },

        load: function (id, fqn, pk) { /** To be injected by gwt. */ },

        cancel: function (id) { /** To be injected by gwt. */ },

        detach: function (id) { /** To be injected by gwt. */ },

        focus: function (id) { /** To be injected by gwt. */ },

        addListener: function (id, listener) { /** To be injected by gwt. */ },

        removeListener: function (id, listener) { /** To be injected by gwt. */ },

        switchUser: function (id) { /** To be injected by gwt. */ },

        userName: function (id, configuration) { /** To be injected by gwt. */ },

        changeOu: function (id) { /** To be injected by gwt. */ },

        shortcuts: function (id) { /** To be injected by gwt. */ },

        feedback: function (id) { /** To be injected by gwt. */ },

        fullscreen: function (id) { /** To be injected by gwt. */ },

        userProfile: function (id) { /** To be injected by gwt. */ },

        logout: function (id) { /** To be injected by gwt. */ },

        toggleMenu: function (id) { /** To be injected by gwt. */ }

    };

    // extend target with source
    var complete = function (target, source) {
        target = target || {};
        if (source) {
            for (var prop in source) {
                if (source.hasOwnProperty(prop) && !target.hasOwnProperty(prop)) {
                    target[prop] = source[prop];
                }
            }
        }
        return target;
    };

    sg.switchUser = function (id) {
        sg.inject.exec(function () {
            sg.inject.switchUser(id);
        });
    };

    sg.changeOu = function (id) {
        sg.inject.exec(function () {
            sg.inject.changeOu(id);
        });
    };

    sg.shortcuts = function (id) {
        sg.inject.exec(function () {
            sg.inject.shortcuts(id);
        });
    };

    sg.feedback = function (id) {
        sg.inject.exec(function () {
            sg.inject.feedback(id);
        });
    };

    sg.logout = function (id) {
        sg.inject.exec(function () {
            sg.inject.logout(id);
        });
    };

    sg.fullscreen = function (id) {
        sg.inject.exec(function () {
            sg.inject.fullscreen(id);
        });
    };

    sg.toggleMenu = function (id) {
        sg.inject.exec(function () {
            sg.inject.toggleMenu(id);
        });
    };

    sg.userProfile = function (id) {
        sg.inject.exec(function () {
            sg.inject.userProfile(id);
        });
    };

    sg.userName = function (id, options) {

        complete(options, {
            picture: true,
            username: false
        });

        sg.inject.exec(function () {
            sg.inject.userName(id, options);
        });
    };

    sg.menu = function (id, options) {

        complete(options, {
            position: 'left',
            fqn: undefined,
            hideRoot: false,
            maxMenus: undefined
        });

        sg.inject.exec(function () {
            sg.inject.menu(id, options);
        }, 'menu');
    };

    sg.form = function (id, options) {

        options = complete(options, {
            focusOnLoad: true,
            history: false,
            home: true,
            hideHeader: false,
            hideFooter: false,
            actions: []
        });

        sg.inject.exec(function () {
            sg.inject.box(id, options);
        }, options._priority);

        return {
            load: function (fqn, pk) {
                sg.inject.exec(function () {
                    sg.inject.load(id, fqn, pk);
                });
            },

            cancel: function () {
                sg.inject.exec(function () {
                    sg.inject.cancel(id);
                });
            },

            detach: function () {
                sg.inject.exec(function () {
                    sg.inject.detach(id);
                });
            },

            focus: function () {
                sg.inject.exec(function () {
                    sg.inject.focus(id);
                });
            },

            addListener: function (listener) {
                sg.inject.exec(function () {
                    sg.inject.addListener(id, listener);
                });
            },

            removeListener: function (listener) {
                sg.inject.exec(function () {
                    sg.inject.removeListener(id, listener);
                });
            }
        }
    };


    (function bootstrap(sg) {
        var getSgConfig = function(elem) {
            var userConfig = elem.dataset ? elem.dataset.sgConfig : elem.getAttribute('data-sg-config');
            var result = (userConfig && JSON.parse(userConfig)) || {};
            result._priority = 'bootstrap';
            return result;
        };
        var getSgType = function(elem) {
            return elem.dataset ? elem.dataset.sgType : elem.getAttribute('data-sg-type');
        };

        var bootConfig = getSgConfig(document.querySelector('[data-sg-type="bootstrap"]'));
        if (bootConfig.locale) sg.version = window.__gwt_Locale = bootConfig.locale;
        if (bootConfig.userId) sg.userId = window.__gwt_UserId = bootConfig.userId;
        if (bootConfig.version) sg.version = bootConfig.version;

        var onLoad = function () {
            var sgTypes = document.querySelectorAll('[data-sg-type]');
            for (var i = 0; i < sgTypes.length; i++) {
                var elem = sgTypes[i];
                var type = getSgType(elem);
                if (type != 'bootstrap')
                    sg[type](elem.id, getSgConfig(elem));
            }
        };

        if (document.readyState === "complete" || (document.readyState !== "loading" && !document.documentElement.doScroll)) onLoad();
        // Use the handy event callback
        else if (document.addEventListener) document.addEventListener('DOMContentLoaded', onLoad, false);
        // Fallback to window.onload, that will always work
        else window.onload = onLoad;
    })(sg);


    return sg;
})(window.sg || {});

// once sg object ir ready, load sgforms app
document.write('<script src="/sgforms/sgforms.nocache.js" defer sync></script>');