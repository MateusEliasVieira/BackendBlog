package com.seminfo.api.mapper;

import com.seminfo.api.dto.UserInputDTO;
import com.seminfo.api.dto.UserOutputDTO;
import com.seminfo.domain.model.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public static UserOutputDTO mapperUserToUserOutputDTO(User user){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(user,UserOutputDTO.class);
    }

    public static User mapperUserInputDTOToUser(UserInputDTO userInputDTO){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userInputDTO,User.class);
    }
}
