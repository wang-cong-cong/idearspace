//控制层
app.controller('goodsController', function ($scope, $controller, $location,uploadService, goodsService,itemCatService,typeTemplateService) {

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
    $scope.findOne = function () {

        //获取参数值
        var id = $location.search()["id"];

        //判断id是否为null
        if (id == null) {
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //回显商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //回显图片
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                //回显扩展属性时只写下面这句回显不出来，
                // 是因为在添加商品的读取品牌列表这段代码中也要读取该数据会把数据覆盖掉，
                // 所以需要在那个地方判断一下即写if ($scope.location()['id'] == null) {}这个
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);

                //回显规格
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                //回显sku列表
                for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }
            }
        );
    };

    //保存
    $scope.save = function () {

        $scope.entity.goodsDesc.introduction = editor.html();

        var serviceObject;//服务层对象
        if ($scope.entity.goods.id!= null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    alert("保存成功");
                    location.href = 'goods.html';
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
                /*if ($scope.location()[id] != null){
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }*/
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
    };

    //商家商品审核状态的集合
    $scope.status=["未审核","已审核","已驳回","关闭"];

    //定义一个可以放分类列表的集合
    $scope.itemCatList = [];
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i <response.length; i++) {
                    $scope.itemCatList[response[i].id]=response[i].name;
                }
            }
        );
    };


    $scope.checkAttributeValue = function (specName,optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object =  $scope.searchObjectByKey(items,'attributeName',specName);
        
        if (object == null){
            return false;
        } else{

            if (object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }

});	
