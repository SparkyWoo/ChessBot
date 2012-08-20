package edu.ucsd.cse70.ircclient;

import java.util.ArrayList;
import java.util.Iterator;

public class Chess
{
  public IRCConnection connection;
  boolean DEBUG = true;
  final String black;
  final String white;
  boolean whiteTurn = true;
  int userCount;
  String CHANNEL_NAME;
  Board board;
  ChessPiece[] chessPieceArray = new ChessPiece[13];
  boolean challengerToForfeit = false;
  boolean defenderToForfeit = false;
  boolean isNew;

  public Chess(String paramString1, String paramString2, String paramString3, IRCConnection paramIRCConnection, boolean paramBoolean)
  {
    this.CHANNEL_NAME = paramString3;
    this.black = paramString1;
    this.white = paramString2;
    this.connection = paramIRCConnection;
    if (paramBoolean)
      this.userCount = 1;
    else
      this.userCount = 0;
    this.isNew = true;
    this.board = new Board(paramString3);
    initChessPieceArray();
  }

  public Board getBoard()
  {
    return this.board;
  }

  public void drawBoard()
  {
    this.board.drawBoard(this.connection);
  }

  public void forfeit(String paramString)
  {
    if (paramString.equals(this.white))
    {
      this.connection.send(new IRCMessage("PRIVMSG " + getChannel() + " :" + this.black + " WINS!!!!"));
      this.connection.send(new IRCMessage("PRIVMSG #chess :" + this.white + " forfeited a game with " + this.black));
    }
    else
    {
      this.connection.send(new IRCMessage("PRIVMSG " + getChannel() + " :" + this.white + " WINS!!!!"));
      this.connection.send(new IRCMessage("PRIVMSG #chess :" + this.black + " forfeited a game with " + this.white));
    }
  }

  public void cancelForfeit(String paramString)
  {
    if (paramString.equals(this.black))
      this.challengerToForfeit = false;
    else
      this.defenderToForfeit = false;
  }

  public boolean getForfeitStatus(String paramString)
  {
    if (paramString.equals(this.black))
      return this.challengerToForfeit;
    return this.defenderToForfeit;
  }

  public void setForfeitStatus(boolean paramBoolean, String paramString)
  {
    if (paramString.equals(this.black))
      this.challengerToForfeit = paramBoolean;
    else
      this.defenderToForfeit = paramBoolean;
  }

  public void increaseUser()
  {
    Logger.appendLine();
    Logger.appendLine("Start: increaseUser() for: " + this.CHANNEL_NAME);
    Logger.appendLine("userCount = " + this.userCount);
    this.userCount += 1;
    Logger.appendLine("userCount = " + this.userCount);
    Logger.appendLine("End: increaseUser() for: " + this.CHANNEL_NAME);
    Logger.appendLine();
  }

  public void decreaseUser()
  {
    Logger.appendLine();
    Logger.appendLine("Start: decreaseUser() for: " + this.CHANNEL_NAME);
    Logger.appendLine("userCount = " + this.userCount);
    this.userCount -= 1;
    Logger.appendLine("userCount = " + this.userCount);
    Logger.appendLine("End: decreaseUser() for: " + this.CHANNEL_NAME);
    Logger.appendLine();
  }

  public int getUserCount()
  {
    Logger.appendLine();
    Logger.appendLine("Start: getUserCount() for: " + this.CHANNEL_NAME);
    Logger.appendLine("userCount = " + this.userCount);
    Logger.appendLine("End: getUserCount() for: " + this.CHANNEL_NAME);
    Logger.appendLine();
    return this.userCount;
  }

  public String getChallenger()
  {
    return this.black;
  }

  public String getDefender()
  {
    return this.white;
  }

  public String getChannel()
  {
    return this.CHANNEL_NAME;
  }

  public boolean hasPlayer(String paramString)
  {
    return (paramString.equals(getDefender())) || (paramString.equals(getChallenger()));
  }

  public boolean checkValidMoveSyntax(String paramString1, String paramString2, IRCConnection paramIRCConnection)
  {
    if ((paramString1.length() == 2) && (paramString2.length() == 2) && (paramString1.charAt(0) > '`') && (paramString1.charAt(0) < 'i') && (paramString1.charAt(1) > '0') && (paramString1.charAt(1) < '9') && (paramString2.charAt(0) > '`') && (paramString2.charAt(0) < 'i') && (paramString2.charAt(1) > '0') && (paramString2.charAt(1) < '9'))
      return true;
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :There is no piece to move!!"));
    paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :Invalid parameter [a-h,1-8]"));
    return false;
  }

