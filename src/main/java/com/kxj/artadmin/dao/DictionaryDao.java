package com.kxj.artadmin.dao;

import com.kxj.artadmin.model.dto.DictionaryTreeView;
import com.kxj.artadmin.model.dto.DictionaryView;

import java.util.List;

public interface DictionaryDao {


    List<DictionaryView> getList();

    List<DictionaryTreeView> getFullTree();

}
