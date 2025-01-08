package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.CollectionDao;
import com.kxj.artadmin.model.*;
import com.kxj.artadmin.model.dto.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class CollectionDaoImpl implements CollectionDao {

    @Resource
    private JSqlClient jsqlClient;

    @Override
    public void adminAddCollection(AdminCollectionInput adminCollectionInput) {
        jsqlClient.save(adminCollectionInput, SaveMode.INSERT_ONLY);
    }



    @Override
    public List<AdminCollectionView> getList() {
        CollectionTable table = Tables.COLLECTION_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(table.common().eq(true))
                .select(table.fetch(AdminCollectionView.class))
                .execute();

    }

    @Override
    public void updateCollections(AdminCollectionInput collectionInput) {
        jsqlClient.save(collectionInput, SaveMode.UPDATE_ONLY);
    }

    @Override
    public void addVideo(VideoCollectionInput input) {

        try {
            jsqlClient.save(input, AssociatedSaveMode.APPEND_IF_ABSENT);
        }catch (Exception e) {
            log.warn("重复插入");
        }

    }

    @Override
    public void delete(Long id) {
        CollectionTable table = Tables.COLLECTION_TABLE;
        jsqlClient.createDelete(table)
                .where(table.id().eq(id))
                .execute();
    }

    @Override
    public void deleteVideo(Long id, String uuid) {

        VideoTable videoTable = Tables.VIDEO_TABLE;
        Long videoId = jsqlClient.createQuery(videoTable)
                .where(videoTable.uuid().eq(uuid))
                .select(videoTable.id())
                .fetchOneOrNull();
        jsqlClient.getAssociations(CollectionProps.VIDEOS).delete(id,videoId);

    }

    @Override
    public List<PublicCollectionView> getPublic() {
        CollectionTable table = Tables.COLLECTION_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(table.common().eq(true))
                .select(table.fetch(PublicCollectionView.class))
                .execute();
    }

    @Override
    public void userAddCollection(UserCollectionInput collectionInput) {
        if (collectionInput.getId()==null) {
            jsqlClient.save(collectionInput, SaveMode.INSERT_ONLY);
        }else {
            jsqlClient.save(collectionInput, SaveMode.UPDATE_ONLY);
        }

    }
}
