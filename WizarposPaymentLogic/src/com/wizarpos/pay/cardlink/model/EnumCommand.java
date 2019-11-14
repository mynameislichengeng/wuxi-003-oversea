package com.wizarpos.pay.cardlink.model;

public enum EnumCommand
{ 
  GetParam(      1),
  SetParam(	     2),
  Login(         3),
  Balance(       4),
  Sale(		     5),
  UpcashSale(    6),
  VoidSale(		 7),
  Refund(		 8),
  Settle(		 9),
  EndTrans(	    10);
  
  private int cmd;
  
  private EnumCommand(int cmd)
  {
    this.cmd = cmd;
  }
  
  public int getCmdCode()
  {
    return this.cmd;
  }
  
  public static EnumCommand getCommand(int cmd)
  {
    for (EnumCommand enumCmd : EnumCommand.values()) {
      if (cmd == enumCmd.getCmdCode()) {
        return enumCmd;
      }
    }
    return null;
  }
}