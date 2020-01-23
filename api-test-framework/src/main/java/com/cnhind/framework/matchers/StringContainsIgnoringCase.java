package com.cnhind.framework.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.SubstringMatcher;

public class StringContainsIgnoringCase extends SubstringMatcher {
    public StringContainsIgnoringCase(String substring) {
        super(substring);
    }

    @Override
    protected boolean evalSubstringOf(String s) {
        return s.toLowerCase().contains(substring.toLowerCase());
    }

    @Override
    protected String relationship() {
        return "containing (ignoring case)";
    }

    public static Matcher<String> containsStringIgnoringCase(String substring) {
        return new StringContainsIgnoringCase(substring);
    }

}
