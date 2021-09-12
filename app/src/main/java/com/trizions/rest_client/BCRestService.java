package com.trizions.rest_client;

import com.trizions.model.login.LoginRequest;
import com.trizions.model.login.LoginResponse;
import com.trizions.model.login.forgot_password.ForgotPasswordResponse;
import com.trizions.model.login.register.RegisterRequest;
import com.trizions.model.login.register.RegisterResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BCRestService {

    // Login API Call
    @POST("auth/ctoken")
    Call<LoginResponse> login(
            @Body LoginRequest loginRequest
    );

    // Register API Call
    @POST("auth/cuser")
    Call<RegisterResponse> register(
            @Body RegisterRequest registerRequest
    );

    // ForgotPasswordActivity API Call
    @POST("profile/forgot_password")
    Call<ForgotPasswordResponse> retrieve(
            @Body RequestBody requestBody
    );
}
