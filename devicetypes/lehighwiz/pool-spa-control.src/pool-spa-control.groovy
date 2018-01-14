/**
 *  Pool/Spa: Generic Controller
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
	definition (name: "Pool/Spa: Control", namespace: "lehighwiz", author: "Matthew Brennan") {
		capability "Switch"
		capability "Refresh"
        command "Control"
        command "QueryStringParam"
        
    

	tiles {
		// TODO: define your main and details tiles here
        standardTile("switch", "device.switch", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
		state "On", label: 'On', action: "switch.off", icon: "st.Lighting.light11", backgroundColor: "#00a0dc", nextState:"Off"
		state "Off", label: 'Off', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState:"On"
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
sendEvent (name: "switch", value: "${state}", descriptionText: "State passively changed to: ${stateDesc}")
}

def QueryStringParam(String param, String switchLabel1, String switchLabel2, String switchValue1, String switchValue2) {
state.qStringParam=param
state.switchLabel1=switchLabel1
state.switchLabel2=switchLabel2
state.switchValue1=switchValue1
state.switchValue2=switchValue2
}

def off() {
sendEvent (name: "switch", value: "${state.switchLabel1}", descriptionText: "State changed to: ${state.switchLabel1}")
if (state.qStringParam=="pump") {
parent.sendPoolCommand("pump","0")
runIn(2,parent.sendPoolCommand("pump","0"))
} else {
parent.sendPoolCommand("${state.qStringParam}","${state.switchValue1}")
}
//parent.TimerElapsed()
}

def on() {
sendEvent (name: "switch", value: "${state.switchLabel2}", descriptionText: "State changed to: ${state.switchLabel2}")
parent.sendPoolCommand("${state.qStringParam}","${state.switchValue2}")
//parent.TimerElapsed()
}