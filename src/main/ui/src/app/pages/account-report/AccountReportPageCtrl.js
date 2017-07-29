(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report')
    .controller('AccountReportPageCtrl', AccountReportPageCtrl);

  function AccountReportPageCtrl($scope, $http, $stateParams, accountBalanceChartFactory, categoryChartFactory) {
    var params = { params: {periodStart: $stateParams.periodStart, periodStop: $stateParams.periodStop }}
    $http.get('/api/accounts/reports/' + $stateParams.account, params).then(function(response) {
        $scope.report = response.data;
        accountBalanceChartFactory.create($scope.report.transfers, 'transferTypeChart')
        categoryChartFactory.create($scope.report.categoriesReport, 'categoryChart')
    });
  }

})();