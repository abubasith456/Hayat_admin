package com.trizions.rest_client;

import com.trizions.model.login.LoginRequest;
import com.trizions.model.login.LoginResponse;
import com.trizions.model.login.change_password.ChangePasswordRequest;
import com.trizions.model.login.change_password.ChangePasswordResponse;
import com.trizions.model.login.forgot_password.ForgotPasswordResponse;
import com.trizions.model.login.forgot_password.ForgotPasswordRequest;
import com.trizions.model.login.register.RegisterRequest;
import com.trizions.model.login.register.RegisterResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BCRestService {

    // Login API Call
    @POST("user_login")
    Call<LoginResponse> login(
            @Body LoginRequest loginRequest
    );

    // Register API Call
    @POST("user_register")
    Call<RegisterResponse> register(
            @Body RegisterRequest registerRequest
    );

    // ForgotPasswordActivity API Call
    @POST("user_forgot_password")
    Call<ForgotPasswordResponse> retrieve(
            @Body ForgotPasswordRequest requestBody
    );

    //ChangePasswordActivity API call
    @POST("user_change_password")
    Call<ChangePasswordResponse> changePassword(
            @Body ChangePasswordRequest changePasswordRequest
    );
}
