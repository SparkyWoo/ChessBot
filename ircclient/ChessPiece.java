package edu.ucsd.cse70.ircclient;

public abstract class ChessPiece
{
  String _position;
  char color;
  public boolean WKingMoved = false;
  public boolean BKingMoved = false;
  public boolean WRookLMoved = false;
  public boolean WRookRMoved = false;
  public boolean BRookLMoved = false;
  public boolean BRookRMoved = false;
  final int ASCII_ZERO_VALUE = 48;
  final int COLOR_BLACK = -1;
  final int COLOR_WHITE = 1;
  final int PAWN_WHITE = 1;
  final int PAWN_BLACK = -1;
  final int KNIGHT = 2;
  final int KING = 6;
  final int ROOK = 4;
  final int BISHOP = 3;
  final int QUEEN = 5;

  public ChessPiece(char paramChar)
  {
    this.color = paramChar;
  }

  public char getColor()
  {
    return this.color;
  }

  public boolean checkOnDiagonal(String paramString1, String paramString2)
  {
    int i = paramString1.charAt(0);
    int j = paramString2.charAt(0);
    int k = paramString1.charAt(1);
    int m = paramString2.charAt(1);
    int n = 0;
    int i1 = 0;
    if (i > j)
    {
      if (k > m)
      {
        i1 = i - j;
        n = k - m;
      }
      else
      {
        i1 = i - j;
        n = m - k;
      }
    }
    else if (k > m)
    {
      i1 = j - i;
      n = k - m;
    }
    else
    {
      i1 = j - i;
      n = m - k;
    }
    return i1 == n;
  }

  public boolean clearDiagonal(String paramString1, String paramString2, Board paramBoard)
  {
    int i = paramString1.charAt(0);
    int j = paramString2.charAt(0);
    int k = paramString1.charAt(1);
    int m = paramString2.charAt(1);
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    if (i > j)
    {
      n = j;
      i1 = i;
      i2 = m - 48;
      i3 = k - 48;
    }
    else
    {
      n = i;
      i1 = j;
      i2 = k - 48;
      i3 = m - 48;
    }
    for (int i4 = n + 1; i4 < i1; i4++)
      if (i2 < i3)
      {
        i2++;
        if (!spaceIsEmpty((char)i4 + "" + i2, paramBoard))
          return false;
      }
      else
      {
        i2--;
        if (!spaceIsEmpty((char)i4 + "" + i2, paramBoard))
          return false;
      }
    return true;
  }

  public abstract boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException;

  public void movePiece(String paramString1, String paramString2, IRCConnection paramIRCConnection, Board paramBoard)
    throws MovementException
  {
    checkValidMove(paramString1, paramString2, paramBoard);
    int i = paramBoard.getPieceAt(paramString1);
    if (checkPromotion(paramString2, i, paramBoard))
    {
      if (i == 1)
        paramBoard.setPieceAt(paramString2, 5);
      else
        paramBoard.setPieceAt(paramString2, -5);
    }
    else
      paramBoard.setPieceAt(paramString2, i);
    checkCastle(paramString1, paramString2, i, paramBoard);
    checkIfKingMoved(paramString1, i);
    checkIfRookMoved(paramString1, i);
    paramBoard.setPieceAt(paramString1, 0);
  }

