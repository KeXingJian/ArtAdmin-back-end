package com.kxj.artadmin.dao.impl;

import com.kxj.artadmin.dao.ArtistDao;
import com.kxj.artadmin.enume.Status;
import com.kxj.artadmin.model.*;
import com.kxj.artadmin.model.dto.ArtistAddInput;
import com.kxj.artadmin.model.dto.ArtistSelectView;
import com.kxj.artadmin.model.dto.ArtistUpadteInput;
import com.kxj.artadmin.model.dto.AtistCollectionView;
import com.kxj.artadmin.model.PageParam;
import jakarta.annotation.Resource;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ArtistDaoImpl implements ArtistDao {

    @Resource
    private JSqlClient jsqlClient;

    @Override
    public <V extends View<Artist>> Page<V> getList(Class<V> viewType, PageParam pageParam) {
        ArtistTable table = Tables.ARTIST_TABLE;

        return jsqlClient
                .createQuery(table)
                .select(table.fetch(viewType))
                .fetchPage(pageParam.getPageIndex(), pageParam.getPageSize());

    }

    @Override
    public void addArtist(ArtistAddInput artistInput) {
        jsqlClient.save(artistInput);
    }

    @Override
    public void updateArtist(ArtistUpadteInput artistInput) {
        jsqlClient.update(artistInput);
    }

    @Override
    public void deleteArtist(Long artistId) {
        ArtistTable table = Tables.ARTIST_TABLE;
        jsqlClient
                .createDelete(table)
                .where(table.id().eq(artistId))
                .execute();
    }

    @Override
    public List<ArtistSelectView> getSelect() {
        ArtistTable table = Tables.ARTIST_TABLE;
        return jsqlClient.createQuery(table)
                .select(table.fetch(ArtistSelectView.class))
                .execute();
    }

    @Override
    public Page<AtistCollectionView> collections(PageParam pageParam) {
              ArtistTable table = Tables.ARTIST_TABLE;
        return jsqlClient
                .createQuery(table)
          //      .where(table.videos(video-> video.status().eq(Status.PUBLIC)))
                .select(table.fetch(AtistCollectionView.class))
                .fetchPage(pageParam.getPageIndex()-1, pageParam.getPageSize());
    }

}
