package com.kxj.artadmin.service;

import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.model.dto.AdminVideoView;
import com.kxj.artadmin.model.dto.EditVideoInput;
import com.kxj.artadmin.model.dto.StatusVideoInput;
import com.kxj.artadmin.model.dto.UserVideoView;
import com.kxj.artadmin.model.PageParam;
import org.babyfish.jimmer.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    Page<UserVideoView> getListForUser(PageParam pageParam);

    void loading();

    void updateVideo(MultipartFile file, EditVideoInput videoInput);

    Page<AdminVideoView> getByStatus(PageParam pageParam, Status status);

    AdminVideoView getById(String uuid);

    void changeStatus(StatusVideoInput input);

    UserVideoView searchByUser(String uuid);

    Page<UserVideoView> searchByLabel(List<Long> label, PageParam pageParam);


}
