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

    //展示当前账号的地址信息
    $scope.findAddressList = function () {
        cartListService.findAddressList().success(
            function (response) {
                $scope.addressList = response;
                for (var i = 0; i <$scope.addressList.length; i++) {
                    if ($scope.addressList[i].isDefault == '1'){
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }
            }
        );
    }

    //定一个方法接受页面选中的地址赋值给上下文一个变量
    $scope.selectedAddress = function (address) {
        $scope.address = address;
    };

    //定义一个方法判断页面传回的地址是否和上下文的地址一致
    $scope.isSelectedAddress = function (address) {
        if (address == $scope.address) {
            return true
        }else{
            return false;
        }
    };

    //定义一个订单对象，属性为支付方式，默认值为1
    $scope.order = {paymentType:'1'};

    //定一个方法通过页面传过来的1或者2
    $scope.selectedPayType = function (type) {
        $scope.order.paymentType = type;
    };
    
    $scope.submitOrder = function () {

        $scope.order.receiverAreaName = $scope.order.address;
        $scope.order.receiverMobile = $scope.order.mobile;
        $scope.order.receiver = $scope.order.contact;
        cartListService.submitOrder($scope.order).success(
          function (response) {
              if (response.success){
                  if ($scope.order.paymentType == '1'){
                      location.href = "pay.html";
                  }else{
                      location.href = "paysuccess.html";
                  }
              } else{
                  alert(response.message)
              }
          }
        );
    }
});