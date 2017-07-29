(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report')
    .controller('AccountReportPageCtrl', AccountReportPageCtrl);

  function AccountReportPageCtrl($scope, $http, $stateParams, accountTransferTypeChartFactory, categoryChartFactory) {
    var params = { params: {periodStart: $stateParams.periodStart, periodStop: $stateParams.periodStop }}
    $http.get('/api/accounts/reports/' + $stateParams.account, params).then(function(response) {
        $scope.report = response.data;
        accountTransferTypeChartFactory.create($scope.report)
        categoryChartFactory.create($scope.report.categoriesReport, 'categoryChart')
    });
  }

})();