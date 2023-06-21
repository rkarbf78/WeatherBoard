
function makeWidget(fcstTime, tmpData, rehData, pcpData, wsdData){
    
    // Counter Number
    $('.count').each(function () {
        $(this).prop('Counter',0).animate({
            Counter: $(this).text()
        }, {
            duration: 3000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

    //온도
    var ctx = document.getElementById( "TMPChart" );
    ctx.height = 70;
    var minValue = Math.min(...tmpData);
    var myChart = new Chart( ctx, {
        type: 'bar',
        data: {
            labels: fcstTime,
            datasets: [
                {
                    label: "My First dataset",
                    data: tmpData,
                    borderColor: "rgba(0, 123, 255, 0.9)",
                    //borderWidth: "0",
                    backgroundColor: "rgba(255,255,255,.3)"
                }
            ]
        },
        options: {
              maintainAspectRatio: true,
              legend: {
                display: false
            },
            scales: {
                xAxes: [{
                  display: true,
                  categoryPercentage: 1,
                  barPercentage: 0.8
                }],
                yAxes: [ {
                    display: false,
              	ticks: {
              		min:minValue - 1
              	},
                } ]
            }
        }
    } );

    //습도
    var ctx = document.getElementById( "REHChart" );
    ctx.height = 70;
    var minValue = Math.min(...rehData);
    var myChart = new Chart( ctx, {
        type: 'line',
        data: {
            labels: fcstTime,
            type: 'line',
            datasets: [ {
                data: rehData,
                label: 'Dataset',
                backgroundColor: 'rgba(255,255,255,.2)',
                borderColor: 'rgba(255,255,255,.55)',
            },]
        },
        options: {

            maintainAspectRatio: true,
            legend: {
                display: false
            },
            responsive: true,
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                 yAxes: [ {
                    display: false,
              	ticks: {
              		min:minValue - 5
              	},
                }]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    borderWidth: 2
                },
                point: {
                    radius: 0,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    } );

    //강수량
    var ctx = document.getElementById( "PCPChart" );
    ctx.height = 150;
    var myChart = new Chart( ctx, {
        type: 'line',
        data: {
            labels: fcstTime,
            type: 'line',
            datasets: [ {
                data: pcpData,
                label: 'Dataset',
                backgroundColor: 'transparent',
                borderColor: 'rgba(255,255,255,.55)',
            }, ]
        },
        options: {

            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    },
                }],
                yAxes: [ {
                    display:false,
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    } );


    //바람
    var ctx = document.getElementById( "WSDChart" );
    ctx.height = 150;
    var myChart = new Chart( ctx, {
        type: 'line',
        data: {
            labels: fcstTime,
            type: 'line',
            datasets: [ {
                data: wsdData,
                label: 'Dataset',
                backgroundColor: '#63c2de',
                borderColor: 'rgba(255,255,255,.55)',
            }, ]
        },
        options: {

            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    } );
}
    