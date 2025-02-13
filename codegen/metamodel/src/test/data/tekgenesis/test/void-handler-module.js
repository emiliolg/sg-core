angular.module('voidHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for post method invocation on path '/a/$id' */
            str : function(id, config) {
                return $http.post('/a/' + id, config);
             }
        };
     }]);

