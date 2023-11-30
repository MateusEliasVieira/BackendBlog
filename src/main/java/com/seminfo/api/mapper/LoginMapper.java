package com.seminfo.api.mapper;

import com.seminfo.api.dto.login.LoginInputDTO;
import com.seminfo.api.dto.login.LoginInputGoogleDTO;
import com.seminfo.api.dto.login.LoginOutputDTO;
import com.seminfo.domain.model.User;
import org.modelmapper.ModelMapper;

public class LoginMapper
{

    public static User mapperLoginInputDTOToUser(LoginInputDTO loginInputDTO)
    {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(loginInputDTO, User.class);
    }

    public static User mapperLoginInputGoogleDTOToUser(LoginInputGoogleDTO loginInputGoogleDTO)
    {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(loginInputGoogleDTO, User.class);
    }

    public static LoginOutputDTO mapperUserToLoginOutputDTO(User user){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user,LoginOutputDTO.class);
    }
}
