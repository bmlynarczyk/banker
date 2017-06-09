(function () {
  'use strict';

  angular.module('BlurAdmin.pages.import', [])
      .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('import', {
          url: '/import',
          title: 'Import',
          templateUrl: 'app/pages/import/import.html',
          controller: 'ImportPageCtrl',
          sidebarMeta: {
            icon: 'ion-compose',
            order: 200,
          }
        });
  }

})();
