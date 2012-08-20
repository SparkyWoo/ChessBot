package edu.ucsd.cse70.ircclient;

import java.util.ArrayList;

public class Board
{
  public String CHANNEL_NAME;
  private int[] board = new int[120];
  boolean DEBUG = true;
  private final int ASCII_0 = 48;
  private final int ASCII_1 = 49;
  final int W_PAWN = 1;
  final int W_KNIGHT = 2;
  final int W_BISHOP = 3;
  final int W_ROOK = 4;
  final int W_QUEEN = 5;
  final int W_KING = 6;
  final int B_PAWN = -1;
  final int B_KNIGHT = -2;
  final int B_BISHOP = -3;
  final int B_ROOK = -4;
  final int B_QUEEN = -5;
  final int B_KING = -6;
  private String DARK_SPACE = "\0035,5 \003";
  private String LIGHT_SPACE = "\0037,7 \003";

  public Board(String paramString)
  {
    this.CHANNEL_NAME = paramString;
    InitBoardArray();
    if (this.DEBUG)
      printBoardArray();
  }

  private void InitBoardArray()
  {
    for (int j = 0; j < 96; j++)
    {
      int i = j % 12;
      if ((i == 0) || (i == 1) || (i == 10) || (i == 11))
        this.board[j] = 99;
      else
        this.board[j] = 0;
    }
    for (j = 96; j < 120; j++)
      this.board[j] = 99;
    for (j = 74; j < 82; j++)
      this.board[j] = -1;
    for (j = 14; j < 22; j++)
      this.board[j] = 1;
    this.board[2] = 4;
    this.board[3] = 2;
    this.board[4] = 3;
    this.board[5] = 5;
    this.board[6] = 6;
    this.board[7] = 3;
    this.board[8] = 2;
    this.board[9] = 4;
    this.board[86] = -4;
    this.board[87] = -2;
    this.board[88] = -3;
    this.board[89] = -5;
    this.board[90] = -6;
    this.board[91] = -3;
    this.board[92] = -2;
    this.board[93] = -4;
  }

  private void printBoardArray()
  {
    for (int i = 108; i >= 0; i -= 12)
      for (int j = 0; j < 12; j++)
      {
        if ((i + j) % 12 == 0)
        {
          Logger.appendLine();
        }
        else
        {
          if ((this.board[(i + j)] < 10) && (this.board[(i + j)] >= 0))
            Logger.appendBoard(" ");
          Logger.appendBoard(" ");
        }
        Logger.append(this.board[(i + j)]);
      }
    Logger.appendLine();
  }

  public int getPosition(String paramString)
  {
    int i = 0;
    int j = 0;
    paramString = paramString.toUpperCase();
    int k = paramString.charAt(0);
    int m = paramString.charAt(1);
    j = m - 49;
    switch (k)
    {
    case 65:
      i = 0;
      break;
    case 66:
      i = 1;
      break;
    case 67:
      i = 2;
      break;
    case 68:
      i = 3;
      break;
    case 69:
      i = 4;
      break;
    case 70:
      i = 5;
      break;
    case 71:
      i = 6;
      break;
    case 72:
      i = 7;
      break;
    default:
      i = 9;
    }
    return getBoardArrayIndex(j, i);
  }

  private int getBoardArrayIndex(int paramInt1, int paramInt2)
  {
    return paramInt1 * 12 + 2 + paramInt2;
  }

  public String findKing(char paramChar)
  {
    for (int i = 0; i < 120; i++)
      if (((paramChar == 'w') && (getPieceAt(i) == 6)) || ((paramChar == 'b') && (getPieceAt(i) == -6)))
        return boardIndexToPositionString(i);
    return "I1";
  }

  private String boardIndexToPositionString(int paramInt)
  {
    int i = paramInt / 12 + 1;
    int j = paramInt % 12;
    char c;
    switch (j)
    {
    case 2:
      c = 'a';
      break;
    case 3:
      c = 'b';
      break;
    case 4:
      c = 'c';
      break;
    case 5:
      c = 'd';
      break;
    case 6:
      c = 'e';
      break;
    case 7:
      c = 'f';
      break;
    case 8:
      c = 'g';
      break;
    case 9:
      c = 'h';
      break;
    default:
      c = 'i';
    }
    return "" + c + i;
  }

