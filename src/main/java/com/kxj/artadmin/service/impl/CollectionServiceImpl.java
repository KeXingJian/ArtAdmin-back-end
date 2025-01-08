package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.dao.CollectionDao;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.Collection;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.service.CollectionService;
import com.kxj.artadmin.utils.FileUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Resource
    private CollectionDao collectionDao;

    @Resource
    private UploadConfig uploadConfig;

    @Override
    public void adminAddCollection(
            AdminCollectionInput adminCollectionInput,
            MultipartFile file
    ) {

        String path = FileUtil.uploadPicture(file, uploadConfig.getPictureDir() + Type.COVER.getPath());
        AdminCollectionInput.TargetOf_picture targetOfPicture = new AdminCollectionInput.TargetOf_picture();
        targetOfPicture.setPath(path);
        targetOfPicture.setType(Type.COVER);
        adminCollectionInput.setPicture(targetOfPicture);

        collectionDao.adminAddCollection(adminCollectionInput);
    }

    @Override
    public List<AdminCollectionView> getList() {
        return collectionDao.getList();
    }

    @Override
    public void updateCollection(AdminCollectionInput collectionInput,MultipartFile file) {
        //图片不为空
        if (file!=null  && collectionInput.getPicture() != null) {
            try {

                    FileUtil.removePicture(
                            uploadConfig.getPictureDir()+Type.COVER.getPath()+"/"+collectionInput.getPicture().getPath(),
                            uploadConfig.getVideoRecycle()
                    );
                String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir() + Type.COVER.getPath());
                collectionInput.getPicture().setPath(newPath);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        collectionDao.updateCollections(collectionInput);
    }

    @Override
    public void addVideo(VideoCollectionInput input) {
        collectionDao.addVideo(input);
    }

    @Override
    public void delete(Long id) {
        collectionDao.delete(id);
    }

    @Override
    public void deleteVideo(Long id, String uuid) {
        collectionDao.deleteVideo(id,uuid);
    }

    @Override
    public List<PublicCollectionView> getPublic() {
        return collectionDao.getPublic();
    }

    @Override
    public void upsertByUser(UserCollectionInput userCollectionInput) {
        collectionDao.userAddCollection(userCollectionInput);
    }


}
