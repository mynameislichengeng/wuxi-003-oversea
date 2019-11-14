package com.wizarpos.device.printer;

public class LEDInterface
{
	static
	{
		System.loadLibrary("wizarpos_led");
	}
    public native static int open();
    public native static int close();
    public native static int turn_on(int index);
    public native static int turn_off(int index);
    public native static int get_status(int index);

}
