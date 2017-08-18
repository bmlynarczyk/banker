(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report')
    .controller('CategoryReportPageCtrl', CategoryReportPageCtrl);

  function CategoryReportPageCtrl($scope, $http, $stateParams, categoryChartFactory) {
      $scope.periodStart = $stateParams.periodStart;
      $scope.periodStop = $stateParams.periodStop;
      var params = {params: {periodStart: $scope.periodStart, periodStop: $scope.periodStop}};
    $http.get('/api/categories/reports/', params).then(function(response) {
        $scope.report = response.data;
        categoryChartFactory.create($scope.report, 'categoryChart', $scope.periodStart, $scope.periodStop);
    });
  }

})();