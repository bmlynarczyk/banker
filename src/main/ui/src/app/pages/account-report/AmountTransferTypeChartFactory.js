angular.module('BlurAdmin.pages.account-report')
    .service('accountBalanceChartFactory', AccountBalanceChartFactory);

function AccountBalanceChartFactory(baConfig, layoutPaths, dayBalanceExtractor){

    this.create = function(transfers, chartId){
        var layoutColors = baConfig.colors;
        var lineChart = AmCharts.makeChart(chartId,{
          type: 'serial',
          theme: 'light',
          color: layoutColors.defaultText,
          dataProvider: dayBalanceExtractor.extract(transfers),
          valueAxes: [
            {
              axisAlpha: 0,
              position: 'left',
              gridAlpha: 0.5,
              gridColor: layoutColors.border,
            }
          ],
          graphs: [
            {
              id: 'g1',
              balloonText: '[[balance]]',
              bullet: 'round',
              bulletSize: 8,
              lineColor: layoutColors.danger,
              lineThickness: 1,
              negativeLineColor: layoutColors.warning,
              type: 'smoothedLine',
              valueField: 'balance'
            }
          ],
          chartCursor: {
            categoryBalloonDateFormat: 'YYYY-MM-DD',
            cursorAlpha: 0,
            valueLineEnabled: true,
            valueLineBalloonEnabled: true,
            valueLineAlpha: 0.5,
            fullWidth: true
          },
          dataDateFormat: 'YYYY-MM-DD',
          categoryField: 'date',
          categoryAxis: {
            minPeriod: 'DD',
            parseDates: true,
            minorGridAlpha: 0.1,
            minorGridEnabled: true,
            gridAlpha: 0.5,
            gridColor: layoutColors.border,
          },
          pathToImages: layoutPaths.images.amChart
        });
    }

}