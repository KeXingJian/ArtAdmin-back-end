package com.kxj.artadmin.service;

import com.kxj.artadmin.model.dto.DictionaryTreeView;
import com.kxj.artadmin.model.dto.DictionaryView;

import java.util.List;

public interface DictionaryService {
    List<DictionaryView> getList();

    List<DictionaryTreeView> getFullTree();
}
