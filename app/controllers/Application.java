package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {
    @Before
    @SuppressWarnings("unused")
    public static void before() throws Exception {
        if (Security.isConnected())
            renderArgs.put("user", Security.getSessionUser());
    }

    public static void index() {
        render();
    }
}
