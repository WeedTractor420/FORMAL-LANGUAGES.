package fei.tuke.sk.stmlang;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class Generator {

    private final StateMachineDefinition stateMachine;
    private final Writer writer;

    public Generator(StateMachineDefinition stateMachine, Writer writer) {
        this.stateMachine = stateMachine;
        this.writer = writer;
    }

    public void generate_code() throws IOException {
        writer.write("#include <stdio.h>\n");
        writer.write("#include \"common.h\"\n\n");

        for (String stateName : stateMachine.getStates().keySet()) {
            writer.write("void state_" + stateName + "();\n");
        }
        writer.write("\n");

        for (String stateName : stateMachine.getStates().keySet()) {
            writeState(stateName, stateMachine.getStates().get(stateName));
        }

        // Fetch the initial state name more robustly
        String initialStateName = stateMachine.getInitialStateName();
        if (initialStateName == null) {
            System.err.println("Warning: Initial state is undefined or not found. Falling back to a default state if available.");
            if (!stateMachine.getStates().isEmpty()) {
                initialStateName = stateMachine.getStates().keySet().iterator().next(); // Use the first state as a fallback
            } else {
                initialStateName = "undefinedState"; // Fallback when no states are defined
                writer.write("void state_undefinedState() {\n");
                writer.write("\t// Undefined state.\n");
                writer.write("}\n\n");
            }
        }

        writer.write("int main() {\n");
        writer.write("\tstate_" + initialStateName + "();\n");
        writer.write("\treturn 0;\n");
        writer.write("}\n");
    }

    private void writeState(String name, StateDefinition state) throws IOException {
        writer.write("void state_" + name + "() {\n");

        for (String action : state.getActions()) {
            Character event = stateMachine.getEvents().get(action);
            if (event != null) {
                writer.write("\tsend_event('" + event + "');\n");
            }
        }


        writer.write("\tchar ev;\n");
        writer.write("\twhile ((ev = read_command()) != '\\0') {\n");
        writer.write("\t\tswitch (ev) {\n");

        for (TransitionDefinition transition : state.getTransitions()) {
            Character command = stateMachine.getCommands().get(transition.commandName());
            if (command != null) {
                writer.write("\t\t\tcase '" + command + "':\n");
                if(!Objects.equals(transition.targetName(), "idle")) {
                    writer.write("\t\t\t\treturn state_" + transition.targetName() + "();\n");
                }else{
                    writer.write("\t\t\t\treturn;\n");
                }
            }
        }

        for (String resetCommand : stateMachine.getResetCommands()) {
            Character command = stateMachine.getCommands().get(resetCommand);
            if (command != null) {
                writer.write("\t\t\tcase '" + command + "':\n");
                writer.write("\t\t\t\treturn state_" + stateMachine.getInitialStateName() + "();\n");
            }
        }

        writer.write("\t\t\tdefault:\n");
        writer.write("\t\t\t\tbreak;\n");
        writer.write("\t\t}\n");
        writer.write("\t}\n");
        writer.write("}\n\n");
    }

}