  public void checkCastle(String paramString1, String paramString2, int paramInt, Board paramBoard)
    throws MovementException
  {
    int i = paramString2.charAt(0) - paramString1.charAt(0);
    if (paramInt == 6)
    {
      if (!this.WKingMoved)
        if (i == 2)
        {
          if ((!this.WRookRMoved) && ((spotNotThreatened("e1", paramBoard)) || (spotNotThreatened("g1", paramBoard)) || (spotNotThreatened("f1", paramBoard))))
          {
            paramBoard.setPieceAt("g1", 6);
            paramBoard.setPieceAt("f1", 4);
            paramBoard.setPieceAt("e1", 0);
            paramBoard.setPieceAt("h1", 0);
            this.WKingMoved = true;
            this.WRookRMoved = true;
          }
        }
        else if ((i == -2) && (!this.WRookLMoved) && ((spotNotThreatened("e1", paramBoard)) || (spotNotThreatened("d1", paramBoard)) || (spotNotThreatened("c1", paramBoard))))
        {
          paramBoard.setPieceAt("c1", 6);
          paramBoard.setPieceAt("d1", 4);
          paramBoard.setPieceAt("e1", 0);
          paramBoard.setPieceAt("a1", 0);
          this.WKingMoved = true;
          this.WRookLMoved = true;
        }
    }
    else if ((paramInt == -6) && (!this.BKingMoved))
      if (i == 2)
      {
        if ((!this.BRookRMoved) && ((spotNotThreatened("e8", paramBoard)) || (spotNotThreatened("f8", paramBoard)) || (spotNotThreatened("g8", paramBoard))))
        {
          paramBoard.setPieceAt("g8", -6);
          paramBoard.setPieceAt("f8", -4);
          paramBoard.setPieceAt("e8", 0);
          paramBoard.setPieceAt("h8", 0);
          this.BKingMoved = true;
          this.BRookLMoved = true;
        }
      }
      else if ((i == -2) && (!this.BRookLMoved) && ((spotNotThreatened("e8", paramBoard)) || (spotNotThreatened("d8", paramBoard)) || (spotNotThreatened("c8", paramBoard))))
      {
        paramBoard.setPieceAt("c8", -6);
        paramBoard.setPieceAt("d8", -4);
        paramBoard.setPieceAt("e8", 0);
        paramBoard.setPieceAt("a8", 0);
        this.BKingMoved = true;
        this.BRookRMoved = true;
      }
  }

  public void checkIfRookMoved(String paramString, int paramInt)
  {
    if (paramInt == 4)
    {
      if ((paramString.equals("a1")) || (paramString.equals("d1")))
        this.WRookLMoved = true;
      else if ((paramString.equals("h1")) || (paramString.equals("f1")))
        this.WRookRMoved = true;
    }
    else if (paramInt == -4)
      if ((paramString.equals("a8")) || (paramString.equals("d8")))
        this.BRookLMoved = true;
      else if ((paramString.equals("h8")) || (paramString.equals("f8")))
        this.BRookRMoved = true;
  }

  public void checkIfKingMoved(String paramString, int paramInt)
  {
    if (paramInt == 6)
    {
      if ((paramString.equals("e1")) || (paramString.equals("g1")) || (paramString.equals("c1")))
        this.WKingMoved = true;
    }
    else if ((paramInt == -6) && ((paramString.equals("e8")) || (paramString.equals("g8")) || (paramString.equals("c8"))))
      this.BKingMoved = true;
  }

  public boolean checkPromotion(String paramString, int paramInt, Board paramBoard)
  {
    int i = paramString.charAt(1) - '0';
    if ((paramInt == 1) && (i == 8))
      return true;
    return (paramInt == -1) && (i == 1);
  }

  public boolean checkSameRankOrFile(String paramString1, String paramString2)
  {
    int i = paramString1.charAt(0);
    int j = paramString1.charAt(1);
    int k = paramString2.charAt(0);
    int m = paramString2.charAt(1);
    return (i == k) || (j == m);
  }

