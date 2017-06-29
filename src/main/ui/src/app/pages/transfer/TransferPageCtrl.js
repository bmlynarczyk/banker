(function () {
  'use strict';

  angular.module('BlurAdmin.pages.transfer')
    .controller('TransferPageCtrl', TransferPageCtrl);

  /** @ngInject */
  function TransferPageCtrl($scope, $http) {

    $scope.smartTableData = [];

    $scope.callServer = function callServer(tableState) {

        $scope.isLoading = true;

        var start = tableState.pagination.start || 0;
        var number = 15;
        var page = 0;

        if(start != 0)
            page = start / number

        $http.get('api/transfers?page=' + page + '&size=' + number + '&sort=date,desc,dateTransferNumber,desc').then(function(response) {
            $scope.smartTableData = response.data._embedded.transfers;
            tableState.pagination.numberOfPages = response.data.page.totalPages;
            tableState.pagination.totalItemCount = response.data.page.totalElements;
            $scope.isLoading = false;
        });
      };
  }

})();