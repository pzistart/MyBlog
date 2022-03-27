package com.pzi.blog.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.ParagraphView;
import java.util.List;

/**
 * @author Pzi
 * @create 2022-03-10 16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationVo {

    private Long total;

    private List list;


}
