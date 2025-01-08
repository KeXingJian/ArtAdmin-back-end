package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.ArtUserDao;
import com.kxj.artadmin.dao.VideoDao;
import com.kxj.artadmin.model.*;
import com.kxj.artadmin.model.dto.*;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArtUserDaoImpl implements ArtUserDao {

    private final ArtUserTable table = Tables.ART_USER_TABLE;
    private final CollectionTable collectionTable = Tables.COLLECTION_TABLE;

    @Resource
    private JSqlClient jsqlClient;

    @Resource
    private VideoDao videoDao;

    @Override
    public DetailUserView selectUserByName(String username) {
        return jsqlClient
                .createQuery(table)
                .where(table.account().eq(username))
                .select(table.fetch(DetailUserView.class))
                .fetchOneOrNull();
    }

    @Override
    public void register(RegisterUserInput input) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password ="{bcrypt}";
        ArtUser artUser = Immutables.createArtUser(draft -> {
            draft.setName(input.getName());

            draft.setPassword(password+passwordEncoder.encode(input.getPassword()));
            draft.setAccount(input.getAccount());
            draft.setPicture(Immutables.createPicture(picture -> {
                picture.setId(1L);
            }));
            draft.setRole(Immutables.createRole(role -> {
                role.setName("user");
            }));
            draft.setStop(false);

        });
        jsqlClient.save(artUser, SaveMode.INSERT_ONLY);
    }

    @Override
    public <V extends View<ArtUser>> V getInfo(Class<V> viewType, String account) {
        return jsqlClient.createQuery(table)
                .where(table.account().eq(account))
                .select(table.fetch(viewType))
                .fetchOneOrNull();
    }

    @Override
    public void update(UpdateUserInput userInput) {
        jsqlClient.save(userInput, SaveMode.UPDATE_ONLY);
    }

    @Override
    public List<AdminUserView> getUsers() {
        return jsqlClient
                .createQuery(table)
                .select(table.fetch(AdminUserView.class))
                .execute();
    }

    @Override
    public void changeStatus(DetailUserView detailUserView) {
        jsqlClient.update(
                Immutables.createArtUser(draft -> {
                    draft.setAccount(detailUserView.getAccount());
                    draft.setStatus(true);
                })
        );
    }

    @Override
    public void changeLock(String account) {
        DetailUserView detailUserView = selectUserByName(account);
        jsqlClient.update(
                Immutables.createArtUser(draft -> {
                    draft.setAccount(account);
                    draft.setStop(!detailUserView.isStop());
                })
        );
    }

    @Override
    public UserCollectionSelectView getCollectionSelect(String account) {
        return jsqlClient.createQuery(table)
                .where(table.account().eq(account))
                .select(table.fetch(UserCollectionSelectView.class))
                .fetchOneOrNull();
    }

    @Override
    public void addVideoToCollection(AddUserCollectionInput input) {
        //使用account和collectionId查询是否有该集合
        SimpleCollection view = jsqlClient.createQuery(collectionTable)
                .where(collectionTable.id().eq(input.getCollections().getFirst().getId()))
                .where(collectionTable.artUser().account().eq(input.getAccount()))
                .select(collectionTable.fetch(SimpleCollection.class))
                .fetchOneOrNull();
        if (view != null) {
           //查询视频
            VideoCollectionInput collectionInput = getvideoCollectionInput(input);
            try {
                jsqlClient.save(collectionInput,AssociatedSaveMode.APPEND_IF_ABSENT);
            }catch (Exception e) {
                System.out.println("重复插入");
            }


        }


    }

    private static @NotNull VideoCollectionInput getvideoCollectionInput(AddUserCollectionInput input) {
        //创建input对象
        VideoCollectionInput videoCollectionInput = new VideoCollectionInput();
        //设置集合id
        videoCollectionInput.setId(input.getCollections().getFirst().getId());
        //创建相应视频uuid
        VideoCollectionInput.TargetOf_videos targetOfVideos = new VideoCollectionInput.TargetOf_videos();
        targetOfVideos.setUuid(input.getCollections().getFirst().getVideos().getFirst().getUuid());
        //放入视频集合
        List<VideoCollectionInput.TargetOf_videos> list = new ArrayList<>();
        list.add(targetOfVideos);
        //放入集合
        videoCollectionInput.setVideos(list);
        return videoCollectionInput;
    }


}
