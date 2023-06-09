package com.github.janbnz;

import com.github.janbnz.rest.ActionRegistry;
import com.github.janbnz.rest.RestServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ActionRegistry.init();
        new RestServer();
    }
}