package com.kxj.artadmin.service;

import com.kxj.artadmin.model.dto.MirrorPictureView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureService {

    List<MirrorPictureView> getMirrors();

    void addPicture(MultipartFile file);

    void deleteById(Long id);
}
