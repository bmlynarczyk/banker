angular.module('BlurAdmin.pages.account-report')
    .service('categoryChartFactory', CategoryChartFactory);

function CategoryChartFactory($rootScope, $uibModal, layoutPaths, CategoryColorFactory) {

    var yyyymmdd = function (date) {
        var mm = date.getMonth() + 1;
        var dd = date.getDate();

        return [date.getFullYear(),
            (mm > 9 ? '' : '0') + mm,
            (dd > 9 ? '' : '0') + dd
        ].join('-');
    };

    this.create = function (categoriesReport, chartId, periodStart, periodStop) {

        var graphs = categoriesReport.categories.map(function (category) {
            return {
                balloonText: "<b>[[title]]</b><br><span style='font-size:14px'>[[category]]: <b>[[value]]</b></span>",
                fillAlphas: 0.8,
                labelText: "[[value]]",
                lineAlpha: 0.3,
                fillColors: CategoryColorFactory.getColorForCategory(category),
                title: category.charAt(0).toUpperCase() + category.slice(1).replace("-", " "),
                type: "column",
                valueField: category
            };
        });

        var data = categoriesReport.amountsByMonth.map(function (period) {
            Object.keys(period).forEach(function (key) {
                if (key !== 'month')
                    period[key] = (period[key] * -1) / 1000;
            });
            period.date = new Date(period.month + '-01')
            return period;
        }).sort(function (a, b) {
            return a.date - b.date;
        });

        var showCategoryYearTransfersModal = function (clickLegendEvent) {
            var modalScope = $rootScope.$new();
            modalScope.category = clickLegendEvent.valueField;
            modalScope.start = periodStart;
            modalScope.stop = periodStop;
            modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/category-report/transfers-preview.html',
                size: 'lg',
                controller: CategoryTransfersReportCtrl,
                scope: modalScope
            });
            return false;
        };

        var showCategoryMonthTransfersModal = function (clickGraphItemEvent) {
            var month = clickGraphItemEvent.item.dataContext.month.split("-");
            var modalScope = $rootScope.$new();
            modalScope.category = clickGraphItemEvent.target.valueField;
            modalScope.start = clickGraphItemEvent.item.dataContext.month + "-01";
            modalScope.stop = yyyymmdd(new Date(month[0], month[1], 0));
            modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/category-report/transfers-preview.html',
                size: 'lg',
                controller: CategoryTransfersReportCtrl,
                scope: modalScope
            });
        };

        var showCategoriesMonthReportModal = function (clickAxisLabelEvent) {
            console.log(clickAxisLabelEvent);
            var modalScope = $rootScope.$new();
            modalScope.month = clickAxisLabelEvent.value;
            modalScope.modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/category-report/category-month-report.html',
                size: 'lg',
                controller: CategoryMonthReportCtrl,
                scope: modalScope
            });
        };

        var chart = AmCharts.makeChart(chartId, {
            type: "serial",
            theme: "none",
            legend: {
                horizontalGap: 10,
                maxColumns: 1,
                position: "right",
                useGraphSettings: true,
                clickLabel: showCategoryYearTransfersModal,
                clickMarker: showCategoryYearTransfersModal,
                markerSize: 10
            },
            dataProvider: data,
            valueAxes: [{
                stackType: "regular",
                axisAlpha: 0.3,
                gridAlpha: 0
            }],
            graphs: graphs,
            categoryField: "date",
            categoryAxis: {
                minPeriod: 'MM',
                parseDates: true,
                labelFunction: function (e, sd) {
                    var mm = sd.getMonth() + 1;
                    return sd.getFullYear() + "-" + (mm > 9 ? '' : '0') + mm;
                },
                gridPosition: "start",
                axisAlpha: 0,
                gridAlpha: 0,
                position: "left"
            },
            pathToImages: layoutPaths.images.amChart
        });

        chart.addListener("clickGraphItem", showCategoryMonthTransfersModal);
        chart.categoryAxis.addListener("clickItem", showCategoriesMonthReportModal);

    }
}

