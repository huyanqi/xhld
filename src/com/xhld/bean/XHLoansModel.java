package com.xhld.bean;

import java.io.Serializable;

public class XHLoansModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public int amount;
  public double interest;
  public String loanuuid;
  public int month;
  public String name;
  public int progress;
  public String state;
}