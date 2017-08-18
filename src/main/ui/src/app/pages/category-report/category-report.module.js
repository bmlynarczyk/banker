(function () {
  'use strict';

  angular.module('BlurAdmin.pages.account-report', [])
      .config(routeConfig)
      .factory('CategoryColorFactory', CategoryColorFactory);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('category-report', {
          url: '/category-report',
            title: 'Categories Report',
          templateUrl: 'app/pages/category-report/category-report.html',
          controller: 'CategoryReportPageCtrl',
          params: {
            periodStart: null,
            periodStop: null
          }
        });
  }

    function CategoryColorFactory() {
        var categoryColors = new Map();
        categoryColors.set("bar", "#CC323A");
        categoryColors.set("biżuteria", "#EEC359");
        categoryColors.set("czynsz", "#956a3c");
        categoryColors.set("edukacja-kursy", "#FFFFFF");
        categoryColors.set("edukacja-studia", "#FFFFFF");
        categoryColors.set("elektronika", "#C7053D");
        categoryColors.set("gaz", "#F46B16");
        categoryColors.set("internet", "#2983a6");
        categoryColors.set("kawa", "#820015");
        categoryColors.set("kino", "#F5821E");
        categoryColors.set("komputer", "#9F76B4");
        categoryColors.set("kosmetyki", "#fe97ae");
        categoryColors.set("książka", "#392A25");
        categoryColors.set("księgowość", "#FCB131");
        categoryColors.set("meble", "#003399");
        categoryColors.set("mieszkanie", "#7E6055");
        categoryColors.set("obiad", "#dee9b5");
        categoryColors.set("paliwo", "#D81E05");
        categoryColors.set("pkp", "#013765");
        categoryColors.set("playstation", "#00AA9E");
        categoryColors.set("podatki", "#FFFFFF");
        categoryColors.set("podróże-bilety-galerie", "#FFFFFF");
        categoryColors.set("podróże-hotel", "#FFFFFF");
        categoryColors.set("pranie", "#5D5D55");
        categoryColors.set("prasa", "#FFFFFF");
        categoryColors.set("prąd", "#a7fffd");
        categoryColors.set("restauracja", "#93bbb7");
        categoryColors.set("rtv", "#DF0100");
        categoryColors.set("samochód", "#FFFFFF");
        categoryColors.set("samochód-remont", "#FFFFFF");
        categoryColors.set("sport", "#b78a56");
        categoryColors.set("spożywcze", "#82B437");
        categoryColors.set("usługi internetowe", "#1ED760");
        categoryColors.set("taxi", "#FFDB00");
        categoryColors.set("telefon", "#59398A");
        categoryColors.set("ubezpieczenie", "#026F58");
        categoryColors.set("ubrania", "#4172c6");
        categoryColors.set("uroda", "#ff22fd");
        categoryColors.set("wesele", "#FFFFFF");
        categoryColors.set("wynagrodzenie", "#FFFFFF");
        categoryColors.set("zakupy-multimedia", "#FFFFFF");
        categoryColors.set("zdrowie", "#9BD8EA");
        categoryColors.set("zus", "#026F58");

        return {
            getColorForCategory: function (category) {
                if (categoryColors.has(category))
                    return categoryColors.get(category);
                else
                    return "#FFFFFF";
            }
        }

    }

})();
