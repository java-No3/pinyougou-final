 //控制层 
app.controller('testController' ,function($scope,$controller,$location,testService){

    $scope.test1 = null;
    $scope.test2 = null;
    var testData1 = [];

    $controller('baseController',{$scope:$scope});//继承
	

	

	$scope.findUserbyStatus=function(){
		testService.findUserbyStatus().success(
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
	

	





});	
