package com.test;

import javax.ejb.Remote;

@Remote
public interface ControllerRemote {
	public void mainCall();
}
