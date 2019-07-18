app.controller('brandController',function ($scope,$controller,brandService) {

    //引入父类控制器(包含共有的)
    $controller("base_Controller",{$scope : $scope});

    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        )
    };



    //发送分页请求
    $scope.findPage = function (page,size) {
        brandService.findPage(page,size).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        )
    };

    //保存和修改
    $scope.save = function () {

        var Object = null;

        if ($scope.entity.id!=null){
            Object = brandService.update($scope.entity);
        }else{
            Object = brandService.add($scope.entity);
        }
        Object.success(
            function (response) {
                if (response.success){
                    $scope.reloadList();
                } else{
                    alert(response.message);
                }
            }
        )
    };

    //回显数据
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity=response;
            }
        )
    };

    //批量删除
    $scope.dele = function () {
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                }
            }
        )
    };


    //按照条件分页查询
    $scope.searchEntity = {};

    $scope.search = function(page,size){

        brandService.search(page,size,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        )
    }
});
