angular.module('BlurAdmin.pages.account-report')
    .service('accountReportChartFactory', AccountReportChartFactory);

function AccountReportChartFactory(baConfig, layoutPaths, dayBalanceExtractor){

    this.create = function(report){
        var layoutColors = baConfig.colors;
        var lineChart = AmCharts.makeChart('lineChart', {
          type: 'serial',
          theme: 'light',
          color: layoutColors.defaultText,
          marginTop: 10,
          marginRight: 15,
          dataProvider: dayBalanceExtractor.extract(report.transfers),
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