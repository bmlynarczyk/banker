(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report')
    .controller('AccountReportPageCtrl', AccountReportPageCtrl);

  function AccountReportPageCtrl($scope, $http, $stateParams, accountReportChartFactory) {
    var params = { params: {periodStart: $stateParams.periodStart, periodStop: $stateParams.periodStop }}
    $http.get('/api/accounts/reports/' + $stateParams.account, params).then(function(response) {
        $scope.report = response.data;
        accountReportChartFactory.create($scope.report)
    });
  }

})();