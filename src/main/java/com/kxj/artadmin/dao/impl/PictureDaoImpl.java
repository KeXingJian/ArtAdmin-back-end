package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.PictureDao;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.Picture;
import com.kxj.artadmin.model.PictureTable;
import com.kxj.artadmin.model.Tables;
import com.kxj.artadmin.model.dto.AddPictureInput;
import com.kxj.artadmin.model.dto.MirrorPictureView;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PictureDaoImpl implements PictureDao {

    @Resource
    private JSqlClient jsqlClient;

    private final static PictureTable table = Tables.PICTURE_TABLE;

    @Override
    public Picture getPictureById(Long id) {
        return jsqlClient.findById(Picture.class, id);
    }

    @Override
    public List<MirrorPictureView> getMirrors() {

        return jsqlClient.createQuery(table)
                .where(table.type().eq(Type.MIRROR))
                .select(table.fetch(MirrorPictureView.class))
                .execute();
    }

    @Override
    public void add(AddPictureInput input) {
        jsqlClient.save(input);
    }

    @Override
    public void deleteById(Long id) {
        jsqlClient.deleteById(Picture.class, id);
    }
}
