package it.introsoft.banker.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CategorySum {

    private final Integer year;

    private final Integer month;

    private final String category;

    private final Long transferCount;

    private final Long transferAmountSum;

}