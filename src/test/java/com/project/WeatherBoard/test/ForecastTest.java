package com.project.WeatherBoard.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.WeatherBoard.domain.ForecastDTO;
import com.project.WeatherBoard.service.ForecastPointService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ForecastTest {
	
	@Setter(onMethod_=@Autowired)
	private ForecastPointService service;
	
//		@Test
		public void getForecastTest() {
			ForecastDTO dto = new ForecastDTO();
			
			dto.setAddress("서울특별시");
			dto.setAddress_detail("용산구");
			service.getForecastData(dto);
		}
		
//		@Test
		public void apiTest() throws IOException {
		        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
		        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
		        urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*시도 이름(전국, 서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종)*/
		        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8")); /*버전별 상세 결과 참고*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        System.out.println("Response code: " + conn.getResponseCode());
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
		        System.out.println(sb.toString());
		    }
		
//			@Test
		   public void apiTest2() throws IOException {
		        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*xml 또는 json*/
		        urlBuilder.append("&" + URLEncoder.encode("tmX","UTF-8") + "=" + URLEncoder.encode("465535.790524752", "UTF-8")); /*TM측정방식 X좌표*/
		        urlBuilder.append("&" + URLEncoder.encode("tmY","UTF-8") + "=" + URLEncoder.encode("351548.26587717", "UTF-8")); /*TM측정방식 Y좌표*/
		        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8")); /*버전별 상세 결과 참고*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        System.out.println("Response code: " + conn.getResponseCode());
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
		        System.out.println(sb.toString());
		    }
		   
//		   @Test
		   public void apiTest3() throws IOException {
			   
			   ForecastDTO dto = new ForecastDTO();
			   
			   dto.setAddress("충청남도");
			   dto.setAddress_detail("천안시동남구");
			   
			   //조회 주소 문자열 재조합
			   if(dto.getAddress().length() > 4 || dto.getAddress().length() == 3) {
				   dto.setAddress(dto.getAddress().substring(0,2));
				   System.out.println(dto.getAddress());
			   }else {
				   String firstWord = dto.getAddress().substring(0,1);
				   String secondWord = dto.getAddress().substring(2,3);
				   dto.setAddress(firstWord+secondWord);
				   System.out.println(dto.getAddress());
			   }
			   
			   //얻어온 데이터 대조위해 detail주소 재조합
			   if(dto.getAddress_detail().length() > 4) {
				   dto.setAddress_detail(dto.getAddress_detail().substring(0,2) + "시");
				   System.out.println(dto.getAddress_detail());
			   }
			   
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
		        System.out.println("Response code: " + conn.getResponseCode());
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
		        JsonParser jp = new JsonParser();
		        JsonObject originData = (JsonObject)jp.parse(sb.toString());

		        JsonArray dataList = originData.getAsJsonObject("response").getAsJsonObject("body").getAsJsonArray("items");

		        JsonObject data = new JsonObject();

		        for(int i =0; i<dataList.size(); i++) {
		        	JsonObject searchData = (JsonObject)dataList.get(i);
		        	String cityName = searchData.get("cityName").toString().replace("\"", "");
		        	if(cityName.equals(dto.getAddress_detail())) {
		        		data = searchData;
		        		log.info("반환할 미세먼지 데이터" + data);
		        	}else {
		        		System.out.println("일치데이터 없음");
		        	}
		        }
		    }
		   
		   @Test
		   public void apiTest4() throws IOException {
			   StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo"); /*URL*/
		        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=gENxVbgt5rfdsK9z71GmdcHPzVcOc7BNuu7ZRXwo2bRzaixy7CHzML78MD%2FzFw0uU0pF1RNCrsTkm0c32uY5mA%3D%3D"); /*Service Key*/
		        urlBuilder.append("&" + URLEncoder.encode("locdate","UTF-8") + "=" + URLEncoder.encode("20230616", "UTF-8")); /*날짜(연월일)*/
		        urlBuilder.append("&" + URLEncoder.encode("longitude","UTF-8") + "=" + URLEncoder.encode("127.573633333333", "UTF-8")); /*경도(도, 분형태)*/
		        urlBuilder.append("&" + URLEncoder.encode("latitude","UTF-8") + "=" + URLEncoder.encode("36.3035499999999", "UTF-8")); /*위도(도, 분형태)*/
		        urlBuilder.append("&" + URLEncoder.encode("dnYn","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*실수형태(129.xxx)일경우 Y, 도와 분(128도 00분)형태의 경우 N*/
		        URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        System.out.println("Response code: " + conn.getResponseCode());
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
		        System.out.println(sb.toString());
		    }
		   
		   
		   
		   
		}