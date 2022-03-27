package com.pzi.blog.dao.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pzi
 * @create 2022-03-10 16:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationParam {

    private Long currentPage;

    private Long pageSize;

    private String queryString;

}
