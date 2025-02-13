angular.module('typedHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/a/$id' */
            str : function(id, config) {
                return $http.get('/a/' + id, config);
             },
            /** Promise for get method invocation on path '/b/$id' */
            real : function(id, config) {
                return $http.get('/b/' + id, config);
             },
            /** Promise for get method invocation on path '/c/$id' */
            date : function(id, config) {
                return $http.get('/c/' + id, config);
             },
            /** Promise for get method invocation on path '/d/$id' */
            time : function(id, config) {
                return $http.get('/d/' + id, config);
             }
        };
     }]);

