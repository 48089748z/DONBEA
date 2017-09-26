package com.oriolcunado.pro.multi.Extractor;
import java.util.UUID;
public class RequestIdGenerator
{
    public int generate() {
        return UUID.randomUUID().hashCode();
    }
}
