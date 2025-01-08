package com.kxj.artadmin.dao;

import com.kxj.artadmin.model.Artist;
import com.kxj.artadmin.model.dto.ArtistAddInput;
import com.kxj.artadmin.model.dto.ArtistSelectView;
import com.kxj.artadmin.model.dto.ArtistUpadteInput;
import com.kxj.artadmin.model.dto.AtistCollectionView;
import com.kxj.artadmin.model.PageParam;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;

import java.util.List;

public interface ArtistDao {

    <V extends View<Artist>> Page<V> getList(Class<V> viewType, PageParam pageParam);


    void addArtist(ArtistAddInput artistInput);

    void updateArtist(ArtistUpadteInput artistInput);

    void deleteArtist(Long artistId);

    List<ArtistSelectView> getSelect();

    Page<AtistCollectionView> collections(PageParam pageParam);
}
