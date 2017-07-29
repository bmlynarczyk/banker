angular.module('BlurAdmin.pages.account-report')
    .service('dayBalanceExtractor', DayBalanceExtractor);

function DayBalanceExtractor() {

    function toChartData(val){
        return { date: new Date(val[0]), balance: val[1] };
    }

    this.extract = function(transfers) {
        var balancePerDay = new Map();
        transfers.forEach(function(transfer){
            balancePerDay.set(transfer.date, transfer.balance / 1000)
        });
        return Array.from(balancePerDay).map(toChartData)
    }

}