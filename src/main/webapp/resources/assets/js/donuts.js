function makeDonut(pm10Data, pm25Data, o3Data){
	
	
	var ctx = document.getElementById( "PM10Chart" );
	ctx.height ='200';
	var myDoughnutChart = new Chart(ctx, {
    type: 'doughnut',
    data : {
    labels: ['미세먼지'],
     datasets: [ {
                data: pm10Data,
                label : '미세먼지',
                backgroundColor: ['#228B22'],
         		 borderColor: ['#228B22'],
         		 borderWidth: 1
            }, ]
            },
     options: {}
	});

	var ctx = document.getElementById( "PM25Chart" );
	ctx.height ='200';
	var myDoughnutChart = new Chart(ctx, {
    type: 'doughnut',
    data : {
    labels: ['초미세먼지'],
     datasets: [ {
                data: pm25Data,
                label : '초미세먼지',
                backgroundColor: ['#006400'],
         		 borderColor: ['#006400'],
         		 borderWidth: 1
            }, ]
            },
     options: {}
	});
	
	var ctx = document.getElementById( "O3Chart" );
	ctx.height ='200';
	var myDoughnutChart = new Chart(ctx, {
    type: 'doughnut',
    data : {
    labels: ['오존'],
     datasets: [ {
                data: o3Data,
                label : '오존',
                backgroundColor: ['#FF8C00'],
         		 borderColor: ['#FF8C00'],
         		 borderWidth: 1
            }, ]
            },
     options: {}
	});
}