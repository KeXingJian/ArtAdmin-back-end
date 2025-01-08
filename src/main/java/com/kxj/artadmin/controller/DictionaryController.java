package com.kxj.artadmin.controller;

import com.kxj.artadmin.model.dto.DictionaryTreeView;
import com.kxj.artadmin.model.dto.DictionaryView;
import com.kxj.artadmin.result.Result;
import com.kxj.artadmin.service.DictionaryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @GetMapping("getList")
    public Result<List<DictionaryView>> getList() {
        List<DictionaryView> list =dictionaryService.getList();
        return Result.success(list);
    }

    @GetMapping("getTree")
    public Result<List<DictionaryTreeView>> getFullTree() {
        List<DictionaryTreeView> list =dictionaryService.getFullTree();
        return Result.success(list);
    }

}