  public boolean checkClearRankOrFile(String paramString1, String paramString2, Board paramBoard)
  {
    int i = paramString1.charAt(0);
    int j = paramString1.charAt(1);
    int k = paramString2.charAt(0);
    int m = paramString2.charAt(1);
    int i4;
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
      for (i4 = n + 1; i4 < i1; i4++)
        if (!spaceIsEmpty((char)i + "" + (i4 - 48), paramBoard))
          return false;
    }
    else if (j == m)
    {
      int i2;
      int i3;
      if (i > k)
      {
        i2 = k;
        i3 = i;
      }
      else
      {
        i2 = i;
        i3 = k;
      }
      for (i4 = i2 + 1; i4 < i3; i4++)
        if (!spaceIsEmpty((char)i4 + "" + (j - 48), paramBoard))
          return false;
    }
    return true;
  }

  public boolean spotNotThreatened(String paramString, Board paramBoard)
    throws MovementException
  {
    int i;
    if (this.color == 'w')
      i = -1;
    else
      i = 1;
    if (!checkRow(paramString, paramBoard, i))
      return false;
    if (!checkColumn(paramString, paramBoard, i))
      return false;
    if (!checkDiagonals(paramString, paramBoard, i))
      return false;
    if (!checkKnights(paramString, paramBoard, i))
      return false;
    return checkForKing(paramString, paramBoard, i);
  }

  boolean checkKnights(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0';
    int j = paramString.charAt(0);
    String str;
    if (i > 1)
    {
      str = "" + (char)(j + 2) + (i - 1);
      if (paramBoard.getPieceAt(str) == paramInt * 2)
        return false;
      str = "" + (char)(j - 2) + (i - 1);
      if (paramBoard.getPieceAt(str) == paramInt * 2)
        return false;
      if (i > 2)
      {
        str = "" + (char)(j + 1) + (i - 2);
        if (paramBoard.getPieceAt(str) == paramInt * 2)
          return false;
        str = "" + (char)(j - 1) + (i - 2);
        if (paramBoard.getPieceAt(str) == paramInt * 2)
          return false;
      }
    }
    if (i < 8)
    {
      str = "" + (char)(j + 2) + (i + 1);
      if (paramBoard.getPieceAt(str) == paramInt * 2)
        return false;
      str = "" + (char)(j - 2) + (i + 1);
      if (paramBoard.getPieceAt(str) == paramInt * 2)
        return false;
      if (i < 7)
      {
        str = "" + (char)(j + 1) + (i + 2);
        if (paramBoard.getPieceAt(str) == paramInt * 2)
          return false;
        str = "" + (char)(j - 1) + (i + 2);
        if (paramBoard.getPieceAt(str) == paramInt * 2)
          return false;
      }
    }
    return true;
  }

  boolean checkRow(String paramString, Board paramBoard, int paramInt)
  {
    if (checkRowLeft(paramString, paramBoard, paramInt))
      return false;
    return !checkRowRight(paramString, paramBoard, paramInt);
  }

  boolean checkRowLeft(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0';
    char c = (char)(paramString.charAt(0) - '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while (c >= 'a')
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
        return (k == paramInt * 4) || (k == paramInt * 5);
      c = (char)(c - '\001');
    }
    return false;
  }

  boolean checkRowRight(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0';
    char c = (char)(paramString.charAt(0) + '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while (c != 'i')
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
        return (k == paramInt * 4) || (k == paramInt * 5);
      c = (char)(c + '\001');
    }
    return false;
  }

  boolean checkColumn(String paramString, Board paramBoard, int paramInt)
  {
    if (checkColumnAbove(paramString, paramBoard, paramInt))
      return false;
    return (paramString.charAt(1) - '0' <= 1) || (!checkColumnBelow(paramString, paramBoard, paramInt));
  }

  boolean checkColumnAbove(String paramString, Board paramBoard, int paramInt)
  {
    char c = paramString.charAt(0);
    int i = paramString.charAt(1) - '0' + 1;
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while (i < 9)
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
      {
        if ((k == paramInt * 4) || (k == paramInt * 5))
        {
          Logger.appendLine("Not clear at " + str);
          return true;
        }
        return false;
      }
      i++;
    }
    return false;
  }

  boolean checkColumnBelow(String paramString, Board paramBoard, int paramInt)
  {
    char c = paramString.charAt(0);
    int i = paramString.charAt(1) - '0' - 1;
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while (i > 0)
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
        return (k == paramInt * 4) || (k == paramInt * 5);
      i--;
    }
    return false;
  }

  boolean checkDiagonals(String paramString, Board paramBoard, int paramInt)
  {
    if (upperRightThreatens(paramString, paramBoard, paramInt))
      return false;
    if (upperLeftThreatens(paramString, paramBoard, paramInt))
      return false;
    if (paramString.charAt(1) - '0' > 1)
    {
      if (lowerRightThreatens(paramString, paramBoard, paramInt))
        return false;
      if (lowerLeftThreatens(paramString, paramBoard, paramInt))
        return false;
    }
    return true;
  }

  boolean upperLeftThreatens(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0' + 1;
    char c = (char)(paramString.charAt(0) - '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while ((i < 9) && (c >= 'a'))
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
      {
        if ((k == paramInt * 3) || (k == paramInt * 5))
          return true;
        return checkPawnThreatens(paramString, paramBoard, paramInt);
      }
      c = (char)(c - '\001');
      i++;
    }
    return false;
  }

  boolean upperRightThreatens(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0' + 1;
    char c = (char)(paramString.charAt(0) + '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while ((i != 9) && (c <= 'h'))
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
      {
        if ((k == paramInt * 3) || (k == paramInt * 5))
          return true;
        return checkPawnThreatens(paramString, paramBoard, paramInt);
      }
      c = (char)(c + '\001');
      i++;
    }
    return false;
  }

  boolean lowerLeftThreatens(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0' - 1;
    char c = (char)(paramString.charAt(0) - '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while ((i != 0) && (c >= 'a'))
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
      {
        if ((k == paramInt * 3) || (k == paramInt * 5))
          return true;
        return checkPawnThreatens(paramString, paramBoard, paramInt);
      }
      c = (char)(c - '\001');
      i--;
    }
    return false;
  }

  boolean lowerRightThreatens(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0' - 1;
    char c = (char)(paramString.charAt(0) + '\001');
    int j;
    if (paramInt == 1)
      j = -1;
    else
      j = 1;
    while ((i != 0) && (c <= 'h'))
    {
      String str = "" + c + i;
      int k = paramBoard.getPieceAt(str);
      if ((k != 0) && (k != j * 6))
      {
        if ((k == paramInt * 3) || (k == paramInt * 5))
          return true;
        return checkPawnThreatens(paramString, paramBoard, paramInt);
      }
      c = (char)(c + '\001');
      i--;
    }
    return false;
  }

  boolean checkPawnThreatens(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0';
    int j = paramString.charAt(0);
    String str;
    if (paramInt == -1)
    {
      str = "" + (char)(j + 1) + (i + 1);
      if (paramBoard.getPieceAt(str) == -1)
        return true;
      str = "" + (char)(j - 1) + (i + 1);
      if (paramBoard.getPieceAt(str) == -1)
        return true;
    }
    if ((paramInt == 1) && (i > 1))
    {
      str = "" + (char)(j - 1) + (i - 1);
      if (paramBoard.getPieceAt(str) == 1)
        return true;
      str = "" + (char)(j + 1) + (i - 1);
      if (paramBoard.getPieceAt(str) == 1)
        return true;
    }
    return false;
  }

  boolean checkForKing(String paramString, Board paramBoard, int paramInt)
  {
    int i = paramString.charAt(1) - '0';
    char c = paramString.charAt(0);
    if (i > 1)
    {
      str = "" + (char)(c + '\001') + (i - 1);
      if (paramBoard.getPieceAt(str) == paramInt * 6)
        return false;
      str = "" + c + (i - 1);
      if (paramBoard.getPieceAt(str) == paramInt * 6)
        return false;
      str = "" + (char)(c - '\001') + (i - 1);
      if (paramBoard.getPieceAt(str) == paramInt * 6)
        return false;
    }
    String str = "" + (char)(c + '\001') + i;
    if (paramBoard.getPieceAt(str) == paramInt * 6)
      return false;
    str = "" + (char)(c - '\001') + i;
    if (paramBoard.getPieceAt(str) == paramInt * 6)
      return false;
    str = "" + (char)(c + '\001') + (i + 1);
    if (paramBoard.getPieceAt(str) == paramInt * 6)
      return false;
    str = "" + c + (i + 1);
    if (paramBoard.getPieceAt(str) == paramInt * 6)
      return false;
    str = "" + (char)(c - '\001') + (i + 1);
    return paramBoard.getPieceAt(str) != paramInt * 6;
  }

  public boolean spaceIsEmpty(String paramString, Board paramBoard)
  {
    return paramBoard.getPieceAt(paramString) == 0;
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.ChessPiece
 * JD-Core Version:    0.6.0
 */