function CategoryTransfersReportCtrl($scope, $http, CategoryColorFactory) {

    $http.get("/api/transfers/search/findByCategoryAndDateBetweenOrderByDateDescDateTransferNumberDesc?category="
        + $scope.category + "&start=" + $scope.start + "&stop=" + $scope.stop).success(function (data) {
        $scope.transfers = data._embedded.transfers;
        var chartData = data._embedded.transfers.map(function (transfer) {
            return {value: -(transfer.amount / 1000), date: transfer.date};
        }).sort(function (a, b) {
            var dataPartA = a.date.split('-');
            var dataPartB = b.date.split('-');
            var dateA = new Date(dataPartB[0], dataPartB[1] - 1, dataPartB[2]);
            var dateB = new Date(dataPartA[0], dataPartA[1] - 1, dataPartA[2]);
            return dateB - dateA;
        });
        AmCharts.makeChart('transfersAmountChart', {
            "type": "serial",
            "theme": "light",
            "dataProvider": chartData,
            valueAxes: [{
                stackType: "regular",
                axisAlpha: 0.3,
                gridAlpha: 0
            }],
            "startDuration": 1,
            "graphs": [{
                "balloonText": "<b>[[category]]: [[value]]</b>",
                // "fillColorsField": "color",
                "fillColors": CategoryColorFactory.getColorForCategory($scope.category),
                "fillAlphas": 0.9,
                "fixedColumnWidth": 5,
                "lineAlpha": 0.2,
                "type": "column",
                "valueField": "value"
            }],
            "chartCursor": {
                "categoryBalloonEnabled": false,
                "cursorAlpha": 0,
                "zoomable": false
            },
            "categoryField": "date",
            "categoryAxis": {
                minPeriod: 'DD',
                parseDates: true,
                position: "left"
            }
        });
    }).error(function (data, status) {
        console.log(status)
        console.log(data)
    });

}

function CategoryMonthReportCtrl($scope, $http, CategoryColorFactory) {

    var yyyymmdd = function (date) {
        var mm = date.getMonth() + 1;
        var dd = date.getDate();

        return [date.getFullYear(),
            (mm > 9 ? '' : '0') + mm,
            (dd > 9 ? '' : '0') + dd
        ].join('-');
    };

    var month = $scope.month.split("-");
    $scope.periodStart = $scope.month + "-01";
    $scope.periodStop = yyyymmdd(new Date(month[0], month[1], 0));

    var params = {params: {periodStart: $scope.periodStart, periodStop: $scope.periodStop}};
    $http.get('/api/categories/reports/', params).then(function (response) {
        $scope.report = response.data;
        var data = [];
        $scope.report.amountsByMonth.forEach(function (period) {
            Object.keys(period).forEach(function (key) {
                if (key !== 'month') {
                    data.push({
                        category: key,
                        color: CategoryColorFactory.getColorForCategory(key),
                        value: (period[key] * -1) / 1000
                    });
                }
            });
        });
        data.sort(function (a, b) {
            return b.value - a.value;
        });
        AmCharts.makeChart('categoryMonthChart', {
            "type": "serial",
            "theme": "light",
            "dataProvider": data,
            "valueAxes": [{
                "axisAlpha": 0,
                "position": "left"
            }],
            "startDuration": 1,
            "graphs": [{
                "balloonText": "<b>[[category]]: [[value]]</b>",
                "fillColorsField": "color",
                "fillAlphas": 0.9,
                "lineAlpha": 0.2,
                "type": "column",
                "valueField": "value"
            }],
            "chartCursor": {
                "categoryBalloonEnabled": false,
                "cursorAlpha": 0,
                "zoomable": false
            },
            "categoryField": "category",
            "categoryAxis": {
                "gridPosition": "start",
                "labelRotation": 45
            }
        });
    });

}