package com.seminfo.api.model;

import java.util.List;

public class PaginationPost {
    private List<PostOutput> listPostsOutput;
    private int qtdPages;
    private Long qtdPosts;

    public List<PostOutput> getListPostsOutput() {
        return listPostsOutput;
    }

    public void setListPostsOutput(List<PostOutput> listPostsOutput) {
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
