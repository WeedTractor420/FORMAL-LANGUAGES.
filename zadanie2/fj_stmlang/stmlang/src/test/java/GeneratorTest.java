import fei.tuke.sk.stmlang.Generator;
import fei.tuke.sk.stmlang.StateMachineDefinition;
import fei.tuke.sk.stmlang.StateDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {
    private StateMachineDefinition stateMachine;
    private StringWriter writer;
    private Generator generator;

    @BeforeEach
    void setUp() {
        // Initialize StringWriter and StateMachineDefinition
        writer = new StringWriter();
        stateMachine = new StateMachineDefinition();
    }

    @Test
    void testCodeGenerationForSingleState() {
        stateMachine.setInitialStateName("idle");
        stateMachine.addState("idle", new StateDefinition());
        generator = new Generator(stateMachine, writer);

        try {
            generator.generate_code();
            String generatedCode = writer.toString();

            // Assertions to check the correct generation of code
            assertTrue(generatedCode.contains("#include <stdio.h>\n"));
            assertTrue(generatedCode.contains("#include \"common.h\"\n"));
            assertTrue(generatedCode.contains("void state_idle();\n"));
            assertTrue(generatedCode.contains("int main() {\n"));
            assertTrue(generatedCode.contains("\tstate_idle();\n"));
            assertTrue(generatedCode.contains("\treturn 0;\n"));
            assertTrue(generatedCode.contains("}\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInitialStateFallback() {
        stateMachine.setInitialStateName("initState");
        stateMachine.addState("initState", new StateDefinition());
        generator = new Generator(stateMachine, writer);

        try {
            generator.generate_code();
            String generatedCode = writer.toString();

            // Check the generation when the initial state is defined
            assertTrue(generatedCode.contains("void state_initState();\n"));
            assertTrue(generatedCode.contains("void state_initState() {\n"));
            assertTrue(generatedCode.contains("\tchar ev;\n"));
            assertTrue(generatedCode.contains("\twhile ((ev = read_command()) != '\\0') {\n"));
            assertTrue(generatedCode.contains("\t\tswitch (ev) {\n"));
            assertTrue(generatedCode.contains("\t\t\tdefault:\n"));
            assertTrue(generatedCode.contains("\t\t\t\tbreak;\n"));
            assertTrue(generatedCode.contains("\t\t}\n"));
            assertTrue(generatedCode.contains("\t}\n"));
            assertTrue(generatedCode.contains("}\n"));
            assertTrue(generatedCode.contains("int main() {\n"));
            assertTrue(generatedCode.contains("\tstate_initState();\n"));
            assertTrue(generatedCode.contains("\treturn 0;\n"));
            assertTrue(generatedCode.contains("}\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
