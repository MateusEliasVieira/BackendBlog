package com.seminfo.api.dto.others;

import com.seminfo.api.dto.PostOutputDTO;

import java.util.List;

public class PaginationPost {
    private List<PostOutputDTO> listPostsOutput;
    private int qtdPages;
    private Long qtdPosts;

    public List<PostOutputDTO> getListPostsOutput() {
        return listPostsOutput;
    }

    public void setListPostsOutput(List<PostOutputDTO> listPostsOutput) {
        this.listPostsOutput = listPostsOutput;
    }

    public int getQtdPages() {
        return qtdPages;
    }

    public void setQtdPages(int qtdPages) {
        this.qtdPages = qtdPages;
    }

    public Long getQtdPosts() {
        return qtdPosts;
    }

    public void setQtdPosts(Long qtdPosts) {
        this.qtdPosts = qtdPosts;
    }
}
