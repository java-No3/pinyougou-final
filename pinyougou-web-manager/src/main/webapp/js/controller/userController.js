 //控制层 
app.controller('userController' ,function($scope,$controller ,user1Service){
	
	$controller('baseController',{$scope:$scope});//继承

    // 查询一个:
    $scope.findById = function(id){
        user1Service.findOne(id).success(function(response){
            $scope.entity = response;
        });
    }
	
	//分页
	$scope.findPage=function(page,rows){
        user1Service.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	

	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){
        user1Service.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	// 显示状态
    $scope.status = ["正常用户","非正常用户"];

    $scope.itemCatList = [];


    $scope.test1 = null;
    $scope.test2 = null;
    var testData1 = [];

    $controller('baseController',{$scope:$scope});//继承




    $scope.findUserbyStatus=function(){
        user1Service.findUserbyStatus().success(
            function(response){

                testData1.push(response.nomal);
                testData1.push(response.unnomal);
                $scope.test1 = response.nomal;

                $scope.test2 = response.unnomal;
                // // 基于准备好的dom，初始化echarts实例
                // var myChart = echarts.init(document.getElementById('main'));
                //
                // var test = ["正常用户","冻结用户"];
                // var testdata = [];
                //
                //
                // // 指定图表的配置项和数据
                // var option = {
                //     title: {
                //         text: '用户统计'
                //     },
                //     tooltip: {},
                //     legend: {
                //         data:['数量']
                //     },
                //     xAxis: {
                //         data: test
                //     },
                //     yAxis: {},
                //     series: [{
                //         name: '数量',
                //         type: 'bar',
                //         data:  testData1
                //     }]
                // };
                // // 使用刚指定的配置项和数据显示图表。
                // myChart.setOption(option);



                // alert("正常"+$scope.test1);
                // alert("不正常"+$scope.test2);


                // 绘制图表。
                echarts.init(document.getElementById('main')).setOption({
                    series: {

                        type: 'pie',
                        data: [
                            {name: '正常用户', value:  $scope.test1},
                            {name: '冻结用户', value:  $scope.test2}

                        ]
                    }
                });

            }
        );
    }


    //冻结用户 szj
    $scope.blockUser=function(){
        user1Service.blockUser($scope.selectIds).success(
            function(response){
                if(response.flag){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }



});	
