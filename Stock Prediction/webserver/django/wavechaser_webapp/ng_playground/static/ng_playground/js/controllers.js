var waveChaserControllers = angular.module('waveChaserControllers', []);

waveChaserControllers.controller('ListController', ['$scope', '$http',
  function ($scope, $http) {
      $http.get('ng_playground/api/portfolios/').success(function(data) {
        $scope.portfolios = data;
      });
  }]);
  
waveChaserControllers.controller('DetailController', ['$scope', '$routeParams', '$http',
  function($scope, $routeParams, $http) {
      $http.get('ng_playground/api/portfolios/' + $routeParams.symbolId).success(function(data) {
        $scope.portfolio = data;
      });
  }]);