package com.kxj.artadmin.dao;

import com.kxj.artadmin.model.Picture;
import com.kxj.artadmin.model.dto.AddPictureInput;
import com.kxj.artadmin.model.dto.MirrorPictureView;

import java.util.List;

public interface PictureDao {
    Picture getPictureById(Long id);

    List<MirrorPictureView> getMirrors();

    void add(AddPictureInput input);

    void deleteById(Long id);
}
