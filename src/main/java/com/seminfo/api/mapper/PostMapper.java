package com.seminfo.api.mapper;

import com.seminfo.api.dto.PostInputDTO;
import com.seminfo.api.dto.PostOutputDTO;
import com.seminfo.api.dto.UserOutputDTO;
import com.seminfo.domain.model.Post;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    public static Post mapperPostInputDTOToPost(PostInputDTO postInputDTO) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(postInputDTO, Post.class);
    }

    public static PostOutputDTO mapperPostToPostOutputDTO(Post post) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(post, PostOutputDTO.class);
    }

    public static List<PostOutputDTO> mapperListPostToListPostOutputDTO(List<Post> listPost) {
        ModelMapper mapper = new ModelMapper();
        return listPost
                .stream()
                .map(post -> mapper.map(post, PostOutputDTO.class))
                .collect(Collectors.toList());
    }


}
