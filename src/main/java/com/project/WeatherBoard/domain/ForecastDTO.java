package com.project.WeatherBoard.domain;

import lombok.Data;

@Data
public class ForecastDTO {

	private String address;
	
	private String address_detail;
	
	private String x_point;
	
	private String y_point;
	
	private String forecast_day;
	
	private String forecast_time;
	
	//경도
	private String Longitude;
	
	//위도
	private String Latitude;
	
}
