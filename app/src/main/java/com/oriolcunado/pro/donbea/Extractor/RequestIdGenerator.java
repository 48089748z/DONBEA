package com.oriolcunado.pro.donbea.Extractor;
import java.util.UUID;
public class RequestIdGenerator
{
    public int generate() {
        return UUID.randomUUID().hashCode();
    }
}
