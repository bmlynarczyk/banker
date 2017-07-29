angular.module('BlurAdmin.pages.account-report')
    .service('categoryChartFactory', CategoryChartFactory);

function CategoryChartFactory(baConfig, layoutPaths, dayBalanceExtractor){

    this.create = function(categoriesReport, chartId){
        var layoutColors = baConfig.colors;
        function toTitle(string) {
            return string.charAt(0).toUpperCase() + string.slice(1).replace("-", " ");
        }
        var graphs = [];
        categoriesReport.categories.forEach(function(category){
            graphs.push({
                balloonText: "<b>[[title]]</b><br><span style='font-size:14px'>[[category]]: <b>[[value]]</b></span>",
                fillAlphas: 0.8,
                labelText: "[[value]]",
                lineAlpha: 0.3,
                title: toTitle(category),
                type: "column",
                valueField: category
            });
        });
        var data = categoriesReport.amountsByMonth.map(function(period){
            Object.keys(period).forEach(function(key) {
                if(key !== 'month')
                    period[key] = (period[key] * -1) / 1000;
            });
            period.date = new Date(period.month + '-01')
            return period;
        }).sort(function(a,b){
            return a.date - b.date;
        });
        var chart = AmCharts.makeChart(chartId, {
            type: "serial",
            theme: "none",
            legend: {
                horizontalGap: 10,
                maxColumns: 1,
                position: "right",
                useGraphSettings: true,
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
                gridPosition: "start",
                axisAlpha: 0,
                gridAlpha: 0,
                position: "left"
            },
            pathToImages: layoutPaths.images.amChart
        });
        chart.addListener("clickGraphItem", function(e){
            console.log(e.item.dataContext);
        });
    }
}