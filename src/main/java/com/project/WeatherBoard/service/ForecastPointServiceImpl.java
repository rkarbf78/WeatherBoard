package com.project.WeatherBoard.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.WeatherBoard.domain.ForecastDTO;
import com.project.WeatherBoard.mapper.ForecastPointMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ForecastPointServiceImpl implements ForecastPointService {

	@Setter(onMethod_=@Autowired)
	ForecastPointMapper f_mapper;
	
	@Override
	public List<String> searchByMainAddress() {
		return f_mapper.searchByMainAddress();
	}

	@Override
	public List<String> searchByMiddleAddress(String address) {
		return f_mapper.searchByMiddleAddress(address);
	}
	
	//날씨정보 가져오는 api구현한 메서드
	public String getWeatherData(ForecastDTO dto) {
		
		log.info("기상정보 주소지 확인" + dto.getAddress());
		log.info("기상정보 주소지 확인" + dto.getAddress_detail());
		log.info("기상정보 x 확인" + dto.getX_point());
		log.info("기상정보 y 확인" + dto.getY_point());
		
		//날짜설정
		LocalDate nowDay = LocalDate.now();
		DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("yyyyMMdd");
		String forecast_day = dtfd.format(nowDay);
		dto.setForecast_day(forecast_day);
		
		//시간설정
		LocalTime nowTime = LocalTime.now();
		
		if(nowTime.isAfter(LocalTime.of(2,15,0)) && nowTime.isBefore(LocalTime.of(5,15,0))) {
			dto.setForecast_time("0200");
		}else if(nowTime.isAfter(LocalTime.of(5,15,0)) && nowTime.isBefore(LocalTime.of(8,15,0))) {
			dto.setForecast_time("0500");
		}else if(nowTime.isAfter(LocalTime.of(8,15,0)) && nowTime.isBefore(LocalTime.of(11,15,0))) {
			dto.setForecast_time("0800");
		}else if(nowTime.isAfter(LocalTime.of(11,15,0)) && nowTime.isBefore(LocalTime.of(14,15,0))) {
			dto.setForecast_time("1100");
		}else if(nowTime.isAfter(LocalTime.of(14,15,0)) && nowTime.isBefore(LocalTime.of(17,15,0))) {
			dto.setForecast_time("1400");
		}else if(nowTime.isAfter(LocalTime.of(17,15,0)) && nowTime.isBefore(LocalTime.of(20,15,0))) {
			dto.setForecast_time("1700");
		}else if(nowTime.isAfter(LocalTime.of(20,15,0)) && nowTime.isBefore(LocalTime.of(23,15,0))) {
			dto.setForecast_time("2000");
		}else if(nowTime.isAfter(LocalTime.of(23,15,0)) && nowTime.isBefore(LocalTime.of(23,59,0))) {
			dto.setForecast_time("2300");
		}else {
			dto.setForecast_time("2300");
			LocalDate yesterday = nowDay.minusDays(1); 
			String forecast_yesterday = dtfd.format(yesterday);
			dto.setForecast_day(forecast_yesterday);
		}
		
		
		log.info("기상정보 날짜" + dto.getForecast_day());
		log.info("기상정보 시간" + dto.getForecast_time());
		
		
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
		    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
		    urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
		    urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(dto.getForecast_day(), "UTF-8")); /*발표 날짜*/
		    urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(dto.getForecast_time(), "UTF-8")); /*발표 시각*/
		    urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(dto.getX_point(), "UTF-8")); /*예보지점의 X 좌표값*/
		    urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(dto.getY_point(), "UTF-8")); /*예보지점의 Y 좌표값*/
		    URL url = new URL(urlBuilder.toString());
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("GET");
		    conn.setRequestProperty("Content-type", "application/json");
		    log.info("기상정보 Response code: " + conn.getResponseCode());
		    BufferedReader rd;
		    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    } else {
		        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		    }
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = rd.readLine()) != null) {
		        sb.append(line);
		    }
		    rd.close();
		    conn.disconnect();
		    log.info("기상정보 데이터 확인" + sb.toString());
		    return sb.toString();
		}catch(Exception e) {
			e.printStackTrace();
			return "기상청 api 오류발생";
		}
	}
	
	//대기오염 정보 api 구현 메서드
	public String getAtmosphere(ForecastDTO dto) {
		
		//조회 주소 문자열 재조합
		   if(dto.getAddress().length() > 4 || dto.getAddress().length() == 3) {
			   dto.setAddress(dto.getAddress().substring(0,2));
			   log.info("대기오염 api 주소조회" + dto.getAddress());
		   }else {
			   String firstWord = dto.getAddress().substring(0,1);
			   String secondWord = dto.getAddress().substring(2,3);
			   dto.setAddress(firstWord+secondWord);
			   log.info("대기오염 api 주소조회" + dto.getAddress());
		   }
		   
		   //얻어온 데이터 대조위해 detail주소 재조합
		   
		   //중분류 존재할시(if)
		   if(dto.getAddress_detail() != null && dto.getAddress_detail().length() > 4) {
			   dto.setAddress_detail(dto.getAddress_detail().substring(0,2) + "시");
			   log.info("대기오염 api 중분류 주소조회" + dto.getAddress_detail());
			   
		   //중분류 없을시(else) 대분류값과 격자x,y 위치값 가까운 위치로 설정
		   }else {
			   switch (dto.getAddress()) {
					case "서울":
						dto.setAddress_detail("종로구");
						break;
					case "부산":
						dto.setAddress_detail("동래구");
						break;
					case "대구":
						dto.setAddress_detail("중구");
						break;
					case "인천":
						dto.setAddress_detail("미추홀구");
						break;
					case "광주":
						dto.setAddress_detail("서구");
						break;
					case "대전":
						dto.setAddress_detail("서구");
						break;
					case "울산":
						dto.setAddress_detail("중구");
						break;
					case "경기":
						dto.setAddress_detail("수원시");
						break;
					case "강원":
						dto.setAddress_detail("춘천시");
						break;
					case "충북":
						dto.setAddress_detail("청주시");
						break;
					case "충남":
						dto.setAddress_detail("금산군");
						break;
					case "전북":
						dto.setAddress_detail("전주시");
						break;
					case "전남":
						dto.setAddress_detail("목포시");
						break;
					case "경북":
						dto.setAddress_detail("구미시");
						break;
					case "경남":
						dto.setAddress_detail("창원시");
						break;
					case "제주":
						dto.setAddress_detail("제주시");
						break;
					case "세종":
						dto.setAddress_detail("세종시");
						break;
			   	}
		   }
		   
		   try {
			   
			    StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
		        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		        urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode(dto.getAddress(), "UTF-8")); /*시도 이름*/
		        urlBuilder.append("&" + URLEncoder.encode("searchCondition","UTF-8") + "=" + URLEncoder.encode("HOUR", "UTF-8")); /*요청 데이터기간*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        log.info("대기오염 Response code: " + conn.getResponseCode());
		        BufferedReader rd;
		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		        }
		        StringBuilder sb = new StringBuilder();
		        String line;
		        while ((line = rd.readLine()) != null) {
		            sb.append(line);
		        }
		        rd.close();
		        conn.disconnect();
		        
		        //데이터를 Json으로 파싱
		        JsonParser jp = new JsonParser();
		        //Json객체 형태로 전환
		        JsonObject originData = (JsonObject)jp.parse(sb.toString());
		        //Json 배열로 전환
		        JsonArray dataList = originData.getAsJsonObject("response").getAsJsonObject("body").getAsJsonArray("items");
		        //반환할 Json객체선언
		        JsonObject data = new JsonObject();
		        //반복문통해 주소 일치데이터 search하여 변수에 담기
		        for(int i =0; i<dataList.size(); i++) {
		        	//리스트에서 하나씩 꺼내서 변수화
		        	JsonObject searchData = (JsonObject)dataList.get(i);
		        	//필요한 키값으로 데이터 찾아서 문자열로 변형
		        	String cityName = searchData.get("cityName").toString().replace("\"", "");
		        	//데이터 비교하여 저장
		        	if(cityName.equals(dto.getAddress_detail())) {
		        		data = searchData;
		        	}
		        }
		        log.info("반환할 미세먼지 데이터" + data);
		        return data.toString(); //일치 데이터 반환	  
		   }catch(Exception e) {
			   e.printStackTrace();
			   return null;
		   }
	}
	
	//일출,일몰, 상용박명 시간 api 구현한 메서드 (처음엔 지역으로찾기였지만 경도,위도로 변경함)
	public String getTwilight(ForecastDTO dto) {
		
		//날짜설정
		LocalDate nowDay = LocalDate.now();
		DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("yyyyMMdd");
		String forecast_day = dtfd.format(nowDay);
		dto.setForecast_day(forecast_day);
		   
		try {
			 StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("locdate","UTF-8") + "=" + URLEncoder.encode(dto.getForecast_day(), "UTF-8")); /*날짜(연월일)*/
		        urlBuilder.append("&" + URLEncoder.encode("longitude","UTF-8") + "=" + URLEncoder.encode(dto.getLongitude(), "UTF-8")); /*경도(도, 분형태)*/
		        urlBuilder.append("&" + URLEncoder.encode("latitude","UTF-8") + "=" + URLEncoder.encode(dto.getLatitude(), "UTF-8")); /*위도(도, 분형태)*/
		        urlBuilder.append("&" + URLEncoder.encode("dnYn","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*실수형태(129.xxx)일경우 Y, 도와 분(128도 00분)형태의 경우 N*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        log.info("박명시간 Response code: " + conn.getResponseCode());
		        BufferedReader rd;
		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		        }
		        StringBuilder sb = new StringBuilder();
		        String line;
		        while ((line = rd.readLine()) != null) {
		            sb.append(line);
		        }
		        rd.close();
		        conn.disconnect();
		        log.info("반환할 박명시간 데이터" + sb.toString());
		        return sb.toString();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	@Override
	public String getForecastData(ForecastDTO dto) {
		
		return getWeatherData(f_mapper.getForecastData(dto));
		
	}

	@Override
	public String getAirPollution(ForecastDTO dto) {
		
		return getAtmosphere(dto);
		
	}

	@Override
	public String getTwilightTime(ForecastDTO dto) {

		return getTwilight(f_mapper.getForecastData(dto));
		
	}

}
