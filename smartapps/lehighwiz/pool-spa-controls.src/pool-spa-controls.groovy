/**
 *  Pool/Spa Control
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
definition(
    name: "Pool/Spa Controls",
    namespace: "lehighwiz",
    author: "Matthew Brennan",
    description: "Pool/Spa Control using an Autelis box. (v 1.0)",
    category: "My Apps",
    iconUrl: "http://cdn.device-icons.smartthings.com/Health%20&%20Wellness/health2-icn.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Health%20&%20Wellness/health2-icn@2x.png",
    iconX3Url: "http://cdn.device-icons.smartthings.com/Health%20&%20Wellness/health2-icn@2x.png")


preferences {
	section("SmartThings Hub") {
    input "hostHub", "hub", title: "Select Hub", multiple: false, required: true
  }
  section("Autelis Connection Details") {
    input "autelisURL", "text", title: "Autelis External URL", description: "(ie. http://admin:admin@xxx.xxx.xxx.xxx:8080)", required: true
  }
}

def installed() {
	removeChildDevices()
	runIn(5,initialize)
}

def updated() {
	removeChildDevices()
	runIn(2,initialize)
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    
   //Init all state vars
   state.WaterFeature = ""
    state.Cleaner = ""
    state.Pump = ""
    state.PoolSpa = ""
    state.airTemp = ""
    state.poolTemp = ""
    state.spaTemp = ""
    state.PoolSpa = ""
    state.PoolLight = ""
    state.SpaLight = ""
    state.Cleaner = ""
    state.WaterFeature = ""
    
   //Main Pump and Control Panel Init 
   if (!getChildDevice("10000000")) {
    addChildDevice("lehighwiz", "Pool/Spa: Control", "10000000", hostHub.id, ["name": "Pool/Spa: Main Pump", label: "Pool/Spa: Main Pump", completedSetup: true])
     getChildDevice("10000000").QueryStringParam("pump", "Off", "On", "0", "1")
    log.debug "Adding device: 10000000"
    }
    
    //Pool Mode Init   
    if (!getChildDevice("10000050")) {
    addChildDevice("lehighwiz", "Pool/Spa: Mode", "10000050", hostHub.id, ["name": "Pool/Spa: Mode", label: "Pool/Spa: Mode", completedSetup: true])
    log.debug "Adding device: 10000050 (Pool/Spa Switch)"
    }
    
    //Pool Light Init   
    if (!getChildDevice("10000100")) {
    addChildDevice("lehighwiz", "Pool/Spa: Control", "10000100", hostHub.id, ["name": "Pool/Spa: Pool Light", label: "Pool/Spa: Pool Light", completedSetup: true])
    getChildDevice("10000100").QueryStringParam("aux4", "Off", "On", "0", "1")
        log.debug "Adding device: 10000100 (Pool Light)"
    }
    
    //Spa Light Init
    if (!getChildDevice("10000200")) {
    addChildDevice("lehighwiz", "Pool/Spa: Control", "10000200", hostHub.id, ["name": "Pool/Spa: Spa Light", label: "Pool/Spa: Spa Light", completedSetup: true])
    getChildDevice("10000200").QueryStringParam("aux1", "Off", "On", "0", "1")
       log.debug "Adding device: 10000200 (Spa Light)"
    }
    
    //Cleaner Init
    if (!getChildDevice("10000300")) {
    addChildDevice("lehighwiz", "Pool/Spa: Control", "10000300", hostHub.id, ["name": "Pool/Spa: Cleaner", label: "Pool/Spa: Cleaner", completedSetup: true])
    getChildDevice("10000300").QueryStringParam("aux2", "Off", "On", "0", "1")
        log.debug "Adding device: 10000300 (Cleaner)"
    }
    
     //Water Feature Init
    if (!getChildDevice("10000400")) {
    addChildDevice("lehighwiz", "Pool/Spa: Control", "10000400", hostHub.id, ["name": "Pool/Spa: Water Feature", label: "Pool/Spa: Water Feature", completedSetup: true])
    getChildDevice("10000400").QueryStringParam("aux3", "Off", "On", "0", "1")
        log.debug "Adding device: 10000400 (Water Feature)"
    }
    
    runIn(5,TimerElapsed)
   
}

private removeChildDevices() {
	unschedule()
  getAllChildDevices().each { deleteChildDevice(it.deviceNetworkId)
  log.debug "Removing device: ${it.deviceNetworkId}"
  }
}

def TimerElapsed() {
	unschedule() 
    
	def params = [
    uri: "${autelisURL.value}/status.xml",
    contentType: 'text/xml']
	//log.debug params
	try {
    httpGet(params) { resp ->
    
    //iterate payload data by node
    resp.data[0].children().each {
    it.children().each{
    
    
    try {
    // Capture operatingMode changes
    if (it.name == "spa") {
    def eventMap = ['1':"Spa",'0':"Pool"]
    def newState = eventMap."${it.text()}"
    if (state.PoolSpa != newState) {
    state.poolTemp="0"
    state.spaTemp="0"
    log.debug "Raise Pool/Spa Event Value: ${newState}"
    getChildDevice("10000050").Control(newState) 
    state.PoolSpa= newState}}
    }catch (e) {
    getChildDevice("10000050").Control(newState)
    state.PoolSpa= newState
    log.error "Error reading device state Pool/Spa, reinitializing: $e"
	}
    
    try {
    // Capture poolPump changes
    if (it.name == "pump") {
    def eventMap = ['1':"On",'0':"Off"]
    def newState = eventMap."${it.text()}"
    if (state.Pump != newState) {
    state.poolTemp="0"
    state.spaTemp="0"
    
    //Temp Feature Init
    if (newState == "On") {
    if (!getChildDevice("10000500")) {
    if (state.PoolSpa=="Pool") {
    addChildDevice("lehighwiz", "Pool/Spa: Temp Viewer", "10000500", hostHub.id, ["name": "Pool/Spa: Pool Temp", label: "Pool/Spa: Pool Temp", completedSetup: true])
    log.debug "Adding device: 10000500 (Pool Temp)"
    }
    if (state.PoolSpa=="Spa") {
    addChildDevice("lehighwiz", "Pool/Spa: Temp Viewer", "10000500", hostHub.id, ["name": "Pool/Spa: Spa Temp", label: "Pool/Spa: Spa Temp", completedSetup: true])
    log.debug "Adding device: 10000500 (Spa Temp)"}}
    } else {
    if (getChildDevice("10000500")) {deleteChildDevice("10000500")}
    }
 
    log.debug "Raise Pump Changed Event Value: ${newState}"
    getChildDevice("10000000").Control(newState, newState) 
    state.Pump = newState
    if (state.PoolSpa=="") {state.pump=""}}}
    }catch (e) {
    getChildDevice("10000000").Control(newState, newState)
    state.Pump = newState
    log.error "Error reading device state Pump, reinitializing: $e"
	}
    
    try {
    // Capture pooltemp changes
    if (it.name == "pooltemp") {
    if (state.poolTemp != it.text()) {
    if (state.PoolSpa=="Pool") {
    if (state.Pump=="On") {
    log.debug "Raise poolTemp Event Value: ${it.text()}"
    if (getChildDevice("10000500")) {getChildDevice("10000500").Control(Integer.parseInt(it.text()))
    state.poolTemp= it.text()}}}}}
    }catch (e) {
    if (getChildDevice("10000500")) {getChildDevice("10000500").Control(Integer.parseInt(it.text()))
    state.poolTemp= it.text()}
    log.error "Error reading device state poolTemp, reinitializing: $e"
	}
    
    try {
    // Capture spaTemp changes
    if (it.name == "spatemp") {
    if (state.spaTemp != it.text()) {
    if (state.PoolSpa=="Spa") {
    if (state.Pump=="On") {
    log.debug "Raise spaTemp Event Value: ${it.text()}"
    if (getChildDevice("10000500")) {getChildDevice("10000500").Control(Integer.parseInt(it.text())) 
    state.spaTemp= it.text()}}}}}
    }catch (e) {
    if (getChildDevice("10000500")) {getChildDevice("10000500").Control(Integer.parseInt(it.text()))
    state.spaTemp= it.text()}
    log.error "Error reading device state spaTemp, reinitializing: $e"
	}
    
    try {
    // Capture waterfeature changes
    if (it.name == "aux3") {
    def eventMap = ['1':"On",'0':"Off"]
    def newState = eventMap."${it.text()}"
    if (state.WaterFeature != newState) {
    log.debug "Raise waterFeature Event Value: ${newState}"
    getChildDevice("10000400").Control(newState, newState)
    state.WaterFeature = newState }}
    }catch (e) {
    getChildDevice("10000400").Control(newState, newState)
    state.WaterFeature = newState
    log.error "Error reading device state WaterFeature, reinitializing: $e"
	}
    
    try {
    // Capture poolLight changes
    if (it.name == "aux4") {
    def eventMap = ['1':"On",'0':"Off"]
    def newState = eventMap."${it.text()}"
    if (state.PoolLight != newState) {
    log.debug "Raise Pool Light Event Value: ${newState}"
    getChildDevice("10000100").Control(newState, newState)
    state.PoolLight= newState}}
    }catch (e) {
    getChildDevice("10000100").Control(newState, newState)
    state.PoolLight= newState
    log.error "Error reading device state Pool Light, reinitializing: $e"
	}
    
    try {
    // Capture spaLight changes
    if (it.name == "aux1") {
    def eventMap = ['1':"On",'0':"Off"]
    def newState = eventMap."${it.text()}"
    if (state.SpaLight != newState) {
    log.debug "Raise Spa Light Event Value: ${newState}"
    getChildDevice("10000200").Control(newState, newState)
    state.SpaLight= newState}}
    }catch (e) {
    getChildDevice("10000200").Control(newState, newState)
    state.SpaLight= newState
    log.error "Error reading device state Spa Light, reinitializing: $e"
	}
    
    try {
    // Capture cleaner changes
    if (it.name == "aux2") {
    def eventMap = ['1':"On",'0':"Off"]
    def newState = eventMap."${it.text()}"
    if (state.Cleaner != newState) {
    log.debug "Raise Cleaner Event Value: ${newState}"
    getChildDevice("10000300").Control(newState, newState) 
    state.Cleaner = newState}}
    }catch (e) {
    getChildDevice("10000300").Control(newState, newState)
    state.Cleaner = newState
    log.error "Error reading device state Cleaner, reinitializing: $e"
	}
    
	}}}
    }
    catch (e) {
    log.error "something went wrong: $e"
	}
    
    runEvery1Minute(TimerElapsed)
    
}

def sendPoolCommand(String Field, String Value) {
def params = [uri: "${autelisURL.value}/set.cgi?name=${Field}&value=${Value}"]
//log.debug params
	try {
    httpGet(params) { resp ->
 
    }}catch (e) {
    log.error "something went wrong: $e"
	}
    TimerElapsed()
}

// TODO: implement event handlers