package edu.ucsd.cse70.ircclient;

import java.io.PrintStream;

class Logger
{
  private static final long serialVersionUID = 1L;
  private static final boolean DEBUG = true;

  static void appendLine(String paramString)
  {
    System.err.println(paramString);
  }

  static void appendLine()
  {
    System.err.println();
  }

  static void appendLine(IRCMessage paramIRCMessage)
  {
    System.err.println(paramIRCMessage);
  }

  public static void append(int paramInt)
  {
    System.err.print(paramInt);
  }

  static void appendBoard(String paramString)
  {
    System.err.print(paramString);
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Logger
 * JD-Core Version:    0.6.0
 */