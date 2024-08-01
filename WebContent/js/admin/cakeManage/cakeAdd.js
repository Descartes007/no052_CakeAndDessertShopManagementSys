$(function(){
	var form=$("#cakeAddForm").Validform({
		tiptype:2,//validform初始化
	});
	
	form.addRule([
		{
			ele:"#cakeName",
		    datatype:"*2-20",
		    ajaxurl:"jsp/admin/CakeManageServlet?action=find",
		    nullmsg:"请输入商品名称！",
		    errormsg:"商品名称至少2个字符,最多20个字符！" 
		},
		{ 
			ele:"#catalog",
			datatype:"*",
			nullmsg:"请选择商品分类！",
			errormsg:"请选择商品分类！"
		},
		{
			ele:"#price",
			datatype:"/^[0-9]{1,}([.][0-9]{1,2})?$/",
			mullmsg:"价格不能为空！",
			errormsg:"价格只能为数字"
		},
		
		{
			ele:"#cakeImg", 
		    datatype:"*",
		    nullmsg:"请上传商品图片！",
		    errormsg:"请上传图书图片！"
		}
	
	]);
	
});

