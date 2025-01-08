package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.dao.PictureDao;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.dto.AddPictureInput;
import com.kxj.artadmin.model.dto.MirrorPictureView;
import com.kxj.artadmin.service.PictureService;
import com.kxj.artadmin.utils.FileUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    @Resource
    private PictureDao pictureDao;

    @Resource
    private UploadConfig uploadConfig;

    @Override
    public List<MirrorPictureView> getMirrors() {
        return pictureDao.getMirrors();
    }

    @Override
    public void addPicture(MultipartFile file) {
        AddPictureInput input = new AddPictureInput();
        if (!file.isEmpty()) {
            String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir() + Type.MIRROR.getPath());
            input.setPath(newPath);
            input.setType(Type.MIRROR);
        }

        pictureDao.add(input);

    }

    @Override
    public void deleteById(Long id) {
        pictureDao.deleteById(id);
    }
}
