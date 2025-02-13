angular.module('postWithMultipleBodyHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for post method invocation on path '/products' */
            create : function(body, config) {
                return $http.post('/products', body, config);
             }
        };
     }]);

