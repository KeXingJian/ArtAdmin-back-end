package com.kxj.artadmin.service;

import com.kxj.artadmin.model.Collection;
import com.kxj.artadmin.model.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CollectionService {
    void adminAddCollection(AdminCollectionInput adminCollectionInput, MultipartFile file);



    List<AdminCollectionView> getList();

    void updateCollection(AdminCollectionInput collectionInput,MultipartFile file);

    void addVideo(VideoCollectionInput input);

    void delete(Long id);

    void deleteVideo(Long id, String uuid);

    List<PublicCollectionView> getPublic();

    void upsertByUser(UserCollectionInput userCollectionInput);
}
