/**
 *  Autelis Pool Control
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
	definition (name: "Pool/Spa: Main Pump", namespace: "lehighwiz", author: "Matthew Brennan") {
	capability "Switch"
	capability "Refresh"
            
    command "pump"
    command "PumpOn"
    command "PumpOff"
    
  }

  tiles(scale: 1) {
  // TODO: define your main and details tiles here
        standardTile("switch", "device.switch", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
		state "On", label: 'On', action: "PumpOff", icon: "st.Health & Wellness.health2", backgroundColor: "#00a0dc", nextState:"Off"
		state "Off", label: 'Off', action: "PumpOn", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState:"On"
		}
                
    valueTile("airTemp", "device.airTemp", width: 2, height: 2, decoration: "flat") {
	state("airTemp", label:'Air:\n${currentValue}')
	}
    valueTile("poolTemp", "device.poolTemp", width: 2, height: 2, decoration: "flat") {
	state("poolTemp", label:'Pool:\n${currentValue}')
	}
    valueTile("spaTemp", "device.spaTemp", width: 2, height: 2, decoration: "flat") {
	state("spaTemp", label:'Spa:\n${currentValue}')
	}
    
    main "switch"
    details(["switch", "airTemp", "poolTemp", "spaTemp"])
  }
}

def airTemp(String state) {
	sendEvent (name: "airTemp", value: "${state}", descriptionText: "Air temp changed to: ${state}")
}

def poolTemp(String state) {
	sendEvent (name: "poolTemp", value: "${state}", descriptionText: "Pool temp changed to: ${state}")
}

def spaTemp(String state) {
	sendEvent (name: "spaTemp", value: "${state}", descriptionText: "Spa temp changed to: ${state}")
}

def Pump(String state) {
  sendEvent (name: "switch", value: "${state}", descriptionText: "Pump state passively changed to: ${state}")
}

def PumpOff() {
log.debug "Here"
sendEvent (name: "switch", value: "Off", descriptionText: "Pump state changed to: Off")
parent.sendPoolCommand("pump","0")
runIn(4,parent.sendPoolCommand("pump","0"))
}

def PumpOn() {
log.debug "Here 2"
sendEvent (name: "switch", value: "On", descriptionText: "Pump state changed to: On")
parent.sendPoolCommand("pump","1")
}