package profundo.kml;

/*
 * 
 * Copyright 2009 Erik Martino Hansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class Placemark {
	/** <name>Mental Challenge #1</name> */
	public String name;

	/** <styleUrl>#cacheStyle8</styleUrl> */
	public String styleUrl;

	/**
	 * <Point> <coordinates>10.1891,56.0686</coordinates> </Point>
	 */
	public int longitude;
	public int latitude;

	public void setCoordinates(String newValue) {
		int cpos = newValue.indexOf(',');
		longitude = (int) (1e6f * Float.parseFloat(newValue.substring(0, cpos)));
		latitude = (int) (1e6f * Float.parseFloat(newValue.substring(cpos + 1)));
	}
	
	public String toString() {
		return "Placemark["+gc_code+": "+name+","+gc_wptTypeId+" N"+latitude+" E"+longitude+"]";
	}

	/* Extended Data */

	/**
	 * <Data name="gc_id"> <value>246492</value> </Data>
	 */
	public String gc_id;

	/**
	 * <Data name="gc_name"> <value>Mental Challenge #1</value> </Data>
	 */
	public String gc_name;

	/*
	 * <Data name="gc_wptTypeId"> <value>8</value> </Data>
	 */
	public int gc_wptTypeId;

	/*
	 * <Data name="gc_cacheType"> <value>Mystery Cache</value> </Data>
	 */
	public String gc_cacheType;

	/*
	 * <Data name="gc_code"> <value>GCP299</value> </Data>
	 */
	public String gc_code;

	/*
	 * <Data name="gc_containerId"> <value>8</value> </Data>
	 */
	public int gc_containerId;

	/*
	 * <Data name="gc_difficulty"> <value>07</value> </Data>
	 */
	public int gc_difficulty;

	/*
	 * <Data name="gc_terrain"> <value>04</value> </Data>
	 */
	public int gc_terrain;
}
