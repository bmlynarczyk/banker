(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('category-report', {
          url: '/category-report',
          title: 'Category Report',
          templateUrl: 'app/pages/category-report/category-report.html',
          controller: 'CategoryReportPageCtrl',
          params: {
            periodStart: null,
            periodStop: null
          }
        });
  }

})();
