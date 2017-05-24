package br.com.matteroftime.util;

import com.squareup.otto.Bus;

/**
 * Created by deni on 24/05/2017.
 */

public class EventBus extends Bus {
    private static final EventBus BUS = new EventBus();

    public static Bus getInstance(){
        return BUS;
    }

    private EventBus(){}



}
