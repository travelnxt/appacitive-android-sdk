package com.appacitive.core.query;

import com.appacitive.core.infra.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley.
 */
public class TagFilter extends Filter {

    protected String operator = null;

    protected List<String> tags = new ArrayList<String>();

    public TagFilter matchOneOrMore(List<String> tags) {
        this.operator = "tagged_with_one_or_more";
        this.tags = tags;
        return this;
    }

    public TagFilter matchAll(List<String> tags) {
        this.operator = "tagged_with_all";
        this.tags = tags;
        return this;
    }

    @Override
    public String asString() {
        return String.format("%s('%s')", this.operator, StringUtils.join(this.tags, ","));
    }
}
