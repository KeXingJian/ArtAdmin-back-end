package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.VideoDao;
import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.model.Immutables;
import com.kxj.artadmin.model.Tables;
import com.kxj.artadmin.model.Video;
import com.kxj.artadmin.model.VideoTable;
import com.kxj.artadmin.model.dto.AdminVideoView;
import com.kxj.artadmin.model.dto.EditVideoInput;
import com.kxj.artadmin.model.dto.StatusVideoInput;
import com.kxj.artadmin.model.dto.UserVideoView;
import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.sql.JSqlClient;

import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class VideoDaoImpl implements VideoDao {

    @Resource
    private JSqlClient jsqlClient;

    @Override
    public <V extends View<Video>> Page<V> getList(Class<V> viewType, PageParam pageParam) {
        VideoTable table = Tables.VIDEO_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(
                        table.status().eq(Status.PUBLIC)
                )
                .select(table.fetch(viewType))
                .fetchPage(pageParam.getPageIndex(), pageParam.getPageSize());
    }

    @Override
    public void addVideos( List<Video> videos) {
        jsqlClient.saveEntities(videos);
    }

    @Override
    public void updateVideo(EditVideoInput videoInput) {
        jsqlClient.save(videoInput,SaveMode.UPDATE_ONLY);
    }

    @Override
    public Page<AdminVideoView> getByStatus(PageParam pageParam, Status status) {
        VideoTable table = Tables.VIDEO_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(table.status().eqIf(status))
                .select(table.fetch(AdminVideoView.class))
                .fetchPage(pageParam.getPageIndex(), pageParam.getPageSize());
    }

    @Override
    public AdminVideoView getById(String uuid) {
        VideoTable table = Tables.VIDEO_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(table.uuid().eqIf(uuid))
                .select(table.fetch(AdminVideoView.class))
                .fetchOneOrNull();
    }

    //改变状态
    @Override
    public void changeStatus(StatusVideoInput input) {

        Video video = Immutables.createVideo(draft -> {
            draft.setUuid(input.getUuid());
            draft.setStatus(input.getStatus());

            if (input.getPicture() != null && input.getPicture().getId() != null) {
                draft.setPictureId(input.getPicture().getId());
            }

            if (input.getArtist() != null && input.getArtist().getName() != null) {
                draft.applyArtist(artist -> {
                    artist.setName(input.getArtist().getName());
                });
            }

        });

        jsqlClient.save(video,SaveMode.UPDATE_ONLY);
    }

    @Override
    public void updateUuid(StatusVideoInput input) {
        jsqlClient.save(input,SaveMode.UPDATE_ONLY);
    }

    @Override
    public UserVideoView searchByUser(String uuid) {
        VideoTable table = Tables.VIDEO_TABLE;
        return jsqlClient
                .createQuery(table)
                .where(table.uuid().eqIf(uuid))
                .where(table.status().eq(Status.PUBLIC))
                .select(table.fetch(UserVideoView.class))
                .fetchOneOrNull();
    }

    @Override
    public Page<UserVideoView> getByLabel(List<Long> labelIds,PageParam pageParam) {
        VideoTable table = Tables.VIDEO_TABLE;
        if (labelIds.isEmpty()) {
            return getList(UserVideoView.class,pageParam);
        }
        return jsqlClient
                .createQuery(table)
                .where(table.status().eq(Status.PUBLIC))
                .where(table.dictionaries(dictionaryTableEx -> dictionaryTableEx.id().inIf(labelIds)))
                .select(table.fetch(UserVideoView.class))
                .fetchPage(pageParam.getPageIndex(), pageParam.getPageSize());
    }

}
