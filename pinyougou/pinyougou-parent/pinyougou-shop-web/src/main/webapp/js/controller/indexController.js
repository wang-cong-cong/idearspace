app.controller("indexController",function ($scope,$controller,loginService) {

    $controller('base_Controller',{$scope:$scope});//继承
    //获取用户名
    $scope.findName = function () {
        loginService.getName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }
})