package com.dnd.backend.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.dnd.backend.domain.Campaign;

public class CampaignSpecification {
    
    public static Specification<Campaign> getSpecification(Map<String, String> params) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("title")) {
                predicates.add(builder.like(root.get("title"), params.get("title")));
            }

            if (params.containsKey("status")) {
                List<String> statuses = Arrays.asList(params.get("status").split(","));
                predicates.add(root.get("status").in(statuses));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Sort getSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.unsorted();
        }

        String[] sortParams = sortParam.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
                                   ? Sort.Direction.DESC
                                   : Sort.Direction.ASC;

        return Sort.by(direction, sortBy);
    }
}
