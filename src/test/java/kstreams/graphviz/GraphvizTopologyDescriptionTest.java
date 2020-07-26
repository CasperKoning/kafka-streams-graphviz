package kstreams.graphviz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    @Test
    void testNothing() {
        String description = GraphvizTopologyDescription.describeGraphviz();
        assertNotNull(description);
    }
}
