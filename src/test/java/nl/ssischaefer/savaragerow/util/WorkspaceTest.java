package nl.ssischaefer.savaragerow.util;

import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import org.junit.jupiter.api.Test;

class WorkspaceTest {

    @Test
    public void testShouldDetectWorkspace()  {
        try{
            String currentWorkspace = Workspace.getCurrentWorkspace();
            System.out.println(currentWorkspace);
        }catch (WorkspaceNotSetException e) {
            System.out.println(e);
        }
    }
}