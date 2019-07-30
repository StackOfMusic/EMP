// <Pie 차트 그리기> 
// url: 원하는 통계 데이터의 controller url
// title: chart 제목
// id: html에서 차트가 들어갈 id
// 차트 객체를 반환함
function drawPieChart(data, title, id){
	var labels = [];
	var series = [];
	
/*	$.ajax({ 
		url : url, 
		async: false, 	
		success : 
		function(data, status, xhr) {
			for(key in data){
				labels.push(key);
				series.push(Number(data[key]));
			}
		}, 
		error: function(jqXHR, textStatus, errorThrown) { 
			alert(jqXHR.responseText); 
		} 
	})*/
	
	for(key in data){
		labels.push(key);
		series.push(Number(data[key]));
	}
	 var options = {
	    chart: {
	    	width: '100%',
	    	type: 'pie',
	    },
	    title: {text:title},
	    labels: labels,
	    series: series,
	    responsive: [{
		    breakpoint: 480,
		    options: {
		    	legend: {
		    		position: 'bottom'
		    	}
		   }
	    }]
	}
	
	var chart = new ApexCharts(
	    document.querySelector("#"+id),
	    options
	);
	chart.render();
	return chart;
}

//<Line 차트 그리기>
//url: 원하는 통계 데이터의 controller url
//title: chart 제목
//id: chart가 들어가야 할 html에서 id 태그 
//series_names: 값에 대한 label (array)
//차트 객체를 반환함
function drawLineChart(url, title, id, series_names){
	var xaxis = [];
	var series = [];
	var series_split = series_names.split(",");
	for(i=0; i<series_split.length; i++){
		var obj = {};
		obj["name"] = series_split[i];
		obj["data"] = [];
		series.push(obj);
	}
	$.ajax({ 
		url : url, 
		async: false, 	
		success : 
		function(data, status, xhr) {
			for(key in data){
				xaxis.push(key)
				for(i=0; i<data[key].length; i++){
					series[i]["data"].push(data[key][i]);
				}
			}
		}, 
		error: function(jqXHR, textStatus, errorThrown) { 
			alert(jqXHR.responseText); 
		} 
	})
	var options = {
        chart: {
            type: 'line',
            shadow: {
                enabled: true,
                color: '#000',
                top: 18,
                left: 7,
                blur: 10,
                opacity: 1
            },
            toolbar: {
                show: false
            }
        },
        dataLabels: {
            enabled: true,
        },
        stroke: {
            curve: 'straight'
        },
        series: series
        ,
        title: {
            text: title,
            align: 'left'
        },
        grid: {
            borderColor: '#e7e7e7',
            row: {
         //       colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                opacity: 0.5
            },
        },
        markers: {
            size: 6
        },
        xaxis: {
            categories: xaxis,
        },
        legend: {
            position: 'top',
            horizontalAlign: 'right',
            floating: true,
            offsetY: -25,
            offsetX: -5
        }
    }

    var chart = new ApexCharts(
        document.querySelector("#"+id),
        options
    );

    chart.render();
    return chart;
}

//<Bar 차트 그리기>
//url: 원하는 통계 데이터의 controller의 url 
//title: chart 제목
//id: chart가 들어가야 할 html에서 id 태그 
//series_names: 값에 대한 label (array)
//차트 객체를 반환함
function drawBarChart(url, title, id, series_names){
	var xaxis = [];
	var series = [];
	var series_split = series_names.split(",");
	for(i=0; i<series_split.length; i++){
		var obj = {};
		obj["name"] = series_split[i];
		obj["data"] = [];
		series.push(obj);
	}
	$.ajax({ 
		url : url, 
		async: false, 	
		success : 
		function(data, status, xhr) {
			for(key in data){
				xaxis.push(key)
				for(i=0; i<data[key].length; i++){
					series[i]["data"].push(data[key][i]);
				}
			}
		}, 
		error: function(jqXHR, textStatus, errorThrown) { 
			alert(jqXHR.responseText); 
		} 
	})
	var options = {
        chart: {
            type: 'bar',
        },
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '55%'	
            },
        },
        dataLabels: {
            enabled: true
        },
        stroke: {
            show: true,
            width: 2,
           // colors: ['transparent']
        },
        series: series,
        xaxis: {
            categories: xaxis
        },
        fill: {
            opacity: 1
        },
        title: {
        	text: title
        }
    }

    var chart = new ApexCharts(
        document.querySelector("#"+id),
        options
    );

    chart.render();
    return chart;
}