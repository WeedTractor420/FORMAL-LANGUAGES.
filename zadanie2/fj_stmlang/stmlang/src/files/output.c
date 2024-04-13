#include <stdio.h>
#include "common.h"

void state_initState();
void state_active();
void state_waitingForAirConditionerAndWindow();
void state_waitingForLightAndWindow();
void state_waitingForAirConditionerAndLight();
void state_waitingForAirConditioner();
void state_waitingForLight();
void state_waitingForWindow();
void state_safeOpened();

void state_initState() {
	send_event('doorUnlocked');
	send_event('safeLocked');
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'c':
				return state_active();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_active() {
	send_event('doorLocked');
	send_event('safeLocked');
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'l':
				return state_waitingForAirConditionerAndWindow();
			case 'b':
				return state_waitingForLightAndWindow();
			case 'w':
				return state_waitingForAirConditionerAndLight();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForAirConditionerAndWindow() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'b':
				return state_waitingForLight();
			case 'w':
				return state_waitingForAirConditioner();
			case 't':
				return state_active();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForLightAndWindow() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'l':
				return state_waitingForWindow();
			case 'w':
				return state_waitingForLight();
			case 'n':
				return state_active();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForAirConditionerAndLight() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'l':
				return state_waitingForAirConditioner();
			case 'b':
				return state_waitingForLight();
			case 'e':
				return state_active();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForAirConditioner() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'b':
				return state_safeOpened();
			case 't':
				return state_waitingForAirConditionerAndLight();
			case 'e':
				return state_waitingForAirConditionerAndWindow();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForLight() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'l':
				return state_safeOpened();
			case 'n':
				return state_waitingForAirConditionerAndLight();
			case 'e':
				return state_waitingForLightAndWindow();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_waitingForWindow() {
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 'w':
				return state_safeOpened();
			case 't':
				return state_waitingForLightAndWindow();
			case 'n':
				return state_waitingForAirConditionerAndWindow();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

void state_safeOpened() {
	send_event('doorLocked');
	send_event('safeUnlocked');
	char ev;
	while ((ev = read_command()) != '\0') {
		switch (ev) {
			case 's':
				return state_idle();
			case 'a':
				return state_initState();
			case 'o':
				return state_initState();
			default:
				break;
		}
	}
}

int main() {
	state_initState();
	return 0;
}
