package edu.ucsd.cse70.ircclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class IRCConnection
  implements Runnable
{
  private IRCMessageHandler _messageHandler;
  private String _user;
  private String _name;
  private String _passwd;
  private String _nick;
  private String _host;
  private int _port;
  private Socket _socket = null;
  private Thread _thread = null;
  private PrintWriter _writer;

  IRCConnection(IRCMessageHandler paramIRCMessageHandler)
  {
    this._messageHandler = paramIRCMessageHandler;
  }

  void connect()
    throws UnknownHostException, IOException
  {
    if (this._thread != null)
      try
      {
        this._thread.join();
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    this._socket = new Socket();
    this._socket.connect(new InetSocketAddress(this._host, this._port), 500);
    this._writer = new PrintWriter(new OutputStreamWriter(this._socket.getOutputStream(), "ISO-8859-1"));
    this._thread = new Thread(this);
    this._thread.start();
    if (this._passwd != null)
      send("PASS " + this._passwd);
    send("NICK " + this._nick);
    send("USER " + this._user + " * * :" + this._name);
    send("JOIN #chess andrew");
    send("PRIVMSG chessbot : op team3op");
    send("MODE #chess +tn");
    send("MODE #chess +k andrew");
    send("TOPIC #chess :Your all time best Chess-Stop");
  }

  void disconnect()
    throws IOException
  {
    this._socket.close();
  }

  void send(IRCMessage paramIRCMessage)
    throws IllegalArgumentException
  {
    send(paramIRCMessage.toString());
    if (!paramIRCMessage.isValid())
      throw new IllegalArgumentException("Invalid IRC Message: '" + paramIRCMessage + "'");
  }

  private void send(String paramString)
  {
    this._writer.write(paramString + "\r\n");
    this._writer.flush();
  }

  public void run()
  {
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
      String str;
      while ((this._socket.isConnected()) && (!this._socket.isClosed()) && ((str = localBufferedReader.readLine()) != null))
        this._messageHandler.handle(new IRCMessage(str), this);
    }
    catch (IOException localIOException)
    {
      if (!this._socket.isClosed())
        localIOException.printStackTrace();
    }
    this._socket = null;
  }

  String getName()
  {
    return this._name;
  }

  void setName(String paramString)
  {
    this._name = paramString;
  }

  String getNick()
  {
    return this._nick;
  }

  void setNick(String paramString)
  {
    this._nick = paramString;
  }

  String getPasswd()
  {
    return this._passwd;
  }

  void setPasswd(String paramString)
  {
    this._passwd = paramString;
  }

  String getUser()
  {
    return this._user;
  }

  void setUser(String paramString)
  {
    this._user = paramString;
  }

  String getHost()
  {
    return this._host;
  }

  void setHost(String paramString)
  {
    this._host = paramString;
  }

  int getPort()
  {
    return this._port;
  }

  void setPort(int paramInt)
  {
    this._port = paramInt;
  }

  boolean isConnected()
  {
    return (this._socket != null) && (this._socket.isConnected()) && (!this._socket.isClosed());
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.IRCConnection
 * JD-Core Version:    0.6.0
 */