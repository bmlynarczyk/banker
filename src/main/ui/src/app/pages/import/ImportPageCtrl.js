(function () {
  'use strict';

  angular.module('BlurAdmin.pages.import')
    .controller('ImportPageCtrl', ImportPageCtrl);

  /** @ngInject */
  function ImportPageCtrl($scope, $http) {
    $scope.accounts = [];

    $http.get('/api/accounts').then(function(response) {
      $scope.accounts = response.data._embedded.accounts.map(function(element){
        return element.number
      });
    });

    $scope.selectedAccount = {};

    $scope.importFilePath;

    $scope.categoriesMappingFilePath;

    $scope.import = function(){

        var url = "/api/import?filePath=" + $scope.importFilePath +
              "&account=" + $scope.selectedAccount.selected;

        if($scope.categoriesMappingFilePath)
            url = url + "&categoriesMappingPath=" + $scope.categoriesMappingFilePath;

        $http.post(url).
        success(function(data, status, headers, config) {
            $scope.modalInstance.close();
        }).
        error(function(data, status, headers, config) {
            console.log(data)
        });
    }

  }

})();