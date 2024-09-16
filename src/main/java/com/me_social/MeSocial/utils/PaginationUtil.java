package com.me_social.MeSocial.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PaginationUtil {

    /**
     * Converts a Set to a Page.
     *
     * @param set the Set to be paginated
     * @param pageable the pagination information
     * @param <T> the type of elements in the Set
     * @return a Page containing the elements of the Set
     */
    public static <T> Page<T> convertSetToPage(Set<T> set, Pageable pageable) {
        List<T> list = set.stream().collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        List<T> paginatedList = list.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, list.size());
    }
}
