/**
 * ##### Angular Plugin #####
 *
 * Usage: <sui-switchuser>Switch User</sui-switchuser>
 * Usage: Welcome <sui-username></sui-username>
 * Usage: <sui-menu id="menu" position="left|top" fqn="domain.Some"></sui-menu>
 * Usage: <sui-formbox id="form" fqn="domain.Some" pk="{{pk}}" on-load="onLoad()" history="true" style="padding: 50px;"/>
 */
(function() {

    if (!window.angular) return;

    var module = angular.module('sui-forms', []);

    function directive(name, element, linkFn) {
        module.directive(name, function() { return {
            restrict: 'E',
            transclude: false,
            template: '<'+element+'></'+element+'>',
            replace: true,
            link: linkFn
        }});
    }

    directive('suiSwitchuser', 'li', function(scope, elem) { $(elem).switchUser(); });
    directive('suiLogout', 'li', function(scope, elem) { $(elem).logout(); });
    directive('suiChangeou', 'li', function(scope, elem) { $(elem).changeOu(); });
    directive('suiShortcuts', 'li', function(scope, elem) { $(elem).shortcuts(); });
    directive('suiFeedback', 'li', function(scope, elem) { $(elem).feedback(); });
    directive('suiFullscreen', 'li', function(scope, elem) { $(elem).fullscreen(); });
    directive('suiUserprofile', 'li', function(scope, elem) { $(elem).userProfile(); });
    directive('suiToggle', 'button', function(scope, elem) { $(elem).toggleMenu(); });

    module.directive('suiUsername', function() { return {
        restrict: 'E',
        transclude: false,
        scope: {
            picture : '&',
            username : '&'
        },
        link: function(scope, elem, attrs) {

            // init using jquery plugin passing options (convert to boolean taking default into account)
            $(elem).userName({
                picture : scope.picture(),
                username : scope.username()
            });
        }

    }});

    module.directive('suiMenu', function() { return {
        restrict: 'E',
        transclude: true,
        template: '<nav ng-transclude></nav>',
        replace: true,
        scope: {
            position : '@',
            fqn      : '@',
            hideRoot : '&',
            maxMenus : '@'
        },
        link: function(scope, elem, attrs) {

            // assign auto id if undefined
            if (!elem[0].id) elem[0].id = attrs.fqn || 'menu';

            // init using jquery plugin passing options (convert to boolean taking default into account)
            $(elem).menu({
                position : attrs.position,
                fqn      : attrs.fqn,
                hideRoot : scope.hideRoot(),
                maxMenus : attrs.maxMenus
            });
        }
    }});


    module.directive('suiFormbox', ['$timeout', function($timeout) {
        return {
            restrict: 'E',
            transclude: false,
            template: '<section></section>',
            replace: true,
            scope: {
                // id and pk
                fqn : '@',
                pk  : '@',
                // listeners
                listener : '&',
                //options
                focusOnLoad : '&',
                history     : '&',
                home        : '&',
                hideHeader  : '&',
                hideFooter  : '&'
            },
            link: function(scope, elem, attrs) {
                var formFqn, pk;
                var timeout; // Timeout promise (in need of load cancellations)
    
                // Assign auto id if undefined
                if (!elem[0].id) elem[0].id = attrs.fqn || 'form';
    
                // Init using jquery plugin extending options
                scope.form = $(elem).form({
                    focusOnLoad : scope.focusOnLoad(),
                    history     : scope.history(),
                    home        : scope.home(),
                    hideHeader  : scope.hideHeader(),
                    hideFooter  : scope.hideFooter()
                });
    
                // Add listeners
                scope.form.addListener(scope.listener());
    
                function load() { if (formFqn) { scope.form.load(formFqn, pk); } }
    
                // Schedule load in 10 millis, just to wait for the fqn and the pk to be both available
                // Save the timeout promise in need of future cancellations
                function scheduleLoad() {
                    cancelScheduledLoad();
                    timeout = $timeout(load, 10);
                }
    
                function cancelScheduledLoad() { $timeout.cancel(timeout); }
    
                // Watch the id and pk expression, and schedule a load on change.
                attrs.$observe('fqn', function(newFormFqn) {
                    formFqn = newFormFqn;
                    scheduleLoad();
                });
                attrs.$observe('pk', function(newPk) {
                    pk = newPk;
                    scheduleLoad();
                });
    
                // Listen on DOM destroy (removal) event, and cancel the next UI update
                // to prevent updating time ofter the DOM element was removed.
                elem.bind('$destroy', cancelScheduledLoad);
            }
        };
    }]);
})();