angular.module('postWithBodyHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for post method invocation on path '/product/$id' */
            create : function(id, body, config) {
                return $http.post('/product/' + id, body, config);
             }
        };
     }]);

