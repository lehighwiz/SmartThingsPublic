/**
 *  Virtual Generic Three Position Switch
 *
 *  Copyright 2018 Matthew Brennan 
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
	definition (name: "Virtual Three Position Switch", namespace: "lehighwiz", author: "Matthew Brennan") {
		capability "Actuator"
		capability "Switch"
        command "Control"
        
	tiles {
		// TODO: define your main and details tiles here
        standardTile("switch", "device.switch", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
		state "on", label: 'on', action: "switch.changingoff", icon: "st.Lighting.light11", backgroundColor: "#00a0dc", nextState:"changingoff"
        state "changingoff", label: 'changingoff', action: "switch.off", icon: "st.Lighting.light11", backgroundColor: "#00a0dc", nextState:"off"
        state "changingon", label: 'changingon', action: "switch.on", icon: "st.Lighting.light11", backgroundColor: "#00a0dc", nextState:"on"
		state "off", label: 'off', action: "switch.changingon", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState:"changingon"
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
def Control(String state, String stateDesc) {
sendEvent (name: "switch", value: "${state}", descriptionText: "State changed to: ${stateDesc}")
}

def off() {
sendEvent (name: "switch", value: "off", descriptionText: "State changed to: off")
}

def on() {
sendEvent (name: "switch", value: "on", descriptionText: "State changed to: on")
}

def changingoff() {
sendEvent (name: "switch", value: "changingoff", descriptionText: "State changed to: changingoff")
}

def changingon() {
sendEvent (name: "switch", value: "changingon", descriptionText: "State changed to: changingon")
}