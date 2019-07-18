//控制层
app.controller('goodsController', function ($scope, $controller, uploadService, goodsService,itemCatService,typeTemplateService) {

    $controller('base_Controller', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    };

    //添加
    $scope.add = function () {

        $scope.entity.goodsDesc.introduction = editor.html();

        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert("添加成功");
                    $scope.entity = {};
                    editor.html("");
                } else {
                    alert(response.message);
                }
            }
        );
    };


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };



    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message);
                }
            }
        );
    };


    $scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};

    //添加图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };

    //移除图片列表
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    };


    //读取一级分类列表
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        );
    };

    //读取二级分类列表
    $scope.$watch("entity.goods.category1Id",function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    });

    //读取三级分类列表
    $scope.$watch("entity.goods.category2Id",function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    });


    //读取模板列表
    $scope.$watch("entity.goods.category3Id",function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
            }
        );
    });

    //读取品牌列表
    $scope.$watch("entity.goods.typeTemplateId",function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                //模板对象
                $scope.typeTemplate = response;
                //品牌列表
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

                //扩展属性
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
            }
        );

        //查询规格列表
        typeTemplateService.findSpecList(newValue).success(
            function (response) {

                //[{"options":[{"id":98,"optionName":"移动3G","orders":1,"specId":27},{"id":99,"optionName":"移动4G","orders":2,"specId":27},{"id":100,"optionName":"联通3G","orders":3,"specId":27},{"id":101,"optionName":"联通4G","orders":4,"specId":27},{"id":112,"optionName":"电信3G","orders":5,"specId":27},{"id":113,"optionName":"电信4G","orders":6,"specId":27},{"id":114,"optionName":"移动2G","orders":7,"specId":27},{"id":115,"optionName":"联通2G","orders":8,"specId":27},{"id":116,"optionName":"电信2G","orders":9,"specId":27},{"id":117,"optionName":"双卡","orders":10,"specId":27}],"id":27,"text":"网络"},{"options":[{"id":118,"optionName":"16G","orders":1,"specId":32},{"id":119,"optionName":"32G","orders":2,"specId":32},{"id":120,"optionName":"64G","orders":3,"specId":32},{"id":121,"optionName":"128G","orders":4,"specId":32}],"id":32,"text":"机身内存"}]
                $scope.specList = response;
            }
        );

    });

    //entity.goodsDesc.specificationItems=[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}]



    $scope.updateSpecAttribute = function ($event,name, value) {

        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);

        if (object != null){
            if ($event.target.checked){
                //勾选就添加
                object.attributeValue.push(value);
            }else{//取消勾选就删除值
                object.attributeValue.splice(object.attributeValue.indexOf(value,1));
                //如果attributeValue中没有选项，则整条记录删除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object,1));
                }
            }
        }else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }

    };


    //创建sku列表
    $scope.createItemList = function () {
        //初始化定义itemList集合
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' } ];
        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i <items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    };

    //定义添加字段列值得方法
    addColumn = function (list, columnName, conlumnValues) {

        var newList = [];

        for (var i = 0; i <list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j <conlumnValues.length; j++) {
                //深克隆
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName]=conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

});	
