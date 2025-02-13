angular.module('aliasHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/a/$id' */
            target : function(id, config) {
                return $http.get('/a/' + id, config);
             },
            /** Promise for post method invocation on path '/c/$id' */
            target : function(id, body, config) {
                return $http.post('/c/' + id, body, config);
             },
            /** Promise for get method invocation on path '/failure' */
            failure : function(config) {
                return $http.get('/failure', config);
             },
            /** Promise for post method invocation on path '/failure' */
            failure : function(body, config) {
                return $http.post('/failure', body, config);
             }
        };
     }]);

