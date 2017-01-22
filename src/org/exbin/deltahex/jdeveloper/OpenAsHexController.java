package org.exbin.deltahex.jdeveloper;

import oracle.ide.Context;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.extension.RegisteredByExtension;


/**
 * Controller for action org.exbin.deltahex.jdeveloper.openashex.
 */
@RegisteredByExtension("org.exbin.deltahex.jdeveloper")
public final class OpenAsHexController implements Controller {
    public boolean update(IdeAction action, Context context) {
        // TODO Determine when org.exbin.deltahex.jdeveloper.openashex is enabled, and call action.setEnabled().
        return true;
    }

    public boolean handleEvent(IdeAction action, Context context) {
        // TODO: handle the command here.
        return false;
    }
}
