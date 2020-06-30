package io.github.thesixonenine.search.web;

import io.github.thesixonenine.search.service.ElasticSearchService;
import io.github.thesixonenine.search.vo.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/30 23:39
 * @since 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping(value = {"", "/search.html"})
    public String search(@RequestBody SearchCondition condition){
        elasticSearchService.search(condition);
        return "search";
    }
}
