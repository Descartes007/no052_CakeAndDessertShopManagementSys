function addToCart(cakeId){
	$.ajax({
		url:"CartServlet?action=add",
		dataType:"json",
		async:true,
		data:{"cakeId":cakeId},
		type:"POST",
		success:function(data){
			$("#cart .num").html(data);
		}
			
	})
}



