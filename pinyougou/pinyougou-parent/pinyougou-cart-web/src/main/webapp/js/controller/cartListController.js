app.controller("cartListController",function ($scope, cartListService) {

    $scope.totalValue = null;
    //查询购物车列表
    $scope.showCartList = function () {
        cartListService.showCart().success(
            function (response) {
                $scope.cartList = response;
                $scope.totalValue = cartListService.sum(response);
            }
        );
    };

    //添加购物车明细
    $scope.addCartList = function (itemId, num) {
        cartListService.addCartList(itemId,num).success(
            function (response) {
                if (response) {
                    $scope.showCartList();
                }else{
                    alert(response.message);
                }
            }
        );
    }
});