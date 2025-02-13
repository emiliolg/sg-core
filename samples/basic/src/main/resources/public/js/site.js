// ######################### Controllers #########################

var controllers = angular.module('controllers', []);

controllers.controller('HomeCtrl', ['$scope', function ($scope) {
    $scope.random = 'Home random: ' + Math.random();
}]);

controllers.controller('ProductCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.product = { id: $routeParams.productId, description: $routeParams.productDescription, price: $routeParams.productPrice};

    $scope.randomMsg = 'Detail random: ' + Math.random();
    $scope.newRandomMsg = function() {
        $scope.randomMsg = 'Detail random: ' + Math.random();
    };
}]);


// ######################### APP #########################

var testApp = angular.module('Test', ['ng', 'sui', 'ngRoute', 'controllers']);

testApp.directive('productList', [function() {
    return {
        restrict: 'E',
        templateUrl: '/fragment/productList'
    };
}]);

testApp.config(['$routeProvider', function($routeProvider) {
    //$locationProvider.html5Mode(true)
    $routeProvider.
        when('/', {
            templateUrl: '/fragment/home',
            controller: 'HomeCtrl'
        }).
        when('/product/:productId/:productDescription/:productPrice', {
            templateUrl: '/fragment/product',
            controller: 'ProductCtrl'
        }).
        otherwise({
            redirectTo: '/'
        });
}]);

angular.element(document).ready(function() {
    angular.bootstrap(document, ['Test']);
});
