	$('#btnSave').click(function(e) {
		alert('test');
		e.preventDefault();
		$("#form").submit();
	});
	
	$('#btnList').click(function(e) {
		e.preventDefault();
		location.href="/request/read";
	});
	
	$('#support_type li > a').on('click', function() {
	    // 버튼에 선택된 항목 텍스트 넣기 
	    $('#support_type_status').text($(this).text());
	        
	    // 선택된 항목 값(value) 얻기
	    alert($(this).attr('value'));
	});
	
	$('#exe_type li > a').on('click', function() {
	    // 버튼에 선택된 항목 텍스트 넣기 
	    $('#exe_type_status').text($(this).text());
	        
	    // 선택된 항목 값(value) 얻기
	    alert($(this).attr('value'));
	});
	
	$('#system_loc li > a').on('click', function() {
	    // 버튼에 선택된 항목 텍스트 넣기 
	    $('#system_loc_status').text($(this).text());
	        
	    // 선택된 항목 값(value) 얻기
	    alert($(this).attr('value'));
	});