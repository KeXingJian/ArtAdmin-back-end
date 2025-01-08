package com.kxj.artadmin.dao;


import com.kxj.artadmin.model.dto.*;

import java.util.List;

public interface CollectionDao {
    void adminAddCollection(AdminCollectionInput adminCollectionInput);

    List<AdminCollectionView> getList();

    void updateCollections(AdminCollectionInput collectionInput);

    void addVideo(VideoCollectionInput input);

    void delete(Long id);

    void deleteVideo(Long id, String uuid);

    List<PublicCollectionView> getPublic();

    void userAddCollection(UserCollectionInput collectionInput);


}
