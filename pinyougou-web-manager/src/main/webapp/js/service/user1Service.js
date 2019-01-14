//服务层
app.service('user1Service',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../user/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage.do?page='+page+'&rows='+rows);
	}

    this.search = function(page,rows,searchEntity){
        return $http.post("../user/search.do?pageNum="+page+"&pageSize="+rows,searchEntity);
    }

    this.findUserbyStatus = function(){
        return $http.get("../user/findUserbyStatus.do");
    }
    this.blockUser = function(ids){
        return $http.get("../user/blockUser.do?ids="+ids);
    }

});
