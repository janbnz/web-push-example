package com.github.janbnz.rest;

import com.github.janbnz.rest.actions.SubscribeAction;

import java.util.ArrayList;
import java.util.List;

public class ActionRegistry {

    private static final List<RestAction> actions = new ArrayList<>();

    /**
     * Initializes the ActionRegistry by adding the necessary RestActions.
     */
    public static void init() {
        actions.add(new SubscribeAction());
    }

    public static List<RestAction> getActions() {
        return actions;
    }
}