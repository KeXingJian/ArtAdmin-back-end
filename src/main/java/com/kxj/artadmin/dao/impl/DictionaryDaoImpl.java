package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.DictionaryDao;
import com.kxj.artadmin.model.DictionaryTable;
import com.kxj.artadmin.model.Tables;
import com.kxj.artadmin.model.dto.DictionaryTreeView;
import com.kxj.artadmin.model.dto.DictionaryView;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DictionaryDaoImpl implements DictionaryDao {

    @Resource
    private JSqlClient jsqlClient;

    @Override
    public List<DictionaryView> getList() {

        DictionaryTable table = Tables.DICTIONARY_TABLE;

        return jsqlClient
                .createQuery(table)
                .select(table.fetch(DictionaryView.class))
                .execute();
    }

    @Override
    public List<DictionaryTreeView> getFullTree() {
        DictionaryTable table = Tables.DICTIONARY_TABLE;

        return jsqlClient
                .createQuery(table)
                .where(table.parentId().isNull())
                .select(table.fetch(DictionaryTreeView.class))
                .execute();
    }




}
