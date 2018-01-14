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
	definition (name: "Autelis Pool Control", namespace: "lehighwiz", author: "Matthew Brennan") {
	capability "Switch"
	capability "Refresh"
            
    command "pump"
    command "WaterFeatureOff"
    command "WaterFeatureOn"
    command "WaterFeature"
    
    command "CleanerOff"
    command "CleanerOn"
    command "Cleaner"
    command "PoolSpaSpa"
    command "PoolSpaPool"
    command "PoolSpa"
    command "PumpOn"
    command "PumpOff"
    command "poolTemp"
    
  }

  tiles(scale: 1) {
    multiAttributeTile(name:"Pump", type: "generic", width: 2, height: 1){
      tileAttribute ("device.Pump", key: "PRIMARY_CONTROL") {
        attributeState "Off", label:'Off', action: "PumpOn",  icon:"st.Health & Wellness.health2", backgroundColor:"#ffffff"
        attributeState "On", label:'On', action: "PumpOff", icon:"st.Health & Wellness.health2", backgroundColor:"#00a0dc"
      }
    }
    standardTile("WaterFeature", "device.WaterFeature", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
	state "On", label: 'waterfall', action: "WaterFeatureOff", icon: "st.Outdoor.outdoor16", backgroundColor: "#00a0dc", nextState:"Off"
	state "Off", label: 'waterfall', action: "WaterFeatureOn", icon: "st.Outdoor.outdoor16", backgroundColor: "#ffffff", nextState:"On"
	}
    
    standardTile("Cleaner", "device.Cleaner", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
	state "On", label: 'Cleaner', action: "CleanerOff", icon: "st.Appliances.appliances13", backgroundColor: "#00a0dc", nextState:"Off"
	state "Off", label: 'Cleaner', action: "CleanerOn", icon: "st.Appliances.appliances13", backgroundColor: "#ffffff", nextState:"On"
	}
    
    standardTile("PoolSpa", "device.PoolSpa", width: 2, height: 2, decoration: "flat", canChangeIcon: true, canChangeBackground: true) {
	state "Spa", label: 'Mode: Spa', action: "PoolSpaPool", icon: "st.motion.motion.inactive", backgroundColor: "#ffffff", nextState:"Pool"
	state "Pool", label: 'Mode: Pool', action: "PoolSpaSpa", icon: "st.motion.motion.inactive", backgroundColor: "#ffffff", nextState:"Spa"
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
    
    main "Pump"
    details(["Pump", "PoolSpa", "WaterFeature", "Cleaner", "airTemp", "poolTemp", "spaTemp"])
  }
}

def airTemp(String state) {
	sendEvent (name: "airTemp", value: "${state}", descriptionText: "Air temp changed to: ${state}")
}

def poolTemp(String state) {
	if (device.currentValue("PoolSpa")=="Pool" && device.currentValue("Pump")=="On") {
	sendEvent (name: "poolTemp", value: "${state}", descriptionText: "Pool temp changed to: ${state}")
    }
    else { 
    sendEvent (name: "poolTemp", value: "---", descriptionText: "Pool temp changed to: Off")
    }
}

def spaTemp(String state) {
	if (device.currentValue("PoolSpa")=="Spa" && device.currentValue("Pump")=="On") {
	sendEvent (name: "spaTemp", value: "${state}", descriptionText: "Spa temp changed to: ${state}")
    }
    else { 
    sendEvent (name: "spaTemp", value: "---", descriptionText: "Spa temp changed to: Off")
    }
}

def Pump(String state) {
  sendEvent (name: "Pump", value: "${state}", descriptionText: "Pump state passively changed to: ${state}")
}

def PumpOff() {
sendEvent (name: "Pump", value: "Off", descriptionText: "Pump state changed to: Off")
parent.sendPoolCommand("pump","0")
runIn(4,parent.sendPoolCommand("pump","0"))
}

def PumpOn() {
sendEvent (name: "Pump", value: "On", descriptionText: "Pump state changed to: On")
parent.sendPoolCommand("pump","1")
}

def PoolSpa(String state) {
  sendEvent (name: "operatingMode", value: "${state}", descriptionText: "Mode changed to: ${state}")
  sendEvent (name: "PoolSpa", value: "${state}", descriptionText: "Pool/Spa state passively changed to: ${state}")
}

def PoolSpaSpa() {
sendEvent (name: "PoolSpa", value: "Spa", descriptionText: "Pool/Spa state changed to: Spa")
parent.sendPoolCommand("spa","1")
}

def PoolSpaPool() {
sendEvent (name: "PoolSpa", value: "Pool", descriptionText: "Pool/Spa state changed to: Pool")
parent.sendPoolCommand("spa","0")
}

def WaterFeature(String state, String stateDesc) {
sendEvent (name: "WaterFeature", value: "${state}", descriptionText: "Water feature state passively changed to: ${stateDesc}")
}

def WaterFeatureOff() {
sendEvent (name: "WaterFeature", value: "Off", descriptionText: "Water feature state changed to: Off")
parent.sendPoolCommand("aux3","0")
}

def WaterFeatureOn() {
sendEvent (name: "WaterFeature", value: "On", descriptionText: "Water feature state changed to: On")
parent.sendPoolCommand("aux3","1")
}

def Cleaner(String state, String stateDesc) {
sendEvent (name: "Cleaner", value: "${state}", descriptionText: "Cleaner state passively changed to: ${stateDesc}")
}

def CleanerOff() {
sendEvent (name: "Cleaner", value: "Off", descriptionText: "Cleaner state changed to: Off")
parent.sendPoolCommand("aux2","0")
}

def CleanerOn() {
sendEvent (name: "Cleaner", value: "On", descriptionText: "Cleaner state changed to: On")
parent.sendPoolCommand("aux2","1")
}