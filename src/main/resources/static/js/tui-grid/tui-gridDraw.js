function drawGrid(url, id, columns){
	var dataset= [];
	$.ajax({ 
		url : url, 
		async: false, 	
		success : 
		function(data, status, xhr) {
			var i=0; 
			for(key in data){
				var obj={};
				obj["id"]=i;
				obj["str"] = key;
				obj["num"] = data[key]; 
				dataset.push(obj);
			}
		}, 
		error: function(jqXHR, textStatus, errorThrown) { 
			alert(jqXHR.responseText); 
		} 
	})
	
	const grid = new tui.Grid({
	    el: document.getElementById(id),
	    data: dataset,
	    scrollX: false,
	    scrollY: false,
	    columns: columns
	  });
	return grid;
};
	