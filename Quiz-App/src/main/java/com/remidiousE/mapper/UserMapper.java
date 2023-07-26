package com.remidiousE.mapper;

import com.remidiousE.dto.request.UserRegistrationRequest;
import com.remidiousE.dto.response.UserRegistrationResponse;
import com.remidiousE.model.User;

public class UserMapper {
    public static UserRegistrationResponse map(User user){
        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setMessage("Welcome " + user.getName() + "," + " you have successfully registered");

        return response;
    }
}
