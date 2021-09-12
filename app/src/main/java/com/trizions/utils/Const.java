package com.trizions.utils;

import com.trizions.model.common.AreaResponse;
import com.trizions.model.common.CityResponse;
import com.trizions.model.common.CountryResponse;
import com.trizions.model.common.StateResponse;

import java.util.ArrayList;

public final class Const {
    private Const() {

    }

    public static final String CHAT_SERVER_URL = "http://server.in:8080/ws";
    public static final String IMAGE_PREFIX_SERVER_URL = "http://server.in:8080";

    public static String selectedCity = "india,tamil nadu,chennai";
    public static String currentCountry = "";
    public static String currentState = "";
    public static String currentCity = "";
    public static String receiverName = "";
    public static String receiverId = "";
}
