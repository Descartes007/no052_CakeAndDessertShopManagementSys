$.ajax({
	url:"ShopIndex",
	dataType:"json",
	async:true,
	data:{},
	type:"POST",
	success:function(data){
		datalist(data);
	}
		
})

function datalist(data){
	
	
	//推荐商品
	if(data.recCakes!=null){
		$.each(data.recCakes,function(i,n){ 
			var tag="<li class='col-md-3'><div class='list'>" +
			"<a href='cakedetail?cakeId="+n.cakeId+"'><img class='img-responsive' src='"+n.upLoadImg.imgSrc+"'/></a>"+
			"<div class='proinfo'><h2><a class='text-center' href='cakedetail?cakeId="+n.cakeId+"'>"+n.cakeName+"</a></h2>"+
			"<p><i>￥"+n.price+"</i><a class='btn btn-danger btn-xs' href='javascript:void(0)' onclick='addToCart("+n.cakeId+")' " +
				"data-toggle='modal' data-target='.bs-example-modal-sm'>加入购物车</a></p></div></div></li>";
			
			$("#recCakes ul").append(tag);
		})
	}
	
	//新增加的书
	if(data.newCakes!=null){
		$.each(data.newCakes,function(i,n){ 
			var tag="<li class='col-md-3'><div class='list'>" +
			"<a href='cakedetail?cakeId="+n.cakeId+"'><img class='img-responsive' src='"+n.upLoadImg.imgSrc+"'/></a>"+
			"<div class='proinfo'><h2><a class='text-center' href='cakedetail?cakeId="+n.cakeId+"'>"+n.cakeName+"</a></h2>"+
			"<p><i>￥"+n.price+"</i><a class='btn btn-danger btn-xs' href='javascript:void(0)' onclick='addToCart("+n.cakeId+")' " +
				"data-toggle='modal' data-target='.bs-example-modal-sm'>加入购物车</a></p></div></div></li>";
			
			$("#newCakes ul").append(tag);
			
		})
	}
	
	
}
