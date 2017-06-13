(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account', ['ui.select', 'ngSanitize'])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('account', {
          url: '/account',
          title: 'Accounts',
          templateUrl: 'app/pages/account/account.html',
          controller: 'AccountPageCtrl',
          sidebarMeta: {
            icon: 'ion-compose',
            order: 200,
          }
        });
  }

})();
