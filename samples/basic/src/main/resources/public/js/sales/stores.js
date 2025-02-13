// create StoresApp module
angular.module('StoresApp', [])

/** 
 * Factory holding the available rest api services
 * (Generated using "gradle mmServices" /target/samples/basic/js/services/tekgenesis/sales/basic/store-service-handler-module.js)
 **/
.factory('services', ['$http', function($http) {
    return {
        getStore : function(key, config) {
            return $http.get('/service/store/'+key, config);
        }
    };
}])

/** StoreMap controller, depending on the 'services' factory */
.controller('StoresMap', ['$scope', 'services', function($scope, services) {

    // initially, the dialog is hidden
    $scope.showDialog = false;

    /** Open the form dialog */
    $scope.openDialog = function() {
        $scope.showDialog = true; 
    };

    /**
     * Fetch new store info and add it to the map
     * (the store could be received as a parameters but we fetch as an excuse to invoke a rest service)
     */ 
    $scope.storeCreated = function(key) {
        services.getStore(key).success(function (store) {
            addStoreMarker(store);
            $scope.stores.push(store);
            $scope.showDialog = false;
        });
    };

    /** Hide form dialog on cancel */
    $scope.storeCancelled = function() {
        $scope.showDialog = false;
    };

    // Create google map
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: {lat: -34.455113, lng: -58.86489}
    });

    // create a marker for every store
    for (var i = 0; i < $scope.stores.length; i++) {
        addStoreMarker($scope.stores[i], map);
    }

    /** Add a google point marker using the store position */
    function addStoreMarker(store) {
        var infowindow = new google.maps.InfoWindow({
            content: '<div>' +
                '<h2>' + store.name + '</h2>' +
                '<p>' + store.address + '</p>' +
            '</div>'
        });

        var marker = new google.maps.Marker({
            position: {lat: store.latitude, lng: store.longitude},
            map: map,
            title: store.name
        });

        marker.addListener('click', function () {
            infowindow.open(map, marker);
        });
    }
}]);


/** Method invoked by the StoreForm upon create */
function storeCreated(key) {
    var scope = $('#form').scope();
    scope.$apply(function() {
        scope.storeCreated(key);
    });
}

/** Method invoked by the StoreForm upon update */
function storeUpdated(key) {
    var scope = $('#form').scope();
    scope.$apply(function() {
        scope.storeUpdated(key);
    });
}

/** Method invoked by the StoreForm upon cancel */
function storeCancelled() {
    var scope = $('#form').scope();
    scope.$apply(function() {
        scope.storeCancelled();
    });
}
