/**
 *  Pool/Spa: Pool Light
 *
 *  Copyright 2017 Matthew Brennan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Pool/Spa: Mode", namespace: "lehighwiz", author: "Matthew Brennan") {
		capability "Switch"
        capability "Refresh"
        capability "Sensor"
        capability "Actuator"
        capability "Indicator"
        capability "Polling"
        command "Control"
    
	tiles {
		// TODO: define your main and details tiles here
        standardTile("switch", "device.switch", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
		state "on", label: 'on', action: "switch.off", icon: "st.motion.motion.inactive", backgroundColor: "#00a0dc", nextState:"off"
		state "off", label: 'off', action: "switch.on", icon: "st.motion.motion.inactive", backgroundColor: "#ffffff", nextState:"on"
		}
	}
}
}


// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def Control(String state) {
sendEvent (name: "switch", value: "${state}", descriptionText: "State passively changed to: ${state}")
}


def off() {
sendEvent (name: "switch", value: "off", descriptionText: "State changed to: off")
parent.sendPoolCommand("spa","0")
//parent.TimerElapsed()
}

def on() {
sendEvent (name: "switch", value: "on", descriptionText: "State changed to: on")
parent.sendPoolCommand("spa","1")
//parent.TimerElapsed()
}