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
package es.libresoft.openhealth.utils;

import java.util.ArrayList;

public class FIFO<T> implements IFIFO<T>{
	private ArrayList<T> fifoList;;
	private IUnlock handler;

	public FIFO ()
	{
		fifoList = new ArrayList<T>();
	}

	@Override
	public synchronized boolean add(T obj) {
		if (fifoList.add(obj)){
			if (handler!=null) handler.unlock();
			return true;
		}else return false;
	}

	@Override
	public synchronized T remove() {
		return fifoList.remove(0);
	}

	@Override
	public synchronized int size() {
		return fifoList.size();
	}

	public synchronized String toString(){
		return fifoList.toString();
	}

	@Override
	public synchronized void clear() {
		fifoList.clear();
	}

	@Override
	public synchronized void setHandler(IUnlock handler) {
		this.handler = handler;
	}

}
