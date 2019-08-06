app.controller("payController",function ($scope,$location, payService) {
   $scope.createNative = function () {
       payService.createNative().success(
           function (response) {
               //定义变量接收金额
               $scope.money = (response.total_fee/100).toFixed(2);
               //定义变量接收订单号
               $scope.out_trade_no = response.out_trade_no;

               //生成二维码
               var qr = new QRious({
                   element:document.getElementById("qrious"),
                   size:250,
                   value:response.code_url,
                   level:'H'
               });
               //调用查询
               queryPayStatus();
           }
       );
   };


   queryPayStatus = function () {
       payService.queryPayStatus($scope.out_trade_no).success(
           function (response) {
               if (response.success){
                   location.href = "paysuccess.html#?money="+$scope.money;
               } else{
                   if (response.message == "二维码超时"){
                       $scope.createNative();//重新调用生成二维码
                   }else{
                       location.href = "payfail.html";
                   }
               }
           }
       );
   };


   //获取金额
    $scope.getMoney = function () {
        return $location.search()['money'];
    }
});