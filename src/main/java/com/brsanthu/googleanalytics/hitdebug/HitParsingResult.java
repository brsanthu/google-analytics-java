package com.brsanthu.googleanalytics.hitdebug;

import java.util.ArrayList;
import java.util.List;

public class HitParsingResult {
    private boolean valid;
    private String hit;
    private List<ParserMessage> parserMessage = new ArrayList<>();

    public boolean isValid() {
        return valid;
    }

    public HitParsingResult setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    public String getHit() {
        return hit;
    }

    public HitParsingResult setHit(String hit) {
        this.hit = hit;
        return this;
    }

    public List<ParserMessage> getParserMessage() {
        return parserMessage;
    }

    public HitParsingResult setParserMessage(List<ParserMessage> parserMessage) {
        this.parserMessage = parserMessage;
        return this;
    }

}
