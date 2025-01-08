package com.kxj.artadmin.dao;

import com.kxj.artadmin.model.ArtUser;
import com.kxj.artadmin.model.Artist;
import com.kxj.artadmin.model.PageParam;
import com.kxj.artadmin.model.dto.*;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface ArtUserDao {


    DetailUserView selectUserByName(String username);

    void register(RegisterUserInput input);

    <V extends View<ArtUser>> V getInfo(Class<V> viewType, String account);

    void update(UpdateUserInput userInput);

    List<AdminUserView> getUsers();

    void changeStatus(DetailUserView detailUserView);

    void changeLock(String account);

    UserCollectionSelectView getCollectionSelect(String account);

    void addVideoToCollection(AddUserCollectionInput input);
}
