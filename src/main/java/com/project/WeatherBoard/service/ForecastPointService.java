package com.project.WeatherBoard.service;

import java.util.List;

import com.google.gson.JsonObject;
import com.project.WeatherBoard.domain.ForecastDTO;

public interface ForecastPointService {
	
	public List<String> searchByMainAddress();					// 지역 대분류
	
	public List<String> searchByMiddleAddress(String address);	// 지역 중분류 
	
	public String getForecastData(ForecastDTO dto);
	
	public String getAirPollution(ForecastDTO dto);
	
	public String getTwilightTime(ForecastDTO dto);
	

}