  private String getColoredPiece(String paramString)
  {
    String str = null;
    int i = getPosition(paramString);
    switch (this.board[i])
    {
    case -6:
      str = "\00300,01K\003";
      break;
    case -5:
      str = "\00300,01Q\003";
      break;
    case -4:
      str = "\00300,01R\003";
      break;
    case -3:
      str = "\00300,01B\003";
      break;
    case -2:
      str = "\00300,01N\003";
      break;
    case -1:
      str = "\00300,01P\003";
      break;
    case 6:
      str = "\003K";
      break;
    case 5:
      str = "\003Q";
      break;
    case 4:
      str = "\003R";
      break;
    case 3:
      str = "\003B";
      break;
    case 2:
      str = "\003N";
      break;
    case 1:
      str = "\003P";
      break;
    case 0:
      if (i % 24 < 12)
      {
        if (i % 2 == 0)
          str = this.DARK_SPACE;
        else
          str = this.LIGHT_SPACE;
      }
      else if (i % 2 == 0)
        str = this.LIGHT_SPACE;
      else
        str = this.DARK_SPACE;
    }
    return str;
  }

  public void drawBoard(IRCConnection paramIRCConnection)
  {
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :       a    b    c    d    e    f    g    h       ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  8  ==" + getColoredPiece("a8") + "==;;" + getColoredPiece("b8") + ";;==" + getColoredPiece("c8") + "==;;" + getColoredPiece("d8") + ";;==" + getColoredPiece("e8") + "==;;" + getColoredPiece("f8") + ";;==" + getColoredPiece("g8") + "==;;" + getColoredPiece("h8") + ";;  8  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  7  ;;" + getColoredPiece("a7") + ";;==" + getColoredPiece("b7") + "==;;" + getColoredPiece("c7") + ";;==" + getColoredPiece("d7") + "==;;" + getColoredPiece("e7") + ";;==" + getColoredPiece("f7") + "==;;" + getColoredPiece("g7") + ";;==" + getColoredPiece("h7") + "==  7  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  6  ==" + getColoredPiece("a6") + "==;;" + getColoredPiece("b6") + ";;==" + getColoredPiece("c6") + "==;;" + getColoredPiece("d6") + ";;==" + getColoredPiece("e6") + "==;;" + getColoredPiece("f6") + ";;==" + getColoredPiece("g6") + "==;;" + getColoredPiece("h6") + ";;  6  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  5  ;;" + getColoredPiece("a5") + ";;==" + getColoredPiece("b5") + "==;;" + getColoredPiece("c5") + ";;==" + getColoredPiece("d5") + "==;;" + getColoredPiece("e5") + ";;==" + getColoredPiece("f5") + "==;;" + getColoredPiece("g5") + ";;==" + getColoredPiece("h5") + "==  5  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  4  ==" + getColoredPiece("a4") + "==;;" + getColoredPiece("b4") + ";;==" + getColoredPiece("c4") + "==;;" + getColoredPiece("d4") + ";;==" + getColoredPiece("e4") + "==;;" + getColoredPiece("f4") + ";;==" + getColoredPiece("g4") + "==;;" + getColoredPiece("h4") + ";;  4  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  3  ;;" + getColoredPiece("a3") + ";;==" + getColoredPiece("b3") + "==;;" + getColoredPiece("c3") + ";;==" + getColoredPiece("d3") + "==;;" + getColoredPiece("e3") + ";;==" + getColoredPiece("f3") + "==;;" + getColoredPiece("g3") + ";;==" + getColoredPiece("h3") + "==  3  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  2  ==" + getColoredPiece("a2") + "==;;" + getColoredPiece("b2") + ";;==" + getColoredPiece("c2") + "==;;" + getColoredPiece("d2") + ";;==" + getColoredPiece("e2") + "==;;" + getColoredPiece("f2") + ";;==" + getColoredPiece("g2") + "==;;" + getColoredPiece("h2") + ";;  2  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     =====;;;;;=====;;;;;=====;;;;;=====;;;;;     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :  1  ;;" + getColoredPiece("a1") + ";;==" + getColoredPiece("b1") + "==;;" + getColoredPiece("c1") + ";;==" + getColoredPiece("d1") + "==;;" + getColoredPiece("e1") + ";;==" + getColoredPiece("f1") + "==;;" + getColoredPiece("g1") + ";;==" + getColoredPiece("h1") + "==  1  ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :     ;;;;;=====;;;;;=====;;;;;=====;;;;;=====     ")));
    paramIRCConnection.send(new IRCMessage(colorForChess("PRIVMSG " + this.CHANNEL_NAME + " :       a    b    c    d    e    f    g    h       ")));
  }

