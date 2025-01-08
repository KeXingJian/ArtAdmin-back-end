package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.dao.DictionaryDao;
import com.kxj.artadmin.model.dto.DictionaryTreeView;
import com.kxj.artadmin.model.dto.DictionaryView;
import com.kxj.artadmin.service.DictionaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryDao dictionaryDao;

    @Override
    public List<DictionaryView> getList() {
        return dictionaryDao.getList();
    }

    @Override
    public List<DictionaryTreeView> getFullTree() {
        return dictionaryDao.getFullTree();
    }
}
