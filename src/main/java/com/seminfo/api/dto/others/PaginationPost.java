package com.seminfo.api.dto.others;

import com.seminfo.api.dto.post.PostOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationPost {
    private List<PostOutputDTO> listPostsOutput;
    private int qtdPages;
    private Long qtdPosts;

}
