var waveChaserApp = angular.module('waveChaserApp', [
  'ngRoute',
  'waveChaserControllers'
]);

waveChaserApp.config(['$routeProvider', '$locationProvider',
  function($routeProvider, $locationProvider) {
	$locationProvider.html5Mode(true);
    $routeProvider.
      when('/ng_playground/portfolios', {
        templateUrl: '/ng_playground/templates/list.html',
        controller: 'ListController'
      }).
      when('/ng_playground/portfolios/:symbolId', {
        templateUrl: '/ng_playground/templates/detail.html',
        controller: 'DetailController'
      }).
      otherwise({
        redirectTo: '/ng_playground/portfolios'
      });
  }]);
