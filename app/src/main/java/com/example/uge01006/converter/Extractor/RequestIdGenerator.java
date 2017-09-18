package com.example.uge01006.converter.Extractor;
import java.util.UUID;
public class RequestIdGenerator
{
    public int generate() {
        return UUID.randomUUID().hashCode();
    }
}
