package edu.ucsd.cse70.ircclient;

import java.util.Timer;
import java.util.TimerTask;

public class Challenge extends TimerTask
{
  static int DURATION = 30;
  static int MILICONVERSION = 1000;
  String challenger;
  String defender;
  IRCConnection connection;
  Timer timer = new Timer();
  GameHandler handler;

  Challenge(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection, String paramString, GameHandler paramGameHandler)
  {
    this.timer.schedule(this, DURATION * MILICONVERSION);
    initPlayers(paramIRCMessage, paramString);
    this.connection = paramIRCConnection;
    this.handler = paramGameHandler;
  }

  void initPlayers(IRCMessage paramIRCMessage, String paramString)
  {
    this.challenger = paramIRCMessage.getNick();
    this.defender = paramString;
  }

  boolean match(String paramString1, String paramString2)
  {
    return (this.challenger.equals(paramString1)) && (this.defender.equals(paramString2));
  }

  void acceptedChallenge(String paramString1, String paramString2)
  {
    this.handler.createGame(this.connection, this, paramString1, paramString2);
  }

  void declinedChallenge()
  {
    this.handler.removeChallenge(this);
  }

  public void run()
  {
    this.connection.send(new IRCMessage("PRIVMSG " + this.challenger + " :Opponent did not respond in " + DURATION + " seconds"));
    this.connection.send(new IRCMessage("PRIVMSG " + this.defender + " :You did not respond to " + this.challenger + " in " + DURATION + " seconds"));
    this.handler.removeChallenge(this);
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Challenge
 * JD-Core Version:    0.6.0
 */