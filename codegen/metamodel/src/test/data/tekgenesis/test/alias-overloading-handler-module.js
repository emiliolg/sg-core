angular.module('aliasOverloadingHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/$a' */
            target : function(a, config) {
                return $http.get('/' + a, config);
             },
            /** Promise for get method invocation on path '/$a/$b' */
            target : function(a, b, config) {
                return $http.get('/' + a + '/' + b, config);
             },
            /** Promise for get method invocation on path '/$a/$b/$c' */
            target : function(a, b, c, config) {
                return $http.get('/' + a + '/' + b + '/' + c, config);
             }
        };
     }]);

