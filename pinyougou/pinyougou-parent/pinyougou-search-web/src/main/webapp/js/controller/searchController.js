app.controller("searchController",function ($scope,$location,searchService) {


    //定义搜索对象，用于手机页面用户点击的属性值收集
    $scope.searchMap = {'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};

    //搜索
    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);

        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                //调用方法
                bulidPageLabel();
            }
        );
    };

    //创建循环产生分页码的方法
    bulidPageLabel = function(){
        //构建分页栏集合保存页码
      $scope.pageLabel = [];
      //第一页码
      var firstPage = 1;
      //截止页码
      var lastPage = $scope.resultMap.totalPages;
      //前面有点
        $scope.firstDot = true;
      //后面有点
        $scope.lastDot = true;
      
      if ($scope.resultMap.totalPages > 5) {
          //显示前五页
          if ($scope.searchMap.pageNo <= 3) {
              lastPage = 5;
              $scope.firstDot =  false;
          }else if ($scope.searchMap.pageNo>= lastPage-2){
              //显示后五页
              firstPage = $scope.resultMap.totalPages-4;
              $scope.lastDot =  false;
          }else{
              firstPage =$scope.searchMap.pageNo - 2;
              lastPage = $scope.searchMap.pageNo + 2;
          }
      }else{
          //前面无点
          $scope.firstDot =  false;
          //后面无点
          $scope.lastDot =  false;
      }
      
      
        for (var i = firstPage; i <=lastPage ; i++) {
            $scope.pageLabel.push(i);
        }
    };



    //添加搜索选项
    $scope.addSearchItem = function (key,value) {
        if (key=='category' || key=='brand' || key=='price'){
            $scope.searchMap[key] = value;
        }else{
            $scope.searchMap.spec[key] = value;
        }
        $scope.search();//执行搜索
    };

    //撤销搜索选项
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key=='price') {
            $scope.searchMap[key] = "";
        }else{
         delete $scope.searchMap.spec[key];
        }
        $scope.search();//执行搜索
    };

    //页码查询
    $scope.queryByPage = function (page) {
        if (page < 1 || page>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo = page;
        $scope.search();
    };

    //判断当前页是否是第一页
    $scope.isFirstPage = function () {
        return $scope.searchMap.pageNo == 1;
    };
    //判断当前页是否是最后一页
    $scope.isLastPage = function () {
        return $scope.searchMap.pageNo == $scope.resultMap.totalPages;
    };

    //按照价格升序降序
    $scope.sortSearch = function (sortField,sort) {
        $scope.searchMap.sort=sort;
        $scope.searchMap.sortField = sortField;
        $scope.search();
    };

    //判断关键字是否包含品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i <$scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    };

    //接收首页传来的关键字
    $scope.loadKeywords = function () {
        $scope.searchMap.keywords = $location.search()["keywords"];
        $scope.search();
    }
});