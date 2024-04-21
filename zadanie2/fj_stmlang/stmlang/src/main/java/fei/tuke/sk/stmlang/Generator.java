package fei.tuke.sk.stmlang;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Generator {

    private final StateMachineDefinition stateMachine;
    private final Writer writer;

    public Generator(StateMachineDefinition stateMachine, Writer writer) {
        this.stateMachine = stateMachine;
        this.writer = writer;
    }

    public void generate_code() throws IOException {
        // Initial boilerplate code generation
        writer.write("#include <stdio.h>\n");
        writer.write("#include \"common.h\"\n\n");

        // Ensure that commands exist
        if (stateMachine.getCommands().isEmpty()) {
            System.err.println("Warning: No commands defined in the state machine.");
        }

        // Check for multiple definitions for commands
        Set<Character> commandChars = new HashSet<>();
        Map<String, Character> commands = stateMachine.getCommands();
        for (Map.Entry<String, Character> entry : commands.entrySet()) {
            if (!commandChars.add(entry.getValue())) {
                System.err.println("Warning: Multiple definitions for command '" + entry.getKey() + "' found.");
            }
        }

        // Verify reset commands are valid
        for (String resetCommand : stateMachine.getResetCommands()) {
            if (!commands.containsKey(resetCommand)) {
                System.err.println("Warning: Reset command '" + resetCommand + "' is not defined in the commands.");
            }
        }

        // Check for valid states and initial state
        if (stateMachine.getStates().isEmpty()) {
            System.err.println("Warning: No states defined in the state machine.");
        }
        String initialStateName = stateMachine.getInitialStateName();
        if (initialStateName == null || !stateMachine.getStates().containsKey(initialStateName)) {
            System.err.println("Warning: Initial state is undefined or not found.");
            if (!stateMachine.getStates().isEmpty()) {
                initialStateName = stateMachine.getStates().keySet().iterator().next(); // Use the first state as a fallback
            } else {
                initialStateName = "undefinedState"; // Fallback when no states are defined
                writer.write("void state_undefinedState() {\n");
                writer.write("\t// Undefined state.\n");
                writer.write("}\n\n");
            }
        }

        // Generate state function prototypes
        for (String stateName : stateMachine.getStates().keySet()) {
            writer.write("void state_" + stateName + "();\n");
        }
        writer.write("\n");

        // Generate state functions
        for (String stateName : stateMachine.getStates().keySet()) {
            writeState(stateName, stateMachine.getStates().get(stateName));
        }

        // Main function
        writer.write("int main() {\n");
        writer.write("\tstate_" + initialStateName + "();\n");
        writer.write("\treturn 0;\n");
        writer.write("}\n");
    }

    private void writeState(String name, StateDefinition state) throws IOException {
        writer.write("void state_" + name + "() {\n");

        // Check and write actions
        for (String action : state.getActions()) {
            if (!stateMachine.getEvents().containsKey(action)) {
                System.err.println("Warning: Action '" + action + "' defined in state '" + name + "' is not defined in global actions.");
            }
            Character event = stateMachine.getEvents().get(action);
            if (event != null) {
                writer.write("\tsend_event('" + event.toString().toLowerCase() + "');\n");
            }
        }

        // Begin reading commands from input
        writer.write("\tchar ev;\n");
        writer.write("\twhile ((ev = read_command()) != '\\0') {\n");
        writer.write("\t\tswitch (ev) {\n");

        // Handle transitions
        for (TransitionDefinition transition : state.getTransitions()) {
            Character command = stateMachine.getCommands().get(transition.commandName());
            if (command == null) {
                System.err.println("Warning: Transition command '" + transition.commandName() + "' in state '" + name + "' is not defined in the commands.");
            } else {
                writer.write("\t\t\tcase '" + command + "':\n");
                if (stateMachine.getStates().containsKey(transition.targetName())) {
                    writer.write("\t\t\t\treturn state_" + transition.targetName() + "();\n");
                } else if (Objects.equals(transition.targetName(), "idle")) {
                    writer.write("\t\t\t\treturn;\n");
                } else {
                    System.err.println("Warning: Transition target state '" + transition.targetName() + "' in state '" + name + "' is not defined.");
                }
            }
        }

        // Handle reset commands
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
