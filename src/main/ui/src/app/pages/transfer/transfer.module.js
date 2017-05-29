(function () {
  'use strict';

  angular.module('BlurAdmin.pages.transfer', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('transfer', {
          url: '/transfer',
          title: 'Transfer',
          templateUrl: 'app/pages/transfer/transfer.html',
          controller: 'TransferPageCtrl',
          sidebarMeta: {
            icon: 'ion-compose',
            order: 200,
          }
        });
  }

})();