  private String colorForChess(String paramString)
  {
    paramString = paramString.replaceAll(";", this.DARK_SPACE);
    paramString = paramString.replaceAll("=", this.LIGHT_SPACE);
    return paramString;
  }

  public int getPieceAt(String paramString)
  {
    return this.board[getPosition(paramString)];
  }

  public void setPieceAt(String paramString, int paramInt)
  {
    this.board[getPosition(paramString)] = paramInt;
  }

  public int getPieceAt(int paramInt)
  {
    return this.board[paramInt];
  }

  public String getChannel()
  {
    return this.CHANNEL_NAME;
  }

  public boolean checkAbleToCaptureThreat(char paramChar, String paramString, ArrayList<String> paramArrayList, ChessPiece[] paramArrayOfChessPiece)
  {
    int i;
    if (paramChar == 'w')
      for (i = 0; i < 120; i++)
      {
        if ((getPieceAt(i) <= 0) || (getPieceAt(i) == 99))
          continue;
        paramArrayList.add(boardIndexToPositionString(i));
        try
        {
          if (paramArrayOfChessPiece[processColorCode(getPieceAt(i))].checkValidMove(boardIndexToPositionString(i), paramString, this))
            return true;
        }
        catch (MovementException localMovementException1)
        {
        }
      }
    else
      for (i = 0; i < 120; i++)
      {
        if (getPieceAt(i) >= 0)
          continue;
        paramArrayList.add(boardIndexToPositionString(i));
        try
        {
          if (paramArrayOfChessPiece[processColorCode(getPieceAt(i))].checkValidMove(boardIndexToPositionString(i), paramString, this))
            return true;
        }
        catch (MovementException localMovementException2)
        {
        }
      }
    return false;
  }

  public int processColorCode(int paramInt)
  {
    if ((paramInt > 6) || (paramInt < 0))
      return paramInt * -1 + 6;
    return paramInt;
  }

  public ArrayList<String> whatIsOnDiagonalBetween(String paramString1, String paramString2)
  {
    char c1 = paramString2.charAt(0);
    char c2 = paramString1.charAt(0);
    int i = paramString2.charAt(1);
    int j = paramString1.charAt(1);
    ArrayList localArrayList = new ArrayList();
    int k;
    int m;
    int n;
    if (c1 > c2)
    {
      c3 = c2;
      k = c1;
      m = j - 48;
      n = i - 48;
    }
    else
    {
      c3 = c1;
      k = c2;
      m = i - 48;
      n = j - 48;
    }
    char c3 = (char)(c3 + '\001');
    for (char c4 = c3; c4 < k; c4 = (char)(c4 + '\001'))
    {
      if (m < n)
        m++;
      else
        m--;
      localArrayList.add("" + c4 + m);
    }
    return localArrayList;
  }

  public ArrayList<String> whatIsOnStraightBetween(String paramString1, String paramString2)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramString2.charAt(0);
    int j = paramString2.charAt(1) - '0';
    int k = paramString1.charAt(0);
    int m = paramString1.charAt(1) - '0';
    int i3;
    if (i == k)
    {
      int n;
      int i1;
      if (j > m)
      {
        n = m;
        i1 = j;
      }
      else
      {
        n = j;
        i1 = m;
      }
      for (i3 = n + 1; i3 < i1; i3++)
        localArrayList.add("" + i + i3);
    }
    else if (j == m)
    {
      char c;
      int i2;
      if (i > k)
      {
        c = k;
        i2 = i;
      }
      else
      {
        c = i;
        i2 = k;
      }
      for (i3 = c + '\001'; i3 < i2; i3++)
        localArrayList.add("" + (char)i3 + j);
    }
    return localArrayList;
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Board
 * JD-Core Version:    0.6.0
 */