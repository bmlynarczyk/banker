(function () {
  'use strict';

  angular.module('BlurAdmin.pages.import')
    .controller('ImportPageCtrl', ImportPageCtrl);

  /** @ngInject */
  function ImportPageCtrl($scope, $http) {
    $scope.accounts = [];

    $http.get('http://localhost:8080/accounts').then(function(response) {
      $scope.accounts = response.data._embedded.accounts.map(function(element){
        return element.number
      });
    });

    $scope.selectedAccount = {};

    $scope.importFilePath;

    $scope.import = function(){
        $http.post("http://localhost:8080/import?filePath=" + $scope.importFilePath + "&account=" + $scope.selectedAccount.selected).
        success(function(data, status, headers, config) {
            $scope.modalInstance.close();
        }).
        error(function(data, status, headers, config) {
            console.log(data)
        });
    }

  }

})();