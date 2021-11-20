package com.trizions.rest_client;

import com.trizions.model.clients.ClientsResponse;
import com.trizions.model.clients.ClientsRequest;
import com.trizions.model.clients.add_clients.AddClientsRequest;
import com.trizions.model.clients.add_clients.AddClientsResponse;
import com.trizions.model.clients.delete_clients.DeleteClientsRequest;
import com.trizions.model.clients.delete_clients.DeleteClientsResponse;
import com.trizions.model.clients.update_clients.UpdateClientsRequest;
import com.trizions.model.clients.update_clients.UpdateClientsResponse;
import com.trizions.model.company_contact.CompanyContactRequest;
import com.trizions.model.company_contact.CompanyContactResponse;
import com.trizions.model.company_contact.add_company_contact.AddCompanyContactRequest;
import com.trizions.model.company_contact.add_company_contact.AddCompanyContactResponse;
import com.trizions.model.company_contact.delete_company_contact.DeleteCompanyContactResponse;
import com.trizions.model.company_contact.delete_company_contact.DeleteCompanyContactRequest;
import com.trizions.model.company_contact.update_company_contact.UpdateCompanyContactRequest;
import com.trizions.model.company_contact.update_company_contact.UpdateCompanyContactResponse;
import com.trizions.model.login.LoginRequest;
import com.trizions.model.login.LoginResponse;
import com.trizions.model.login.change_password.ChangePasswordRequest;
import com.trizions.model.login.change_password.ChangePasswordResponse;
import com.trizions.model.login.forgot_password.ForgotPasswordResponse;
import com.trizions.model.login.forgot_password.ForgotPasswordRequest;
import com.trizions.model.login.register.RegisterRequest;
import com.trizions.model.login.register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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

    //ClientFragment API call
    @POST("get_list_of_clients")
    Call<ClientsResponse> clients(
            @Body ClientsRequest clientsRequest
    );

    //AddClients API Call
    @POST("add_client")
    Call<AddClientsResponse> addClients(
            @Body AddClientsRequest addClientsRequest
    );

    //UpdateClient API Call
    @PUT("update_or_delete_client/1")
    Call<UpdateClientsResponse> updateClients(
            @Body UpdateClientsRequest updateClientsRequest
    );

    //Delete Clients API Call
    @DELETE("update_or_delete_client/3")
    Call<DeleteClientsResponse> deleteClients(
            @Body DeleteClientsRequest deleteClientsRequest
    );

    //Company contact API call
    @POST("get_list_of_company_contact")
    Call<CompanyContactResponse> companyContact(
            @Body CompanyContactRequest companyContactRequest
    );

    //Add company contact API Call
    @POST("add_company_contact")
    Call<AddCompanyContactResponse> addCompanyContact(
            @Body AddCompanyContactRequest addCompanyContactRequest
    );

    //Update Company contact Api Call
    @PUT("update_or_delete_company_contact/2")
    Call<UpdateCompanyContactResponse> updateCompanyContact(
            @Body UpdateCompanyContactRequest updateCompanyContactRequest
    );

    //Delete Company Conatct API Call
    @DELETE("update_or_delete_company_contact/3")
    Call<DeleteCompanyContactResponse> deleteCompanyContact(
            @Body DeleteCompanyContactRequest deleteCompanyContactRequest
    );
}
