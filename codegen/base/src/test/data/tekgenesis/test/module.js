angular.module('productHandler', ['config'])
    .factory('services', ['$http', function($http) {
        return {
            search : function(query, limit) {
                return $http.get('/services/search', {
                    params : {
                        q : query,
                        l : limit
                    }
                });
             }
        };
     }]);

