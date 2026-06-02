package com.fms.service;

import com.fms.dto.SearchResult;
import com.fms.entity.Users;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface SearchServiceLocal {

 List<SearchResult> globalSearch(
        String keyword,
        Users loggedUser);
}