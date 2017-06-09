(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account')
    .controller('AddAccountPageCtrl', AddAccountPageCtrl)
    .controller('AccountPageCtrl', AccountPageCtrl);

  /** @ngInject */
  function AccountPageCtrl($rootScope, $scope, $uibModal, $http) {

    $scope.accounts = [];

    var modalScope = $rootScope.$new();

    $http.get('http://localhost:8080/accounts').then(function(response) {
        $scope.accounts = response.data._embedded.accounts;
    });

    $scope.open = function() {
        modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/account/addAccount.html',
                size: 'md',
                controller: AddAccountPageCtrl,
                scope: modalScope
        });
        modalScope.modalInstance.result.then(function () {
            $http.get('http://localhost:8080/accounts').then(function(response) {
                $scope.accounts = response.data._embedded.accounts;
            });
        }, function () {
            // Dismissed
        });
    };
  }

  function AddAccountPageCtrl($scope, $http) {
    $scope.accountNumber;

    $scope.addAccount = function(){
        var data = {number: $scope.accountNumber, currentBalance: 0};
        $http.post("http://localhost:8080/accounts", data).then(function() {
            $scope.modalInstance.close();
        });
    }

  }

})();