package nl.ssischaefer.savaragerow.v2.util;

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