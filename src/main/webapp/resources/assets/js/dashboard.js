const brandSuccess = "#4dbd74";
const brandInfo = "#63c2de";
const brandDanger = "#f86c6b";
const brandBlack = "#000000	";
const brandWhite = "#ffffff";

const wsd_e = new Image();
const wsd_se = new Image();
const wsd_s = new Image();
const wsd_ws = new Image();
const wsd_w = new Image();
const wsd_nw = new Image();
const wsd_n = new Image();
const wsd_ne = new Image();
wsd_e.src = "/resources/images/weatherIcons/wd-e.png";
wsd_se.src = "/resources/images/weatherIcons/wd-se.png";
wsd_s.src = "/resources/images/weatherIcons/wd-s.png";
wsd_ws.src = "/resources/images/weatherIcons/wd-ws.png";
wsd_w.src = "/resources/images/weatherIcons/wd-w.png";
wsd_nw.src = "/resources/images/weatherIcons/wd-nw.png";
wsd_n.src = "/resources/images/weatherIcons/wd-n.png";
wsd_ne.src = "/resources/images/weatherIcons/wd-ne.png";

const sun = new Image();
const moon = new Image();
const cloud = new Image();
const rain = new Image();
const shower = new Image();
const snow = new Image();
const snowORrain = new Image();
const thunder = new Image();
const wind = new Image();
const hail = new Image();
const sun_cloud = new Image();
const moon_cloud = new Image();
const thunder_cloud = new Image();
sun.src = "/resources/images/weatherIcons/sun.png";
moon.src = "/resources/images/weatherIcons/moon.png";
cloud.src = "/resources/images/weatherIcons/cloud.png";
rain.src = "/resources/images/weatherIcons/rain.png";
shower.src = "/resources/images/weatherIcons/shower.png";
snow.src = "/resources/images/weatherIcons/snow.png";
snowORrain.src = "/resources/images/weatherIcons/snowORrain.png";
thunder.src = "/resources/images/weatherIcons/thunder.png";
wind.src = "/resources/images/weatherIcons/wind.png";
hail.src = "/resources/images/weatherIcons/hail.png";
sun_cloud.src = "/resources/images/weatherIcons/sun-cloud.png";
moon_cloud.src = "/resources/images/weatherIcons/moon-cloud.png";
thunder_cloud.src = "/resources/images/weatherIcons/thunder-cloud.png";

function convertHex(hex, opacity) {
    hex = hex.replace("#", "");
    const r = parseInt(hex.substring(0, 2), 16);
    const g = parseInt(hex.substring(2, 4), 16);
    const b = parseInt(hex.substring(4, 6), 16);

    const result = "rgba(" + r + "," + g + "," + b + "," + opacity / 100 + ")";
    return result;
}

