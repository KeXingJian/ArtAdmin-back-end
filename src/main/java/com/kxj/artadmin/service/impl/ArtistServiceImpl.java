package com.kxj.artadmin.service.impl;

import com.kxj.artadmin.config.UploadConfig;
import com.kxj.artadmin.dao.ArtistDao;
import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.enume.Type;
import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.service.ArtistService;
import com.kxj.artadmin.utils.FileUtil;
import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Resource
    private UploadConfig uploadConfig;

    @Resource
    private ArtistDao artistDao;

    @Override
    public Page<ArtistView> getList(PageParam pageParam) {
        return artistDao.getList(ArtistView.class, pageParam);
    }

    @Override
    public List<ArtistSelectView> getSelect() {
        return artistDao.getSelect();
    }

    @Override
    public void addArtist(MultipartFile file, ArtistAddInput artistInput) {

        //图片存放
        String newFilename = FileUtil.uploadPicture(file, uploadConfig.getPictureDir()+Type.TEACHER.getPath());

        ArtistAddInput.TargetOf_picture picture = new ArtistAddInput.TargetOf_picture();

        picture.setPath(newFilename);
        picture.setType(Type.TEACHER);
        artistInput.setPicture(picture);

        artistDao.addArtist(artistInput);

    }

    @Override
    public void updateArtist(MultipartFile file, ArtistUpadteInput artistInput) {


        if (file != null && artistInput.getPicture() != null) {
            String newPath = FileUtil.uploadPicture(file, uploadConfig.getPictureDir()+Type.TEACHER.getPath());

            try {
                FileUtil.removePicture(uploadConfig.getPictureDir()+Type.TEACHER.getPath()+"/" + artistInput.getPicture().getPath(),
                        uploadConfig.getPictureRecycle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            artistInput.getPicture().setPath(newPath);


        }


        System.out.println(artistInput);

        artistDao.updateArtist(artistInput);
    }

    @Override
    public void deleteArtist(Long artistId, String path) {

        try {
            FileUtil.removePicture(uploadConfig.getPictureDir()+Type.TEACHER.getPath()+ "/" + path,
                    uploadConfig.getPictureRecycle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        artistDao.deleteArtist(artistId);
    }

    @Cacheable("data")
    @Override
    public Page<AtistCollectionView> getCollection(PageParam pageParam) {
        Page<AtistCollectionView> collections = artistDao.collections(pageParam);
        collections.getRows().forEach(artist->{
            List<AtistCollectionView.TargetOf_videos> list = artist.getVideos().stream().filter(video -> video.getStatus() == Status.PUBLIC).toList();
            artist.setVideos(list);
        });
        return collections;
    }


}
