angular.module('htmlRoutingHandlerWithParameters')
    .factory('services', ['$http', function($http) {
        return {
            /** Promise for get method invocation on path '/nothing' */
            nothing : function(config) {
                return $http.get('/nothing', config);
             },
            /** Promise for get method invocation on path '/home' */
            home : function(from, to, config) {
                config = angular.merge(config || {}, {
                    parameters : {
                        from : from,
                        to : to
                    }
                });
                return $http.get('/home', config);
             },
            /** Promise for get method invocation on path '/some/$id' */
            someId : function(id, scheme, type, config) {
                config = angular.merge(config || {}, {
                    parameters : {
                        scheme : scheme,
                        type : type
                    }
                });
                return $http.get('/some/' + id, config);
             },
            /** Promise for post method invocation on path '/product/$id' */
            create : function(id, body, a, b, c, d, config) {
                config = angular.merge(config || {}, {
                    parameters : {
                        a : a,
                        b : b,
                        c : c,
                        d : d
                    }
                });
                return $http.post('/product/' + id, body, config);
             },
            /** Promise for post method invocation on path '/returning/$id' */
            create : function(id, body, x, config) {
                config = angular.merge(config || {}, {
                    parameters : {
                        x : x
                    }
                });
                return $http.post('/returning/' + id, body, config);
             }
        };
     }]);

