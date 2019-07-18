
app.controller('base_Controller',function ($scope) {

    $scope.paginationConf = {
        totalItems: 10,
        currentPage: 1,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    //重新加载页面
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };


    //用户勾选的元素放置处
    $scope.selectIds = [];
    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var number = $scope.selectIds.indexOf(id);//找到要移除的元素位置
            $scope.selectIds.splice(number, 1);//根据位置移除元素
        }
    };

    $scope.formatJson = function (jsonStr,key) {
        var jsonArray = JSON.parse(jsonStr);
        var str = "";
        for (var i = 0; i <jsonArray.length; i++) {
            var json = jsonArray[i];
            var value = json[key];
            if (i >= jsonArray.length-1) {
                str += value;
            }else{
                str += value+",";
            }
        }
        return str;
    };

    //list集合中根据某key的值查询对象
    $scope.searchObjectByKey = function (list, key, keyValue) {
        for (var i = 0; i <list.length; i++) {
            if (list[i][key] == keyValue) {
                return list[i];
            }
        }
        return null;
    }
});

