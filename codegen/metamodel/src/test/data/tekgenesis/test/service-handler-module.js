angular.module('serviceHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/' */
            all : function(config) {
                return $http.get('/', config);
             },
            /** Promise for post method invocation on path '/' */
            create : function(config) {
                return $http.post('/', config);
             },
            /** Promise for get method invocation on path '/$id' */
            get : function(id, config) {
                return $http.get('/' + id, config);
             },
            /** Promise for post method invocation on path '/$id' */
            update : function(id, config) {
                return $http.post('/' + id, config);
             },
            /** Promise for get method invocation on path '/list/$id' */
            list : function(id, config) {
                return $http.get('/list/' + id, config);
             }
        };
     }]);

