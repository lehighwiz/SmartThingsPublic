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
	definition (name: "Pool/Spa: Temp Viewer", namespace: "lehighwiz", author: "Matthew Brennan") {
		capability "Temperature Measurement"
		capability "Sensor"
		capability "Refresh"
        command "Control"
	tiles {
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
    state("temperature", label:'${currentValue}', unit:"dF",
        backgroundColors:[
            [value: 65, color: "#153591"],
            [value: 70, color: "#1e9cbb"],
            [value: 75, color: "#90d2a7"],
            [value: 80, color: "#44b621"],
            [value: 84, color: "#f1d801"],
            [value: 95, color: "#d04e00"],
            [value: 103, color: "#bc2323"]])}}
}
}
// handle commands
def Control(Integer newTemp) {
sendEvent (name: "temperature", value: "${newTemp}", descriptionText: "Temperature changed to: ${newTemp}")
}
