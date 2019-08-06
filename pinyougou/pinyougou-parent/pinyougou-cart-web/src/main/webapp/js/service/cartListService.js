app.service("cartListService",function ($http) {

    //查询购物车列表
    this.showCart = function () {
        return $http.get("/cart/findCartList.do");
    };

    //增加购物车明细
    this.addCartList = function (itemId,num) {
        return $http.get("/cart/addGoodToCart.do?itemId="+itemId+"&num="+num);
    };

    //合计总金额和总商品数
    this.sum = function (cartList) {
        var totalValue = {totalNum:0,totalMoney:0};
        for (var i = 0; i <cartList.length; i++) {
            var cart = cartList[i];
            for (var j = 0; j <cart.orderItemList.length; j++) {
                var orderItem = cart.orderItemList[j];
                totalValue.totalNum += orderItem.num;
                totalValue.totalMoney += orderItem.totalFee;
            }
        }
        return totalValue;
    };

    //获取当前账号的地址展示地址信息
    this.findAddressList = function () {
        return $http.get("address/findAddressByUserId.do")
    };

    this.submitOrder = function (order) {
        return $http.post('order/add.do',order);
    };

    
});