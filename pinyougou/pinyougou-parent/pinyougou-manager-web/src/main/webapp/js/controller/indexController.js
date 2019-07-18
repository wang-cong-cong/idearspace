app.controller('indexController',function ($scope, loginService) {
    $scope.indexName = function () {
        loginService.loginName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }
})