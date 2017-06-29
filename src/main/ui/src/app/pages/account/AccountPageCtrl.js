(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account')
    .controller('CustomAccountReportCtrl', CustomAccountReportCtrl)
    .controller('AddAccountPageCtrl', AddAccountPageCtrl)
    .controller('AccountPageCtrl', AccountPageCtrl);

  /** @ngInject */
  function AccountPageCtrl($rootScope, $scope, $uibModal, $http, $state) {

    $scope.accounts = [];

    var modalScope = $rootScope.$new();

    function formatAccountNumber(accountNumber){
        return accountNumber.substr(0, 2)
        + " " +
        accountNumber.substr(2, 4)
        + " " +
        accountNumber.substr(6, 4)
        + " " +
        accountNumber.substr(10, 4)
        + " " +
        accountNumber.substr(14, 4)
        + " " +
        accountNumber.substr(18, 4)
        + " " +
        accountNumber.substr(22, 4)
    }

    $http.get('/api/accounts').then(function(response) {
        $scope.accounts = response.data._embedded.accounts.map(function(account){
            return {number: account.number, formattedNumber: formatAccountNumber(account.number), currentBalance: account.currentBalance}
        });
    });



    $scope.openReport = function(accountNumber){
        var today = new Date()
        $state.go("account-report", {
            account: accountNumber,
            periodStart: today.getFullYear() + "-01-01",
            periodStop: today.getFullYear() + "-12-31"
        });
    }

    $scope.openAddAccount = function() {
        modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/account/addAccount.html',
                size: 'md',
                controller: AddAccountPageCtrl,
                scope: modalScope
        });
        modalScope.modalInstance.result.then(function () {
            $http.get('/api/accounts').then(function(response) {
                $scope.accounts = response.data._embedded.accounts;
            });
        }, function () {
            // Dismissed
        });
    };
    $scope.openCustomAccountReport = function(accountNumber) {
        modalScope.selectedAccountNumber = accountNumber;
        modalScope.state = $state;
        modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/account/customAccountReportDialog.html',
                size: 'md',
                controller: CustomAccountReportCtrl,
                scope: modalScope
        });
        modalScope.modalInstance.result.then(function () {
        }, function () {
        });
    };
  }

  function CustomAccountReportCtrl($scope) {

    $scope.periodStart;
    $scope.periodStop;

    $scope.openedPeriodStart = false;
    $scope.openedPeriodStop = false;

    $scope.openPeriodStart = function() {
        $scope.openedPeriodStart = true;
    }

    $scope.openPeriodStop = function() {
        $scope.openedPeriodStop = true;
    }

    $scope.openCustomAccountReport = function() {
        $scope.modalInstance.close();
        $scope.state.go("account-report", {
            account: $scope.selectedAccountNumber,
            periodStart: $scope.periodStart,
            periodStop: $scope.periodStop
        });
    }

  }

  function AddAccountPageCtrl($scope, $http) {
    $scope.accountNumber;

    $scope.selectedBank = {};

    $scope.banks = [];

    $http.get('/banks').then(function(response) {
        $scope.banks = response.data;
    });

    $scope.addAccount = function(){
        var data = {number: $scope.accountNumber, currentBalance: 0, bank: $scope.selectedBank.selected};
        $http.post("/api/accounts", data).
        success(function(data, status, headers, config) {
            $scope.modalInstance.close();
        }).
        error(function(data, status, headers, config) {
            console.log(data)
        });
    }

  }

})();