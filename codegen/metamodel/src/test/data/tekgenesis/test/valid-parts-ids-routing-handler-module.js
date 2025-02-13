angular.module('validPartsIdsRoutingHandler')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/a/$id' */
            someA : function(id, config) {
                return $http.get('/a/' + id, config);
             },
            /** Promise for get method invocation on path '/b/$id2' */
            someB : function(id2, config) {
                return $http.get('/b/' + id2, config);
             },
            /** Promise for get method invocation on path '/c/$id2and3' */
            someC : function(id2and3, config) {
                return $http.get('/c/' + id2and3, config);
             },
            /** Promise for get method invocation on path '/d/$iso2' */
            someD : function(iso2, config) {
                return $http.get('/d/' + iso2, config);
             }
        };
     }]);

