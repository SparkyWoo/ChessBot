package edu.ucsd.cse70.ircclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class GameHandler
{
  public int gameCounter = 1;
  public String personToChal = "";
  public String challenger = "";
  public ArrayList<Challenge> challengeList = new ArrayList();
  public int MAX_NUM_GAMES = 20;
  public ArrayList<Chess> chessgame = new ArrayList();
  public ArrayList<String> users = new ArrayList();

  void challenge(IRCMessage paramIRCMessage, String paramString, IRCConnection paramIRCConnection)
  {
    if (!paramString.equals(""))
    {
      this.personToChal = paramString;
      this.challenger = paramIRCMessage.getNick();
      if (chessChanContainsUser(this.personToChal))
      {
        paramIRCConnection.send(new IRCMessage("PRIVMSG #chess :" + this.challenger + " challenges " + this.personToChal));
        paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.personToChal + " :" + paramIRCMessage.getNick() + " wants to challenge you."));
        paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.personToChal + " :Would you like to accept? " + "(Type /PRIVMSG " + "chessbot" + " accept <nick> (or decline <nick>))"));
        createChallenge(paramIRCMessage, paramIRCConnection, this.personToChal);
        Logger.appendLine("Challenge List Size: " + this.challengeList.size());
      }
      else
      {
        paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.challenger + " :" + "Player not in Channel"));
      }
    }
    else
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramIRCMessage.getNick() + " :To Challenge" + " someone type: /PRIVMSG " + "chessbot" + " :challenge <opponent_nick>"));
    }
  }

  Chess getGame(IRCMessage paramIRCMessage)
  {
    String str = paramIRCMessage.getParam(0).substring(5);
    return (Chess)this.chessgame.get(Integer.parseInt(str) - 1);
  }

  Chess getGame(int paramInt)
  {
    return (Chess)this.chessgame.get(paramInt - 1);
  }

  ArrayList<Challenge> getChallengeList()
  {
    return this.challengeList;
  }

  void accept(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    String str = endChallenge(paramIRCMessage, true);
    if (!str.equals(""))
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramIRCMessage.getNick() + " :You've not been challenged by " + str + "!"));
  }

  String endChallenge(IRCMessage paramIRCMessage, boolean paramBoolean)
  {
    String str1 = paramIRCMessage.getNick();
    String str2 = paramIRCMessage.getParam(1).substring(7).trim();
    return endChallenge(str2, str1, paramBoolean);
  }

  String endChallenge(String paramString1, String paramString2, boolean paramBoolean)
  {
    Iterator localIterator = this.challengeList.iterator();
    while (localIterator.hasNext())
    {
      Challenge localChallenge = (Challenge)localIterator.next();
      if (localChallenge.match(paramString1, paramString2))
      {
        if (paramBoolean)
        {
          localChallenge.acceptedChallenge(paramString1, paramString2);
          return "";
        }
        localChallenge.declinedChallenge();
        return "";
      }
    }
    return paramString1;
  }

  public void createGame(IRCConnection paramIRCConnection, Challenge paramChallenge, String paramString1, String paramString2)
  {
    Logger.appendLine();
    Logger.appendLine("Creating game for " + paramString1 + " vs " + paramString2);
    this.gameCounter = 1;
    Iterator localIterator = this.chessgame.iterator();
    while (localIterator.hasNext())
    {
      Chess localChess = (Chess)localIterator.next();
      if ((localChess.getUserCount() <= 1) && (!localChess.isNew()))
        break;
      this.gameCounter += 1;
    }
    Logger.appendLine("Game Counter: " + this.gameCounter);
    if (this.gameCounter > this.MAX_NUM_GAMES)
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG #chess :All games are full."));
    }
    else
    {
      removeChallenge(paramChallenge);
      paramIRCConnection.send(new IRCMessage("JOIN #game" + this.gameCounter));
      paramIRCConnection.send(new IRCMessage("MODE #game" + this.gameCounter + " +tniI"));
      paramIRCConnection.send(new IRCMessage("TOPIC #game" + this.gameCounter + " :" + paramString1 + " vs. " + paramString2));
      paramIRCConnection.send(new IRCMessage("INVITE " + paramString2 + " " + "#game" + this.gameCounter));
      paramIRCConnection.send(new IRCMessage("INVITE " + paramString1 + " " + "#game" + this.gameCounter));
      if (this.chessgame.size() >= this.gameCounter)
        this.chessgame.set(this.gameCounter - 1, new Chess(paramString1, this.personToChal, "#game" + this.gameCounter, paramIRCConnection, true));
      else
        this.chessgame.add(this.gameCounter - 1, new Chess(paramString1, this.personToChal, "#game" + this.gameCounter, paramIRCConnection, false));
    }
  }

  void removeChallenge(Challenge paramChallenge)
  {
    this.challengeList.remove(paramChallenge);
    paramChallenge.cancel();
    paramChallenge.timer.cancel();
    Logger.appendLine("Challenge List Size: " + this.challengeList.size());
  }

  void decline(IRCMessage paramIRCMessage, String paramString, IRCConnection paramIRCConnection)
  {
    String str = endChallenge(paramIRCMessage, false);
    Logger.appendLine("In decline we have a challenger_" + str + "_and a defender_" + paramIRCMessage.getNick());
    if (str.isEmpty())
    {
      str = paramIRCMessage.getParam(1).substring(7).trim();
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + str + " :Challenge has been Declined"));
    }
    else
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + paramIRCMessage.getNick() + " :Not a valid player!"));
    }
  }

  void forfeit(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + getGame(paramIRCMessage).getChannel() + " :Do you wish to forfeit the match? " + "(type 'forfeit yes' or 'forfeit no')"));
    getGame(paramIRCMessage).setForfeitStatus(true, paramIRCMessage.getNick());
  }

  void forfeitConfirm(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    if (getGame(paramIRCMessage).getForfeitStatus(paramIRCMessage.getNick()))
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + getGame(paramIRCMessage).getChannel() + " :" + paramIRCMessage.getNick() + " has forfeited the game."));
      getGame(paramIRCMessage).forfeit(paramIRCMessage.getNick());
      paramIRCConnection.send(new IRCMessage("PART " + getGame(paramIRCMessage).getChannel()));
    }
    else
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + getGame(paramIRCMessage).getChannel() + " :" + paramIRCMessage.getNick() + ", You have not sent a forfeit request."));
    }
  }

  void forfeitCancel(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection)
  {
    if (getGame(paramIRCMessage).getForfeitStatus(paramIRCMessage.getNick()))
    {
      getGame(paramIRCMessage).cancelForfeit(paramIRCMessage.getNick());
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + getGame(paramIRCMessage).getChannel() + " :Forfeit has been cancelled."));
    }
    else
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + getGame(paramIRCMessage).getChannel() + " :" + paramIRCMessage.getNick() + ", You have not sent a forfeit request."));
    }
  }

  void addUser(IRCMessage paramIRCMessage)
  {
    String str1 = paramIRCMessage.getParam(0);
    String str2 = paramIRCMessage.getNick();
    Logger.appendLine();
    Logger.appendLine(str2 + " has joined: " + str1);
    if (str1.equals("#chess"))
    {
      this.users.add(str2);
      printUserList(str1);
    }
    else
    {
      Iterator localIterator = this.chessgame.iterator();
      while (localIterator.hasNext())
      {
        Chess localChess = (Chess)localIterator.next();
        if (localChess.getChannel().equals(str1))
        {
          localChess.increaseUser();
          if (!str2.equals("chessbot"))
            localChess.setUsed();
        }
        drawBoard(localChess);
      }
    }
    printUserList(str1);
  }

  public void drawBoard(Chess paramChess)
  {
    paramChess.drawBoard();
  }

  public Chess getGame(String paramString)
  {
    Iterator localIterator = this.chessgame.iterator();
    while (localIterator.hasNext())
    {
      Chess localChess = (Chess)localIterator.next();
      if (localChess.getChannel().equals(paramString))
        return localChess;
    }
    return null;
  }

  void removeUser(String paramString1, String paramString2)
  {
    Logger.appendLine();
    Logger.appendLine(paramString1 + " has parted: " + paramString2);
    if (paramString2.equals("#chess"))
    {
      this.users.remove(paramString1);
    }
    else
    {
      Iterator localIterator = this.chessgame.iterator();
      while (localIterator.hasNext())
      {
        Chess localChess = (Chess)localIterator.next();
        if (localChess.getChannel().equals(paramString2))
        {
          localChess.decreaseUser();
          drawBoard(localChess);
        }
      }
    }
    printUserList(paramString2);
  }

  void changeUser(IRCMessage paramIRCMessage)
  {
    this.users.remove(paramIRCMessage.getNick());
    this.users.add(paramIRCMessage.getParam(0));
    printUserList(paramIRCMessage.getChannel());
  }

  void printUserList(String paramString)
  {
    Logger.appendLine();
    Logger.appendLine("Users List for " + paramString);
    for (int i = 0; i < this.users.size(); i++)
      Logger.appendLine((String)this.users.get(i));
  }

  int getUsersArraySize()
  {
    return this.users.size();
  }

  String getUser(int paramInt)
  {
    return (String)this.users.get(paramInt);
  }

  boolean chessChanContainsUser(String paramString)
  {
    return this.users.contains(paramString);
  }

  void createChallenge(IRCMessage paramIRCMessage, IRCConnection paramIRCConnection, String paramString)
  {
    Challenge localChallenge = new Challenge(paramIRCMessage, paramIRCConnection, paramString, this);
    this.challengeList.add(localChallenge);
    Logger.appendLine("Challenge List Size: " + this.challengeList.size());
  }

  public void rejoinRequest(IRCMessage paramIRCMessage, String paramString, IRCConnection paramIRCConnection)
  {
    String str = paramIRCMessage.getNick();
    Iterator localIterator = this.chessgame.iterator();
    while (localIterator.hasNext())
    {
      Chess localChess = (Chess)localIterator.next();
      if (localChess.getChannel().equals(paramString))
      {
        if (localChess.hasPlayer(str))
          paramIRCConnection.send(new IRCMessage("INVITE " + str + " " + paramString));
        else
          paramIRCConnection.send(new IRCMessage("PRIVMSG " + str + " :You do not belong to this game " + "or your game has ended"));
      }
      else if (this.chessgame.indexOf(localChess) == this.chessgame.size() - 1)
        paramIRCConnection.send(new IRCMessage("PRIVMSG " + str + " :The game Channel " + paramString + " does not exist."));
    }
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.GameHandler
 * JD-Core Version:    0.6.0
 */