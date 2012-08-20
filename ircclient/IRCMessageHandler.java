package edu.ucsd.cse70.ircclient;

class IRCMessageHandler
{
  static final boolean DEBUG = false;
  public Logger _logger;
  GameHandler _gameHandler;

  IRCMessageHandler()
  {
    this._gameHandler = new GameHandler();
  }

  IRCMessageHandler(GameHandler paramGameHandler)
  {
    this._gameHandler = paramGameHandler;
  }

  void setLogger()
  {
    this._logger = new Logger();
  }

  void handle(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    try
    {
      if ("PING".equalsIgnoreCase(paramIRCMessage.getCommand()))
      {
        paramIRCConnection.send(new IRCMessage("PONG " + paramIRCConnection.getNick()));
      }
      else if (("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("chessbot".equals(paramIRCMessage.getParam(0))))
      {
        handleChessBotMessages(paramIRCMessage, paramIRCConnection);
      }
      else if ("JOIN".equalsIgnoreCase(paramIRCMessage.getCommand()))
      {
        this._gameHandler.addUser(paramIRCMessage);
      }
      else if (("PART".equalsIgnoreCase(paramIRCMessage.getCommand())) || ("QUIT".equalsIgnoreCase(paramIRCMessage.getCommand())))
      {
        this._gameHandler.removeUser(paramIRCMessage.getNick(), paramIRCMessage.getChannel());
      }
      else if ("NICK".equalsIgnoreCase(paramIRCMessage.getCommand()))
      {
        this._gameHandler.changeUser(paramIRCMessage);
      }
      else if (("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("#game".equals(paramIRCMessage.getParam(0).substring(0, 5))) && ("drawboard".equals(paramIRCMessage.getParam(1))))
      {
        Logger.appendLine(paramIRCMessage);
        this._gameHandler.drawBoard(this._gameHandler.getGame(paramIRCMessage));
      }
      else if (("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("#game".equals(paramIRCMessage.getParam(0).substring(0, 5))) && ("forfeit".equals(paramIRCMessage.getParam(1))))
      {
        Logger.appendLine(paramIRCMessage);
        this._gameHandler.forfeit(paramIRCMessage, paramIRCConnection);
      }
      else if (("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("#game".equals(paramIRCMessage.getParam(0).substring(0, 5))) && ("forfeit yes".equals(paramIRCMessage.getParam(1))))
      {
        Logger.appendLine(paramIRCMessage);
        this._gameHandler.forfeitConfirm(paramIRCMessage, paramIRCConnection);
      }
      else if (("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("#game".equals(paramIRCMessage.getParam(0).substring(0, 5))) && ("forfeit no".equals(paramIRCMessage.getParam(1))))
      {
        Logger.appendLine(paramIRCMessage);
        this._gameHandler.forfeitCancel(paramIRCMessage, paramIRCConnection);
        Logger.appendLine("message.getParam(1) = " + paramIRCMessage.getParam(1));
        Logger.appendLine("message from drawboard " + paramIRCMessage);
        this._gameHandler.drawBoard(this._gameHandler.getGame(paramIRCMessage));
      }
      else if ((paramIRCMessage.getParam(1).length() > 3) && ("PRIVMSG".equalsIgnoreCase(paramIRCMessage.getCommand())) && ("#game".equals(paramIRCMessage.getParam(0).substring(0, 5))) && ("move".equals(paramIRCMessage.getParam(1).substring(0, 4))) && (10 == paramIRCMessage.getParam(1).length()))
      {
        this._gameHandler.getGame(paramIRCMessage).movePiece(paramIRCMessage.getParam(1).substring(5, 7), paramIRCMessage.getParam(1).substring(8, 10), paramIRCMessage.getNick(), paramIRCConnection);
      }
      else
      {
        Logger.appendLine(paramIRCMessage);
      }
    }
    catch (Exception localException)
    {
      Logger.appendLine();
      Logger.appendLine("** ERROR ** ");
      Logger.appendLine("EXEPTION THROWN:");
      Logger.appendLine("Exception Type: " + localException.getClass());
      Logger.appendLine("Exception message: " + localException.getMessage());
      localException.printStackTrace();
      Logger.appendLine("** End Error **");
      Logger.appendLine();
    }
  }

  void handleChessBotMessages(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    String str1 = paramIRCMessage.getParam(1);
    String str2 = "";
    if (str1.indexOf(" ") > 0)
    {
      str2 = str1.substring(0, str1.indexOf(" ")).trim();
      str1 = str1.substring(str1.indexOf(" ") + 1).trim();
    }
    if (str1.equals(""))
    {
      generateHelp(str2, paramIRCMessage.getNick(), paramIRCConnection);
      return;
    }
    if (str2.equals("chesshelp"))
    {
      displayHelpAll(str2, paramIRCMessage.getNick(), paramIRCConnection);
      return;
    }
    if (str2.equals("op"))
      makeOp(paramIRCMessage, paramIRCConnection, str1);
    else if (str2.equals("challenge"))
      this._gameHandler.challenge(paramIRCMessage, str1, paramIRCConnection);
    else if (str2.equals("accept"))
      this._gameHandler.accept(paramIRCMessage, paramIRCConnection);
    else if (str2.equals("decline"))
      this._gameHandler.decline(paramIRCMessage, str1, paramIRCConnection);
    else if (str2.equals("rejoin"))
      this._gameHandler.rejoinRequest(paramIRCMessage, str1, paramIRCConnection);
    else if (!paramIRCMessage.getNick().equals("chessbot"))
      displayHelpAll(str2, paramIRCMessage.getNick(), paramIRCConnection);
  }

  void generateHelp(String paramString1, String paramString2, IRCConnection paramIRCConnection)
  {
    if (paramString1.equals("challenge"))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :Use this format: challenge <opponent_nick>"));
    else if (paramString1.equals("accept"))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :Use this format: accept <opponent_nick>"));
    else if (paramString1.equals("decline"))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :Use this format: decline <opponent_nick>"));
    else if (paramString1.equals("rejoin"))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :Use this format: rejoin #game<number>"));
    else if (paramString1.equals("op"))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :Use this format: op <password>"));
  }

  void displayHelpAll(String paramString1, String paramString2, IRCConnection paramIRCConnection)
  {
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :The available commands are:"));
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :In Channel: challenge opponent_nick," + " accept opponent_nick, " + "decline opponent_nick, " + "rejoin #game_number"));
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramString2 + " :In Game: forfeit opponent_nick," + " move position1 position2"));
  }

  private void makeOp(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection, String paramString)
  {
    if (paramString.equals("team3op"))
      paramIRCConnection.send(new IRCMessage("MODE #chess +o " + paramIRCMessage.getNick()));
    else
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramIRCMessage.getNick() + " :Invalid Password. Try again!"));
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.IRCMessageHandler
 * JD-Core Version:    0.6.0
 */