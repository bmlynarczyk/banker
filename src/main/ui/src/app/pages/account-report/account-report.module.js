(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('account-report', {
          url: '/account-report',
          title: 'Account Report',
          templateUrl: 'app/pages/account-report/account-report.html',
          controller: 'AccountReportPageCtrl',
          params: {
            account: null,
            periodStart: null,
            periodStop: null
          }
        });
  }

})();
