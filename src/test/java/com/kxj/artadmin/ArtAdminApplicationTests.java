package com.kxj.artadmin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kxj.artadmin.dao.ArtUserDao;
import com.kxj.artadmin.model.dto.AddUserCollectionInput;
import com.kxj.artadmin.service.VideoService;
import com.kxj.artadmin.utils.FileUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ArtAdminApplicationTests {

    @Resource
    private VideoService videoService;


    @Test
    void contextLoads() {
        videoService.loading();
    }


    @Resource
    private ArtUserDao artUserDao;

    @Test
    void demo(){
        AddUserCollectionInput input = new AddUserCollectionInput();

        List<AddUserCollectionInput.TargetOf_collections.TargetOf_videos> videosList = new ArrayList<>();
        AddUserCollectionInput.TargetOf_collections.TargetOf_videos video = new AddUserCollectionInput.TargetOf_collections.TargetOf_videos();
        video.setUuid("6b27a3b5a6334413a62ded04274a4834");
        videosList.add(video);

        AddUserCollectionInput.TargetOf_collections collection = new AddUserCollectionInput.TargetOf_collections();
        collection.setId(7L);
        collection.setVideos(videosList);

        List<AddUserCollectionInput.TargetOf_collections> list = new ArrayList<>();
        list.add(collection);
        input.setCollections(list);
        input.setAccount("2787901285");

        System.out.println(input);

        artUserDao.addVideoToCollection(input);
    }

    public static void main(String[] args) {
        int stop=0;

        for (;;) {
            if (stop==5) {
                break;
            }
            stop++;
            System.out.println(stop);
        }
    }


}