function makeDashBoard(chartDataList) {
    var wsd = chartDataList.wsdData.slice(0, 24); //풍속 데이터 가져오기
    var maxValue = Math.max(...wsd) + 0.3; //가장 큰 값의 2곱해서 y축 만들기
    var vec = chartDataList.vecData.slice(0, 24); //풍향 데이터 가져오기
    var wsdPointImageList = []; //포인트 이미지 담을 빈 배열 생성

    //풍향에 따라 포인트 이미지 바꾸기
    for (i = 0; i < vec.length; i++) {
        //값에 따라 포인트 이미지 배열에 담기
        var roundVec = Math.round(vec[i] / 45);
        switch (roundVec) {
            case 0:
            case 8:
                wsdPointImageList.push(wsd_n);
                break;
            case 1:
                wsdPointImageList.push(wsd_ne);
                break;
            case 2:
                wsdPointImageList.push(wsd_e);
                break;
            case 3:
                wsdPointImageList.push(wsd_se);
                break;
            case 4:
                wsdPointImageList.push(wsd_s);
                break;
            case 5:
                wsdPointImageList.push(wsd_ws);
                break;
            case 6:
                wsdPointImageList.push(wsd_w);
                break;
            case 7:
                wsdPointImageList.push(wsd_nw);
                break;
        }
    }

    var pty = chartDataList.ptyData.slice(0, 24);
    var sky = chartDataList.skyData.slice(0, 24);
    var civilm = Number(chartDataList.twilightData.civilm);
    var civile = Number(chartDataList.twilightData.civile);

    var ptyPointImageList = [];

    for (let i = 0; i < 24; i++) {
        if (pty[i] === "0") {
            if (sky[i] === "1") {
                if (
                    Number(chartDataList.fcstTime[i]) >= civilm &&
                    Number(chartDataList.fcstTime[i]) <= civile
                ) {
                    ptyPointImageList.push(sun);
                } else {
                    ptyPointImageList.push(moon);
                }
            } else if (sky[i] === "3") {
                if (
                    Number(chartDataList.fcstTime[i]) >= civilm &&
                    Number(chartDataList.fcstTime[i]) <= civile
                ) {
                    ptyPointImageList.push(sun_cloud);
                } else {
                    ptyPointImageList.push(moon_cloud);
                }
            } else if (sky[i] === "4") {
                ptyPointImageList.push(cloud);
            }
        } else if (pty[i] === "1") {
            ptyPointImageList.push(rain);
        } else if (pty[i] === "2") {
            ptyPointImageList.push(snowORrain);
        } else if (pty[i] === "3") {
            ptyPointImageList.push(snow);
        } else if (pty[i] === "4") {
            ptyPointImageList.push(hail);
        }
    }

    var ctx = document.getElementById("trafficChart");
    var minValue = Math.min(...ptyData);
    var myChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: chartDataList.fcstTime.slice(0, 24),
            datasets: [
                {
                    label: "풍속",
                    backgroundColor: "transparent",
                    borderColor: brandBlack,
                    borderWidth: 1,
                    yAxisID: "wsd-y-axis",
                    data: chartDataList.wsdData.slice(0, 24),
                    pointStyle: wsdPointImageList,
                },
                {
                    label: "온도",
                    backgroundColor: convertHex(brandInfo, 10),
                    borderColor: brandInfo,
                    pointHoverBackgroundColor: "#fff",
                    borderWidth: 2,
                    yAxisID: "left-y-axis",
                    data: chartDataList.tmpData.slice(0, 24),
                },
                {
                    label: "습도",
                    backgroundColor: "transparent",
                    borderColor: brandSuccess,
                    pointHoverBackgroundColor: "#fff",
                    borderWidth: 2,
                    yAxisID: "right-y-axis",
                    data: chartDataList.rehData.slice(0, 24),
                },

                {
                    label: "강수량",
                    backgroundColor: "transparent",
                    borderColor: brandDanger,
                    pointHoverBackgroundColor: "#fff",
                    borderWidth: 1,
                    borderDash: [8, 5],
                    yAxisID: "right-y-axis",
                    data: chartDataList.pcpData.slice(0, 24),
                },
                {
                    label: "강수확률",
                    backgroundColor: "transparent",
                    borderColor: brandDanger,
                    borderWidth: 1,
                    yAxisID: "right-y-axis",
                    data: chartDataList.popData.slice(0, 24),
                },
                {
                    label: "",
                    backgroundColor: "transparent",
                    borderColor: brandWhite,
                    borderWidth: 1,
                    yAxisID: "left-y-axis",
                    data: [
                        2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5,
                        2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5,
                        2.5, 2.5,
                    ],
                    pointStyle: ptyPointImageList,
                },
            ],
        },
        options: {
            maintainAspectRatio: true,
            legend: {
                display: true,
                position: "top",
                labels: {
                    fontSize: 15,
                },
            },
            responsive: true,
            scales: {
                xAxes: [
                    {
                        ticks: {
                            maxTicksLimit: 24,
                        },
                        gridLines: {
                            drawOnChartArea: false,
                        },
                    },
                ],
                yAxes: [
                    {
                        id: "left-y-axis",
                        position: "left",
                        ticks: {
                            beginAtZero: true,
                            maxTicksLimit: 5,
                            stepSize: 5,
                            max: 50,
                            callback: function (value) {
                                return value + "°C";
                            },
                        },
                        gridLines: {
                            display: true,
                        },
                    },
                    {
                        id: "right-y-axis",
                        position: "right",
                        ticks: {
                            beginAtZero: true,
                            maxTicksLimit: 5,
                            stepSize: 10,
                            max: 100,
                            callback: function (value) {
                                return value + "%";
                            },
                        },
                        gridLines: {
                            display: false,
                        },
                    },
                    {
                        id: "wsd-y-axis",
                        position: "right",
                        display: false,
                        ticks: {
                            beginAtZero: false,
                            maxTicksLimit: 5,
                            max: maxValue,
                            min: -0.5,
                            stepSize: 0.5,
                            callback: function (value) {
                                return value + "m/s";
                            },
                        },
                        gridLines: {
                            display: false,
                        },
                    },
                ],
            },
            elements: {
                point: {
                    radius: 1,
                    hitRadius: 10,
                },
            },
        },
    });
}
