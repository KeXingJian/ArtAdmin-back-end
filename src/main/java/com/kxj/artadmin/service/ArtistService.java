package com.kxj.artadmin.service;


import com.kxj.artadmin.model.dto.*;
import com.kxj.artadmin.model.PageParam;
import org.babyfish.jimmer.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArtistService {

    Page<ArtistView> getList(PageParam pageParam);


    List<ArtistSelectView> getSelect();


    void addArtist(MultipartFile file, ArtistAddInput artistInput);

    void updateArtist(MultipartFile file, ArtistUpadteInput artistInput);

    void deleteArtist(Long artistId,String path);

    Page<AtistCollectionView> getCollection(PageParam pageParam);
}
