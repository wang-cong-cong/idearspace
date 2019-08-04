 //控制层 
app.controller('itemController' ,function($scope,$http){	
	
	$scope.addNum = function(x){
		$scope.num = $scope.num+x;
		if($scope.num<1){
			$scope.num = 1;
		}
	};
	
	$scope.specificationItems={};//记录用户选择的规格
	
	//用户选择规格
	$scope.specification= function(key,value){
		$scope.specificationItems[key]=value;
		//用户选择规格后调用此方法读取sku
		searchSku();
	};
	//判断某个规格是否被选中	
	$scope.isSelected = function(key,value){
		return $scope.specificationItems[key]==value;
	}
    
	$scope.sku = {};
	//加载默认sku信息
	$scope.loadSku = function(){
		$scope.sku = skuList[0];
		$scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
	}
	
	//js判断内容是否相等
	matchObject = function(map1,map2){
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}
		};
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}
		};
		return true;
	}
	
	//查询sku
	searchSku = function(){
		for(var i = 0;i<skuList.length;i++){
			if(matchObject(skuList[i],$scope.specificationItems)){
				$scope.sku = skuList[i];
				return;
			}
		}
		//没有匹配时
		$scope.sku = {id:0,title:"----",price:0};
	}
	
	//添加到购物车
	$scope.addToCart = function(){
		//alert("skuid:"+$scope.sku.id);
		$http.get("http://localhost:9107/cart/addGoodToCart.do?itemId="+$scope.sku.id+"&num="+$scope.num,{"withCredentials":true}).success(
				function(response){
					if(response.success){
						location.href="http://localhost:9107/cart.html"
					}else{
						alert(response.message);
					}
				}
		);
	}
	
	
});	