  public void initChessPieceArray()
  {
    this.chessPieceArray[1] = new Pawn('w');
    this.chessPieceArray[4] = new Rook('w');
    this.chessPieceArray[7] = new Pawn('b');
    this.chessPieceArray[10] = new Rook('b');
    this.chessPieceArray[3] = new Bishop('w');
    this.chessPieceArray[9] = new Bishop('b');
    this.chessPieceArray[2] = new Knight('w');
    this.chessPieceArray[8] = new Knight('b');
    this.chessPieceArray[5] = new Queen('w');
    this.chessPieceArray[11] = new Queen('b');
    this.chessPieceArray[12] = new King('b');
    this.chessPieceArray[6] = new King('w');
  }

  public void movePiece(String paramString1, String paramString2, String paramString3, IRCConnection paramIRCConnection)
  {
    if ((paramString3.equals(this.white)) && (!this.whiteTurn))
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :Wait for your turn, " + this.white + "!"));
    }
    else if ((paramString3.equals(this.black)) && (this.whiteTurn))
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :Wait for your turn, " + this.black + "!"));
    }
    else if ((paramString3.equals(this.white)) && (this.board.getPieceAt(paramString1) < 0))
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :You cannot move black pieces, " + this.white + "!"));
    }
    else if ((paramString3.equals(this.black)) && (this.board.getPieceAt(paramString1) > 0))
    {
      paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.CHANNEL_NAME + " :You cannot move white pieces, " + this.black + "!"));
    }
    else if (checkValidMoveSyntax(paramString1, paramString2, paramIRCConnection))
    {
      int i = this.board.getPieceAt(paramString1);
      if (i == 0)
        paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :You've selected an empty space!!"));
      else
        try
        {
          Logger.appendLine();
          Logger.appendLine(paramString3 + " moving a piece from " + paramString1 + " to " + paramString2);
          int j = this.board.getPieceAt(paramString2);
          this.chessPieceArray[this.board.processColorCode(i)].movePiece(paramString1, paramString2, paramIRCConnection, this.board);
          boolean bool;
          if (this.whiteTurn)
            bool = this.chessPieceArray[6].spotNotThreatened(this.board.findKing('w'), this.board);
          else
            bool = this.chessPieceArray[12].spotNotThreatened(this.board.findKing('b'), this.board);
          if (!bool)
          {
            this.board.setPieceAt(paramString1, i);
            this.board.setPieceAt(paramString2, j);
            throw new MovementException("Check");
          }
          this.board.drawBoard(paramIRCConnection);
          if (this.whiteTurn)
          {
            this.whiteTurn = false;
            bool = this.chessPieceArray[12].spotNotThreatened(this.board.findKing('b'), this.board);
            if (!bool)
              if (checkIfCheckmate('b', this.board, paramString2))
                endThisGame(paramIRCConnection, "whiteWins");
              else
                paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :The Black King is in Check!!"));
          }
          else
          {
            this.whiteTurn = true;
            bool = this.chessPieceArray[6].spotNotThreatened(this.board.findKing('w'), this.board);
            if (!bool)
            {
              Logger.appendLine();
              Logger.appendLine("Checkmate: " + checkIfCheckmate('w', this.board, paramString2));
              Logger.appendLine();
              if (checkIfCheckmate('w', this.board, paramString2))
                endThisGame(paramIRCConnection, "blackWins");
              else
                paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :The White King is in Check!!"));
            }
          }
        }
        catch (MovementException localMovementException)
        {
          if (localMovementException.getMessage().equals("Check"))
            paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :That would put your King in check!"));
          else
            paramIRCConnection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :Invalid " + localMovementException.getMessage() + " piece move!!"));
        }
    }
  }

  public void setUsed()
  {
    this.isNew = false;
  }

  public boolean isNew()
  {
    return this.isNew;
  }

  private void endThisGame(IRCConnection paramIRCConnection, String paramString)
  {
    if (paramString.equals("blackWins"))
    {
      this.connection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :CHECKMATE! " + this.black + " WINS!"));
      this.connection.send(new IRCMessage("PRIVMSG #chess :" + this.black + " defeated " + this.white));
      this.connection.send(new IRCMessage("PART " + this.board.getChannel()));
    }
    else
    {
      this.connection.send(new IRCMessage("PRIVMSG " + this.board.getChannel() + " :CHECKMATE! " + this.white + " WINS!"));
      this.connection.send(new IRCMessage("PRIVMSG #chess :" + this.white + " defeated " + this.black));
      this.connection.send(new IRCMessage("PART " + this.board.getChannel()));
    }
  }

  public boolean checkIfCheckmate(char paramChar, Board paramBoard, String paramString)
  {
    String str = paramBoard.findKing(paramChar);
    if (kingCanMove(paramChar, paramBoard, str))
      return false;
    int i = paramBoard.getPieceAt(paramString);
    ArrayList localArrayList = new ArrayList();
    if (paramBoard.checkAbleToCaptureThreat(paramChar, paramString, localArrayList, this.chessPieceArray))
      return false;
    if (paramChar == 'b')
      return canBlockSaveBlackKing(paramBoard, paramString, str, i, localArrayList);
    return canBlockSaveWhiteKing(paramBoard, paramString, str, i, localArrayList);
  }

  private boolean canBlockSaveWhiteKing(Board paramBoard, String paramString1, String paramString2, int paramInt, ArrayList<String> paramArrayList)
  {
    paramBoard.getClass();
    if (paramInt != -2)
    {
      paramBoard.getClass();
      if (paramInt != -1);
    }
    else
    {
      return true;
    }
    paramBoard.getClass();
    if ((paramInt == -3) && (checkCheckmateOnDiagonal(paramBoard, paramString1, paramString2, paramArrayList)))
      return true;
    paramBoard.getClass();
    if (paramInt == -4)
    {
      if (checkCheckmateOnStaight(paramBoard, paramString1, paramString2, paramArrayList))
        return true;
    }
    else
    {
      paramBoard.getClass();
      if (paramInt == -5)
        if ((paramString1.charAt(0) == paramString2.charAt(0)) || (paramString1.charAt(1) == paramString2.charAt(1)))
        {
          if (checkCheckmateOnStaight(paramBoard, paramString1, paramString2, paramArrayList))
            return true;
        }
        else if (checkCheckmateOnDiagonal(paramBoard, paramString1, paramString2, paramArrayList))
          return true;
    }
    return false;
  }

  private boolean canBlockSaveBlackKing(Board paramBoard, String paramString1, String paramString2, int paramInt, ArrayList<String> paramArrayList)
  {
    paramBoard.getClass();
    if (paramInt != 2)
    {
      paramBoard.getClass();
      if (paramInt != 1);
    }
    else
    {
      return true;
    }
    paramBoard.getClass();
    if ((paramInt == 3) && (checkCheckmateOnDiagonal(paramBoard, paramString1, paramString2, paramArrayList)))
      return true;
    paramBoard.getClass();
    if (paramInt == 4)
    {
      if (checkCheckmateOnStaight(paramBoard, paramString1, paramString2, paramArrayList))
        return true;
    }
    else
    {
      paramBoard.getClass();
      if (paramInt == 5)
        if ((paramString1.charAt(0) == paramString2.charAt(0)) || (paramString1.charAt(1) == paramString2.charAt(1)))
        {
          if (checkCheckmateOnStaight(paramBoard, paramString1, paramString2, paramArrayList))
            return true;
        }
        else if (checkCheckmateOnDiagonal(paramBoard, paramString1, paramString2, paramArrayList))
          return true;
    }
    return false;
  }

  private boolean kingCanMove(char paramChar, Board paramBoard, String paramString)
  {
    for (int i = paramString.charAt(1) - '0'; i <= paramString.charAt(1) + '\001'; i++)
    {
      int k;
      for (int j = (char)(paramString.charAt(0) - '\001'); j <= paramString.charAt(0) + '\001'; k = (char)(j + 1))
      {
        String str = "" + j + i;
        try
        {
          if ((paramChar == 'w') && (this.chessPieceArray[6].checkValidMove(paramString, str, paramBoard)))
            return true;
          if ((paramChar == 'b') && (this.chessPieceArray[12].checkValidMove(paramString, str, paramBoard)))
            return true;
        }
        catch (MovementException localMovementException)
        {
        }
      }
    }
    return false;
  }

  private boolean checkCheckmateOnDiagonal(Board paramBoard, String paramString1, String paramString2, ArrayList<String> paramArrayList)
  {
    ArrayList localArrayList = paramBoard.whatIsOnDiagonalBetween(paramString1, paramString2);
    if (localArrayList.size() == 0)
      return true;
    Iterator localIterator1 = localArrayList.iterator();
    while (localIterator1.hasNext())
    {
      String str1 = (String)localIterator1.next();
      Iterator localIterator2 = paramArrayList.iterator();
      while (localIterator2.hasNext())
      {
        String str2 = (String)localIterator2.next();
        try
        {
          this.chessPieceArray[paramBoard.getPieceAt(str2)].checkValidMove(str2, str1, paramBoard);
          return false;
        }
        catch (MovementException localMovementException)
        {
        }
      }
    }
    return true;
  }

  private boolean checkCheckmateOnStaight(Board paramBoard, String paramString1, String paramString2, ArrayList<String> paramArrayList)
  {
    ArrayList localArrayList = paramBoard.whatIsOnStraightBetween(paramString1, paramString2);
    if (localArrayList.size() == 0)
      return true;
    Iterator localIterator1 = localArrayList.iterator();
    while (localIterator1.hasNext())
    {
      String str1 = (String)localIterator1.next();
      Iterator localIterator2 = paramArrayList.iterator();
      while (localIterator2.hasNext())
      {
        String str2 = (String)localIterator2.next();
        try
        {
          this.chessPieceArray[paramBoard.getPieceAt(str2)].checkValidMove(str2, str1, paramBoard);
          return false;
        }
        catch (MovementException localMovementException)
        {
        }
      }
    }
    return true;
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Chess
 * JD-Core Version:    0.6.0
 */