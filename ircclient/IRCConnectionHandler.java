package edu.ucsd.cse70.ircclient;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;

class IRCConnectionHandler
{
  private IRCConnection _connection;

  void setConnection(IRCConnection paramIRCConnection)
  {
    this._connection = paramIRCConnection;
  }

  void connect()
  {
    try
    {
      this._connection.connect();
    }
    catch (UnknownHostException localUnknownHostException)
    {
      System.err.println(localUnknownHostException);
      localUnknownHostException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      System.err.println(localIOException);
      localIOException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.IRCConnectionHandler
 * JD-Core Version:    0.6.0
 */