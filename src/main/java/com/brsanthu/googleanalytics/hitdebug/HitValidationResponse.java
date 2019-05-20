package com.brsanthu.googleanalytics.hitdebug;

import java.util.ArrayList;
import java.util.List;

public class HitValidationResponse {
    private List<HitParsingResult> hitParsingResult = new ArrayList<>();
    private List<ParserMessage> parserMessage = new ArrayList<>();

    public HitValidationResponse() {
        // default
    }

    public List<HitParsingResult> getHitParsingResult() {
        return hitParsingResult;
    }

    public HitValidationResponse setHitParsingResult(List<HitParsingResult> hitParsingResult) {
        this.hitParsingResult = hitParsingResult;
        return this;
    }

    public List<ParserMessage> getParserMessage() {
        return parserMessage;
    }

    public HitValidationResponse setParserMessage(List<ParserMessage> parserMessage) {
        this.parserMessage = parserMessage;
        return this;
    }
}
