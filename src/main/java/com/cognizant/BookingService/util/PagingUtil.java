package com.cognizant.BookingService.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

public class PagingUtil {
    public static Pageable buildPageable(Integer page, Integer size, String sort, String direction, List<String> allowedProps) {
        if (page == null || size == null) {
            return null;
        }
        int p = Math.max(0, page);
        int s = Math.min(Math.max(1, size), 100);
        Sort sortSpec = buildSort(sort, direction, allowedProps);
        if (sortSpec.isUnsorted()) {
            return PageRequest.of(p, s);
        }
        return PageRequest.of(p, s, sortSpec);
    }

    public static Sort buildSort(String sort, String direction, List<String> allowedProps) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }
        Set<String> allowed = new HashSet<>(allowedProps);
        String property = sort.trim();
        if (!allowed.contains(property)) {
            return Sort.unsorted();
        }
        Sort.Direction dir = (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(dir, property);
    }

    public static HttpHeaders buildHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        long total = page.getTotalElements();
        int start = page.getNumber() * page.getSize();
        int end = start + page.getNumberOfElements() - 1;
        headers.add("X-Total-Count", String.valueOf(total));
        headers.add("Content-Range", String.format("items %d-%d/%d", start, Math.max(start, end), total));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count, Content-Range");
        return headers;
    }
}
