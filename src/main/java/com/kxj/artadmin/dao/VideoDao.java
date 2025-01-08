package com.kxj.artadmin.dao;

import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.model.Video;
import com.kxj.artadmin.model.dto.AdminVideoView;
import com.kxj.artadmin.model.dto.EditVideoInput;
import com.kxj.artadmin.model.dto.StatusVideoInput;
import com.kxj.artadmin.model.dto.UserVideoView;
import com.kxj.artadmin.model.PageParam;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;

import java.util.List;

public interface VideoDao {
    <V extends View<Video>> Page<V> getList(Class<V> viewType, PageParam pageParam);

    void addVideos( List<Video>  videos);

    void updateVideo(EditVideoInput videoInput);

    Page<AdminVideoView> getByStatus(PageParam pageParam, Status status);

   AdminVideoView getById(String uuid);

    void changeStatus(StatusVideoInput input);


    void updateUuid(StatusVideoInput input);

    UserVideoView searchByUser(String uuid);

    Page<UserVideoView> getByLabel(List<Long> labelIds,PageParam pageParam);
}
