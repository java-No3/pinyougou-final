//服务层
app.service('sellerService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../seller/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../seller/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../seller/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../seller/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../seller/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../seller/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../seller/search.do?page='+page+"&rows="+rows, searchEntity);
	}    
	
	this.updateStatus = function(sellerId,status){
		return $http.get('../seller/updateStatus.do?sellerId='+sellerId+"&status="+status);
	}

	// 导出excel szj
	this.exportExcel = function(){
		return $http.get('../seller/exportSellerInfoExcel.do');
	}

	// 导出excel 测试 szj
	this.exportExcel2 = function(){
		//return $http.get('../seller/exportSellerInfoExcel2.do');
        return $http({
            method: 'POST',
            url: '../seller/exportSellerInfoExcel.do',
            data: {fileName: name},
            responseType: 'arraybuffer'
        }).success(function (data, status, headers) {
            headers = headers();
            var contentType = headers['content-type'];
            var linkElement = document.createElement('a');
            try {
                var blob = new Blob([data], {type: contentType});
                var url = window.URL.createObjectURL(blob);
                linkElement.setAttribute('href', url);
                linkElement.setAttribute("download", name);
                var clickEvent = new MouseEvent("click", {
                    "view": window,
                    "bubbles": true,
                    "cancelable": false
                });
                linkElement.dispatchEvent(clickEvent);
            } catch (ex) {
                console.log(ex);
            }
        }).error(function (data) {
            console.log(data);
        });
	}

// 导出excel 测试 szj
    this.exportExcel3 = function() {
        $http({
            url: '../seller/exportSellerInfoExcel.do',
            method: "GET",
            // params: data,
            responseType: "blob"

        }).then(function (response, status, header, config, statusText) {
            // var fileName = response.headers("Content-disposition").split(";")[1].split("filename=")[1];
            // var fileNameUnicode = response.headers("Content-disposition").split("filename*=")[1];
            // if (fileNameUnicode) {//当存在 filename* 时，取filename* 并进行解码（为了解决中文乱码问题）
            //     fileName = decodeURIComponent(fileNameUnicode.split("''")[1]);
            // }
            var fileName ="卖家统计表";
            var blob = response.data;
            if ('msSaveOrOpenBlob' in navigator) {//IE导出
                window.navigator.msSaveOrOpenBlob(blob, fileName);
            }
            else {
                var reader = new FileReader();
                reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a表情href
                reader.onload = function (e) {
                    // 转换完成，创建一个a标签用于下载
                    var a = document.createElement('a');
                    a.download = fileName;
                    a.href = e.target.result;
                    $("body").append(a);
                    a.click();
                    $(a).remove();
                }
            }


        });
    }

});
