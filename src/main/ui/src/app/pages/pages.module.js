/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages', [
    'ui.router',

      'BlurAdmin.pages.account',
      'BlurAdmin.pages.import',
      'BlurAdmin.pages.account-report',
      'BlurAdmin.pages.category-report'
  ])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($urlRouterProvider) {
      $urlRouterProvider.otherwise('/account');
  }

})();
