app.controller('contentController',function ($scope, contentService) {

    //定义广告的集合
    $scope.contentList = [];

    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;
            }
        );
    };

    //接收关键字跳转页面
    $scope.search = function () {
      location.href = "http://localhost:9104/search.html#?keywords="+$scope.keywords;
    }
});