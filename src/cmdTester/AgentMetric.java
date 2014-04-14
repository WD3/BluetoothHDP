/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
package cmdTester;

import java.util.ArrayList;
import java.util.List;

public class AgentMetric {

	public ArrayList<Object> attributes = new ArrayList<Object>();
	public ArrayList<Object> measures = new ArrayList<Object>();

	public AgentMetric(){}

	public void addMeasure(Object obj) {
		measures.add(obj);
	}

	public void addAttribute(Object obj) {
		attributes.add(obj);
	}

	public List<Object> getAttributes(){
		return attributes;
	}

	public List<Object> getMeasures(){
		return measures;
	}

	public void clearMeasures() {
		measures.clear();
	}
	public void clearAttributes() {
		measures.clear();
	}
}
