// 定义服务层:
app.service("testService",function($http){
	this.findUserbyStatus = function(){
		return $http.get("../test/findUserbyStatus.do");
	}
	

});