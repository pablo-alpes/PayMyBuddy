package com.paymybudy.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 *  * This code snippet is used thanks to frontbackend website:
 * https://frontbackend.com/thymeleaf/spring-boot-bootstrap-thymeleaf-table
 * It allows to build the pagination entirely with the classes with minimal intervention to the template and the controller/service.
 */

@Data
@Getter
@AllArgsConstructor
public class Page<T> {
    List<T> content;
    int totalPages;

}