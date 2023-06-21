<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html style="height:100%;">

        <head>
            <meta charset="UTF-8">
            <title>weather board</title>
            <meta name="description" content="Sufee Admin - HTML5 Admin Template">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link rel="apple-touch-icon" href="apple-icon.png" />
            <link rel="shortcut icon" href="favicon.ico" />
            <link rel="stylesheet" href="/resources/vendors/bootstrap/dist/css/bootstrap.min.css" />
            <link rel="stylesheet" href="/resources/vendors/font-awesome/css/font-awesome.min.css" />
            <!-- 확인 후 삭제 -->
            <link rel="stylesheet" href="/resources/vendors/themify-icons/css/themify-icons.css" />
            <link rel="stylesheet" href="/resources/vendors/flag-icon-css/css/flag-icon.min.css" />
            <link rel="stylesheet" href="/resources/vendors/selectFX/css/cs-skin-elastic.css" />
            <link rel="stylesheet" href="/resources/vendors/jqvmap/dist/jqvmap.min.css" />
            <link rel="stylesheet" href="/resources/assets/css/style.css" />
            <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800" rel="stylesheet"
                type="text/css" />
            <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
            <script src="https://kit.fontawesome.com/f4f7b7924c.js" crossorigin="anonymous"></script>
            <script type="text/javascript" src="/resources/assets/js/d3.js"></script>
			<script type="text/javascript" src="/resources/assets/js/korea.js"></script>
        </head>
        <script>
            $(document).ready(function () {
                /* 지역별 시/군 검색 */
                $("select[class='address']").change(function () {
                    $.ajax({
                        url: "/address?address=" + $(this).val(),
                        type: "get",
                        dateType: "json",
                        success: function (result) {
                            $(".address_detail").empty();
                            $(".address_detail").append("<option selected='selected'>전체</option>");
                            result.forEach(element => {
                                if (element == null) element = '전체';
                                $(".address_detail").append("<option value='" + element + "'>" + element + "</option>");
                            });
                        }
                    });
                });

                var address = $("select[name=address] option:selected").text();
                var address_detail = $("select[name=address_detail] option:selected").text();

                if (address_detail == "전체") {
                    address_detail = "";
                }

                /* 날씨정보 로드시 전달할 데이터 */
                var formData = {
                    address: address,
                    address_detail: address_detail,
                };

                /* 최초 로드시 데이터 가져오기 및 랜더링 */
                dataInit(formData);

                /* 체크항목 변경사항 반영 이벤트 */
                $(".chartSelect").change(function () {
                    drawWithCheck(chartDataList);
                });

                /* 지역 변경사항 반영 이벤트 */
                $(document).on('click', '#search', function () {
                    address = $("select[name=address] option:selected").text();
                    address_detail = $("select[name=address_detail] option:selected").text();
                    if (address_detail == "전체") {
                        address_detail = "";
                    }
                    formData = {
                        address: address,
                        address_detail: address_detail,
                    };
                    dataInit(formData);
                });

                /* 조회 날짜 변경사항 반영 이벤트 */
                $(".daySelect").change(function () {
                	
                    //데이터 복사하기위한 변수
                    var dayCheckData = {};
                    
                    //날짜 비교를위한 변수
                    var checkDate = new Date();
                    var year = checkDate.getFullYear();
                    var month = ("0" + (1 + checkDate.getMonth())).slice(-2);  //한자리일경우 앞에 0을 붙이기위한작업
                    var dayNow = ("0" + checkDate.getDate()).slice(-2);		   //한자리일경우 앞에 0을 붙이기위한작업 + 현재날짜 비교위한변수
                    var dayTomorrow = ("0" + (checkDate.getDate()+1)).slice(-2); //한자리일경우 앞에 0을 붙이기위한작업 + 내일날짜 비교위한변수
                    
                    if ($(this).attr("id") == "today") {
                        console.log("today");
                        console.log(chartDataList);
                        drawWithCheck(chartDataList);
                    } else if ($(this).attr("id") == "tomorrow") {
                        console.log("tomorrow");
                        for (const key in chartDataList) {
                            if(key != "twilightData"){
                            	dayCheckData[key] = chartDataList[key].filter((value,index) => {
                            		if(chartDataList["fcstDate"][index] == year+month+dayNow){
                            			return false;
                            		}else{
                            			return true;
                            		}
                            	});  	
                            }
                        }
                        dayCheckData["twilightData"] = chartDataList["twilightData"]; //박명시간 데이터 할당
                        console.log(dayCheckData);
                        drawWithCheck(dayCheckData);
                    } else if ($(this).attr("id") == "DAtomorrow") {
                        console.log("DAtomorrow");
                        for (const key in chartDataList) {
                            if(key != "twilightData"){
                            	dayCheckData[key] = chartDataList[key].filter((value,index) => {
                            		if(chartDataList["fcstDate"][index] == year+month+dayNow){
                            			return false;
                            		}else if(chartDataList["fcstDate"][index] == year+month+dayTomorrow){
                            			return false;
                            		}else{
                            			return true;
                            		}
                            	});  	
                            }
                        }
                        dayCheckData["twilightData"] = chartDataList["twilightData"]; //박명시간 데이터 할당
                        console.log(dayCheckData);
                        drawWithCheck(dayCheckData);
                    }
                });
                
                $(document).on("click","path",function(){
                	console.log($(this).attr("id"));
                });

                setInterval(() => {
                    var d = new Date();
                    var hur = d.getHours();
                    var min = d.getMinutes();
                    var sec = d.getSeconds();
                    var time =
                        '현재 시간 : ' + hur + '시 ' + min + '분 ' + sec + '초';
                    $('#time').text(time);
                }, 1000); //setInterval

            }); //document 끝 @@@@@@

            //기상정보 api에서 가져온 데이터를 저장할 변수
            var chartDataList = {};
            var fcstDate = []; //날짜
            var fcstTime = []; //시간
            var tmpData = []; //온도
            var rehData = []; //습도
            var pcpData = []; //강수
            var wsdData = []; //풍속
            var vecData = []; //풍향
            var popData = []; //강수확률
            var ptyData = []; //강수형태
            var skyData = []; //하늘상태

            //박명시간 api에서 가져온 데이터를 저장할 변수
            var twilightData = {};

            //대기오염 api에서 가져온 데이터를 저장할 변수
            var pm10Data = []; //미세먼지
            var pm25Data = []; //초미세먼지
            var o3Data = []; //오존


            function dataInit(formData) {

                //데이터항목 초기화
                //기상정보 api에서 가져온 데이터를 저장할 변수
                chartDataList = {};
                fcstDate = []; //날짜
                fcstTime = []; //시간
                tmpData = []; //온도
                rehData = []; //습도
                pcpData = []; //강수
                wsdData = []; //풍속
                vecData = []; //풍향
                popData = []; //강수확률
                ptyData = []; //강수형태
                skyData = []; //하늘상태

                //박명시간 api에서 가져온 데이터를 저장할 변수
                twilightData = {};

                //대기오염 api에서 가져온 데이터를 저장할 변수
                pm10Data = []; //미세먼지
                pm25Data = []; //초미세먼지
                o3Data = []; //오존

                let promise = new Promise((resolve, reject) => {
                    $.ajax({
                        url: '/getWeatherData',
                        type: 'GET',
                        data: formData,
                        dataType: "json",
                        success: function (result) {
                            if (result != '기상청 api 오류발생') {
                                let items = result.response.body.items.item;
                                $.each(items, function (idx, data) {
                                    if (data.category == 'TMP') {
                                        fcstDate.push(data.fcstDate);
                                        fcstTime.push(data.fcstTime); //시간데이터저장
                                        tmpData.push(data.fcstValue); //기온데이터저장
                                    } else if (data.category == 'REH') {
                                        rehData.push(data.fcstValue); //습도데이터저장
                                    } else if (data.category == 'PCP') {
                                        if (data.fcstValue == '강수없음') {
                                            pcpData.push(0); //강수없음저장
                                        } else {
                                            pcpData.push(data.fcstValue); //강수데이터저장
                                        }
                                    } else if (data.category == 'WSD') {
                                        wsdData.push(data.fcstValue); //풍속데이터저장
                                    } else if (data.category == 'VEC') {
                                        vecData.push(data.fcstValue); //풍향데이터저장
                                    } else if (data.category == 'POP') {
                                        popData.push(data.fcstValue); //강수확률데이터저장
                                    } else if (data.category == 'PTY') {
                                        ptyData.push(data.fcstValue); //강수형태데이터저장
                                    } else if (data.category == 'SKY') {
                                        skyData.push(data.fcstValue); //하늘형태데이터저장
                                    }
                                });

                                chartDataList = {
                                    fcstDate,
                                    fcstTime,
                                    tmpData,
                                    rehData,
                                    pcpData,
                                    wsdData,
                                    popData,
                                    ptyData,
                                    vecData,
                                    skyData,
                                };

                                $('.TMPcount').text(tmpData[0] + '°C'); //온도차트 현재온도표기
                                $('.REHcount').text(rehData[0] + '%'); //습도차트 현재습도표기
                                if (pcpData[0] == 0) {
                                    //강수차트 현재강수표기
                                    $('.PCPcount').text('강수없음');
                                } else {
                                    $('.PCPcount').text(pcpData[0] + 'mm');
                                }
                                $('.WSDcount').text(wsdData[0] + 'm/s'); //바람차트 현재바람표기
                                
                                $('#TMP-text').text(tmpData[0] + '°C');
                                $('#REH-text').text(rehData[0] + '%');
                                $('#WSD-text').text(wsdData[0] + 'm/s');
                                if (pcpData[0] == 0) {
                                    $('#PCP-text').text('강수없음');
                                } else {
                                    $('#PCP-text').text(pcpData[0] + 'mm');
                                }   
                            }else{
                            	alert('데이터를 불러오지 못했습니다.'); //api에서 데이터 못불러온경우	
                            }
                            resolve();
                        },

                    }); //getWeatherData        	
                });

                //기상데이터 가져오는 ajax 실행이후 실행됨!
                promise.then(() => {

                    //대기오염 정보 가져오기
                    $.ajax({
                        url: '/getAtmosphere',
                        type: 'GET',
                        data: formData,
                        success: function (result) {
                            pm10Data.push(result.pm10Value);
                            pm25Data.push(result.pm25Value);
                            o3Data.push(result.o3Value);
                            makeDonut(pm10Data, pm25Data, o3Data);
                        },
                    }); //getAtmosphere

                    //박명시간 정보 가져오기
                    $.ajax({
                        url: '/getTwilight',
                        type: 'GET',
                        data: formData,
                        success: function (result) {
                            //조회 데이터 변수저장
                            twilightData.civilm =
                                result.getElementsByTagName('civilm').item(0).firstChild
                                    .nodeValue.replaceAll(" ", "");

                            twilightData.civile =
                                result.getElementsByTagName('civile').item(0).firstChild
                                    .nodeValue.replaceAll(" ", "");

                            twilightData.sunrise =
                                result.getElementsByTagName('sunrise').item(0)
                                    .firstChild.nodeValue.replaceAll(" ", "");

                            twilightData.sunset =
                                result.getElementsByTagName('sunset').item(0).firstChild
                                    .nodeValue.replaceAll(" ", "");

                            chartDataList.twilightData = twilightData;

                            //대쉬보드 데이터 삽입
                            drawWithCheck(chartDataList);

                            //위젯 데이터 삽입
                            makeWidget(
                                fcstTime.slice(0, 9),
                                tmpData.slice(0, 9),
                                rehData.slice(0, 9),
                                pcpData.slice(0, 9),
                                wsdData.slice(0, 9)
                            );
                        },
                    });	//getTwilight
                }); //promise
            }

            function drawWithCheck(chartDataList) {

                /* 체크 항목만을 담을 객체선언,초기화 */
                var chartCheckData = {};

                /* 객체 깊은복사 */
                for (const key in chartDataList) {
                    chartCheckData[key] = chartDataList[key];
                }

                $('input[name=state]').each(function () {
                    if ($(this).prop('checked') === false) {
                        if ($(this).attr('id') == 'temperature') {
                            chartCheckData.tmpData = [];
                        } else if ($(this).attr('id') == 'humidity') {
                            chartCheckData.rehData = [];
                        } else if ($(this).attr('id') == 'precipitation') {
                            chartCheckData.pcpData = [];
                        } else if ($(this).attr('id') == 'windSpeed') {
                            chartCheckData.wsdData = [];
                        } else if (
                            $(this).attr('id') == 'probabilityOfPrecipitation'
                        ) {
                            chartCheckData.popData = [];
                        }
                    }
                });
                $('#trafficDiv').html(
                    "<canvas id='trafficChart' height='100%'></canvas>"
                );
                makeDashBoard(chartCheckData);
            }

        </script>

        <body>
            <nav class="navbar navbar-expand-sm navbar-default">
                <div class="page-header">
                    <a class="navbar-brand" href="./"><img src="/resources/images/3738.jpg" id="header_logo"></a>
                </div>
            </nav>

            <div class="wrap">
                <div class="content">
                    <p id="time"></p>
                    <div class="search_boxes">
                        <div class="address_box">
                            <select class="address" name="address">
                                <c:forEach var="mainAddress" items="${mainAddressList}">
                                    <c:choose>
                                        <c:when test="${mainAddress == '서울특별시'}">
                                            <option selected value="${mainAddress}">${mainAddress}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${mainAddress}">${mainAddress}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                            <select class="address_detail" name="address_detail">
                                <option selected="selected">전체</option>
                            </select>
                            <a style="cursor:pointer;" id="search">검색하기</a>
                        </div>
                    </div>
                    <div class="widgets-wrap">
                        <div class="col-sm-6 col-lg-3">
                            <div class="card text-white bg-flat-color-4">
                                <div class="card-body pb-20">
                                    <p class="text-light">기온</p>
                                    <div class="weatherIcons">
                                        <i class="fa-solid fa-temperature-high fa-2xl" id="weatherIcon"></i>
                                    </div>
                                    <h4 class="mb-0">
                                        <span class="TMPcount"></span>
                                    </h4>
                                    <div class="chart-wrapper px-0" style="height:70px;">
                                        <canvas id="TMPChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6 col-lg-3">
                            <div class="card text-white bg-flat-color-3">
                                <div class="card-body pb-20">
                                    <p class="text-light">습도</p>
                                    <div class="weatherIcons">
                                        <i class="fa-regular fa-sun fa-2xl " id="weatherIcon"></i>
                                    </div>
                                    <h4 class="mb-0">
                                        <span class="REHcount"></span>
                                    </h4>
                                    <div class="chart-wrapper px-0" style="height:70px;">
                                        <canvas id="REHChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6 col-lg-3">
                            <div class="card text-white bg-flat-color-1">
                                <div class="card-body pb-20">
                                    <p class="text-light">강수량</p>
                                    <div class="weatherIcons">
                                        <i class="fa-solid fa-cloud-showers-heavy fa-2xl" id="weatherIcon"></i>
                                    </div>
                                    <h4 class="mb-0">
                                        <span class="PCPcount"></span>
                                    </h4>
                                    <div class="chart-wrapper px-0" style="height:70px;">
                                        <canvas id="PCPChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6 col-lg-3">
                            <div class="card text-white bg-flat-color-2">
                                <div class="card-body pb-20">
                                    <p class="text-light">바람</p>
                                    <div class="weatherIcons">
                                        <i class="fa-solid fa-wind fa-2xl" id="weatherIcon"></i>
                                    </div>
                                    <h4 class="mb-0">
                                        <span class="WSDcount"></span>
                                    </h4>
                                    <div class="chart-wrapper px-0" style="height:70px;">
                                        <canvas id="WSDChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-6 KorMapArea">
                        <div class="KorMap">
                            <div class="card-header">
                                <h4>Map</h4>
                            </div>
                           <div id="KorMapContainer"></div>
                        </div>
                    </div>
                    <div class="weatherstatus col-lg-6">
                        <div class="now_weather">
                            <div class="TMP_space">
                                <div class="TMP-header">
                                    <strong id="TMP-title">현재 온도</strong>
                                </div>
                                <div class="TMP-body">
                                    <p id="TMP-text"></p>
                                </div>
                            </div>
                            <hr>
                            <div class="weather_space">
                                <div class="weather-header">
                                    <strong class="weather-title">습도</strong>
                                </div>
                                <div class="weather-body">
                                    <p id="REH-text"></p>
                                </div>
                            </div>
                            <div class="weather_space">
                                <div class="weather-header">
                                    <strong class="weather-title">바람</strong>
                                </div>
                                <div class="weather-body">
                                    <p id="WSD-text"></p>
                                </div>
                            </div>
                            <div class="weather_space">
                                <div class="weather-header">
                                    <strong class="weather-title">1시간 강수량</strong>
                                </div>
                                <div class="weather-body">
                                    <p id="PCP-text"></p>
                                </div>
                            </div>
                        </div>
                        <div class="donut">
                            <div class="donutChart">
                                <canvas id="PM10Chart"></canvas>
                            </div>
                            <div class="donutChart">
                                <canvas id="PM25Chart"></canvas>
                            </div>
                            <div class="donutChart">
                                <canvas id="O3Chart"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-12">
                        <div class="weatherChartKorea">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h4 class="card-title mb-0">Weather Chart</h4>
                                        <div class="small text-muted">Korea</div>
                                    </div>
                                    <div class="col-sm-6 hidden-sm-down">
                                        <div class="btn-toolbar float-right" role="toolbar"
                                            aria-label="Toolbar with button groups">
                                            <div class="btn-group mr-3" aria-label="First group" id="checkWrap">
                                                <label class="btn btn-outline-secondary">
                                                    <input class="chartSelect" type="checkbox" name="state"
                                                        id="temperature" checked="checked"> 기온
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="chartSelect" type="checkbox" name="state"
                                                        id="precipitation" checked="checked"> 강수량
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="chartSelect" type="checkbox" name="state"
                                                        id="humidity" checked="checked">
                                                    습도
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="chartSelect" type="checkbox" name="state"
                                                        id="windSpeed" checked="checked" />
                                                    풍속
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="chartSelect" type="checkbox" name="state"
                                                        id="probabilityOfPrecipitation" checked="checked" />
                                                    강수확률
                                                </label>
                                            </div>
                                        </div>
                                        <div class="btn-toolbar float-right" role="toolbar"
                                            aria-label="Toolbar with button groups">
                                            <div class="btn-group mr-3" aria-label="First group" id="dayCheckWrap">
                                                <label class="btn btn-outline-secondary">
                                                    <input class="daySelect" type="radio" name="day" id="today"
                                                        checked="checked"> 오늘
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="daySelect" type="radio" name="day" id="tomorrow"> 내일
                                                </label>
                                                <label class="btn btn-outline-secondary">
                                                    <input class="daySelect" type="radio" name="day" id="DAtomorrow"> 모레
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="trafficChart-wrapper mt-2" id="trafficDiv">
                                    <canvas id="trafficChart" height="100%"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer>
                <div class="footer_box">
                    <img src="/resources/images/3738.jpg" id="footer_logo">
                    <div id="address">
                        <ul class="li-group">
                            <li>경기도 수원시 팔달구 어디게</li>
                            <li>TEL : 031-123-1234 Email:carrotcarrot@gmail.com</li>
                            <li>COPYRIGHT (C) 당근 ALL RIGHTS RESERVED</li>
                        </ul>
                    </div>
                </div>
            </footer>
            <script src="/resources/vendors/peity/jquery.peity.min.js"></script>
            <script src="/resources/assets/js/init-scripts/peitychart/peitychart.init.js"></script>
            <script src="/resources/vendors/jquery/dist/jquery.min.js"></script>
            <script src="/resources/vendors/popper.js/dist/umd/popper.min.js"></script>
            <script src="/resources/vendors/bootstrap/dist/js/bootstrap.min.js"></script>
            <script src="/resources/assets/js/main.js"></script>
            <script src="/resources/vendors/chart.js/dist/Chart.bundle.min.js"></script>
            <script src="/resources/assets/js/dashboard.js"></script>
            <script src="/resources/assets/js/widgets.js"></script>
            <script src="/resources/assets/js/donuts.js"></script>
        </body>

        </html>