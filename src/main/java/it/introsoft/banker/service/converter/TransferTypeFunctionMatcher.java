package it.introsoft.banker.service.converter;

import it.introsoft.banker.model.raw.TransferType;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;
import java.util.regex.Matcher;

@AllArgsConstructor
@Value
class TransferTypeFunctionMatcher {
    private final Function<String, Matcher> matcherFunction;
    private final TransferType transferType;

    static TransferTypeFunctionMatcher of(TransferType transferType, Function<String, Matcher> matcherFunction) {
        return new TransferTypeFunctionMatcher(matcherFunction, transferType);
    }
}
