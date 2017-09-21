package com.extractor.uri48089748z.converter.Extractor;
import java.util.UUID;
public class RequestIdGenerator
{
    public int generate() {
        return UUID.randomUUID().hashCode();
    }
}
