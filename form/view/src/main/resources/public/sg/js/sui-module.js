(function() {
    // HtmlUnit is missing trim function :(
    if (!String.prototype.trim) {
        String.prototype.trim = function () {
            var result = this;
            while (result.charAt(0) == " ") result = result.substring(1);
            while (result.charAt(result.length-1) == " ") result = result.substring(0, result.length-1);
            return result;
        };
    }

    var module = angular.module("sui", ["ng"]).directive('suiScope',
        function() {
            return {
                priority: 1001,
                compile: function() {
                    return {
                        pre: function(scope, element, attrs) {
                            var scopeId = attrs.suiScope;
                            var cache = window.suiScopesCache ? window.suiScopesCache[scopeId] : null;
                            angular.forEach(cache, function(value, key) { scope[key] = value; });
                        }
                    }
                }
    }});
})();
