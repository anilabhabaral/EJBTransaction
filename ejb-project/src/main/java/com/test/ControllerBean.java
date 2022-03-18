package com.test;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;


@Stateless(name = "Controller", mappedName = "usertransaction/Controller!com.banctec.cdi.ControllerRemote")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Local(ControllerLocal.class)
@Remote(ControllerRemote.class)

public class ControllerBean implements ControllerLocal, ControllerRemote {
	public void mainCall() {
		System.out.println("In mainCall");
	}
}

